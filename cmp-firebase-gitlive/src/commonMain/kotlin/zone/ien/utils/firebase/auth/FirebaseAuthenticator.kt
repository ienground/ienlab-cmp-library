package zone.ien.utils.firebase.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.AuthCredential as GitLiveAuthCredential

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

    suspend fun signOut()
}

/** GitLive Firebase Authentication을 사용하는 기본 [FirebaseAuthGateway]입니다. */
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
            currentUser.linkWithCredential(firebaseCredential)
        } else {
            auth.signInWithCredential(firebaseCredential)
        }
        val user = result.user ?: throw IllegalStateException("Firebase authentication returned no user")
        return user.toAuthUser(credential.providerId)
    }

    override fun currentUser(): FirebaseAuthUser? = auth.currentUser?.toAuthUser()

    override suspend fun signOut() = auth.signOut()

    private fun AuthCredential.toFirebaseCredential(): GitLiveAuthCredential = when (this) {
        is AuthCredential.IdToken -> if (providerId == AuthProviderIds.Google) {
            GoogleAuthProvider.credential(idToken, accessToken)
        } else {
            OAuthProvider.credential(
                providerId = providerId,
                accessToken = accessToken,
                idToken = idToken,
                rawNonce = rawNonce,
            )
        }
    }

    private fun FirebaseUser.toAuthUser(providerId: String? = null): FirebaseAuthUser =
        FirebaseAuthUser(
            uid = uid,
            email = email,
            displayName = displayName,
            photoUrl = photoURL,
            providerId = providerId,
        )
}

/** 공통 인증 흐름에 GitLive Firebase 세션을 연결합니다. */
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

    suspend fun signOut() = gateway.signOut()
}
