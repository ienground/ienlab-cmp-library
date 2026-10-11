package zone.ien.utils.firebase.auth

/** Google, Apple 등의 플랫폼 인증 흐름을 실행하는 공급자입니다. */
fun interface AuthProvider {
    suspend fun authenticate(): AuthProviderResult
}

/** 외부 인증 공급자 흐름의 결과입니다. */
sealed interface AuthProviderResult {
    data class Authenticated(val credential: AuthCredential) : AuthProviderResult
    data object Canceled : AuthProviderResult
    data class Failure(val cause: Throwable) : AuthProviderResult
}
