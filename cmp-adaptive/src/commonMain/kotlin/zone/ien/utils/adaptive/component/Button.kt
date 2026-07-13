package zone.ien.utils.adaptive.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.ui.screen.IenBackButton

/**
 * 적응형 뒤로가기 버튼 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param icon 뒤로가기 아이콘 데이터
 * @param enabled 버튼 활성화 여부
 * @param visible 버튼 가시성 여부
 * @param backdrop Backdrop 컴포넌트 (iOS에서 사용함)
 * @param isBackgroundAdaptive 배경 적응 여부 (iOS에서 사용함)
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return 뒤로가기 버튼 컴포저블
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveBackButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    visible: Boolean = true,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean = true,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(1.2f)
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(spring(1.2f)),
        exit = slideOutHorizontally(spring(1.2f))
    ) {
        AdaptiveWidget(
            material = {
                IenBackButton(
                    modifier = modifier,
                    icon = icon,
                    enabled = enabled,
                    onClick = onClick
                )
            },
            cupertino = {
                CupertinoLiquidIconButton(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .graphicsLayer {
                            this.alpha = alpha
                            this.compositingStrategy = CompositingStrategy.ModulateAlpha
                        }
                    ,
                    enabled = enabled,
                    backdrop = backdrop,
                    isBackgroundAdaptive = isBackgroundAdaptive,
                    onClick = onClick
                ) {
                    ComplexIcon(
                        icon = icon,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

