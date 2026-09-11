package zone.ien.utils.firebase.auth.phone

import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseAuth
import zone.ien.firebase.auth.auth
import zone.ien.firebase.functions.FirebaseFunctions
import zone.ien.firebase.functions.functions
import zone.ien.utils.error.RichError
import zone.ien.utils.error.RichErrorException

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

/** 함수명·payload·응답 코드 변환을 조율하는 전화번호 인증 클라이언트입니다. */
class PhoneVerifyClient(
    private val functionCaller: PhoneVerifyFunctionCaller,
    private val userIdProvider: () -> String? = { null },
    private val config: PhoneVerifyConfig = PhoneVerifyConfig(),
) {
    /** Firebase Functions에 인증 코드 발송을 요청합니다. */
    suspend fun sendPhoneVerifyCode(phoneNumber: String): PhoneVerifyState {
        requirePhoneNumber(phoneNumber)
        val userId = userIdForRequest()
        if (config.userIdParameter != null && userId == null) return PhoneVerifyState.FAILURE_UNKNOWN
        val response = functionCaller.call(
            functionName = config.sendFunctionName,
            data = buildPayload(phoneNumber = phoneNumber, userId = userId),
        )
        return PhoneVerifyState.fromValue(
            phoneVerifyResponseCode(response, config.resultCodeField)
                ?: PhoneVerifyState.FAILURE_UNKNOWN.code,
        )
    }

    /** Firebase Functions에 인증 코드 검증을 요청합니다. */
    suspend fun verifyPhoneCode(
        phoneNumber: String,
        code: String,
    ): PhoneVerifyResultState {
        requirePhoneNumber(phoneNumber)
        if (code.isBlank()) {
            throw RichErrorException(RichError(PhoneVerifyErrorCode.VERIFICATION_CODE_REQUIRED))
        }
        val userId = userIdForRequest()
        if (config.userIdParameter != null && userId == null) return PhoneVerifyResultState.FAILURE_UNKNOWN
        val response = functionCaller.call(
            functionName = config.verifyFunctionName,
            data = buildPayload(phoneNumber = phoneNumber, userId = userId, code = code),
        )
        return PhoneVerifyResultState.fromValue(
            phoneVerifyResponseCode(response, config.resultCodeField)
                ?: PhoneVerifyResultState.FAILURE_UNKNOWN.code,
        )
    }

    private fun userIdForRequest(): String? =
        config.userIdParameter?.let { userIdProvider()?.takeIf(String::isNotBlank) }

    private fun buildPayload(
        phoneNumber: String,
        userId: String?,
        code: String? = null,
    ): Map<String, Any?> = buildMap {
        put(config.phoneNumberParameter, phoneNumber)
        config.userIdParameter?.let { put(it, userId) }
        code?.let { put(config.verificationCodeParameter, it) }
    }

    private fun requirePhoneNumber(phoneNumber: String) {
        if (phoneNumber.isBlank()) {
            throw RichErrorException(RichError(PhoneVerifyErrorCode.PHONE_NUMBER_REQUIRED))
        }
    }

    companion object {
        /** 기본 Firebase Auth·Functions를 사용하는 클라이언트를 생성합니다. */
        fun firebase(
            auth: FirebaseAuth = Firebase.auth,
            functions: FirebaseFunctions = Firebase.functions,
            config: PhoneVerifyConfig = PhoneVerifyConfig(),
        ): PhoneVerifyClient = PhoneVerifyClient(
            functionCaller = PhoneVerifyFunctionCaller { functionName, data ->
                functions.httpsCallable(functionName).invoke(data).data
            },
            userIdProvider = { auth.currentUser?.uid },
            config = config,
        )
    }
}

private fun phoneVerifyResponseCode(data: Any?, key: String): Int? {
    val result = data as? Map<*, *> ?: return null
    return when (val code = result[key]) {
        is Number -> code.toInt()
        is String -> code.toIntOrNull()
        else -> null
    }
}
