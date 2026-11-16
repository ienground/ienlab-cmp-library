package zone.ien.utils.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import zone.ien.inputactions.InputAction
import zone.ien.inputactions.InputActionStyle
import zone.ien.inputactions.inputActions

@Composable
internal fun Modifier.keyboardDismissAction(): Modifier {
    val keyboardController = LocalSoftwareKeyboardController.current
    return inputActions(
        InputAction(
            title = "Done",
            style = InputActionStyle.Done,
            onClick = { keyboardController?.hide() },
        ),
    )
}
