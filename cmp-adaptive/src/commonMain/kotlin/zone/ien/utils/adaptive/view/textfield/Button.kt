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
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

/**
 * 적응형 텍스트 필드 아이콘 버튼 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 텍스트 필드 아이콘 버튼을 제공합니다.
 * 
 * @param modifier 버튼에 적용할 수정자
 * @param onClick 버튼 클릭 시 호출되는 콜백
 * @param onLongClick 길게 클릭 시 호출되는 콜백 (기본값: 빈 함수)
 * @param loading 버튼이 로딩 상태인지 여부
 * @param enabled 버튼이 활성화되어 있는지 여부
 * @param interactionSource 상호작용 소스
 * @param icon 버튼에 사용할 아이콘
 * @param contentDescription 아이콘의 콘텐츠 설명
 */
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
                modifier = modifier,
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
                modifier = modifier,
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
                    material = { M3SystemIcons.Cancel },
                    cupertino = { "xmark.circle.fill" }
                )
            ),
            onClick = onClick
        )
    }
}