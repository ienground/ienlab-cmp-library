package zone.ien.utils.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.back
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.icon.LocalCloseButtonIcon
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.view.IenTooltipBox

/**
 * 네비게이션 버튼의 [IenButtonVariant]를 제공하기 위한 CompositionLocal입니다.
 */
internal val LocalIenNavigationButtonVariant = staticCompositionLocalOf<IenButtonVariant?> { null }

/**
 * IenBackButton은 뒤로 가기 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 표시할 아이콘
 * @param enabled 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenBackButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IenTooltipBox(
        label = stringResource(Res.string.back)
    ) {
        Box(
            modifier = modifier
                .shadow(elevation = IenTheme.elevation.floating, shape = CircleShape, clip = false)
                .background(IenTheme.colors.surface.copy(alpha = 0.92f), CircleShape)
                .clip(CircleShape)
                .padding(IenTheme.spacing.xxs),
            contentAlignment = Alignment.Center,
        ) {
            IenIconButton(
                onClick = onClick,
                size = IenButtonSize.Medium,
                variant = IenButtonVariant.Ghost,
                tone = IenSemanticTone.Neutral,
                state = IenButtonState(enabled = enabled),
            ) {
                ComplexIcon(
                    icon = icon,
                    contentDescription = stringResource(Res.string.back)
                )
            }
        }
    }
}

/**
 * IenCloseButton은 닫기 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 표시할 아이콘
 * @param enabled 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenCloseButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalCloseButtonIcon.current ?: LocalButtonProviderDefault.CloseIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IenTooltipBox(
        label = stringResource(Res.string.close)
    ) {
        IenIconButton(
            onClick = onClick,
            modifier = modifier,
            size = IenButtonSize.Medium,
            variant = IenButtonVariant.Ghost,
            tone = IenSemanticTone.Neutral,
            state = IenButtonState(enabled = enabled),
        ) {
            ComplexIcon(
                icon = icon,
                contentDescription = stringResource(Res.string.close)
            )
        }
    }
}
