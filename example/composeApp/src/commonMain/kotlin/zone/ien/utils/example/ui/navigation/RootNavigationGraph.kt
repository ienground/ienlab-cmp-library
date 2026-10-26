package zone.ien.utils.example.ui.navigation

import zone.ien.utils.icon.IconStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import zone.ien.utils.example.ui.screens.designsystem.ColorTokenScreen
import zone.ien.utils.example.ui.screens.home.HomeScreen
import zone.ien.utils.example.ui.screens.designsystem.DesignSystemScreen
import zone.ien.utils.example.ui.screens.lazy.LazySectionScreen
import zone.ien.utils.example.ui.screens.navigation.NavigationScreen
import zone.ien.utils.example.ui.screens.playground.AdaptivePlaygroundScreen
import zone.ien.utils.example.ui.screens.playground.HapticFeedbackTestScreen
import zone.ien.utils.example.ui.screens.playground.PlaygroundScreen
import zone.ien.utils.example.ui.screens.playground.IenPlaygroundScreen
import zone.ien.utils.example.ui.screens.section.SectionScreen
import zone.ien.utils.example.ui.screens.settings.SettingsScreen
import zone.ien.utils.example.ui.screens.auth.FirebaseAuthScreen
import zone.ien.utils.example.ui.screens.auth.AuthFormScreen
import zone.ien.utils.example.ui.screens.scrollingbubble.ScrollingBubbleScreen
import zone.ien.utils.example.ui.screens.permission.PermissionScreen
import zone.ien.utils.navigation.BaseNavDisplay
import zone.ien.utils.navigation.getConfig
import zone.ien.utils.navigation.navigateBack
import zone.ien.utils.navigation.result.rememberResultStore
import zone.ien.utils.ui.foundation.IenTheme

@Serializable
sealed interface RootRoute: NavKey {
    @Serializable data object Home: RootRoute
    @Serializable data object Settings: RootRoute
    @Serializable data object Playground: RootRoute
    @Serializable data object IenPlayground: RootRoute
    @Serializable data object HapticFeedbackTest: RootRoute
    @Serializable data object AdaptivePlayground: RootRoute
    @Serializable data object Section: RootRoute
    @Serializable data object LazySection: RootRoute
    @Serializable data object Navigation: RootRoute
    @Serializable data object DesignSystem: RootRoute
    @Serializable data class DesignSystemComponent(val componentId: String): RootRoute
    @Serializable data object ColorTokens: RootRoute
    @Serializable data object FirebaseAuth: RootRoute
    @Serializable data object AuthForm: RootRoute
    @Serializable data object ScrollingBubble: RootRoute
    @Serializable data object Permission: RootRoute
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RootNavigationGraph(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<RootRoute>,
    iconStyle: IconStyle,
    onIconStyleChange: (IconStyle) -> Unit,
) {
    val resultStore = rememberResultStore()
    val listDetailStrategy = rememberListDetailSceneStrategy<RootRoute>(
        directive = calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(
            currentWindowAdaptiveInfoV2(),
        ),
    )
    val isListDetailLayout = listDetailStrategy.directive.maxHorizontalPartitions > 1

    BaseNavDisplay(
        backStack = backStack,
        modifier = modifier,
        sceneStrategies = listOf(listDetailStrategy),
        entryProvider = entryProvider {
            entry<RootRoute.Home> {
                HomeScreen(
                    backStack = backStack,
                    resultStore = resultStore,
                    iconStyle = iconStyle,
                    onIconStyleChange = onIconStyleChange,
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
            entry<RootRoute.IenPlayground> {
                IenPlaygroundScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.HapticFeedbackTest> {
                HapticFeedbackTestScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.AdaptivePlayground> {
                AdaptivePlaygroundScreen(
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
            entry<RootRoute.Navigation> {
                NavigationScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.DesignSystem>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(IenTheme.spacing.lg),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "목록에서 컴포넌트를 선택하세요.",
                                style = IenTheme.typography.body1,
                                color = IenTheme.colors.textSecondary,
                            )
                        }
                    },
                ) + ListDetailSceneStrategy.preferredPaneSize(width = 0.4f),
            ) {
                DesignSystemScreen(
                    navigateBack = {
                        while (backStack.lastOrNull() is RootRoute.DesignSystemComponent) {
                            backStack.navigateBack()
                        }
                        backStack.navigateBack()
                    },
                    navigateToColor = { backStack.add(RootRoute.ColorTokens) },
                    navigateToComponent = {
                        val route = RootRoute.DesignSystemComponent(it)
                        if (backStack.lastOrNull() != route) {
                            if (isListDetailLayout) {
                                while (backStack.lastOrNull() is RootRoute.DesignSystemComponent) {
                                    backStack.navigateBack()
                                }
                            }
                            backStack.add(route)
                        }
                    },
                )
            }
            entry<RootRoute.DesignSystemComponent>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { route ->
                DesignSystemScreen(
                    navigateBack = { backStack.navigateBack() },
                    navigateToColor = { backStack.add(RootRoute.ColorTokens) },
                    componentId = route.componentId,
                )
            }
            entry<RootRoute.ColorTokens> {
                ColorTokenScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.FirebaseAuth> {
                FirebaseAuthScreen(
                    navigateBack = { backStack.navigateBack() }
                )
            }
            entry<RootRoute.AuthForm> {
                AuthFormScreen(
                    navigateBack = { backStack.navigateBack() },
                )
            }
            entry<RootRoute.ScrollingBubble> {
                ScrollingBubbleScreen(
                    navigateBack = { backStack.navigateBack() },
                )
            }
            entry<RootRoute.Permission> {
                PermissionScreen(
                    navigateBack = { backStack.navigateBack() },
                )
            }
        }
    )
}
