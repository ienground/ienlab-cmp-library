package zone.ien.utils.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.cinterop.objcPtr
import kotlinx.coroutines.test.TestResult
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIImageView
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.UIVisualEffectView
import platform.WebKit.WKWebView
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme

@OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class, InternalComposeTracingApi::class, ExperimentalAdaptiveApi::class)
class WebViewScrollEffectTest {
    @Test
    fun nativeContentWaitsForFirstDraw() = runComposeUiTest {
        mainClock.autoAdvance = false
        var shellDrawn = false
        var nativeSawShellDraw: Boolean? = null
        var nativeCreations = 0
        setContent {
            Box(Modifier.fillMaxSize().drawWithContent {
                drawContent()
                shellDrawn = true
            }) {
                WebViewAfterFirstFrame {
                    DisposableEffect(Unit) {
                        nativeSawShellDraw = shellDrawn
                        nativeCreations++
                        onDispose {}
                    }
                }
            }
        }
        onRoot().captureToImage()
        repeat(3) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        runOnIdle {
            assertEquals(true, nativeSawShellDraw, "기존 화면을 그린 뒤 네이티브 웹뷰를 생성해야 합니다")
            assertEquals(1, nativeCreations)
        }
    }

    @Test
    fun nativeContentIsNotCreatedAfterEarlyRemoval() = runComposeUiTest {
        mainClock.autoAdvance = false
        val mounted = mutableStateOf(true)
        var nativeCreations = 0
        setContent {
            if (mounted.value) {
                WebViewAfterFirstFrame {
                    DisposableEffect(Unit) {
                        nativeCreations++
                        onDispose {}
                    }
                }
            }
        }
        runOnIdle { mounted.value = false }
        repeat(3) {
            mainClock.advanceTimeByFrame()
            waitForIdle()
        }
        runOnIdle {
            assertEquals(0, nativeCreations, "첫 그리기 전에 닫힌 화면은 네이티브 웹뷰를 만들면 안 됩니다")
        }
    }

    @Test
    fun scaffoldLayoutDoesNotRecomposeForNativeScrollProgress() = runComposeUiTest {
        val state = WebViewScrollState()
        var compositionCount = 0
        val tracer = object : CompositionTracer {
            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                if (info.startsWith("zone.ien.utils.webview.WebViewScaffoldLayout (")) {
                    compositionCount++
                }
            }
        }
        Composer.setTracer(tracer)
        try {
            val controller = UIViewController()
            setContent {
                CompositionLocalProvider(LocalUIViewController provides controller) {
                    IenAdaptiveTheme(target = Theme.Material3) {
                        WebViewScaffoldLayout(
                            modifier = Modifier,
                            scrollState = state,
                            title = { Box(Modifier.height(64.dp)) },
                            navigationIcon = {},
                            webView = { Box(Modifier.fillMaxSize()) },
                        )
                    }
                }
            }
            val initialCompositionCount = runOnIdle { compositionCount }
            assertTrue(initialCompositionCount > 0, "실제 Scaffold 레이아웃의 합성이 추적되어야 합니다")
            repeat(48) { index ->
                runOnIdle { state.update((index + 1).toFloat(), 120f) }
                waitForIdle()
            }
            runOnIdle {
                assertTrue(state.canScrollBackward)
                assertEquals(48f, state.offset)
                assertEquals(
                    initialCompositionCount,
                    compositionCount,
                    "iOS 스크롤 진행도 변경으로 실제 Scaffold 레이아웃을 다시 합성하면 안 됩니다",
                )
            }
        } finally {
            Composer.setTracer(null)
        }
    }

    @Test
    fun scrollingReusesBlurMaskImagesAndDisposesObservers(): TestResult = runComposeUiTest {
        val mounted = mutableStateOf(true)
        val state = WebViewScrollState()
        lateinit var container: UIView
        lateinit var webView: WKWebView
        runOnUiThread {
            container = UIView(CGRectMake(0.0, 0.0, 320.0, 480.0))
            webView = WKWebView(CGRectMake(0.0, 0.0, 320.0, 480.0))
            container.addSubview(webView)
            webView.scrollView.setContentSize(CGSizeMake(320.0, 2400.0))
        }
        setContent {
            if (mounted.value) {
                WebViewScrollEffect(webView, state, true, IenScaffoldContentEdge(), 56.dp)
            }
        }
        runOnIdle {
            val blurs = container.subviews.filterIsInstance<UIVisualEffectView>()
            assertEquals(2, blurs.size)
            val topMask = blurs[0].maskView as UIImageView
            val bottomMask = blurs[1].maskView as UIImageView
            val topImage = assertNotNull(topMask.image)
            val bottomImage = assertNotNull(bottomMask.image)
            val topAlphas = mutableListOf<Double>()
            repeat(40) { index ->
                webView.scrollView.setContentOffset(CGPointMake(0.0, (index + 1).toDouble()), false)
                assertEquals(topImage.objcPtr(), topMask.image?.objcPtr(), "상단 마스크는 스크롤마다 재생성하면 안 됩니다: $index")
                assertEquals(bottomImage.objcPtr(), bottomMask.image?.objcPtr(), "하단 마스크는 스크롤마다 재생성하면 안 됩니다: $index")
                topAlphas += topMask.alpha
            }
            assertTrue(topAlphas.first() < topAlphas.last(), "마스크를 재사용해도 페이드가 갱신되어야 합니다")
            assertTrue(state.offset > 0f)

            val maxOffset = webView.scrollView.contentSize.useContents { height } -
                webView.scrollView.bounds.useContents { size.height }
            val bottomAlphas = mutableListOf<Double>()
            repeat(40) { index ->
                webView.scrollView.setContentOffset(CGPointMake(0.0, maxOffset - 40 + index), false)
                assertEquals(topImage.objcPtr(), topMask.image?.objcPtr())
                assertEquals(bottomImage.objcPtr(), bottomMask.image?.objcPtr())
                bottomAlphas += bottomMask.alpha
            }
            assertTrue(bottomAlphas.first() > bottomAlphas.last(), "하단 페이드도 스크롤을 따라야 합니다")
            webView.setFrame(CGRectMake(0.0, 0.0, 480.0, 320.0))
            webView.scrollView.setBounds(CGRectMake(0.0, 0.0, 480.0, 320.0))
            assertNotEquals(topImage.objcPtr(), topMask.image?.objcPtr(), "크기가 변하면 상단 마스크를 다시 만들어야 합니다")
            assertNotEquals(bottomImage.objcPtr(), bottomMask.image?.objcPtr(), "크기가 변하면 하단 마스크를 다시 만들어야 합니다")
            assertEquals(480.0, topMask.bounds.useContents { size.width })
            mounted.value = false
        }
        runOnIdle {
            assertTrue(container.subviews.filterIsInstance<UIVisualEffectView>().isEmpty())
            assertEquals(null, state.scrollBy)
            webView.scrollView.setContentOffset(CGPointMake(0.0, 80.0), false)
        }
    }
}
