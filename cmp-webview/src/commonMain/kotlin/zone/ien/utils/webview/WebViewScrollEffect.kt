package zone.ien.utils.webview

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebViewFactoryParam
import zone.ien.utils.ui.screen.IenScaffoldContentEdge

internal class WebViewScrollState : ScrollableState {
    var headerHeight by mutableFloatStateOf(0f)
        private set
    var headerOffset by mutableFloatStateOf(0f)
        private set
    private var bodyOffset by mutableFloatStateOf(0f)
    var maxOffset by mutableFloatStateOf(0f)
        private set
    val offset: Float get() = headerOffset + bodyOffset
    var scrollBy: ((Float) -> Float)? = null

    private val delegate = ScrollableState { delta -> scrollBy?.invoke(delta) ?: 0f }

    override val canScrollBackward: Boolean get() = offset > 0f
    override val canScrollForward: Boolean get() = offset < maxOffset
    override val isScrollInProgress: Boolean get() = delegate.isScrollInProgress

    override suspend fun scroll(scrollPriority: MutatePriority, block: suspend ScrollScope.() -> Unit) {
        delegate.scroll(scrollPriority, block)
    }

    override fun dispatchRawDelta(delta: Float): Float = delegate.dispatchRawDelta(delta)

    fun updateHeaderHeight(height: Float) {
        val previousHeight = headerHeight
        val wasCollapsed = headerOffset == previousHeight && bodyOffset > 0f
        headerHeight = height.coerceAtLeast(0f)
        headerOffset = if (wasCollapsed) headerHeight else headerOffset.coerceAtMost(headerHeight)
        if (maxOffset > 0f) maxOffset = (maxOffset + headerHeight - previousHeight).coerceAtLeast(0f)
    }

    fun consumeHeaderDelta(delta: Float): Float {
        val previousOffset = headerOffset
        headerOffset = (headerOffset + delta).coerceIn(0f, headerHeight)
        return headerOffset - previousOffset
    }

    fun updateBody(offset: Float, maxOffset: Float) {
        val bodyMaxOffset = maxOffset.coerceAtLeast(0f)
        bodyOffset = offset.coerceIn(0f, bodyMaxOffset)
        this.maxOffset = headerHeight + bodyMaxOffset
    }

    fun update(offset: Float, maxOffset: Float) {
        this.maxOffset = maxOffset.coerceAtLeast(0f)
        val totalOffset = offset.coerceIn(0f, this.maxOffset)
        headerOffset = totalOffset.coerceAtMost(headerHeight)
        bodyOffset = (totalOffset - headerHeight).coerceAtLeast(0f)
    }
}

@Composable
internal expect fun rememberWebViewFactory(
    scrollState: WebViewScrollState,
): ((WebViewFactoryParam) -> NativeWebView)?

@Composable
internal expect fun WebViewScrollEffect(
    webView: NativeWebView?,
    scrollState: WebViewScrollState,
    isContentReady: Boolean,
    contentEdge: IenScaffoldContentEdge,
    topBarHeight: Dp,
)
