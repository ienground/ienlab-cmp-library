package zone.ien.utils.example.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()

    AdaptiveTopAppBarScaffold(
        navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
        modifier = modifier
    ) { pv, title ->

    }
}