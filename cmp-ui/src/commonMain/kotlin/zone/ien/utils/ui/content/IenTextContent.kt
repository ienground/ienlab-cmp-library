package zone.ien.utils.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * 제목, 본문, 강조 텍스트, 푸터를 포함할 수 있는 문단 UI 컴포넌트입니다.
 *
 * @param title 문단의 제목 텍스트 (선택 사항)
 * @param body 문단의 본문 텍스트
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param emphasis 본문 텍스트 내에서 강조할 문자열 (선택 사항)
 * @param footer 문단 하단에 표시할 설명이나 추가 정보 텍스트 (선택 사항)
 */
@Composable
fun IenParagraph(
    title: String? = null,
    body: String,
    modifier: Modifier = Modifier,
    emphasis: String? = null,
    footer: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        if (title != null) {
            IenText(title, style = IenTheme.typography.title3)
        }
        if (emphasis != null) {
            IenHighlightText(
                text = body,
                highlights = listOf(emphasis),
            )
        } else {
            IenText(body, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
        if (footer != null) {
            IenText(footer, style = IenTheme.typography.caption, color = IenTheme.colors.textTertiary)
        }
    }
}

/**
 * 작성자 정보, 제목, 설명, 미디어 영역, 메타데이터, 그리고 액션 버튼 등을 포함하는 포스트/카드 UI 컴포넌트입니다.
 *
 * @param title 포스트의 제목
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param author 포스트 작성자 이름 (선택 사항)
 * @param description 포스트의 상세 설명 텍스트 (선택 사항)
 * @param media 포스트에 삽입할 미디어 컴포저블 (선택 사항)
 * @param metadata 포스트 상단 우측에 표시할 메타데이터 영역 (선택 사항)
 * @param action 포스트 하단에 표시할 추가 액션 컴포저블 (선택 사항)
 */
@Composable
fun IenPost(
    title: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    description: String? = null,
    media: (@Composable () -> Unit)? = null,
    metadata: (@Composable RowScope.() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        ) {
            if (author != null || metadata != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    if (author != null) {
                        IenText(
                            text = author,
                            modifier = Modifier.weight(1f),
                            style = IenTheme.typography.caption,
                            color = IenTheme.colors.textTertiary,
                        )
                    }
                    metadata?.invoke(this)
                }
            }
            IenText(title, style = IenTheme.typography.title3, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (description != null) {
                IenText(description, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
            }
            media?.invoke()
            action?.invoke()
        }
    }
}
