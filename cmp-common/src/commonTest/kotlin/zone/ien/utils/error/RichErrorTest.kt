package zone.ien.utils.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class RichErrorTest {
    @Test
    fun `오류 코드는 문맥과 원인 예외를 함께 보존한다`() {
        val cause = IllegalArgumentException("invalid field")
        val error = RichError(
            code = "profile.invalid",
            context = mapOf("field" to "displayName"),
        )

        val exception = RichErrorException(error, cause)

        assertEquals("profile.invalid", exception.message)
        assertEquals("displayName", exception.error.context["field"])
        assertSame(cause, exception.cause)
    }

    @Test
    fun `일반 예외는 지정한 코드와 예외 타입을 문맥으로 변환한다`() {
        val richError = IllegalStateException("failed").asRichError("common.failed")

        assertEquals("common.failed", richError.code)
        assertEquals("IllegalStateException", richError.context["exception"])
    }
}
