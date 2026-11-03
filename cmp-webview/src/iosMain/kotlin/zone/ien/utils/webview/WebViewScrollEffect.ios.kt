package zone.ien.utils.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebViewFactoryParam
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.cinterop.interpretObjCPointer
import observer.ObserverProtocol
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CATransaction
import platform.UIKit.UIBlurEffect
import platform.UIKit.UIBlurEffectStyle.UIBlurEffectStyleSystemMaterial
import platform.UIKit.UIColor
import platform.UIKit.UIVisualEffectView
import platform.UIKit.UIImageView
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIScrollViewContentInsetAdjustmentBehavior.UIScrollViewContentInsetAdjustmentNever
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.darwin.NSObject

@Composable
internal actual fun rememberWebViewFactory(
    scrollState: WebViewScrollState,
): ((WebViewFactoryParam) -> NativeWebView)? = null

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun WebViewScrollEffect(
    webView: NativeWebView?,
    scrollState: WebViewScrollState,
    isContentReady: Boolean,
    contentEdge: IenScaffoldContentEdge,
    topBarHeight: Dp,
) {
    val density = LocalDensity.current.density
    val headerHeight = scrollState.headerHeight
    DisposableEffect(webView, scrollState, isContentReady, density, headerHeight, contentEdge.topHeight, contentEdge.bottomHeight, contentEdge.scrollFadeDistance, topBarHeight) {
        if (webView == null || !isContentReady) {
            scrollState.update(0f, 0f)
            onDispose {}
        } else {
            val scrollView = webView.scrollView
            val originalInsets = scrollView.contentInset
            val originalInsetAdjustmentBehavior = scrollView.contentInsetAdjustmentBehavior
            val previousOffset = scrollView.contentOffset.useContents { y } +
                scrollView.adjustedContentInset.useContents { top }
            scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever
            scrollView.contentInset = originalInsets.useContents {
                UIEdgeInsetsMake(top + headerHeight / density, left, bottom, right)
            }
            scrollView.setContentOffset(
                CGPointMake(
                    scrollView.contentOffset.useContents { x },
                    previousOffset - scrollView.adjustedContentInset.useContents { top },
                ),
                animated = false,
            )
            val topBlur = UIVisualEffectView(effect = UIBlurEffect.effectWithStyle(UIBlurEffectStyleSystemMaterial))
            val bottomBlur = UIVisualEffectView(effect = UIBlurEffect.effectWithStyle(UIBlurEffectStyleSystemMaterial))
            val opaqueColor = interpretObjCPointer<NSObject>(UIColor.blackColor.CGColor!!.rawValue)
            val transparentColor = interpretObjCPointer<NSObject>(UIColor.clearColor.CGColor!!.rawValue)
            val topMask = CAGradientLayer().apply {
                colors = listOf(opaqueColor, opaqueColor, transparentColor)
                startPoint = CGPointMake(0.5, 0.0)
                endPoint = CGPointMake(0.5, 1.0)
            }
            val bottomMask = CAGradientLayer().apply {
                colors = listOf(transparentColor, opaqueColor, opaqueColor)
                locations = listOf(0.0, 0.58, 1.0)
                startPoint = CGPointMake(0.5, 0.0)
                endPoint = CGPointMake(0.5, 1.0)
            }
            topBlur.userInteractionEnabled = false
            bottomBlur.userInteractionEnabled = false
            val topMaskView = UIImageView()
            val bottomMaskView = UIImageView()
            val blurContainer = webView.superview ?: webView
            blurContainer.addSubview(topBlur)
            blurContainer.addSubview(bottomBlur)
            fun updateScroll() {
                val insets = scrollView.adjustedContentInset
                val topInset = insets.useContents { top }
                val bottomInset = insets.useContents { bottom }
                val offset = scrollView.contentOffset.useContents { y } + topInset
                val maxOffset = scrollView.contentSize.useContents { height } + topInset + bottomInset -
                    scrollView.bounds.useContents { size.height }
                scrollState.update((offset * density).toFloat(), (maxOffset * density).toFloat())
                val width = webView.bounds.useContents { size.width }
                val height = webView.bounds.useContents { size.height }
                val topFadeHeight = contentEdge.topHeight.value.toDouble()
                val topHeight = if (topFadeHeight > 0.0) {
                    maxOf(topFadeHeight, topBarHeight.value + topFadeHeight * 0.58).coerceAtMost(height)
                } else {
                    0.0
                }
                val bottomHeight = contentEdge.bottomHeight.value.toDouble().coerceAtMost(height)
                topBlur.setFrame(webView.convertRect(CGRectMake(0.0, 0.0, width, topHeight), toView = blurContainer))
                bottomBlur.setFrame(webView.convertRect(CGRectMake(0.0, height - bottomHeight, width, bottomHeight), toView = blurContainer))
                val fadeDistance = contentEdge.scrollFadeDistance.value * density
                val topProgress = (scrollState.offset / fadeDistance).coerceIn(0f, 1f)
                val bottomProgress = ((scrollState.maxOffset - scrollState.offset) / fadeDistance).coerceIn(0f, 1f)
                topBlur.hidden = topProgress == 0f || topHeight <= 0.0
                bottomBlur.hidden = bottomProgress == 0f || bottomHeight <= 0.0
                val topMaskChanged = topMask.opacity != topProgress || topMaskView.bounds.useContents {
                    size.width != width || size.height != topHeight
                }
                val bottomMaskChanged = bottomMask.opacity != bottomProgress || bottomMaskView.bounds.useContents {
                    size.width != width || size.height != bottomHeight
                }
                if (topMaskChanged || bottomMaskChanged) {
                    CATransaction.begin()
                    CATransaction.setDisableActions(true)
                    if (topMaskChanged && topHeight > 0.0) {
                        topMaskView.setFrame(topBlur.bounds)
                        topMask.setFrame(CGRectMake(0.0, 0.0, 1.0, topHeight))
                        topMask.locations = listOf(0.0, ((topHeight - topFadeHeight * 0.58) / topHeight).coerceIn(0.0, 1.0), 1.0)
                        topMask.opacity = topProgress
                        topMaskView.image = UIGraphicsImageRenderer(size = CGSizeMake(1.0, topHeight)).imageWithActions {
                            topMask.renderInContext(it!!.CGContext)
                        }
                        topBlur.maskView = null
                        topBlur.maskView = topMaskView
                    }
                    if (bottomMaskChanged && bottomHeight > 0.0) {
                        bottomMaskView.setFrame(bottomBlur.bounds)
                        bottomMask.setFrame(CGRectMake(0.0, 0.0, 1.0, bottomHeight))
                        bottomMask.opacity = bottomProgress
                        bottomMaskView.image = UIGraphicsImageRenderer(size = CGSizeMake(1.0, bottomHeight)).imageWithActions {
                            bottomMask.renderInContext(it!!.CGContext)
                        }
                        bottomBlur.maskView = null
                        bottomBlur.maskView = bottomMaskView
                    }
                    CATransaction.commit()
                }
            }
            val observer = object : NSObject(), ObserverProtocol {
                override fun observeValueForKeyPath(
                    keyPath: String?,
                    ofObject: Any?,
                    change: Map<Any?, *>?,
                    context: COpaquePointer?,
                ) {
                    updateScroll()
                }
            }
            val keyPaths = listOf("contentOffset", "contentSize", "bounds", "adjustedContentInset")
            keyPaths.forEach { keyPath ->
                scrollView.addObserver(observer, forKeyPath = keyPath, options = NSKeyValueObservingOptionNew, context = null)
            }
            scrollState.scrollBy = { delta ->
                val before = scrollView.contentOffset.useContents { y }
                val x = scrollView.contentOffset.useContents { x }
                val topInset = scrollView.adjustedContentInset.useContents { top }
                val target = (before + delta / density).coerceIn(
                    -topInset,
                    scrollState.maxOffset / density - topInset,
                )
                scrollView.setContentOffset(CGPointMake(x, target), animated = false)
                updateScroll()
                ((scrollView.contentOffset.useContents { y } - before) * density).toFloat()
            }
            updateScroll()
            onDispose {
                keyPaths.forEach { keyPath -> scrollView.removeObserver(observer, forKeyPath = keyPath) }
                scrollState.scrollBy = null
                topBlur.removeFromSuperview()
                bottomBlur.removeFromSuperview()
                val offset = scrollView.contentOffset.useContents { y } +
                    scrollView.adjustedContentInset.useContents { top }
                scrollView.contentInset = originalInsets
                scrollView.contentInsetAdjustmentBehavior = originalInsetAdjustmentBehavior
                scrollView.setContentOffset(
                    CGPointMake(
                        scrollView.contentOffset.useContents { x },
                        offset - scrollView.adjustedContentInset.useContents { top },
                    ),
                    animated = false,
                )
            }
        }
    }
}
