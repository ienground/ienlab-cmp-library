package zone.ien.utils.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlin.math.abs
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSNumberFormatterRoundHalfEven
import platform.posix.snprintf

@OptIn(ExperimentalForeignApi::class)
actual class DecimalFormat actual constructor(pattern: String) {
    private val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        roundingMode = NSNumberFormatterRoundHalfEven
        var quoted = false
        val separator = pattern.indexOfFirst { character ->
            if (character == '\'') quoted = !quoted
            character == ';' && !quoted
        }
        val positive = if (separator < 0) pattern else pattern.substring(0, separator)
        positiveFormat = positive
        negativeFormat = if (separator < 0) "-$positive" else pattern.substring(separator + 1)
        // Java는 여러 그룹 구분자가 있어도 마지막 구분자의 간격만 사용합니다.
        secondaryGroupingSize = groupingSize
    }

    actual constructor() : this("#,##0")

    actual fun format(number: Double): String =
        checkNotNull(formatter.stringFromNumber(NSNumber(double = adjustBinaryRoundingBoundary(number))))

    actual fun format(number: Int): String =
        checkNotNull(formatter.stringFromNumber(NSNumber(int = number)))

    private fun adjustBinaryRoundingBoundary(number: Double): Double {
        if (!number.isFinite() || number == 0.0) return number
        val scaled = abs(number * (formatter.multiplier?.doubleValue ?: 1.0))
        if (!scaled.isFinite() || scaled == 0.0) return number
        val decimal = NSNumber(double = scaled).stringValue.lowercase()
        val mantissa = decimal.substringBefore('e')
        val exponent = decimal.substringAfter('e', "0").toInt()
        val digits = mantissa.replace(".", "").trimStart('0').trimEnd('0')
        if (!digits.endsWith('5')) return number
        val integerDigits = mantissa.substringBefore('.').length + exponent -
            mantissa.replace(".", "").takeWhile { it == '0' }.length
        var quoted = false
        val scientific = formatter.positiveFormat.withIndex().any { (index, character) ->
            if (character == '\'') quoted = !quoted
            character == 'E' && !quoted && index > 0 &&
                formatter.positiveFormat[index - 1] in "0#" &&
                formatter.positiveFormat.getOrNull(index + 1) == '0'
        }
        val retainedDigits = if (scientific) {
            formatter.minimumIntegerDigits.toInt() + formatter.maximumFractionDigits.toInt()
        } else {
            integerDigits + formatter.maximumFractionDigits.toInt()
        }
        if (retainedDigits != digits.length - 1) return number

        // Foundation은 짧은 십진 표현을 반올림하지만 Java는 Double의 이진 오차도 반영합니다.
        val binaryDecimal = memScoped {
            val buffer = allocArray<ByteVar>(128)
            snprintf(buffer, 128u, "%.38g", scaled)
            buffer.toKString().substringBefore('e').replace(".", "").trimStart('0')
        }
        val width = maxOf(digits.length, binaryDecimal.length)
        val comparison = binaryDecimal.padEnd(width, '0').compareTo(digits.padEnd(width, '0'))
        if (comparison == 0) return number
        val retained = digits.take(retainedDigits).ifEmpty { "0" }.toLong()
        val rounded = if (comparison < 0) retained else retained + 1
        val roundedScaled = "${rounded}e${integerDigits - retainedDigits}".toDouble()
        val adjusted = roundedScaled / (formatter.multiplier?.doubleValue ?: 1.0)
        return if (number < 0) -adjusted else adjusted
    }
}

actual class DoubleFormat actual constructor(decimalPlaces: Int) {
    private val formatter = NSNumberFormatter()
    init {
        formatter.numberStyle = NSNumberFormatterDecimalStyle

        formatter.minimumFractionDigits = decimalPlaces.toULong()
        formatter.maximumFractionDigits = decimalPlaces.toULong()
        formatter.usesGroupingSeparator = false // 천 단위 구분 기호는 사용하지 않는 것이 %.2f 포맷과 유사
    }
    actual fun format(number: Double): String {
        return formatter.stringFromNumber(NSNumber(double = number))!!
    }
}