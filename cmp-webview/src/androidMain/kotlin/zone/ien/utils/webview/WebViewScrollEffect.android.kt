package zone.ien.utils.webview

import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.widget.OverScroller
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebViewFactoryParam
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import kotlin.math.roundToInt
import kotlin.math.abs

@Composable
internal actual fun rememberWebViewFactory(
    scrollState: WebViewScrollState,
): ((WebViewFactoryParam) -> NativeWebView)? {
    val context = LocalContext.current
    return remember(context, scrollState) { { HeaderScrollingWebView(context, scrollState) } }
}

@Composable
internal actual fun WebViewScrollEffect(
    webView: NativeWebView?,
    scrollState: WebViewScrollState,
    isContentReady: Boolean,
    contentEdge: IenScaffoldContentEdge,
    topBarHeight: Dp,
) {
    DisposableEffect(webView, scrollState, isContentReady) {
        if (webView == null || !isContentReady) {
            scrollState.update(0f, 0f)
            onDispose {}
        } else {
            fun updateScroll() {
                @Suppress("DEPRECATION")
                val contentHeight = webView.contentHeight * webView.scale
                val viewportHeight = webView.height - webView.paddingTop - webView.paddingBottom
                scrollState.updateBody(webView.scrollY.toFloat(), contentHeight - viewportHeight)
            }
            val observer = webView.viewTreeObserver
            val scrollListener = ViewTreeObserver.OnScrollChangedListener { updateScroll() }
            val layoutListener = ViewTreeObserver.OnGlobalLayoutListener { updateScroll() }
            observer.addOnScrollChangedListener(scrollListener)
            observer.addOnGlobalLayoutListener(layoutListener)
            scrollState.scrollBy = { delta ->
                var remaining = delta
                if (remaining > 0f) remaining -= scrollState.consumeHeaderDelta(remaining)
                val before = webView.scrollY
                val bodyMaxOffset = (scrollState.maxOffset - scrollState.headerHeight).coerceAtLeast(0f)
                val target = (before + remaining).coerceIn(0f, bodyMaxOffset).roundToInt()
                webView.scrollTo(webView.scrollX, target)
                remaining -= webView.scrollY - before
                updateScroll()
                if (remaining < 0f) remaining -= scrollState.consumeHeaderDelta(remaining)
                delta - remaining
            }
            updateScroll()
            onDispose {
                val currentObserver = if (observer.isAlive) observer else webView.viewTreeObserver
                if (currentObserver.isAlive) {
                    currentObserver.removeOnScrollChangedListener(scrollListener)
                    currentObserver.removeOnGlobalLayoutListener(layoutListener)
                }
                scrollState.scrollBy = null
            }
        }
    }
}

private class HeaderScrollingWebView(
    context: Context,
    private val scrollState: WebViewScrollState,
) : WebView(context) {
    private val location = IntArray(2)
    private val configuration = ViewConfiguration.get(context)
    private val scroller = OverScroller(context)
    private var velocityTracker: VelocityTracker? = null
    private var gestureOriginY = 0
    private var previousRawY = 0f
    private var downRawY = 0f
    private var consumedHeader = 0f
    private var isDragging = false
    private var isTouching = false
    private var isBridgeFling = false
    private var previousFlingY = 0
    private val flingFrame = object : Runnable {
        override fun run() {
            if (!scroller.computeScrollOffset()) {
                isBridgeFling = false
                return
            }
            val delta = scroller.currY - previousFlingY
            previousFlingY = scroller.currY
            scrollState.dispatchRawDelta(delta.toFloat())
            postOnAnimation(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            removeCallbacks(flingFrame)
            scroller.abortAnimation()
            isBridgeFling = false
            isTouching = true
            getLocationInWindow(location)
            gestureOriginY = location[1]
            previousRawY = event.rawY
            downRawY = event.rawY
            consumedHeader = 0f
            isDragging = false
            velocityTracker?.recycle()
            velocityTracker = VelocityTracker.obtain()
        }
        val rawEvent = MotionEvent.obtain(event)
        rawEvent.offsetLocation(event.rawX - event.x, event.rawY - event.y)
        velocityTracker?.addMovement(rawEvent)
        rawEvent.recycle()
        if (event.actionMasked == MotionEvent.ACTION_MOVE && event.pointerCount == 1) {
            if (abs(event.rawY - downRawY) > configuration.scaledTouchSlop) isDragging = true
            if (isDragging) {
                val delta = previousRawY - event.rawY
                val headerDelta = if (delta > 0f) delta else (delta + scrollY).coerceAtMost(0f)
                consumedHeader += scrollState.consumeHeaderDelta(headerDelta)
            }
        }
        previousRawY = if (event.actionMasked == MotionEvent.ACTION_POINTER_UP && event.actionIndex == 0) {
            event.getY(1) + event.rawY - event.y
        } else {
            event.rawY
        }
        getLocationInWindow(location)
        val adjustedEvent = MotionEvent.obtain(event)
        adjustedEvent.offsetLocation(0f, location[1] - gestureOriginY + consumedHeader)
        val bridgeFling = event.actionMasked == MotionEvent.ACTION_UP && isDragging
        if (bridgeFling) adjustedEvent.action = MotionEvent.ACTION_CANCEL
        val handled = super.onTouchEvent(adjustedEvent)
        adjustedEvent.recycle()
        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) {
            isTouching = false
            if (bridgeFling) {
                velocityTracker?.computeCurrentVelocity(1000, configuration.scaledMaximumFlingVelocity.toFloat())
                val velocity = -(velocityTracker?.yVelocity ?: 0f)
                if (abs(velocity) >= configuration.scaledMinimumFlingVelocity) {
                    previousFlingY = (scrollState.headerOffset + scrollY).roundToInt()
                    scroller.fling(
                        0, previousFlingY, 0, velocity.roundToInt(),
                        0, 0, 0, scrollState.maxOffset.roundToInt(),
                    )
                    isBridgeFling = true
                    postOnAnimation(flingFrame)
                }
            }
            velocityTracker?.recycle()
            velocityTracker = null
        }
        return handled || consumedHeader != 0f
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_SCROLL) {
            val delta = -event.getAxisValue(MotionEvent.AXIS_VSCROLL) * configuration.scaledVerticalScrollFactor
            if (delta != 0f && scrollState.dispatchRawDelta(delta) != 0f) return true
        }
        return super.onGenericMotionEvent(event)
    }

    override fun onScrollChanged(left: Int, top: Int, oldLeft: Int, oldTop: Int) {
        super.onScrollChanged(left, top, oldLeft, oldTop)
        if (top > 0 && oldTop == 0 && !isTouching && !isBridgeFling) {
            scrollState.consumeHeaderDelta(scrollState.headerHeight - scrollState.headerOffset)
        }
        if (top == 0 && oldTop > 0 && !isTouching && !isBridgeFling && scrollState.headerOffset > 0f) {
            previousFlingY = scrollState.headerOffset.roundToInt()
            scroller.startScroll(
                0, previousFlingY, 0, -previousFlingY,
                resources.getInteger(android.R.integer.config_shortAnimTime),
            )
            isBridgeFling = true
            postOnAnimation(flingFrame)
        }
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(flingFrame)
        scroller.abortAnimation()
        velocityTracker?.recycle()
        velocityTracker = null
        super.onDetachedFromWindow()
    }
}
