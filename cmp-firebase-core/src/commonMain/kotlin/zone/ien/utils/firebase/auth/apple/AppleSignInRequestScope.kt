package zone.ien.utils.firebase.auth.apple

/** Apple 로그인 시 요청할 사용자 정보 스코프입니다. */
sealed interface AppleSignInRequestScope {
    data object FullName : AppleSignInRequestScope
    data object Email : AppleSignInRequestScope
}
