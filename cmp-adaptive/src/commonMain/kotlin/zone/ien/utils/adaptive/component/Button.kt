package zone.ien.utils.adaptive.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.CupertinoNavigateBackButton
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.ui.screen.M3BackButton

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveBackButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean = true,
    onClick: () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3BackButton(
                modifier = modifier,
                icon = icon,
                enabled = enabled,
                onClick = onClick
            )
        },
        cupertino = {
            CupertinoLiquidIconButton(
                modifier = modifier.padding(horizontal = 16.dp),
                enabled = enabled,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive,
                onClick = onClick
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    )
}

