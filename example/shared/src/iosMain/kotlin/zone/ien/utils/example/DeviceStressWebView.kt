package zone.ien.utils.example

import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewFactoryParam
import com.multiplatform.webview.web.defaultWebViewFactory
import kotlinx.coroutines.delay
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSProcessInfo
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.webview.AdaptiveWebViewScaffold

@OptIn(InternalComposeTracingApi::class)
@Composable
internal fun DeviceStressWebView(baseline: Boolean = false) {
    val html = remember {
        val start = NSProcessInfo.processInfo.systemUptime
        deviceStressHtml().also { reportWebViewMeasurement("html", start) }
    }
    val stateStart = NSProcessInfo.processInfo.systemUptime
    val state = rememberWebViewStateWithHTMLData(data = html)
    remember(state) { reportWebViewMeasurement("state", stateStart) }

    if (baseline) {
        WebView(
            state = state,
            modifier = Modifier.fillMaxSize(),
            factory = remember {
                { param: WebViewFactoryParam ->
                    val start = NSProcessInfo.processInfo.systemUptime
                    defaultWebViewFactory(param).also { reportWebViewMeasurement("native-factory", start) }
                }
            },
        )
        return
    }
    val tracer = remember {
        object : CompositionTracer {
            var scaffoldCompositions = 0
            var events = 0

            override fun isTraceInProgress(): Boolean = true
            override fun traceEventEnd() = Unit
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) {
                events++
                if (info.startsWith("zone.ien.utils.webview.WebViewScaffoldLayout (")) {
                    scaffoldCompositions++
                }
            }
        }
    }
    DisposableEffect(tracer) {
        Composer.setTracer(tracer)
        onDispose { Composer.setTracer(null) }
    }
    LaunchedEffect(tracer) {
        delay(2000)
        val initialCompositions = tracer.scaffoldCompositions
        delay(4000)
        reportWebViewCount("scaffold-scroll-compositions", tracer.scaffoldCompositions - initialCompositions)
        reportWebViewCount("composition-trace-events", tracer.events)
    }
    IenAdaptiveTheme(target = Theme.Cupertino) {
        AdaptiveWebViewScaffold(
            state = state,
            title = { Text(text = "웹뷰 스크롤 스트레스") },
            navigationIcon = { AdaptiveBackButton(onClick = {}) },
            errorContent = { Text(text = "로컬 테스트 페이지를 불러오지 못했습니다.") },
        )
    }
}

private fun reportWebViewCount(name: String, count: Int) {
    NSNotificationCenter.defaultCenter.postNotificationName(
        aName = "IENDeviceStressMeasurement",
        `object` = null,
        userInfo = mapOf("name" to name, "count" to count),
    )
}

private fun deviceStressHtml(): String = buildString {
    append(
        """<!doctype html>
            <html lang="ko">
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <style>
                * { box-sizing: border-box; }
                body { margin: 0; background: #f2f4f7; color: #20242a; font: 16px -apple-system, BlinkMacSystemFont, sans-serif; }
                main { padding: 16px; }
                article { min-height: 240px; margin: 0 0 14px; padding: 20px; border-radius: 18px; background: #fff; border: 1px solid #e1e5ea; }
                .eyebrow { margin: 0 0 8px; color: #64748b; font-size: 12px; font-weight: 600; letter-spacing: .08em; }
                h2 { margin: 0 0 12px; font-size: 21px; }
                p { margin: 0 0 12px; line-height: 1.55; }
                .detail { color: #596273; }
              </style>
            </head>
            <body><main>""".trimIndent(),
    )
    repeat(200) { index ->
        val row = index + 1
        append("<article><p class=\"eyebrow\">DEVICE STRESS · ROW $row / 200</p>")
        append("<h2>웹 콘텐츠 스크롤 항목 $row</h2>")
        append("<p>이 문서는 네트워크 요청 없이 로컬 HTML만 렌더링합니다. 반복되는 텍스트와 카드 구조를 사용해 긴 페이지를 만듭니다.</p>")
        append("<p class=\"detail\">WebKit이 긴 문서의 레이아웃, 합성, 스크롤 위치를 갱신하는 동안 Compose 화면 위에 실제 WKWebView가 표시됩니다. 이 행은 200개 중 $row 번째입니다.</p>")
        append("</article>")
    }
    append("</main></body></html>")
}

private fun reportWebViewMeasurement(name: String, startUptime: Double) {
    NSNotificationCenter.defaultCenter.postNotificationName(
        aName = "IENDeviceStressMeasurement",
        `object` = null,
        userInfo = mapOf(
            "name" to name,
            "durationMillis" to (NSProcessInfo.processInfo.systemUptime - startUptime) * 1000,
        ),
    )
}
