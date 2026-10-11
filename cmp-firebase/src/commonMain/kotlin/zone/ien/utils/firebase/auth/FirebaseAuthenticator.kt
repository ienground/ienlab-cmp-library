package zone.ien.utils.firebase.auth

import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseAuth
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.GoogleAuthProvider
import zone.ien.firebase.auth.OAuthProvider
import zone.ien.firebase.auth.auth
import zone.ien.firebase.auth.AuthCredential as ZoneFirebaseAuthCredential

/** Firebase SDK 호출 경계입니다. 커스텀 백엔드나 테스트 대역으로 교체할 수 있습니다. */
interface FirebaseAuthGateway : FirebaseAuthSessionGateway {
    suspend fun signIn(
        credential: AuthCredential,
        linkAccount: Boolean = false,
    ): FirebaseAuthUser

    override suspend fun signInCredential(
        credential: AuthCredential,
        linkAccount: Boolean,
    ): FirebaseAuthUser = signIn(credential, linkAccount)

    override fun currentUser(): FirebaseAuthUser?

    fun signOut()
}

/** `zone.ien.firebase.auth`를 사용하는 기본 [FirebaseAuthGateway]입니다. */
class ZoneFirebaseAuthGateway(
    private val auth: FirebaseAuth = Firebase.auth,
) : FirebaseAuthGateway {
    override suspend fun signIn(
        credential: AuthCredential,
        linkAccount: Boolean,
    ): FirebaseAuthUser {
        val firebaseCredential = credential.toFirebaseCredential()
        val currentUser = auth.currentUser
        val result = if (linkAccount && currentUser != null) {
            currentUser.link(firebaseCredential)
        } else {
            auth.signInWithCredential(firebaseCredential)
        }
        val user = result.user ?: throw IllegalStateException("Firebase authentication returned no user")
        return user.toAuthUser(credential.providerId)
    }

    override fun currentUser(): FirebaseAuthUser? = auth.currentUser?.toAuthUser()

    override fun signOut() = auth.signOut()

    private fun AuthCredential.toFirebaseCredential(): ZoneFirebaseAuthCredential = when (this) {
        is AuthCredential.IdToken -> if (providerId == AuthProviderIds.Google) {
            GoogleAuthProvider.getCredential(idToken, accessToken)
        } else {
            OAuthProvider(providerId).getCredential(idToken, rawNonce, accessToken)
        }
    }

    private fun FirebaseUser.toAuthUser(providerId: String? = null): FirebaseAuthUser =
        FirebaseAuthUser(
            uid = uid,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            providerId = providerId,
        )
}

/** 공통 인증 흐름에 Firebase SDK 기본 세션을 연결합니다. */
class FirebaseAuthenticator(
    private val gateway: FirebaseAuthGateway = ZoneFirebaseAuthGateway(),
) : FirebaseSignInFlow {
    private val core = FirebaseAuthenticatorCore(gateway)

    override suspend fun authenticate(
        provider: AuthProvider,
        linkAccount: Boolean,
    ): FirebaseAuthResult = core.signIn(provider, linkAccount)

    suspend fun signIn(
        provider: AuthProvider,
        linkAccount: Boolean = false,
    ): FirebaseAuthResult = authenticate(provider, linkAccount)

    fun currentUser(): FirebaseAuthUser? = core.currentUser()

    fun signOut() = gateway.signOut()
}
