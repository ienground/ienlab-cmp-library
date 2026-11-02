package zone.ien.utils.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import androidx.compose.foundation.layout.Box

/**
 * 제목, 본문, 강조 텍스트, 푸터를 포함할 수 있는 문단 UI 컴포넌트입니다.
 *
 * @param title 문단의 제목 텍스트 (선택 사항)
 * @param body 문단의 본문 텍스트
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param emphasis 본문 텍스트 내에서 강조할 문자열 목록
 * @param footer 문단 하단에 표시할 설명이나 추가 정보 텍스트 (선택 사항)
 * @param tone 강조 텍스트에 사용할 의미적 톤 설정 ([IenSemanticTone])
 * @param bold 강조 구간을 볼드체로 표시할지 여부 (기본값: false)
 */
@Composable
fun IenParagraph(
    title: String? = null,
    body: String,
    modifier: Modifier = Modifier,
    emphasis: List<String> = emptyList(),
    footer: String? = null,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    bold: Boolean = false,
) {
    IenParagraph(
        title = title?.let { value -> { Text(text = value) } },
        body = {
            if (emphasis.any(String::isNotEmpty)) {
                IenHighlightText(text = body, highlights = emphasis, tone = tone, bold = bold)
            } else {
                Text(text = body)
            }
        },
        modifier = modifier,
        footer = footer?.let { value -> { Text(text = value) } },
    )
}

/** 문단의 제목, 본문, 푸터를 컴포저블로 구성합니다. 강조 표시는 본문 슬롯에서 직접 지정합니다. */
@Composable
fun IenParagraph(
    title: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    footer: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        if (title != null) {
            IenProvideTextStyle(style = IenTheme.typography.title3) { title() }
        }
        IenProvideTextStyle(
            style = IenTheme.typography.body2,
            color = IenTheme.colors.textSecondary,
        ) { body() }
        if (footer != null) {
            IenProvideTextStyle(
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textTertiary,
            ) { footer() }
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
    IenPost(
        title = {
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = modifier,
        author = author?.let { value -> { Text(text = value) } },
        description = description?.let { value -> { Text(text = value) } },
        media = media,
        metadata = metadata,
        action = action,
    )
}

/**
 * 포스트의 제목, 작성자, 설명을 컴포저블로 구성합니다.
 *
 * @param title 포스트 제목 슬롯
 * @param author 작성자 슬롯. null이면 작성자 영역을 생략합니다.
 * @param description 설명 슬롯. null이면 설명 영역을 생략합니다.
 */
@Composable
fun IenPost(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    author: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
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
                        Box(modifier = Modifier.weight(1f)) {
                            IenProvideTextStyle(
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.textTertiary,
                            ) {
                                author()
                            }
                        }
                    }
                    metadata?.invoke(this)
                }
            }
            IenProvideTextStyle(
                style = IenTheme.typography.title3,
            ) {
                title()
            }
            if (description != null) {
                IenProvideTextStyle(
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textSecondary,
                ) {
                    description()
                }
            }
            media?.invoke()
            action?.invoke()
        }
    }
}
