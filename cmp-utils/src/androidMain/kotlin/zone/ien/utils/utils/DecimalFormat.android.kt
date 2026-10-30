package zone.ien.utils.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

actual class DecimalFormat actual constructor(pattern: String) {
    private val formatter = java.text.DecimalFormat(normalizeGroupingPattern(pattern)).apply {
        roundingMode = RoundingMode.HALF_EVEN
    }
    private val usesScientificNotation = hasUnquotedExponent(pattern)

    actual constructor() : this("#,##0")

    actual fun format(number: Double): String =
        if (isBinaryRoundingBoundary(number)) formatter.format(BigDecimal(number))
        else formatter.format(number)

    actual fun format(number: Int): String = formatter.format(number)

    private fun isBinaryRoundingBoundary(number: Double): Boolean {
        if (!number.isFinite() || number == 0.0) return false

        val multiplier = formatter.multiplier.toLong()
        if (multiplier == 0L) return false

        val absoluteValue = BigDecimal.valueOf(kotlin.math.abs(number))
        val decimalValue = absoluteValue.multiply(BigDecimal.valueOf(multiplier))
        val roundingScale = if (usesScientificNotation) {
            formatter.minimumIntegerDigits + formatter.maximumFractionDigits - 1 -
                (decimalValue.precision() - decimalValue.scale() - 1)
        } else {
            formatter.maximumFractionDigits
        }
        val roundedValue = decimalValue.movePointRight(roundingScale)
        if (roundedValue.remainder(BigDecimal.ONE).abs().compareTo(HALF) != 0) return false

        val exactBinaryValue = BigDecimal(kotlin.math.abs(number))
            .multiply(BigDecimal.valueOf(multiplier))
        return exactBinaryValue.compareTo(decimalValue) != 0
    }

    private companion object {
        val HALF = BigDecimal("0.5")

        fun normalizeGroupingPattern(pattern: String): String {
            val normalized = StringBuilder()
            var sectionStart = 0
            var quoted = false
            var index = 0

            while (index < pattern.length) {
                val character = pattern[index]
                if (character == '\'') {
                    if (pattern.getOrNull(index + 1) == '\'') {
                        index += 2
                        continue
                    }
                    quoted = !quoted
                } else if (character == ';' && !quoted) {
                    normalized.append(normalizeGroupingSection(pattern.substring(sectionStart, index)))
                    normalized.append(character)
                    sectionStart = index + 1
                }
                index++
            }

            normalized.append(normalizeGroupingSection(pattern.substring(sectionStart)))
            return normalized.toString()
        }

        fun normalizeGroupingSection(section: String): String {
            val groupingSeparators = mutableListOf<Int>()
            var quoted = false
            var index = 0

            while (index < section.length) {
                val character = section[index]
                if (character == '\'') {
                    if (section.getOrNull(index + 1) == '\'') {
                        index += 2
                        continue
                    }
                    quoted = !quoted
                } else if (
                    character == ',' && !quoted &&
                    index > 0 && index < section.lastIndex &&
                    section[index - 1] in DIGIT_PATTERN_CHARACTERS &&
                    section[index + 1] in DIGIT_PATTERN_CHARACTERS
                ) {
                    groupingSeparators += index
                }
                index++
            }

            val lastSeparator = groupingSeparators.lastOrNull() ?: return section
            return buildString(section.length) {
                section.forEachIndexed { index, character ->
                    if (index !in groupingSeparators || index == lastSeparator) append(character)
                }
            }
        }

        fun hasUnquotedExponent(pattern: String): Boolean {
            var quoted = false
            var index = 0

            while (index < pattern.length) {
                val character = pattern[index]
                if (character == '\'') {
                    if (pattern.getOrNull(index + 1) == '\'') {
                        index += 2
                        continue
                    }
                    quoted = !quoted
                } else if (
                    character == 'E' && !quoted &&
                    index > 0 && pattern[index - 1] in DIGIT_PATTERN_CHARACTERS &&
                    pattern.getOrNull(index + 1) == '0'
                ) {
                    return true
                }
                index++
            }

            return false
        }

        const val DIGIT_PATTERN_CHARACTERS = "0#?"
    }
}

/**
 * Android용 소수점 형식화 클래스 (정밀도 지정).
 * 
 * 이 클래스는 지정된 소수점 자릿수로 숫자를 형식화하는 구현을 제공합니다.
 * 
 * @param decimalPlaces 표시할 소수점 자릿수
 * @see DecimalFormat
 * @see DoubleFormat
 */
actual class DoubleFormat actual constructor(decimalPlaces: Int) {
    private val formatString = "%.${decimalPlaces}f"

    /**
     * 지정된 소수점 자릿수로 소수점 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 소수점 숫자
     * @return 지정된 소수점 자릿수로 형식화된 문자열 표현
     */
    actual fun format(number: Double): String {
        return String.format(Locale.getDefault(), formatString, number)
    }
}
