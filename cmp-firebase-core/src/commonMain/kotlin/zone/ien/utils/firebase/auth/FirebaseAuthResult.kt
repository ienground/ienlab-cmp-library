package zone.ien.utils.firebase.auth

/** 플랫폼 객체를 노출하지 않는 Firebase 사용자 정보입니다. */
data class FirebaseAuthUser(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val providerId: String? = null,
)

/** Firebase 인증의 최종 결과입니다. */
sealed interface FirebaseAuthResult {
    data class Success(val user: FirebaseAuthUser) : FirebaseAuthResult
    data object Canceled : FirebaseAuthResult
    data class Failure(val cause: Throwable) : FirebaseAuthResult
}
