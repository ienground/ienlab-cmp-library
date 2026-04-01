package zone.ien.utils.adaptive.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import zone.ien.hig.CupertinoNavigationBarItemData
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveNavigationBarNative
import zone.ien.hig.adaptive.CupertinoNavigationBarAdaptation
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.MaterialNavigationBarAdaptation
import zone.ien.utils.icon.IconData

data class NavigationBarItem(
    val onClick: () -> Unit,
    val icon: IconData,
    val selectedIcon: IconData? = null,
    val label: String
)

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveNavigationBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    adaptation: AdaptationScope<CupertinoNavigationBarAdaptation, MaterialNavigationBarAdaptation>.() -> Unit = {},
    items: List<NavigationBarItem>
) {
    AdaptiveNavigationBarNative(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = onTabSelected,
        adaptation = adaptation,
        items = items.map {
            CupertinoNavigationBarItemData(
                onClick = it.onClick,
                icon = when (it.icon) {
                    is IconData.Vector -> rememberVectorPainter(it.icon.imageVector)
                    is IconData.Paint -> it.icon.painter
                },
                selectedIcon = it.selectedIcon?.let {
                    when (it) {
                        is IconData.Vector -> rememberVectorPainter(it.imageVector)
                        is IconData.Paint -> it.painter
                    }
                },
                label = it.label
            )
        }
    )
}