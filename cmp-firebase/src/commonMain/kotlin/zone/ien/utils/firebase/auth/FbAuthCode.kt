package zone.ien.utils.firebase.auth

import androidx.compose.runtime.Composable
import zone.ien.utils.cmp_firebase.generated.resources.Res
import zone.ien.utils.cmp_firebase.generated.resources.account_does_not_exist
import zone.ien.utils.cmp_firebase.generated.resources.email_already_in_use
import zone.ien.utils.cmp_firebase.generated.resources.email_badly_formatted
import zone.ien.utils.cmp_firebase.generated.resources.email_not_verified
import zone.ien.utils.cmp_firebase.generated.resources.error_wrong_platform
import zone.ien.utils.cmp_firebase.generated.resources.format_empty
import zone.ien.utils.cmp_firebase.generated.resources.password_not_match
import zone.ien.utils.cmp_firebase.generated.resources.password_rule_failed
import zone.ien.utils.cmp_firebase.generated.resources.success_email_sent
import zone.ien.utils.cmp_firebase.generated.resources.user_canceled
import zone.ien.utils.cmp_firebase.generated.resources.user_is_null
import zone.ien.utils.cmp_firebase.generated.resources.wrong_password
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * Firebase 인증 코드를 정의하는 sealed class
 * 인증 성공과 실패 상황을 구분하여 처리
 */
sealed class FbAuthCode {
    /**
     * 인증 성공 케이스
     */
    sealed class Success: FbAuthCode() {
        /**
         * 이메일 전송 성공
         */
        data object EmailSent: Success()
    }

    /**
     * 인증 에러 케이스
     */
    sealed class Error: FbAuthCode() {
        /**
         * 사용자 객체가 null인 경우
         */
        data object UserNull: Error()
        
        /**
         * 사용자가 인증을 취소한 경우
         */
        data object UserCanceled: Error()
        
        /**
         * 잘못된 비밀번호인 경우
         */
        data object WrongPassword: Error()
        
        /**
         * 플랫폼과 관련된 잘못된 인증 정보
         */
        data object WrongPlatform: Error()
        
        /**
         * 사용자 계정이 존재하지 않는 경우
         */
        data object UserNotFound: Error()
        
        /**
         * 이메일 인증이 완료되지 않은 경우
         */
        data object EmailNotVerified: Error()
        
        /**
         * 이미 사용 중인 이메일 주소
         */
        data object EmailAlreadyInUse: Error()
        
        /**
         * 비밀번호가 일치하지 않는 경우
         */
        data object PasswordNotMatched: Error()
        
        /**
         * 비밀번호 규칙에 맞지 않는 경우
         */
        data object PasswordRuleFailed: Error()
        
        /**
         * 이메일 형식이 잘못된 경우
         */
        data object EmailBadlyFormatted: Error()
        
        /**
         * 입력값이 비어 있는 경우
         */
        data object Empty: Error()
        
        /**
         * 기타 에러 상황
         * @param message 에러 메시지
         */
        data class Etc(val message: String): Error()
    }

    /**
     * Composable 함수를 사용하여 에러/성공에 대한 문자열을 반환
     * @return 에러/성공에 대한 문자열
     */
    @Composable
    fun labelResource(): String {
        return when (this) {
            is Error.Etc -> message
            else -> getLabelBasic()?.let { stringResource(it) }.orEmpty()
        }
    }

    /**
     * 비동기 함수를 사용하여 에러/성공에 대한 라벨을 반환
     * @return 에러/성공에 대한 문자열
     */
    suspend fun getLabel(): String {
        return when (this) {
            is Error.Etc -> message
            else -> getLabelBasic()?.let { getString(it) }.orEmpty()
        }
    }

    /**
     * 기본 에러/성공 메시지 리소스를 얻는 내부 함수
     * @return 해당 상태에 대한 문자열 리소스
     */
    private fun getLabelBasic(): StringResource? {
        return when (this) {
            Success.EmailSent -> Res.string.success_email_sent
            Error.UserNull -> Res.string.user_is_null
            Error.UserCanceled -> Res.string.user_canceled
            Error.WrongPassword -> Res.string.wrong_password
            Error.WrongPlatform -> Res.string.error_wrong_platform
            Error.UserNotFound -> Res.string.account_does_not_exist
            Error.EmailNotVerified -> Res.string.email_not_verified
            Error.EmailAlreadyInUse -> Res.string.email_already_in_use
            Error.PasswordNotMatched -> Res.string.password_not_match
            Error.PasswordRuleFailed -> Res.string.password_rule_failed
            Error.EmailBadlyFormatted -> Res.string.email_badly_formatted
            Error.Empty -> Res.string.format_empty
            is Error.Etc -> null
        }
    }
}