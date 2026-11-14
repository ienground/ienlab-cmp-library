package zone.ien.utils.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.Image
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.addressOf
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.create
import platform.Foundation.writeToFile
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.foundation.IenTheme
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import platform.UIKit.UIViewController
import zone.ien.utils.ui.screen.IenTopBarTransition
import zone.ien.utils.ui.screen.LocalIenTopBarTransition
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
class NavigationTopBarTransitionTest {
    private data object Root : NavKey
    private data object Detail : NavKey

    @Test
    fun matchedButtonsStayAtTheTopDuringPushAndPop() = verifyButtonPosition(true)

    @Test
    fun unmatchedButtonStaysAtTheTopDuringPushAndPop() = verifyButtonPosition(false)

    @Test
    fun actionsResizeAtTheTrailingEdge() = verifyButtonPosition(true, actions = true)

    @Test
    fun materialNavigationButtonStaysAtTheTop() = verifyButtonPosition(true, material = true)

    @Test
    fun materialUnmatchedNavigationButtonStaysAtTheTop() = verifyButtonPosition(false, material = true)

    @Test
    fun materialActionsResizeAtTheTrailingEdge() = verifyButtonPosition(true, actions = true, material = true)

    @Test
    fun materialPrimaryActionResizesAtTheTrailingEdge() =
        verifyButtonPosition(true, actions = true, material = true, primary = true)

    @Test
    fun matchedButtonShadowSurvivesSlowPop() = verifyShadowDuringPop(true)

    @Test
    fun unmatchedButtonShadowSurvivesSlowPop() = verifyShadowDuringPop(false)

    @Test
    fun shadowSurvivesSeekingAndCancellingBackGesture() = runComposeUiTest {
        val state = SeekableTransitionState(true)
        lateinit var animationScope: CoroutineScope
        setContent {
            animationScope = rememberCoroutineScope()
            SharedTransitionLayout {
                val topBarTransition = remember(this) { NavigationTopBarTransition(this) }
                val transition = rememberTransition(state, label = "SlowBackGesture")
                transition.AnimatedContent(
                    modifier = Modifier.size(320.dp, 480.dp),
                    transitionSpec = {
                        slideInHorizontally(tween(1200)) { -it / 3 } togetherWith
                            slideOutHorizontally(tween(1200)) { it }
                    },
                ) { detail ->
                    CompositionLocalProvider(LocalNavAnimatedContentScope provides this) {
                        Box(Modifier.fillMaxSize().background(Color.White)) {
                            Box(Modifier.offset(64.dp, 128.dp)) {
                                Box(
                                    topBarTransition.modifier(IenTopBarTransition.Slot.Actions)
                                        .size(if (detail) 80.dp else 48.dp, 48.dp)
                                        .drawBehind {
                                            drawRect(Color.Black.copy(alpha = 0.25f), Offset(-12f, -12f), Size(size.width + 24f, size.height + 24f))
                                        }
                                        .shadow(16.dp, RectangleShape, clip = false)
                                        .background(Color.Red),
                                )
                            }
                        }
                    }
                }
            }
        }
        waitForIdle()
        mainClock.autoAdvance = false
        for (fraction in listOf(0.05f, 0.15f, 0.35f, 0.65f, 0.35f, 0.15f)) {
            runOnIdle { animationScope.launch { state.seekTo(fraction, false) } }
            mainClock.advanceTimeByFrame()
            waitForIdle()
            val captured = onRoot().captureToImage()
            if (fraction == 0.35f) {
                val bytes = Image.makeFromBitmap(captured.asSkiaBitmap()).encodeToData()!!.bytes
                bytes.usePinned {
                    NSData.create(bytes = it.addressOf(0), length = bytes.size.toULong())
                        .writeToFile(NSTemporaryDirectory() + "navigation-shadow-seek.png", atomically = true)
                }
            }
            val pixels = captured.toPixelMap()
            assertTrue(pixels[88, 120].red < 0.98f, "뒤로가기 진행도 $fraction 에서 그림자가 잘리면 안 됩니다: ${pixels[88, 120]}")
        }
        runOnIdle { animationScope.launch { state.snapTo(true) } }
        mainClock.autoAdvance = true
        waitForIdle()
    }

    private fun verifyShadowDuringPop(matched: Boolean) = runComposeUiTest {
        val stack = NavBackStack<NavKey>(Root, Detail)
        setContent {
            CompositionLocalProvider(LocalUIViewController provides UIViewController()) {
                Box(Modifier.size(320.dp, 480.dp).background(Color.White)) {
                    BaseNavDisplay(
                        backStack = stack,
                        animateTopBar = true,
                        popTransitionSpec = {
                            slideInHorizontally(tween(1200)) { -it / 3 } togetherWith
                                slideOutHorizontally(tween(1200)) { it }
                        },
                    ) { key ->
                        NavEntry(key) {
                            Box(Modifier.fillMaxSize().background(Color.White)) {
                                if (matched || key == Detail) {
                                    Box(Modifier.offset(64.dp, 128.dp)) {
                                        Box(
                                            LocalIenTopBarTransition.current!!
                                                .modifier(IenTopBarTransition.Slot.Actions)
                                                .size(48.dp)
                                                .drawBehind {
                                                    drawRect(Color.Black.copy(alpha = 0.25f), Offset(-12f, -12f), Size(size.width + 24f, size.height + 24f))
                                                }
                                                .shadow(16.dp, RectangleShape, clip = false)
                                                .background(Color.Red),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        waitForIdle()
        mainClock.autoAdvance = false
        runOnUiThread { stack.navigateBack() }
        mainClock.advanceTimeBy(80)
        waitForIdle()
        val pixels = onRoot().captureToImage().toPixelMap()
        assertTrue(pixels[88, 120].red < 0.98f, "느린 복귀 중 버튼 경계 밖 그림자가 유지되어야 합니다: ${pixels[88, 120]}")
        mainClock.advanceTimeBy(1600)
        mainClock.autoAdvance = true
    }

    private fun verifyButtonPosition(
        rootHasButton: Boolean,
        actions: Boolean = false,
        material: Boolean = false,
        primary: Boolean = false,
    ) = runComposeUiTest {
        val stack = NavBackStack<NavKey>(Root)
        val controller = UIViewController()
        setContent {
            CompositionLocalProvider(LocalUIViewController provides controller) {
                Box(Modifier.size(320.dp, 480.dp).background(Color.White)) {
                    BaseNavDisplay(
                        backStack = stack,
                        animateTopBar = true,
                        transitionSpec = {
                            slideInHorizontally(tween(1000)) { it } togetherWith
                                slideOutHorizontally(tween(1000)) { -it / 3 }
                        },
                        popTransitionSpec = {
                            slideInHorizontally(tween(1000)) { -it / 3 } togetherWith
                                slideOutHorizontally(tween(1000)) { it }
                        },
                    ) { key ->
                        NavEntry(key) {
                            Box(Modifier.fillMaxSize().background(Color.White)) {
                                if (material) {
                                    IenTheme {
                                        val button: (@androidx.compose.runtime.Composable () -> Unit)? =
                                            if (key == Detail || rootHasButton) {
                                                {
                                                    Box(Modifier.size(if (actions && key == Detail) 80.dp else 40.dp, 40.dp).background(Color.Red))
                                                }
                                            } else null
                                        IenTopAppBar(
                                            title = {},
                                            windowInsets = WindowInsets(0),
                                            navigationIcon = if (!actions) button else null,
                                            actions = if (actions && !primary && button != null) ({ button() }) else null,
                                            primaryAction = if (primary && button != null) ({ button() }) else null,
                                        )
                                    }
                                } else if (key == Detail || rootHasButton) {
                                    Box(Modifier.align(if (actions) Alignment.TopEnd else Alignment.TopStart).padding(16.dp)) {
                                        Box(
                                            LocalIenTopBarTransition.current!!
                                                .modifier(if (actions) IenTopBarTransition.Slot.Actions else IenTopBarTransition.Slot.NavigationIcon)
                                                .size(if (actions && key == Detail) 80.dp else 40.dp, 40.dp)
                                                .background(Color.Red),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        waitForIdle()
        mainClock.autoAdvance = false
        runOnUiThread { stack.add(Detail) }
        mainClock.advanceTimeBy(160)
        waitForIdle()
        val pushed = onRoot().captureToImage().toPixelMap()
        val x = (pushed.width * (if (actions) 288f else 32f) / 320f).toInt()
        val y = (pushed.height * 32f / 480f).toInt()
        assertTrue(pushed[x, y].green < 0.8f, "진입 중 버튼은 상단 위치를 유지해야 합니다")
        if (actions) {
            val width = (0 until pushed.width).count { pushed[it, y].green < 0.8f }
            assertTrue(width > 40 && width < 80, "액션 영역 너비는 두 화면의 크기 사이를 보간해야 합니다: $width")
        }
        mainClock.advanceTimeBy(1000)
        waitForIdle()
        runOnUiThread { stack.navigateBack() }
        mainClock.advanceTimeBy(160)
        waitForIdle()
        val popped = onRoot().captureToImage().toPixelMap()
        assertTrue(popped[x, y].green < 0.95f, "복귀 중 버튼은 상단 위치에서 사라져야 합니다")
        mainClock.advanceTimeBy(1200)
        mainClock.autoAdvance = true
        waitForIdle()
        val settled = onRoot().captureToImage().toPixelMap()
        assertTrue(
            if (rootHasButton) settled[x, y].green < 0.2f else settled[x, y].green > 0.95f,
            "전환 종료 후에는 현재 화면의 버튼만 남아야 합니다",
        )
    }
}
