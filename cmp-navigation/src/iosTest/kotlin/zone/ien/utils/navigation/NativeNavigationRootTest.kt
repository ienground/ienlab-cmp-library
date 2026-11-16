package zone.ien.utils.navigation

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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.childViewControllers
import zone.ien.navigation.NativeNavigationViewController
import zone.ien.navigation.NativeTab
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

    private fun verifyGraph(popAll: Boolean) {
        val graph = Route("메인 그래프")
        val tab = Route("타이머")
        val detail = Route("상세")
        val login = Route("로그인")
        val rootStack = NavBackStack<NavKey>(graph, login)
        val sharedStack = NavBackStack<NavKey>(detail)
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
                                BaseTabNavDisplay<NavKey>(
                                    tabs = listOf(NativeTab("타이머", "flame", listOf(tab))),
                                    selectedTab = 0,
                                    onTabSelected = {},
                                    sharedBackStack = sharedStack,
                                    onSharedBack = { sharedStack.removeLastOrNull() },
                                    entryProvider = { NavEntry(it) {} },
                                ) { error("iOS에서 다른 플랫폼 UI가 실행됨") }
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
            assertEquals(emptyList<NavKey>(), sharedStack.toList())
        } else {
            navigation.popViewControllerAnimated(false)
            navigation.delegate!!.navigationController(navigation, didShowViewController = navigation.topViewController!!, animated = false)
            assertEquals(listOf<NavKey>(graph), rootStack.toList())
            assertEquals(listOf<NavKey>(detail), sharedStack.toList())
            Snapshot.sendApplyNotifications()
            repeat(4) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }

            navigation.popViewControllerAnimated(false)
            navigation.delegate!!.navigationController(navigation, didShowViewController = navigation.topViewController!!, animated = false)
            assertEquals(emptyList<NavKey>(), sharedStack.toList())
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
