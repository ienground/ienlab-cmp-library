package zone.ien.utils.ui.utils.visual_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

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
