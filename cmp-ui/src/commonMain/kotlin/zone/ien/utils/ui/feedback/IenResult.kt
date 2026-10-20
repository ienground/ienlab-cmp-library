package zone.ien.utils.ui.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import zone.ien.utils.ui.primitives.IenAssetFrame
import zone.ien.utils.ui.primitives.IenAssetFrameShape
import zone.ien.utils.ui.primitives.IenAssetFrameSize
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle

/**
 * 결과 카드([IenResult])의 결과 유형(성공, 실패 등) 및 테마 톤을 결정하는 Enum 클래스입니다.
 */
enum class IenResultTone { Success, Failure, Empty, Info }

/**
 * 특정 액션 또는 작업의 결과 화면(예: 빈 페이지, 오류 안내, 성공 알림 등)을 레이아웃으로 렌더링하는 범용 결과 표시 컴포저블입니다.
 *
 * @param title 결과 화면에 보여줄 메인 제목 컴포저블
 * @param modifier 적용할 [Modifier]
 * @param description 메인 제목 하단에 배치할 보조 설명 컴포저블
 * @param tone 결과 상태 및 아이콘 프레임의 시맨틱 톤 ([IenResultTone])
 * @param icon 결과 톤이 적용된 원형 프레임 안에 배치할 아이콘 콘텐츠
 * @param primaryAction 주요 클릭 이벤트/액션 버튼 컴포저블
 * @param secondaryAction 보조 클릭 이벤트/액션 버튼 컴포저블
 */
@Composable
fun IenResult(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    tone: IenResultTone = IenResultTone.Info,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    primaryAction: (@Composable () -> Unit)? = null,
    secondaryAction: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(IenTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            IenTheme.spacing.md,
            Alignment.CenterVertically,
        ),
    ) {
        val iconTone = when (tone) {
            IenResultTone.Success -> IenSemanticTone.Success
            IenResultTone.Failure -> IenSemanticTone.Danger
            IenResultTone.Empty -> IenSemanticTone.Neutral
            IenResultTone.Info -> IenSemanticTone.Info
        }
        IenAssetFrame(
            size = IenAssetFrameSize.Large,
            tone = iconTone,
            shape = IenAssetFrameShape.Circle,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                icon?.invoke(this)
            }
        }
        IenProvideTextStyle(
            style = IenTheme.typography.title2.copy(
                color = IenTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        ) {
            Box(modifier = Modifier.padding(top = IenTheme.spacing.xs)) {
                title()
            }
        }
        if (description != null) {
            IenProvideTextStyle(
                style = IenTheme.typography.body2.copy(
                    color = IenTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                ),
            ) {
                description()
            }
        }
        primaryAction?.invoke()
        secondaryAction?.invoke()
    }
}

/** 기존 문자열 인자 호출을 현재 결과 슬롯 API로 전달합니다. */
@Deprecated("Use the composable title and description slots instead.")
@Composable
fun IenResult(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: IenResultTone = IenResultTone.Info,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    primaryAction: (@Composable () -> Unit)? = null,
    secondaryAction: (@Composable () -> Unit)? = null,
) {
    IenResult(
        title = { Text(title) },
        modifier = modifier,
        description = description?.let { text -> { Text(text) } },
        tone = tone,
        icon = icon,
        primaryAction = primaryAction,
        secondaryAction = secondaryAction,
    )
}
