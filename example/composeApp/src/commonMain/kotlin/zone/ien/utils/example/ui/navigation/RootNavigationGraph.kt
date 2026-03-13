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
import zone.ien.utils.navigation.navigateBack

@Serializable
sealed interface RootRoute: NavKey {
    @Serializable data object Home: RootRoute
    @Serializable data object Settings: RootRoute
    @Serializable data object Playground: RootRoute
    @Serializable data object Section: RootRoute
    @Serializable data object LazySection: RootRoute
}

val rootConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RootRoute.Home::class, RootRoute.Home.serializer())
            subclass(RootRoute.Settings::class, RootRoute.Settings.serializer())
            subclass(RootRoute.Playground::class, RootRoute.Playground.serializer())
            subclass(RootRoute.Section::class, RootRoute.Section.serializer())
            subclass(RootRoute.LazySection::class, RootRoute.LazySection.serializer())
        }
    }
}

@Composable
fun RootNavigationGraph(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {
    BaseNavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<RootRoute.Home> {
                HomeScreen(
                    backStack = backStack
                )
            }
            entry<RootRoute.Settings> {
                SettingsScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.Playground> {
                PlaygroundScreen()
            }
            entry<RootRoute.Section> {
                SectionScreen(
                    navigateBack = { backStack.navigateBack() }
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