package zone.ien.utils.navigation

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import platform.Foundation.NSDate
import platform.Foundation.NSRunLoop
import platform.Foundation.runUntilDate
import platform.Foundation.dateWithTimeIntervalSinceNow
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIScreen
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.childViewControllers
import zone.ien.navigation.NativeNavigationViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertNotNull

class NativeNavigationRootTest {
    private data class Route(val name: String) : NavKey
    private class ScopeModel(private val cleared: () -> Unit) : ViewModel() {
        override fun onCleared() = cleared()
    }

    @Test
    fun `하위 탭 그래프는 UI 없는 호스트에서 실행되고 상위 공유 화면을 받는다`() = verifyGraph(popAll = false)

    @Test
    fun `네이티브 루트까지 pop은 부모와 하위 공유 스택을 함께 정리한다`() = verifyGraph(popAll = true)

    private enum class MainKey : NavKey { Home, Detail, Denied }
    private enum class TabKey : NavKey { Timer, Settings }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `기존 typed NavDisplay를 감싸면 탭 선택과 공유 pop 및 overlay를 공통으로 처리한다`() {
        val mainStack = NavBackStack(MainKey.Home, MainKey.Detail)
        val tabStack = NavBackStack(TabKey.Timer)
        var detailDecorators = 0
        var overlays = 0
        var tabBars = 0
        var deniedEntries = 0
        var deniedCallbacks = 0
        val renderedTabs = mutableMapOf<TabKey, TabKey>()
        val controller = NativeNavigationViewController {
            BaseNavigationFrame {
                BaseNavigationTabs(
                    tabBackStack = tabStack,
                    tabs = TabKey.entries,
                    title = { it.name },
                    systemImage = { "flame" },
                    tabContent = { tab ->
                        BaseNavigationTabBar { SideEffect { tabBars++ } }
                        BaseNavDisplay(
                            backStack = tabStack,
                            entryProvider = { key ->
                                NavEntry(key) {
                                    val current = currentNavigationRoute(tabStack)
                                    SideEffect { renderedTabs[tab] = requireNotNull(current) }
                                }
                            },
                        )
                    },
                    overlay = {
                        DisposableEffect(Unit) {
                            overlays++
                            onDispose { overlays-- }
                        }
                    },
                ) {
                    BaseNavigationGuard(
                        backStack = mainStack,
                        isAllowed = { it != MainKey.Denied },
                        onDenied = { deniedCallbacks++ },
                    ) {
                        BaseNavDisplay(
                            backStack = mainStack,
                            entryDecorators = listOf(NavEntryDecorator { entry ->
                                DisposableEffect(entry.contentKey) {
                                    detailDecorators++
                                    onDispose { detailDecorators-- }
                                }
                                entry.Content()
                            }),
                            entryProvider = { key ->
                                check(key in mainStack) { "제거된 엔트리의 provider를 다시 조회하면 안 됩니다." }
                                NavEntry(key) {
                                    check(key != MainKey.Home) { "탭 루트는 UIKit이 표시해야 합니다." }
                                    if (key == MainKey.Denied) SideEffect { deniedEntries++ }
                                }
                            },
                        )
                    }
                }
            }
        }
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            controller.loadViewIfNeeded()
            val tabs = assertIs<UITabBarController>(controller.childViewControllers.single())
            assertEquals(2, tabs.viewControllers!!.size)
            val timer = assertIs<UINavigationController>(tabs.viewControllers!![0])
            assertEquals(2, timer.viewControllers.size)
            timer.topViewController!!.loadViewIfNeeded()
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            assertEquals(1, detailDecorators, "공유 엔트리 decorator는 중복 실행되면 안 됩니다.")
            assertEquals(1, overlays, "overlay는 보이는 화면에서만 실행되어야 합니다.")

            timer.popToRootViewControllerAnimated(false)
            timer.delegate!!.navigationController(timer, didShowViewController = timer.topViewController!!, animated = false)
            assertEquals(listOf(MainKey.Home), mainStack.toList())
            Snapshot.sendApplyNotifications()
            repeat(6) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            assertEquals(1, timer.viewControllers.size)
            assertEquals(TabKey.Timer, renderedTabs[TabKey.Timer])

            val settings = assertIs<UINavigationController>(tabs.viewControllers!![1])
            tabs.selectedIndex = 1u
            tabs.delegate!!.tabBarController(tabs, didSelectViewController = settings)
            assertEquals(listOf(TabKey.Settings), tabStack.toList())
            settings.topViewController!!.loadViewIfNeeded()
            Snapshot.sendApplyNotifications()
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            assertEquals(TabKey.Settings, renderedTabs[TabKey.Settings])
            assertEquals(TabKey.Timer, renderedTabs[TabKey.Timer], "숨겨진 탭의 route는 다른 탭 선택으로 바뀌면 안 됩니다.")
            assertEquals(0, tabBars, "네이티브 탭 위에 앱 탭 바를 중복 표시하면 안 됩니다.")

            mainStack.add(MainKey.Denied)
            Snapshot.sendApplyNotifications()
            repeat(6) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            settings.topViewController!!.loadViewIfNeeded()
            repeat(6) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            assertEquals(listOf(MainKey.Home), mainStack.toList())
            assertEquals(1, settings.viewControllers.size)
            assertEquals(0, deniedEntries, "허용되지 않은 화면의 내용과 진입 효과를 실행하면 안 됩니다.")
            assertEquals(1, deniedCallbacks)
        } finally {
            window.hidden = true
            window.rootViewController = null
        }
    }

    private fun verifyGraph(popAll: Boolean) {
        val graph = Route("메인 그래프")
        val tab = Route("타이머")
        val detail = Route("상세")
        val login = Route("로그인")
        val rootStack = NavBackStack<NavKey>(graph, login)
        val home = Route("홈")
        val tabStack = NavBackStack<NavKey>(tab)
        val sharedStack = NavBackStack<NavKey>(home, detail)
        var hostOwner: ViewModelStoreOwner? = null
        var graphOwner: ViewModelStoreOwner? = null
        var cleared = 0
        val controller = NativeNavigationViewController {
            BaseNavigationFrame {
                hostOwner = LocalViewModelStoreOwner.current
                BaseNavDisplay(
                    backStack = rootStack,
                    entryProvider = entryProvider<NavKey> {
                        entry<Route>(metadata = { key ->
                            if (key == graph) BaseNavDisplay.navigationGraph() else emptyMap()
                        }) { key ->
                            if (key == graph) {
                                graphOwner = LocalViewModelStoreOwner.current
                                viewModel<ScopeModel> { ScopeModel { cleared++ } }
                                BaseNavigationTabs(
                                    tabBackStack = tabStack,
                                    tabs = listOf(tab),
                                    title = { "타이머" },
                                    systemImage = { "flame" },
                                    tabContent = {},
                                ) {
                                    BaseNavDisplay(
                                        backStack = sharedStack,
                                        entryProvider = { NavEntry(it) {} },
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
        controller.loadViewIfNeeded()
        val tabs = assertIs<UITabBarController>(controller.childViewControllers.single())
        val navigation = assertIs<UINavigationController>(tabs.viewControllers!!.single())
        assertEquals(3, navigation.viewControllers.size)
        assertIs<UIViewController>(navigation.topViewController)
        assertNotNull(graphOwner)
        assertNotSame(hostOwner, graphOwner)

        if (popAll) {
            navigation.popToRootViewControllerAnimated(false)
            navigation.delegate!!.navigationController(navigation, didShowViewController = navigation.topViewController!!, animated = false)
            assertEquals(listOf<NavKey>(graph), rootStack.toList())
            assertEquals(listOf<NavKey>(home), sharedStack.toList())
        } else {
            navigation.popViewControllerAnimated(false)
            navigation.delegate!!.navigationController(navigation, didShowViewController = navigation.topViewController!!, animated = false)
            assertEquals(listOf<NavKey>(graph), rootStack.toList())
            assertEquals(listOf<NavKey>(home, detail), sharedStack.toList())
            Snapshot.sendApplyNotifications()
            repeat(4) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }

            navigation.popViewControllerAnimated(false)
            navigation.delegate!!.navigationController(navigation, didShowViewController = navigation.topViewController!!, animated = false)
            assertEquals(listOf<NavKey>(home), sharedStack.toList())
        }
        assertEquals(0, cleared)

        rootStack.clear()
        rootStack.add(login)
        Snapshot.sendApplyNotifications()
        repeat(40) {
            if (cleared == 0) NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05))
        }
        assertEquals(1, cleared, "하위 그래프를 떠날 때 ViewModel이 정리되어야 함")
    }
}
