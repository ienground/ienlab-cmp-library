package zone.ien.utils.ui.utils.visual_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import zone.ien.utils.utils.DecimalFormat

/**
 * MoneyCommaVisualTransformation은 금액에 콤마를 추가하여 표시하는 VisualTransformation입니다.
 * 
 * 이 클래스는 숫자 값을 입력 받아 천 단위로 콤마를 추가하고, 원(₩) 기호를 추가하여 
 * 금액을 표시할 때 사용됩니다.
 * 
 * @param symbol 표시할 통화 기호 (기본값은 "₩")
 */
open class MoneyCommaVisualTransformation(private val symbol: String) : VisualTransformation {
    private val decimalFormat = DecimalFormat()

    /**
     * 입력된 숫자 텍스트에 천 단위 콤마와 통화 기호를 추가하고 오프셋을 매핑합니다.
     *
     * @param text 원본 입력 텍스트
     * @return 통화 기호와 천 단위 콤마가 적용된 텍스트와 오프셋 매퍼가 포함된 [TransformedText]
     */
    override fun filter(text: AnnotatedString): TransformedText {
        // 숫자만 추출 (숫자 이외는 무시)
        val raw = text.text.filter { it.isDigit() }
        // 비었으면 기본값 반환
        if (raw.isEmpty()) {
            return TransformedText(AnnotatedString(symbol + "0"), MoneyCommaOffsetMapping(raw.length, 2))
        }
        // 콤마 포매팅 + 원기호 붙이기
        val formatted = symbol + decimalFormat.format(raw.toInt())

        return TransformedText(AnnotatedString(formatted), MoneyCommaOffsetMapping(raw.length, formatted.length))
    }
}

/**
 * WonCommaVisualTransformation은 원(₩) 기호가 포함된 금액 표시를 위한 VisualTransformation입니다.
 * 
 * 이 클래스는 MoneyCommaVisualTransformation의 구체적 구현으로,
 * 기본적으로 원(₩) 기호를 사용하여 금액을 표시합니다.
 */
class WonCommaVisualTransformation: MoneyCommaVisualTransformation("₩")

/**
 * MoneyCommaOffsetMapping은 입력과 출력 간의 위치 매핑을 처리하는 클래스입니다.
 * 
 * 이 클래스는 콤마가 추가된 텍스트에서 원래 숫자로의 위치 매핑을 처리합니다.
 * 
 * @param originalLength 원본 문자열의 길이
 * @param transformedLength 변형된 문자열의 길이
 */
private class MoneyCommaOffsetMapping(
    private val originalLength: Int,
    private val transformedLength: Int
) : OffsetMapping {
    // 입력(원본)에서 출력(포맷된)으로 변환
    override fun originalToTransformed(offset: Int): Int {
        // 첫째자리에서 ₩ 때문에 +1, 그 뒤로 콤마 추가 위치만큼 오프셋
        if (originalLength == 0) return 2 // ₩0 기호 2글자
        val commas = when {
            originalLength < 4 -> 0
            originalLength < 7 -> 1
            originalLength < 10 -> 2
            else -> (originalLength - 1) / 3
        }
        return (offset + 1 + commas).coerceAtMost(transformedLength)
    }

    // 출력(포맷된)에서 입력(원본)으로 변환
    override fun transformedToOriginal(offset: Int): Int {
        if (originalLength == 0) return 0 // 입력이 비었을 때 항상 0 반환
        val noWon = offset - 1
        if (noWon <= 0) return 0
        val commas = noWon / 4  // 한자리마다 ₩+세자리+콤마 기준 역산
        return (noWon - commas).coerceAtMost(originalLength)
    }
}