package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.CoreGraphics.CGRectMake
import kotlinx.cinterop.ExperimentalForeignApi
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.feedback.IenBottomSheet
import zone.ien.utils.ui.feedback.IenBottomSheetState
import zone.ien.utils.ui.feedback.IenSheetDetent

class IenDuoLayoutTest {
    @OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
    @Test
    fun openSheetDoesNotResizeToStaleHostWindowBounds(): TestResult = runComposeUiTest {
        val state = IenBottomSheetState(visible = false, detent = IenSheetDetent.Medium)
        val staleHostSize = mutableStateOf<IntSize?>(null)
        var originalWindowSize = IntSize.Zero
        var contentToken: Any? = null
        setContent {
            val windowInfo = LocalWindowInfo.current
            originalWindowSize = windowInfo.containerSize
            val hostWindowInfo = remember(windowInfo) {
                object : WindowInfo by windowInfo {
                    override val containerSize: IntSize
                        get() = staleHostSize.value ?: windowInfo.containerSize
                }
            }
            IenTheme {
                val density = LocalDensity.current.density
                val window = remember {
                    UIWindow(CGRectMake(
                            0.0, 0.0,
                            (originalWindowSize.width / density).toDouble(),
                            (originalWindowSize.height / density).toDouble(),
                    ))
                }
                val controller = remember(window) {
                    UIViewController().also { window.addSubview(it.view) }
                }
                CompositionLocalProvider(
                    LocalUIViewController provides controller,
                    LocalWindowInfo provides hostWindowInfo,
                ) {
                    IenBottomSheet(state = state) {
                        val token = remember { Any() }
                        SideEffect { contentToken = token }
                        Box(Modifier.fillMaxSize().testTag("rotating-sheet-body"))
                    }
                }
            }
        }
        runOnIdle { state.show(IenSheetDetent.Medium) }
        val initialBounds = onNodeWithTag("rotating-sheet-body", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot
        var initialToken: Any? = null
        runOnIdle {
            initialToken = contentToken
            staleHostSize.value = IntSize(originalWindowSize.width / 2, originalWindowSize.height)
        }
        mainClock.advanceTimeBy(500)
        waitForIdle()
        assertEquals(initialBounds, onNodeWithTag("rotating-sheet-body", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot)
        runOnIdle { assertSame(initialToken, contentToken) }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun sheetEntersFromBelowAfterItsFirstLayout(): TestResult = runComposeUiTest {
        val state = IenBottomSheetState(visible = false, detent = IenSheetDetent.Medium)
        setContent {
            IenTheme {
                val controller = remember { UIViewController() }
                CompositionLocalProvider(LocalUIViewController provides controller) {
                    IenBottomSheet(state = state) {
                        Box(Modifier.fillMaxSize().testTag("entering-sheet-body"))
                    }
                }
            }
        }
        mainClock.autoAdvance = false
        runOnIdle { state.show(IenSheetDetent.Medium) }
        mainClock.advanceTimeBy(64)
        waitForIdle()
        val firstTop = onNodeWithTag("entering-sheet-body", useUnmergedTree = true).getUnclippedBoundsInRoot().top
        mainClock.advanceTimeBy(64)
        waitForIdle()
        val middleTop = onNodeWithTag("entering-sheet-body", useUnmergedTree = true).getUnclippedBoundsInRoot().top
        mainClock.advanceTimeBy(400)
        waitForIdle()
        val finalTop = onNodeWithTag("entering-sheet-body", useUnmergedTree = true).getUnclippedBoundsInRoot().top
        assertTrue(firstTop > middleTop, "진입 초기부터 중간 프레임까지 위로 이동해야 합니다.")
        assertTrue(middleTop > finalTop, "중간 프레임에서 최종 위치까지 계속 올라와야 합니다.")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun reopenedSheetUsesRequestedHeightFromFirstLayout(): TestResult = runComposeUiTest {
        val state = IenBottomSheetState(visible = false, detent = IenSheetDetent.Medium)
        val heights = mutableListOf<Int>()
        setContent {
            IenTheme {
                val controller = remember { UIViewController() }
                CompositionLocalProvider(LocalUIViewController provides controller) {
                    IenBottomSheet(state = state) {
                        Box(Modifier.fillMaxSize().onSizeChanged { heights.add(it.height) })
                    }
                }
            }
        }
        runOnIdle { state.show(IenSheetDetent.Medium) }
        waitForIdle()
        var mediumHeight = 0
        runOnIdle {
            mediumHeight = heights.last()
            state.hide()
        }
        mainClock.advanceTimeBy(1000)
        waitForIdle()
        runOnIdle {
            heights.clear()
            state.show(IenSheetDetent.Full)
        }
        waitForIdle()
        runOnIdle {
            assertTrue(heights.isNotEmpty())
            assertTrue(heights.first() > mediumHeight)
            assertEquals(1, heights.distinct().size, "다시 연 시트는 첫 배치부터 요청한 높이를 유지해야 합니다.")
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun horizontalActionsStayBesideTheStatusRegion(): TestResult = runComposeUiTest {
        val direction = mutableStateOf(LayoutDirection.Ltr)
        val alignment = mutableStateOf(IenTopBarTitleAlignment.Start)
        val placement = mutableStateOf<IenBarPlacement?>(
            IenBarPlacement(topStatusBounds = DpRect(280.dp, 8.dp, 400.dp, 72.dp)),
        )
        setContent {
            IenTheme {
                CompositionLocalProvider(
                    LocalLayoutDirection provides direction.value,
                    LocalIenBarPlacement provides placement.value,
                ) {
                    Box(Modifier.width(400.dp)) {
                        IenTopBar(
                            title = "Duo",
                            titleAlignment = alignment.value,
                            windowInsets = WindowInsets(top = 72.dp),
                            navigationIcon = { Box(Modifier.size(48.dp).testTag("navigation")) },
                            actions = { Box(Modifier.size(48.dp).testTag("action")) },
                            primaryAction = { Box(Modifier.size(48.dp).testTag("primary")) },
                        )
                        Box(
                            Modifier.align(AbsoluteAlignment.TopLeft)
                                .absoluteOffset(x = 280.dp, y = 8.dp)
                                .size(120.dp, 64.dp)
                                .testTag("status"),
                        )
                    }
                }
            }
        }
        for (layoutDirection in LayoutDirection.entries) {
            for (titleAlignment in IenTopBarTitleAlignment.entries) {
                runOnIdle {
                    direction.value = layoutDirection
                    alignment.value = titleAlignment
                }
                val status = onNodeWithTag("status").fetchSemanticsNode().boundsInRoot
                for (tag in listOf("navigation", "action", "primary")) {
                    val button = onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot
                    assertEquals(status.center.y, button.center.y, 1f, "$layoutDirection $titleAlignment $tag center")
                    assertTrue(button.right < status.left, "$layoutDirection $titleAlignment $tag overlaps status")
                }
            }
        }
        runOnIdle { placement.value = null }
        val status = onNodeWithTag("status").fetchSemanticsNode().boundsInRoot
        val action = onNodeWithTag("action").fetchSemanticsNode().boundsInRoot
        assertTrue(action.top >= status.bottom, "일반 기기는 상태 표시줄 아래에 배치해야 합니다.")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun topBarRespectsHorizontalWindowInsets(): TestResult = runComposeUiTest {
        val direction = mutableStateOf(LayoutDirection.Ltr)
        setContent {
            IenTheme {
                CompositionLocalProvider(LocalLayoutDirection provides direction.value) {
                    IenTopBar(
                        title = "Duo",
                        modifier = Modifier.fillMaxWidth().testTag("bar"),
                        windowInsets = WindowInsets(left = 80, top = 0, right = 100, bottom = 0),
                        navigationIcon = { Box(Modifier.size(48.dp).testTag("navigation")) },
                        actions = { Box(Modifier.size(48.dp).testTag("action")) },
                    )
                }
            }
        }
        for (layoutDirection in LayoutDirection.entries) {
            runOnIdle { direction.value = layoutDirection }
            val bar = onNodeWithTag("bar").fetchSemanticsNode().boundsInRoot
            for (tag in listOf("navigation", "action")) {
                val button = onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot
                assertTrue(button.left >= bar.left + 80, "$layoutDirection $tag left")
                assertTrue(button.right <= bar.right - 100, "$layoutDirection $tag right")
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun changingBarGeometryDoesNotRecomposeUnrelatedContent(): TestResult = runComposeUiTest {
        val placement = mutableStateOf(IenBarPlacement(sideWidth = 84.dp))
        var unrelatedCompositions = 0
        var observedWidth = 0.dp
        setContent {
            val content = remember {
                @Composable {
                    UnrelatedContent { unrelatedCompositions++ }
                    BarGeometryReader { observedWidth = it }
                }
            }
            CompositionLocalProvider(LocalIenBarPlacement provides placement.value, content = content)
        }
        var initialCompositions = 0
        runOnIdle {
            initialCompositions = unrelatedCompositions
            placement.value = IenBarPlacement(sideWidth = 76.dp)
        }
        runOnIdle {
            assertEquals(76.dp, observedWidth)
            assertEquals(initialCompositions, unrelatedCompositions)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rotatingBetweenSideAndHorizontalBarsPreservesSlotState(): TestResult = runComposeUiTest {
        val placement = mutableStateOf(IenBarPlacement())
        val slots = mutableMapOf<String, Any>()
        setContent {
            IenTheme {
                val controller = remember { UIViewController() }
                CompositionLocalProvider(
                    LocalUIViewController provides controller,
                    LocalIenBarPlacement provides placement.value,
                ) {
                    IenScaffold(
                        topBar = { RememberedSlot { slots["top"] = it } },
                        bottomBar = { RememberedSlot { slots["bottom"] = it } },
                        floating = { RememberedSlot { slots["floating"] = it } },
                        snackbarHost = { RememberedSlot { slots["snackbar"] = it } },
                    ) {
                        RememberedSlot { slots["content"] = it }
                    }
                }
            }
        }
        var initialSlots = emptyMap<String, Any>()
        runOnIdle {
            initialSlots = slots.toMap()
            assertEquals(setOf("top", "bottom", "floating", "snackbar", "content"), initialSlots.keys)
            placement.value = IenBarPlacement(sideWidth = 84.dp, controlsTop = 120.dp, controlsBottom = 24.dp)
        }
        runOnIdle {
            initialSlots.forEach { (name, token) -> assertSame(token, slots[name], name) }
            placement.value = IenBarPlacement()
        }
        runOnIdle {
            initialSlots.forEach { (name, token) -> assertSame(token, slots[name], name) }
        }
    }
}

@Composable
private fun UnrelatedContent(onCompose: () -> Unit) {
    SideEffect(onCompose)
}

@Composable
private fun BarGeometryReader(onWidth: (Dp) -> Unit) {
    val width = LocalIenBarPlacement.current?.sideWidth ?: 0.dp
    SideEffect { onWidth(width) }
}

@Composable
private fun RememberedSlot(onToken: (Any) -> Unit) {
    val token = remember { Any() }
    SideEffect { onToken(token) }
    Box(Modifier.height(48.dp))
}
