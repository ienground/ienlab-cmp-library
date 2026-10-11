package zone.ien.utils.firebase.auth.phone

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.functions.FirebaseFunctions
import dev.gitlive.firebase.functions.functions
import zone.ien.utils.error.RichError
import zone.ien.utils.error.RichErrorException

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
                functions.httpsCallable(functionName).invoke(data).data<Map<String, Any?>>()
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
