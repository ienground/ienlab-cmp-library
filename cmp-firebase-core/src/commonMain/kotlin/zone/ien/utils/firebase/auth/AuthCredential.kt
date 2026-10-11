package zone.ien.utils.firebase.auth

/** Firebase가 사용하는 인증 공급자 ID입니다. */
object AuthProviderIds {
    const val Google: String = "google.com"
    const val Apple: String = "apple.com"
    const val GitHub: String = "github.com"
    const val Facebook: String = "facebook.com"
}

/** 외부 인증 공급자에서 발급받아 Firebase에 교환할 credential입니다. */
sealed interface AuthCredential {
    val providerId: String

    /**
     * OpenID Connect 토큰 기반 credential입니다.
     *
     * [toString]은 토큰과 nonce를 노출하지 않습니다.
     */
    class IdToken(
        override val providerId: String,
        val idToken: String,
        val accessToken: String? = null,
        val rawNonce: String? = null,
    ) : AuthCredential {
        init {
            require(providerId.isNotBlank()) { "providerId must not be blank" }
            require(idToken.isNotBlank()) { "idToken must not be blank" }
        }

        override fun toString(): String =
            "AuthCredential.IdToken(providerId=$providerId, idToken=<redacted>, " +
                "accessToken=${accessToken.redacted()}, rawNonce=${rawNonce.redacted()})"

        private fun String?.redacted(): String = if (this == null) "null" else "<redacted>"
    }

    companion object {
        /** Google 로그인 결과 OIDC 토큰을 생성합니다. */
        fun google(idToken: String, accessToken: String? = null): AuthCredential =
            IdToken(providerId = AuthProviderIds.Google, idToken = idToken, accessToken = accessToken)

        /** Apple Sign-In 결과 OIDC 토큰을 생성합니다. */
        fun apple(idToken: String, rawNonce: String? = null, accessToken: String? = null): AuthCredential =
            IdToken(providerId = AuthProviderIds.Apple, idToken = idToken, rawNonce = rawNonce, accessToken = accessToken)
    }
}
