package zone.ien.utils.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertTrue
import org.junit.Test
import zone.ien.utils.ui.screen.IenTopBarTransition
import zone.ien.utils.ui.screen.LocalIenTopBarTransition

class ShadowTransitionTestActivity : ComponentActivity()

@OptIn(ExperimentalTestApi::class)
class NavigationTopBarShadowTest {
    private data object Root : NavKey
    private data object Detail : NavKey

    @Test
    fun matchedButtonKeepsDrawingOutsideItsBounds() = verifyOverflow(true)

    @Test
    fun unmatchedButtonKeepsDrawingOutsideItsBounds() = verifyOverflow(false)

    @Test
    fun matchedButtonKeepsAndroidElevationShadow() = verifyOverflow(true, syntheticOverflow = false)

    @Test
    fun unmatchedButtonKeepsAndroidElevationShadow() = verifyOverflow(false, syntheticOverflow = false)

    private fun verifyOverflow(matched: Boolean, syntheticOverflow: Boolean = true) = runAndroidComposeUiTest<ShadowTransitionTestActivity> {
        val stack = NavBackStack<NavKey>(Root)
        setContent {
            Box(Modifier.size(320.dp, 480.dp).background(Color.White)) {
                BaseNavDisplay(
                    backStack = stack,
                    animateTopBar = true,
                    transitionSpec = {
                        slideInHorizontally(tween(1000)) { it } togetherWith
                            slideOutHorizontally(tween(1000)) { -it / 3 }
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
                                                // 안드로이드 그림자 강도와 무관하게 경계 밖 렌더링도 검증합니다.
                                                if (syntheticOverflow) {
                                                    val overflow = 12.dp.toPx()
                                                    drawRect(
                                                        Color.Black.copy(alpha = 0.25f),
                                                        Offset(-overflow, -overflow),
                                                        Size(size.width + overflow * 2, size.height + overflow * 2),
                                                    )
                                                }
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
        waitForIdle()
        mainClock.autoAdvance = false
        runOnUiThread { stack.add(Detail) }
        mainClock.advanceTimeBy(160)
        waitForIdle()
        val pixels = onRoot().captureToImage().toPixelMap()
        val scale = pixels.width / 320f
        val outsideY = if (syntheticOverflow) 120 else 126
        val outside = pixels[(88 * scale).toInt(), (outsideY * scale).toInt()]
        val threshold = if (syntheticOverflow) 0.98f else 0.995f
        assertTrue("전환 도중에도 버튼 위쪽 경계 밖 그림자가 보여야 합니다: $outside", outside.red < threshold)
        mainClock.advanceTimeBy(1500)
        mainClock.autoAdvance = true
    }
}
