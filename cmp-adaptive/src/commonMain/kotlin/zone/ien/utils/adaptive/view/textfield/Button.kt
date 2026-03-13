package zone.ien.utils.adaptive.view.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.utils.hig.view.textfield.HigTextFieldIconButton
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveTextFieldIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    loading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: IconData,
    contentDescription: String? = null
) {
    AdaptiveWidget(
        material = {
            M3TextFieldIconButton(
                onClick = onClick,
                onLongClick = onLongClick,
                loading = loading,
                enabled = enabled,
                interactionSource = interactionSource,
                icon = icon,
                contentDescription = contentDescription
            )
        },
        cupertino = {
            HigTextFieldIconButton(
                onClick = onClick,
                loading = loading,
                enabled = enabled,
                interactionSource = interactionSource,
                icon = icon,
                contentDescription = contentDescription
            )
        }
    )
}

@Composable
fun AdaptiveTextFieldClearButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring(1.2f)) + scaleIn(spring(1.2f), initialScale = 0.75f),
        exit = fadeOut(spring(1.2f)) + scaleOut(spring(1.2f), targetScale = 0.75f)
    ) {
        AdaptiveTextFieldIconButton(
            icon = IconData.Paint(
                AdaptiveIcons.painter(
                    material = { MaterialIcons.Cancel },
                    cupertino = { "xmark.circle.fill" }
                )
            ),
            onClick = onClick
        )
    }
}