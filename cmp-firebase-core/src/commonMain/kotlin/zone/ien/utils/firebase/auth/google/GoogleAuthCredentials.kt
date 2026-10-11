package zone.ien.utils.firebase.auth.google

/**
 * Google Auth Credentials 설정 클래스입니다.
 * @param serverId Google Cloud Console에서 생성한 Web Client ID (BuildKonfig.GCP_WEB_CLIENT_ID 등)
 * @param redirectUri 루프백 리다이렉트 URI (Desktop 전용)
 */
data class GoogleAuthCredentials(
    val serverId: String,
    val redirectUri: String = "http://localhost:8080/callback",
)

/** Google 사용자 정보 클래스입니다. */
data class GoogleUser(
    val idToken: String?,
    val accessToken: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val profilePicUrl: String? = null,
)
