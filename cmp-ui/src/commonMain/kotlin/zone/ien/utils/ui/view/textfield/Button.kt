package zone.ien.utils.ui.view.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcons

/**
 * M3TextFieldIconButton은 텍스트 필드의 아이콘 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param onClick 클릭 시 호출되는 콜백 함수
 * @param onLongClick 긴 클릭 시 호출되는 콜백 함수
 * @param loading 로딩 상태
 * @param enabled 활성화 여부
 * @param interactionSource 상호작용 소스
 * @param icon 표시할 아이콘
 * @param contentDescription 콘텐츠 설명
 */
@Composable
fun M3TextFieldIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    loading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: IconData,
    contentDescription: String? = null
) {
    val buttonColors = IconButtonDefaults.iconButtonColors()
    val containerColor = if (enabled) buttonColors.containerColor else buttonColors.disabledContainerColor
    val contentColor = if (enabled) buttonColors.contentColor else buttonColors.disabledContentColor

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(40.dp)
            .clip(CircleShape)
            .background(color = containerColor)
            .combinedClickable(
                enabled = enabled && !loading,
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button,
                interactionSource = interactionSource
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp)
        ) {
            AnimatedVisibility(
                visible = !loading,
                enter = fadeIn(tween(700)),
                exit = fadeOut(tween(700))
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    ComplexIcon(
                        icon = icon,
                        contentDescription = contentDescription
                    )
                }
            }
            AnimatedVisibility(
                visible = loading,
                enter = fadeIn(tween(700)),
                exit = fadeOut(tween(700))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * M3TextFieldClearButton은 텍스트 필드의 클리어 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param visible 표시 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@Composable
fun M3TextFieldClearButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring(1.2f)) + scaleIn(spring(1.2f), initialScale = 0.75f),
        exit = fadeOut(spring(1.2f)) + scaleOut(spring(1.2f), targetScale = 0.75f)
    ) {
        M3TextFieldIconButton(
            icon = IconData.Vector(M3SystemIcons.Cancel),
            onClick = onClick
        )
    }
}