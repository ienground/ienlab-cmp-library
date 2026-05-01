package zone.ien.utils.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import zone.ien.utils.example.ui.screens.home.HomeScreen
import zone.ien.utils.example.ui.screens.lazy.LazySectionScreen
import zone.ien.utils.example.ui.screens.playground.PlaygroundScreen
import zone.ien.utils.example.ui.screens.section.SectionScreen
import zone.ien.utils.example.ui.screens.settings.SettingsScreen
import zone.ien.utils.navigation.BaseNavDisplay
import zone.ien.utils.navigation.getConfig
import zone.ien.utils.navigation.navigateBack
import zone.ien.utils.navigation.result.rememberResultStore

@Serializable
sealed interface RootRoute: NavKey {
    @Serializable data object Home: RootRoute
    @Serializable data object Settings: RootRoute
    @Serializable data object Playground: RootRoute
    @Serializable data object Section: RootRoute
    @Serializable data object LazySection: RootRoute
}

@Composable
fun RootNavigationGraph(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<RootRoute>
) {
    val resultStore = rememberResultStore()

    BaseNavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<RootRoute.Home> {
                HomeScreen(
                    backStack = backStack,
                    resultStore = resultStore
                )
            }
            entry<RootRoute.Settings> {
                SettingsScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.Playground> {
                PlaygroundScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.Section> {
                SectionScreen(
                    navigateBack = { backStack.navigateBack() },
                    resultStore = resultStore
                )
            }
            entry<RootRoute.LazySection> {
                LazySectionScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
        }
    )
}