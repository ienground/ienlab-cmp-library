package zone.ien.utils.ui.utils.visual_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * PhoneVisualTransformation은 전화번호를 위한 VisualTransformation 구현체입니다.
 * 
 * 이 클래스는 전화번호를 입력할 때 자동으로 하이픈(-)을 삽입하여 
 * 사용자에게 친숙한 전화번호 형식을 제공합니다.
 * 다음과 같은 형식으로 전화번호를 표시합니다:
 * - 3자리: 010
 * - 4-6자리: 010-123 
 * - 7자리: 010-123-4
 * - 8-10자리: 010-123-4567
 * - 11자리: 010-1234-5678
 * 
 * 입력된 전화번호는 숫자 이외의 문자는 무시됩니다.
 */
class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }
        val trimmed = if (raw.length > 11) raw.substring(0, 11) else raw

        val formatted = buildString {
            when (trimmed.length) {
                in 0..3 -> {
                    append(trimmed)
                }
                in 4..6 -> {
                    append(trimmed.substring(0, 3))
                    append("-")
                    append(trimmed.substring(3))
                }
                7 -> {
                    append(trimmed.substring(0, 3))
                    append("-")
                    append(trimmed.substring(3, 6))
                    append("-")
                    append(trimmed.substring(6))
                }
                in 8..10 -> {
                    // 10자리(0101234567)는 010-123-4567
                    append(trimmed.substring(0, 3))
                    append("-")
                    append(trimmed.substring(3, 6))
                    append("-")
                    append(trimmed.substring(6))
                }
                11 -> {
                    // 11자리(01012345678)는 010-1234-5678
                    append(trimmed.substring(0, 3))
                    append("-")
                    append(trimmed.substring(3, 7))
                    append("-")
                    append(trimmed.substring(7))
                }
            }
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    // 4~6자리: 010-1, 010-12, 010-123
                    trimmed.length in 4..6 -> offset + 1
                    // 7~10자리: 010-123-4, 010-123-45, 010-123-456, 010-123-4567
                    trimmed.length in 7..10 -> when {
                        offset <= 3 -> offset
                        offset in 4..6 -> offset + 1
                        offset in 7..10 -> offset + 2
                        else -> formatted.length
                    }
                    // 11자리: 010-1234-5678
                    trimmed.length == 11 -> when {
                        offset <= 3 -> offset
                        offset in 4..7 -> offset + 1
                        offset in 8..11 -> offset + 2
                        else -> formatted.length
                    }
                    else -> offset
                }
            }
            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    trimmed.length in 4..6 -> offset - 1
                    trimmed.length in 7..10 -> when {
                        offset <= 3 -> offset
                        offset in 4..7 -> offset - 1
                        offset in 8..12 -> offset - 2
                        else -> trimmed.length
                    }
                    trimmed.length == 11 -> when {
                        offset <= 3 -> offset
                        offset in 4..8 -> offset - 1
                        offset in 9..13 -> offset - 2
                        else -> trimmed.length
                    }
                    else -> offset
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }
}
