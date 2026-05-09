package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.back
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.icon.LocalCloseButtonIcon
import zone.ien.utils.ui.view.M3TooltipBox

/**
 * M3BackButton은 뒤로 가기 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 표시할 아이콘
 * @param enabled 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3BackButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    M3TooltipBox(
        label = stringResource(Res.string.back)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            ComplexIcon(
                icon = icon,
                contentDescription = stringResource(Res.string.back)
            )
        }
    }
}

/**
 * M3CloseButton은 닫기 버튼을 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 표시할 아이콘
 * @param enabled 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3CloseButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalCloseButtonIcon.current ?: LocalButtonProviderDefault.CloseIcon,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    M3TooltipBox(
        label = stringResource(Res.string.close)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
        ) {
            ComplexIcon(
                icon = icon,
                contentDescription = stringResource(Res.string.close)
            )
        }
    }
}