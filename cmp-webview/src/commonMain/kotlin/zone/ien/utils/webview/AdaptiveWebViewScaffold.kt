package zone.ien.utils.webview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawPlainBackdrop
import com.kyant.backdrop.effects.blur
import com.multiplatform.webview.web.NativeWebView
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.ui.feedback.IenLoadingIndicator
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.utils.PlatformDependent

private sealed interface WebViewContentState {
    data object Loading : WebViewContentState
    data object Error : WebViewContentState
    data object Content : WebViewContentState
}

/**
 * 큰 제목과 웹뷰를 함께 스크롤하고, 플랫폼별 상·하단 블러를 적용합니다.
 *
 * 웹뷰 상태와 탐색은 호출부가 소유하며 로딩 및 오류 콘텐츠를 교체할 수 있습니다.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveWebViewScaffold(
    modifier: Modifier = Modifier,
    state: WebViewState,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    errorContent: @Composable () -> Unit,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    loadingContent: @Composable () -> Unit = { IenLoadingIndicator() },
) {
    val backdrop = rememberDefaultBackdrop()
    val titleBackdrop = rememberLayerBackdrop()
    val scrollState = remember { WebViewScrollState() }
    val contentEdge = IenScaffoldContentEdge(scrollableState = scrollState)
    val fadeDistance = with(LocalDensity.current) { contentEdge.scrollFadeDistance.toPx() }
    AdaptiveTopAppBarScaffold(
        navigationIcon = navigationIcon,
        title = title,
        backdrop = backdrop,
        contentEdge = contentEdge.copy(
            topProgress = (scrollState.offset / fadeDistance).coerceIn(0f, 1f),
            bottomProgress = ((scrollState.maxOffset - scrollState.offset) / fadeDistance).coerceIn(0f, 1f),
        ),
        modifier = modifier,
    ) { paddingValues, title ->
        val layoutDirection = LocalLayoutDirection.current
        val topBarHeight = paddingValues.calculateTopPadding()
        val topBarHeightPx = with(LocalDensity.current) { topBarHeight.toPx() }
        Layout(
            content = {
                Box(
                    modifier = Modifier
                        .onSizeChanged { scrollState.updateHeaderHeight(it.height.toFloat()) }
                        .then(
                            if (PlatformDependent.isIos) {
                                Modifier
                                    .drawWithContent {
                                        clipRect(top = topBarHeightPx + scrollState.offset) {
                                            this@drawWithContent.drawContent()
                                        }
                                    }
                                    .layerBackdrop(titleBackdrop)
                            } else {
                                Modifier
                            },
                        )
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = IenTheme.spacing.md,
                        ),
                ) { title() }
                ScaffoldWebView(
                    modifier = Modifier,
                    state = state,
                    navigator = navigator,
                    loadingContent = loadingContent,
                    errorContent = errorContent,
                    scrollState = scrollState,
                    topBarHeight = paddingValues.calculateTopPadding(),
                )
                if (PlatformDependent.isIos) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(topBarHeight)
                            .drawPlainBackdrop(
                                backdrop = titleBackdrop,
                                shape = { RectangleShape },
                                effects = { blur(contentEdge.radius.toPx()) },
                            ),
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .padding(
                    start = paddingValues.calculateStartPadding(layoutDirection),
                    end = paddingValues.calculateEndPadding(layoutDirection),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .clipToBounds(),
        ) { measurables, constraints ->
            val titlePlaceable = measurables[0].measure(constraints.copy(minHeight = 0))
            val titleOffset = scrollState.offset.roundToInt().coerceAtLeast(0)
            val visibleTitleHeight = (titlePlaceable.height - scrollState.headerOffset.roundToInt()).coerceAtLeast(0)
            val documentPlaceable = measurables[1].measure(constraints)
            val titleBlurPlaceable = measurables.getOrNull(2)?.measure(constraints.copy(minHeight = 0))
            layout(constraints.maxWidth, constraints.maxHeight) {
                documentPlaceable.placeRelative(0, if (PlatformDependent.isIos) 0 else visibleTitleHeight)
                titlePlaceable.placeRelative(0, -titleOffset)
                titleBlurPlaceable?.placeRelative(0, 0)
            }
        }
    }
}

@Composable
private fun ScaffoldWebView(
    modifier: Modifier,
    state: WebViewState,
    navigator: WebViewNavigator,
    loadingContent: @Composable () -> Unit,
    errorContent: @Composable () -> Unit,
    scrollState: WebViewScrollState,
    topBarHeight: Dp,
) {
    var nativeWebView by remember { mutableStateOf<NativeWebView?>(null) }
    val hasMainFrameError = state.errorsForCurrentRequest.any { it.isFromMainFrame }

    val contentState = when {
        hasMainFrameError -> WebViewContentState.Error
        state.isLoading -> WebViewContentState.Loading
        else -> WebViewContentState.Content
    }
    val contentEdge = IenScaffoldContentEdge(scrollableState = scrollState)
    WebViewScrollEffect(
        webView = nativeWebView,
        scrollState = scrollState,
        isContentReady = contentState == WebViewContentState.Content,
        contentEdge = contentEdge,
        topBarHeight = topBarHeight,
    )
    Box(modifier = modifier) {
        WebView(
            state = state,
            modifier = Modifier.fillMaxSize(),
            navigator = navigator,
            captureBackPresses = false,
            factory = rememberWebViewFactory(scrollState),
            onCreated = { webView: NativeWebView -> nativeWebView = webView },
            onDispose = { _: NativeWebView -> nativeWebView = null },
        )
        when (contentState) {
            WebViewContentState.Error -> Box(modifier = Modifier.fillMaxSize()) {
                errorContent()
            }
            WebViewContentState.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                loadingContent()
            }
            WebViewContentState.Content -> Unit
        }
    }
}
