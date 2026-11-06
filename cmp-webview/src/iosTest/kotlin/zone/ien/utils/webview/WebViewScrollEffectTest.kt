package zone.ien.utils.webview

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
import platform.UIKit.UIVisualEffectView
import platform.WebKit.WKWebView
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import zone.ien.utils.ui.screen.IenScaffoldContentEdge

@OptIn(ExperimentalTestApi::class, ExperimentalForeignApi::class)
class WebViewScrollEffectTest {
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
