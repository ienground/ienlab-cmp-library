package zone.ien.utils.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.back
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.icon.LocalCloseButtonIcon
import zone.ien.utils.ui.view.MyTooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3BackButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    MyTooltipBox(
        label = stringResource(Res.string.back)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(Res.string.back)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3CloseButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = LocalCloseButtonIcon.current ?: LocalButtonProviderDefault.CloseIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    MyTooltipBox(
        label = stringResource(Res.string.close)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(Res.string.close)
            )
        }
    }
}