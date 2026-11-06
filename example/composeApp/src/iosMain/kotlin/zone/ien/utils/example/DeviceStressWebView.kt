package zone.ien.utils.example

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.webview.AdaptiveWebViewScaffold

@Composable
internal fun DeviceStressWebView() {
    val html = remember { deviceStressHtml() }
    val state = rememberWebViewStateWithHTMLData(data = html)

    IenAdaptiveTheme(target = Theme.Cupertino) {
        AdaptiveWebViewScaffold(
            state = state,
            title = { Text(text = "웹뷰 스크롤 스트레스") },
            navigationIcon = { AdaptiveBackButton(onClick = {}) },
            errorContent = { Text(text = "로컬 테스트 페이지를 불러오지 못했습니다.") },
        )
    }
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
