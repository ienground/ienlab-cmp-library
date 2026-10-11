package zone.ien.utils.firebase.auth

/**
 * 인증 에러 코드를 처리하여 적절한 FbAuthCode 오브젝트를 반환하는 함수
 * @param message Firebase에서 반환한 에러 메시지
 * @return 매칭된 FbAuthCode (성공 또는 에러)
 */
fun processAuthErrorCode(
    message: String
): FbAuthCode {
    return with (message) { when {
        contains("There is no user record corresponding to this identifier", ignoreCase = true) -> FbAuthCode.Error.UserNotFound
        contains("The email address is badly formatted", ignoreCase = true) -> FbAuthCode.Error.EmailBadlyFormatted
        contains("The supplied auth credential is incorrect", ignoreCase = true) || contains("The supplied auth credential is malformed or has expired", ignoreCase = true) -> FbAuthCode.Error.WrongPassword
        contains("The password is invalid or the user does not have a password", ignoreCase = true) -> FbAuthCode.Error.WrongPlatform
        contains("The email address is already in use", ignoreCase = true) -> FbAuthCode.Error.EmailAlreadyInUse
        contains("Given String is empty or null", ignoreCase = true) -> FbAuthCode.Error.Empty
        contains("Idtoken is null", ignoreCase = true) ||
                contains("user cancelled", ignoreCase = true) ||
                contains("The web operation was canceled by the user", ignoreCase = true) -> FbAuthCode.Error.UserCanceled
        else -> FbAuthCode.Error.Etc(message)
    } }
}