package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface

@Composable
internal fun IenDialogFrame(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 320.dp,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.lg),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    horizontalMargin: Dp = 24.dp,
    fixedWidth: Dp? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = usePlatformDefaultWidth,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalMargin),
        ) {
            IenSurface(
                modifier = modifier
                    .align(Alignment.Center)
                    .then(fixedWidth?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth())
                    .widthIn(max = maxWidth),
                color = IenTheme.colors.surfaceRaised,
                shape = RoundedCornerShape(IenTheme.radius.xl),
                tonalElevation = IenTheme.elevation.overlay,
            ) {
                Column(
                    modifier = Modifier.padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    content = content,
                )
            }
        }
    }
}
