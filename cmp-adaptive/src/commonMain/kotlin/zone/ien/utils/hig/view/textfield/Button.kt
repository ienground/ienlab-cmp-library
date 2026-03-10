package zone.ien.utils.hig.view.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.hig.CupertinoActivityIndicator
import zone.ien.hig.CupertinoButton
import zone.ien.hig.CupertinoButtonDefaults.plainButtonColors
import zone.ien.hig.CupertinoButtonSize
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoIconDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.utils.IconData
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

@OptIn(ExperimentalCupertinoApi::class)
@Composable
fun HigTextFieldIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    loading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: IconData,
    contentDescription: String? = null
) {
    CupertinoButton(
        onClick = onClick,
        modifier = modifier.size(CupertinoIconDefaults.MediumSize),
        enabled = enabled && !loading,
        colors = plainButtonColors(
            contentColor = CupertinoTheme.colorScheme.tertiaryLabel,
            disabledContentColor = CupertinoTheme.colorScheme.tertiaryLabel.copy(alpha = 0.15f)
        ),
        size = CupertinoButtonSize.Regular,
        shape = CircleShape,
        border = null,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(CupertinoIconDefaults.MediumSize)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !loading,
                    enter = fadeIn(tween(700)),
                    exit = fadeOut(tween(700))
                ) {
                    when (icon) {
                        is IconData.Vector -> {
                            CupertinoIcon(
                                imageVector = icon.imageVector,
                                contentDescription = contentDescription,
                                modifier = Modifier.size(CupertinoIconDefaults.MediumSize),
                            )
                        }
                        is IconData.Paint -> {
                            CupertinoIcon(
                                painter = icon.painter,
                                contentDescription = contentDescription,
                                modifier = Modifier.size(CupertinoIconDefaults.MediumSize),
                            )
                        }
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = loading,
                    enter = fadeIn(tween(700)),
                    exit = fadeOut(tween(700))
                ) {
                    CupertinoActivityIndicator(
                        modifier = Modifier.size(CupertinoIconDefaults.MediumSize),
                    )
                }
            }
        }
    )
}



@Composable
fun HigTextFieldClearButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring(1.2f)) + scaleIn(spring(1.2f), initialScale = 0.75f),
        exit = fadeOut(spring(1.2f)) + scaleOut(spring(1.2f), targetScale = 0.75f)
    ) {
        HigTextFieldIconButton(
            icon = IconData.Vector(MaterialIcons.Cancel),
            onClick = onClick
        )
    }
}