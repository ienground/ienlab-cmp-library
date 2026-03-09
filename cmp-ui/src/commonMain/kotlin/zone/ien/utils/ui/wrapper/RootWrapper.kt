package zone.ien.utils.ui.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.utils.dragToKeyboardClose
import zone.ien.utils.ui.utils.keyboardAsState

@Composable
fun M3RootWrapper(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .dragToKeyboardClose(isKeyboardVisible)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content(Modifier.weight(1f))
            }
        }
    }
}