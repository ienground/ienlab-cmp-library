package zone.ien.utils.ui.utils

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

data class TextFieldDialogData(
    val initialValue: String = "",
    val valid: (String) -> Boolean = { true },
    val placeholder: String = "",
    val prefix: String? = null,
    val suffix: String? = null,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val keyboardType: KeyboardType = KeyboardType.Unspecified,
    val imeAction: ImeAction = ImeAction.Unspecified,
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1
)