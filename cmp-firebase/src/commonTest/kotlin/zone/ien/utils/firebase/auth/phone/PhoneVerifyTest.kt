package zone.ien.utils.firebase.auth.phone

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class PhoneVerifyTest {
    @Test
    fun `전화번호 인증 응답 코드를 상태로 변환한다`() {
        assertEquals(PhoneVerifyState.SUCCESS, PhoneVerifyState.fromValue(200))
        assertEquals(PhoneVerifyState.FAILURE_OVER_LIMIT, PhoneVerifyState.fromValue(401))
        assertEquals(PhoneVerifyState.FAILURE_UNKNOWN, PhoneVerifyState.fromValue(999))
        assertEquals(PhoneVerifyResultState.VERIFIED, PhoneVerifyResultState.fromValue(200))
        assertEquals(PhoneVerifyResultState.FAILURE_WRONG, PhoneVerifyResultState.fromValue(402))
    }

    @Test
    fun `기본 설정으로 함수명과 payload를 구성하고 문자열 응답 코드도 처리한다`() = kotlinx.coroutines.runBlocking {
        val caller = RecordingCaller(mapOf("code" to "200"))
        val client = PhoneVerifyClient(
            functionCaller = caller,
            userIdProvider = { "user-1" },
        )

        val state = client.sendPhoneVerifyCode("010-1234-5678")

        assertEquals(PhoneVerifyState.SUCCESS, state)
        assertEquals("sendPhoneVerificationCode", caller.functionName)
        assertEquals(
            mapOf(
                "phoneNumber" to "010-1234-5678",
                "uid" to "user-1",
            ),
            caller.data,
        )
    }

    @Test
    fun `설정으로 함수명과 필드명을 바꾸고 UID 없이 호출할 수 있다`() = kotlinx.coroutines.runBlocking {
        val caller = RecordingCaller(mapOf("result" to 402))
        val client = PhoneVerifyClient(
            functionCaller = caller,
            userIdProvider = { null },
            config = PhoneVerifyConfig(
                sendFunctionName = "requestCode",
                verifyFunctionName = "confirmCode",
                phoneNumberParameter = "phone",
                userIdParameter = null,
                verificationCodeParameter = "verificationCode",
                resultCodeField = "result",
            ),
        )

        val state = client.verifyPhoneCode("010-0000-0000", "123456")

        assertEquals(PhoneVerifyResultState.FAILURE_WRONG, state)
        assertEquals("confirmCode", caller.functionName)
        assertEquals(
            mapOf(
                "phone" to "010-0000-0000",
                "verificationCode" to "123456",
            ),
            caller.data,
        )
    }

    @Test
    fun `필수 입력이 비어 있으면 구조화된 오류를 던진다`() = kotlinx.coroutines.runBlocking {
        val client = PhoneVerifyClient(
            functionCaller = RecordingCaller(emptyMap<String, Any?>()),
            userIdProvider = { "user-1" },
        )

        val exception = assertFailsWith<zone.ien.utils.error.RichErrorException> {
            client.verifyPhoneCode(" ", "123456")
        }

        assertEquals(PhoneVerifyErrorCode.PHONE_NUMBER_REQUIRED, exception.error.code)
    }

    @Test
    fun `인증 코드가 비어 있으면 구조화된 오류를 던진다`() = kotlinx.coroutines.runBlocking {
        val client = PhoneVerifyClient(
            functionCaller = RecordingCaller(emptyMap<String, Any?>()),
            userIdProvider = { "user-1" },
        )

        val exception = assertFailsWith<zone.ien.utils.error.RichErrorException> {
            client.verifyPhoneCode("010-1234-5678", " ")
        }

        assertEquals(PhoneVerifyErrorCode.VERIFICATION_CODE_REQUIRED, exception.error.code)
    }

    @Test
    fun `UID가 필요한데 인증 사용자가 없으면 함수를 호출하지 않고 알 수 없음으로 반환한다`() = kotlinx.coroutines.runBlocking {
        val caller = RecordingCaller(emptyMap<String, Any?>())
        val client = PhoneVerifyClient(
            functionCaller = caller,
            userIdProvider = { null },
        )

        val state = client.sendPhoneVerifyCode("010-1234-5678")

        assertEquals(PhoneVerifyState.FAILURE_UNKNOWN, state)
        assertFalse(caller.wasCalled)
    }

    private class RecordingCaller(
        private val response: Any?,
    ) : PhoneVerifyFunctionCaller {
        var wasCalled: Boolean = false
            private set
        var functionName: String? = null
            private set
        var data: Map<String, Any?>? = null
            private set

        override suspend fun call(functionName: String, data: Map<String, Any?>): Any? {
            wasCalled = true
            this.functionName = functionName
            this.data = data
            return response
        }
    }
}
