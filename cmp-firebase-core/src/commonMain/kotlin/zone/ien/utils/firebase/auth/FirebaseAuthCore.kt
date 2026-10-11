package zone.ien.utils.firebase.auth

import kotlinx.coroutines.CancellationException

/** Firebase 인증 사용자 정보와 세션을 제공하는 SDK 중립 경계입니다. */
interface FirebaseAuthSessionGateway {
    /** 인증 정보를 Firebase 세션에 교환합니다. */
    suspend fun signInCredential(
        credential: AuthCredential,
        linkAccount: Boolean,
    ): FirebaseAuthUser

    /** 현재 로그인 사용자를 반환합니다. */
    fun currentUser(): FirebaseAuthUser?
}

/** 인증 공급자와 Firebase 세션 교환을 수행하는 공통 인증 흐름입니다. */
interface FirebaseSignInFlow {
    /** 공급자 인증 결과를 Firebase 인증 결과로 변환합니다. */
    suspend fun authenticate(
        provider: AuthProvider,
        linkAccount: Boolean,
    ): FirebaseAuthResult
}

/** [FirebaseAuthSessionGateway]를 이용해 공급자 인증을 조율합니다. */
class FirebaseAuthenticatorCore(
    private val gateway: FirebaseAuthSessionGateway,
) {
    /** 공급자 인증 후 결과를 Firebase 인증 결과로 반환합니다. */
    suspend fun signIn(
        provider: AuthProvider,
        linkAccount: Boolean = false,
    ): FirebaseAuthResult = try {
        when (val providerResult = provider.authenticate()) {
            is AuthProviderResult.Authenticated -> FirebaseAuthResult.Success(
                gateway.signInCredential(providerResult.credential, linkAccount)
            )
            AuthProviderResult.Canceled -> FirebaseAuthResult.Canceled
            is AuthProviderResult.Failure -> FirebaseAuthResult.Failure(providerResult.cause)
        }
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        FirebaseAuthResult.Failure(error)
    }

    /** 현재 로그인 사용자를 반환합니다. */
    fun currentUser(): FirebaseAuthUser? = gateway.currentUser()
}
