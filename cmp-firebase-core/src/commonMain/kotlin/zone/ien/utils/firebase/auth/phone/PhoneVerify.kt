package zone.ien.utils.firebase.auth.phone

/** 전화번호 인증 코드 발송 요청의 결과 상태입니다. */
enum class PhoneVerifyState(val code: Int) {
    IDLE(0),
    REQUESTING(1),
    SUCCESS(200),
    FAILURE_OVER_LIMIT(401),
    FAILURE_UNKNOWN(-1),
    ;

    companion object {
        val Default: PhoneVerifyState get() = FAILURE_UNKNOWN

        fun fromValue(code: Int): PhoneVerifyState =
            entries.find { it.code == code } ?: FAILURE_UNKNOWN
    }
}

/** 전화번호 인증 코드 검증의 결과 상태입니다. */
enum class PhoneVerifyResultState(val code: Int) {
    IDLE(0),
    REQUESTING(1),
    FAILURE_NO_SEND(401),
    FAILURE_WRONG(402),
    VERIFIED(200),
    FAILURE_UNKNOWN(-1),
    ;

    companion object {
        val Default: PhoneVerifyResultState get() = FAILURE_UNKNOWN

        fun fromValue(code: Int): PhoneVerifyResultState =
            entries.find { it.code == code } ?: FAILURE_UNKNOWN
    }
}

/** 전화번호 인증 Firebase Functions의 함수명과 입출력 필드 설정입니다. */
data class PhoneVerifyConfig(
    val sendFunctionName: String = "sendPhoneVerificationCode",
    val verifyFunctionName: String = "verifyCode",
    val phoneNumberParameter: String = "phoneNumber",
    val userIdParameter: String? = "uid",
    val verificationCodeParameter: String = "code",
    val resultCodeField: String = "code",
)

/** 전화번호 인증 함수 호출 경계입니다. 테스트나 다른 백엔드 호출자로 교체할 수 있습니다. */
fun interface PhoneVerifyFunctionCaller {
    suspend fun call(functionName: String, data: Map<String, Any?>): Any?
}

/** 전화번호 인증에 사용하는 공통 오류 코드입니다. */
object PhoneVerifyErrorCode {
    const val PHONE_NUMBER_REQUIRED = "phone_verify.phone_number_required"
    const val VERIFICATION_CODE_REQUIRED = "phone_verify.verification_code_required"
}
