package zone.ien.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import platform.UIKit.UIColor
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.MaterialThemeSpec
import zone.ien.hig.adaptive.AdaptiveTheme
import kotlin.test.assertTrue
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import platform.CoreGraphics.CGPointMake
import platform.UIKit.UIScrollView
import zone.ien.utils.ui.screen.IenLazyColumn
import zone.ien.utils.ui.screen.ienVerticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.NativeScreenBar
import platform.UIKit.UIBarButtonItem
import platform.UIKit.tabBarItem
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
import platform.Foundation.timeIntervalSinceNow
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
import platform.UIKit.navigationItem
import platform.UIKit.childViewControllers
import zone.ien.navigation.nativeScreen
import zone.ien.navigation.NativeNavigationViewController
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class NativeNavigationRootTest {
    private val navigationVisible = mutableStateOf(true)
    private var nativeHost: UIViewController? = null

    private fun navigationController(
        create: (@Composable () -> Unit) -> UIViewController = { NativeNavigationViewController(content = it) },
        content: @Composable () -> Unit,
    ): UIViewController = create {
        if (navigationVisible.value) {
            val host = LocalUIViewController.current
            SideEffect { nativeHost = host }
            content()
        }
    }

    @AfterTest
    fun removeNativeNavigation() {
        val host = nativeHost ?: return
        nativeHost = null
        navigationVisible.value = false
        Snapshot.sendApplyNotifications()
        val deadline = NSDate.dateWithTimeIntervalSinceNow(1.0)
        while (host.childViewControllers.isNotEmpty() && deadline.timeIntervalSinceNow > 0) {
            NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02))
        }
        assertTrue(host.childViewControllers.isEmpty(), "테스트가 끝나면 네이티브 내비게이션 화면을 정리해야 합니다.")
    }

    private data class Route(val name: String) : NavKey
    private class ScopeModel(private val cleared: () -> Unit) : ViewModel() {
        override fun onCleared() = cleared()
    }

    @Test
    fun `하위 탭 그래프는 UI 없는 호스트에서 실행되고 상위 공유 화면을 받는다`() = verifyGraph(popAll = false)

    @Test
    fun `네이티브 루트까지 pop은 부모와 하위 공유 스택을 함께 정리한다`() = verifyGraph(popAll = true)

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `테마 brand는 화면 구성 전에 탭에 적용되고 다크 테마 변경을 따른다`() {
        val dark = mutableStateOf(false)
        val defaults = defaultIenTokens()
        val tokens = defaults.copy(
            lightColors = defaults.lightColors.copy(brand = Color.Red),
            darkColors = defaults.darkColors.copy(brand = Color.Blue),
        )
        val stack = NavBackStack(TabKey.Timer)
        var screenThemeApplied = false
        val controller = navigationController {
            BaseNavigationFrame(
                tokens = tokens,
                darkTheme = dark.value,
                frame = { content ->
                    AdaptiveTheme(target = Theme.Cupertino, material = MaterialThemeSpec.Default()) {
                        SideEffect { screenThemeApplied = true }
                        content()
                    }
                },
            ) {
                BaseNavigationTabs(
                    tabBackStack = stack, tabs = TabKey.entries, title = { it.name },
                    systemImage = { "house" }, tabContent = {},
                ) {
                    BaseNavDisplay(stack, entryProvider = { NavEntry(it) {} })
                }
            }
        }
        controller.loadViewIfNeeded()
        val tabs = assertIs<UITabBarController>(controller.childViewControllers.single())
        assertEquals(UIColor(red = 1.0, green = 0.0, blue = 0.0, alpha = 1.0), tabs.tabBar.tintColor)
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertTrue(screenThemeApplied, "실제 화면에서는 Cupertino 테마와 햅틱 환경이 함께 구성되어야 합니다.")
            dark.value = true
            Snapshot.sendApplyNotifications()
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertEquals(UIColor(red = 0.0, green = 0.0, blue = 1.0, alpha = 1.0), tabs.tabBar.tintColor)
        } finally {
            window.hidden = true
            window.rootViewController = null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `공통 스크롤 컨테이너는 목록과 일반 스크롤을 UIKit에 연결한다`() {
        val showList = mutableStateOf(true)
        val userScrollEnabled = mutableStateOf(true)
        val listState = LazyListState()
        val scrollState = ScrollState(0)
        val stack = NavBackStack(Route("scroll"))
        val controller = navigationController {
            BaseNavigationFrame {
                BaseNavDisplay(stack, entryProvider = { key ->
                    NavEntry(key) {
                        if (showList.value) {
                            IenLazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                                userScrollEnabled = userScrollEnabled.value,
                            ) {
                                items(100) { Box(Modifier.height(40.dp)) }
                            }
                        } else {
                            Column(Modifier.fillMaxSize().ienVerticalScroll(scrollState, enabled = userScrollEnabled.value)) {
                                repeat(100) { Box(Modifier.height(40.dp)) }
                            }
                        }
                    }
                })
            }
        }
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            repeat(20) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            val navigation = assertIs<UINavigationController>(controller.childViewControllers.single())
            val scrollView = assertIs<UIScrollView>(navigation.topViewController!!.view)
            scrollView.setContentOffset(CGPointMake(0.0, 200.0), animated = false)
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertTrue(listState.firstVisibleItemIndex > 0, "UIKit 스크롤이 LazyListState에 전달되어야 합니다.")
            userScrollEnabled.value = false
            Snapshot.sendApplyNotifications()
            repeat(20) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertFalse(scrollView.scrollEnabled, "목록의 사용자 스크롤을 끄면 UIKit 제스처도 비활성화되어야 합니다.")

            showList.value = false
            Snapshot.sendApplyNotifications()
            repeat(20) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertFalse(scrollView.scrollEnabled, "비활성화된 일반 스크롤에는 UIKit 제스처가 연결되면 안 됩니다.")

            userScrollEnabled.value = true
            Snapshot.sendApplyNotifications()
            repeat(20) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            scrollView.setContentOffset(CGPointMake(0.0, 200.0), animated = false)
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertTrue(scrollState.value > 0, "컨테이너 교체 후에는 새 ScrollState로 연결되어야 합니다.")
        } finally {
            window.hidden = true
            window.rootViewController = null
        }
    }

    private val tabIcon = ImageVector.Builder(
        name = "TabIcon", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4f, 4f)
            lineTo(20f, 4f)
            lineTo(12f, 20f)
            close()
        }
    }.build()

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
        val routeActivity = mutableMapOf<TabKey, Boolean>()
        val controller = navigationController {
            BaseNavigationFrame {
                BaseNavigationTabs(
                    tabBackStack = tabStack,
                    tabs = TabKey.entries,
                    title = { it.name },
                    icon = { IconData.Vector(tabIcon) },
                    selectedIcon = { IconData.Paint(ColorPainter(Color.Black)) },
                    tabContent = { tab ->
                        BaseNavigationTabBar { SideEffect { tabBars++ } }
                        BaseNavDisplay(
                            backStack = tabStack,
                            entryProvider = { key ->
                                NavEntry(key) {
                                    val current = currentNavigationRoute(tabStack)
                                    val active = isNavigationRouteActive(tabStack, tab)
                                    SideEffect {
                                        renderedTabs[tab] = requireNotNull(current)
                                        routeActivity[tab] = active
                                    }
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
                                NavEntry(key, metadata = nativeScreen(title = key.name)) {
                                    check(key != MainKey.Home) { "탭 루트는 UIKit이 표시해야 합니다." }
                                    NativeScreenBar(actions = listOf(ActionMenuItem.IconMenuItem.AlwaysShown(
                                        title = "저장", onClick = {}, icon = IconData.Vector(tabIcon),
                                    )))
                                    if (key == MainKey.Denied) SideEffect { deniedEntries++ }
                                }
                            },
                        )
                    }
                }
            }
        }
        controller.loadViewIfNeeded()
        val initialTabs = assertIs<UITabBarController>(controller.childViewControllers.single())
        val initialStack = assertIs<UINavigationController>(initialTabs.viewControllers!![0])
        assertEquals("Detail", initialStack.topViewController!!.navigationItem.title)
        assertEquals(0, detailDecorators, "화면 본문이 구성되기 전에도 초기 제목을 표시해야 합니다.")
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            controller.loadViewIfNeeded()
            val tabs = assertIs<UITabBarController>(controller.childViewControllers.single())
            assertEquals(2, tabs.viewControllers!!.size)
            val timer = assertIs<UINavigationController>(tabs.viewControllers!![0])
            assertEquals(2, timer.viewControllers.size)
            assertNotNull(timer.tabBarItem.image)
            assertNotNull(timer.tabBarItem.selectedImage)
            timer.topViewController!!.loadViewIfNeeded()
            repeat(10) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.05)) }
            assertEquals(1, detailDecorators, "공유 엔트리 decorator는 중복 실행되면 안 됩니다.")
            assertEquals(1, overlays, "overlay는 보이는 화면에서만 실행되어야 합니다.")
            assertNotNull(assertIs<UIBarButtonItem>(timer.topViewController!!.navigationItem.rightBarButtonItems!!.single()).image)

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
            assertEquals(true, routeActivity[TabKey.Settings])
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

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `공통 entry 메타데이터는 개별 설정을 우선하고 뒤로가기 시 화면을 보존한다`() {
        val first = Route("화면")
        val second = Route("개별")
        val stack = NavBackStack(first, second)
        val controller = navigationController {
            BaseNavigationFrame {
                BaseNavDisplay(
                    backStack = stack,
                    entryMetadata = { route -> nativeScreen(title = "기본 ${route.name}") },
                    entryProvider = { route ->
                        NavEntry(route, metadata = if (route == second) nativeScreen(title = "개별 제목") else emptyMap()) {}
                    },
                )
            }
        }
        controller.loadViewIfNeeded()
        val navigation = assertIs<UINavigationController>(controller.childViewControllers.single())
        assertEquals("개별 제목", navigation.topViewController!!.navigationItem.title)
        val firstController = navigation.viewControllers.first() as UIViewController
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            stack.removeLastOrNull()
            Snapshot.sendApplyNotifications()
            repeat(25) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            assertEquals("기본 화면", navigation.topViewController!!.navigationItem.title)
            kotlin.test.assertSame(firstController, navigation.topViewController)
        } finally {
            window.hidden = true
            window.rootViewController = null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `같은 세션의 마지막 화면이 빠질 때만 공유 ViewModel을 정리한다`() {
        val stack = NavBackStack(Route("루트"), Route("채팅"), Route("리뷰"))
        var currentModel: ScopeModel? = null
        var cleared = 0
        val controller = navigationController {
            BaseNavigationFrame {
                val owners = rememberNavigationViewModelStoreOwners(stack) { route ->
                    "대화".takeIf { route.name != "루트" }
                }
                val model = owners["대화"]?.let { owner ->
                    viewModel<ScopeModel>(viewModelStoreOwner = owner) { ScopeModel { cleared++ } }
                }
                SideEffect { currentModel = model }
                BaseNavDisplay(backStack = stack, entryProvider = { NavEntry(it) {} })
            }
        }
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            fun removeTopAndWait() {
                stack.removeLastOrNull()
                Snapshot.sendApplyNotifications()
                repeat(25) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            }
            repeat(25) {
                if (currentModel == null) NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02))
            }
            val initialModel = assertNotNull(currentModel)
            removeTopAndWait()
            assertSame(initialModel, currentModel)
            assertEquals(0, cleared)
            removeTopAndWait()
            assertEquals(null, currentModel)
            assertEquals(1, cleared)
        } finally {
            window.hidden = true
            window.rootViewController = null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun `iOS 공통 호스트는 초기화와 네이티브 화면 연결을 수행한다`() {
        val controller = navigationController(create = ::BaseNavigationViewController) {
            BaseNavigationFrame {
                BaseNavDisplay(
                    backStack = NavBackStack(Route("호스트")),
                    entryMetadata = { nativeScreen(title = it.name) },
                    entryProvider = { NavEntry(it) {} },
                )
            }
        }
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window.rootViewController = controller
        window.makeKeyAndVisible()
        try {
            repeat(20) { NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.02)) }
            fun findNavigation(root: UIViewController): UINavigationController? =
                root as? UINavigationController ?: root.childViewControllers
                    .filterIsInstance<UIViewController>().firstNotNullOfOrNull(::findNavigation)
            val navigation = assertNotNull(findNavigation(controller))
            assertEquals("호스트", navigation.topViewController!!.navigationItem.title)
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
        val controller = navigationController {
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
