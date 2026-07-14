package zone.ien.utils.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneWeakColor
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * 말풍선(Bubble)의 꼬리 방향을 나타내는 열거형 클래스입니다.
 */
enum class IenBubbleTail {
    /** 꼬리 없음 */
    None,
    /** 시작 부분(왼쪽)에 꼬리 배치 */
    Start,
    /** 끝 부분(오른쪽)에 꼬리 배치 */
    End,
}

/**
 * 텍스트를 포함하는 말풍선 UI 컴포넌트입니다.
 *
 * @param text 말풍선에 표시될 텍스트
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param tone 말풍선의 의미적 톤 설정 ([IenSemanticTone])
 * @param tail 말풍선 꼬리의 위치 및 방향 ([IenBubbleTail])
 * @param contentPadding 말풍선 내부 텍스트의 패딩 ([PaddingValues])
 */
@Composable
fun IenBubble(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    tail: IenBubbleTail = IenBubbleTail.None,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
) {
    val container = if (tone == IenSemanticTone.Neutral) IenTheme.colors.surfaceWeak else toneWeakColor(tone)
    val content = if (tone == IenSemanticTone.Neutral) IenTheme.colors.textPrimary else toneColor(tone)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (tail == IenBubbleTail.Start) {
            BubbleTail(color = container)
        }
        IenSurface(
            color = container,
            contentColor = content,
            shape = ContinuousRoundedRectangle(IenTheme.radius.lg),
        ) {
            IenText(
                text = text,
                modifier = Modifier.padding(contentPadding),
                style = IenTheme.typography.body2,
                color = LocalContentColor.current,
            )
        }
        if (tail == IenBubbleTail.End) {
            BubbleTail(color = container)
        }
    }
}

@Composable
private fun BubbleTail(color: Color) {
    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .rotate(45f)
            .background(color)
            .padding(4.dp),
    )
}

/**
 * 지정된 텍스트 내에서 특정 하이라이트 키워드들을 강조 표시하는 텍스트 컴포넌트입니다.
 *
 * @param text 전체 텍스트
 * @param highlights 강조 표시할 키워드 목록
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param tone 강조 표시 색상에 사용할 의미적 톤 설정 ([IenSemanticTone])
 * @param ignoreCase 키워드 매칭 시 대소문자를 구분하지 않을지 여부 (기본값: true)
 */
@Composable
fun IenHighlightText(
    text: String,
    highlights: List<String>,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    ignoreCase: Boolean = true,
) {
    val highlightColor = toneColor(tone)
    val annotated = buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            val match = highlights
                .asSequence()
                .filter { it.isNotEmpty() }
                .mapNotNull { query ->
                    val index = text.indexOf(query, startIndex = cursor, ignoreCase = ignoreCase)
                    if (index >= 0) index to query.length else null
                }
                .minByOrNull { it.first }

            if (match == null) {
                append(text.substring(cursor))
                break
            }

            val start = match.first
            val end = start + match.second
            if (cursor < start) append(text.substring(cursor, start))
            withStyle(SpanStyle(color = highlightColor)) {
                append(text.substring(start, end))
            }
            cursor = end
        }
    }

    androidx.compose.material3.Text(
        text = annotated,
        modifier = modifier,
        style = IenTheme.typography.body2,
        color = IenTheme.colors.textPrimary,
    )
}
