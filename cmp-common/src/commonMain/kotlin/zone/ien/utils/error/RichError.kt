package zone.ien.utils.error

/** 오류를 화면 문구와 분리해 전달하기 위한 구조화된 오류 정보입니다. */
data class RichError(
    val code: String,
    val context: Map<String, String> = emptyMap(),
)

/** 예외에서 구조화된 오류 정보를 꺼냅니다. */
fun Throwable.richErrorOrNull(): RichError? = (this as? RichErrorException)?.error

/** 구조화된 오류가 없을 때 사용할 오류를 생성합니다. */
fun Throwable.asRichError(fallbackCode: String): RichError =
    richErrorOrNull() ?: RichError(
        code = fallbackCode,
        context = mapOf("exception" to (this::class.simpleName ?: "UnknownException")),
    )

/** 구조화된 오류 정보와 원인 예외를 함께 전달합니다. */
class RichErrorException(
    val error: RichError,
    cause: Throwable? = null,
) : IllegalStateException(error.code, cause)
