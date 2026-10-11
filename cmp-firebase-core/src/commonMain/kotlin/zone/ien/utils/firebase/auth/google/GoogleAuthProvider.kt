package zone.ien.utils.firebase.auth.google

/**
 * Google Auth Provider 클래스입니다.
 * 앱 시작 시 [GoogleAuthProvider.create]를 호출해 [GoogleAuthCredentials]를 설정합니다.
 */
interface GoogleAuthProvider {
    val credentials: GoogleAuthCredentials

    companion object {
        private var instance: GoogleAuthProvider? = null

        /**
         * [GoogleAuthCredentials]를 이용해 싱글톤 [GoogleAuthProvider] 인스턴스를 생성합니다.
         * 예: `GoogleAuthProvider.create(GoogleAuthCredentials(serverId = BuildKonfig.GCP_WEB_CLIENT_ID))`
         */
        fun create(credentials: GoogleAuthCredentials): GoogleAuthProvider {
            val provider = DefaultGoogleAuthProvider(credentials)
            instance = provider
            return provider
        }

        /**
         * [create]를 통해 생성된 [GoogleAuthProvider] 인스턴스를 반환합니다.
         */
        fun get(): GoogleAuthProvider {
            return instance ?: throw IllegalArgumentException(
                "Make sure you invoked GoogleAuthProvider.create method with providing credentials (e.g. GoogleAuthCredentials(serverId = ...))"
            )
        }
    }
}

private class DefaultGoogleAuthProvider(
    override val credentials: GoogleAuthCredentials,
) : GoogleAuthProvider
