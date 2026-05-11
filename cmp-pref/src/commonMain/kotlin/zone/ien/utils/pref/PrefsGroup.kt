package zone.ien.utils.pref

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSection

/**
 * 제목과 캡션이 있는 설정 그룹을 생성하는 Composable 함수입니다.
 *
 * 이 함수는 설정 화면에서 여러 설정 항목을 포함할 수 있는 섹션을 생성합니다.
 * 사용자 경험을 개선하기 위해 관련 설정을 함께 구성하도록 설계되었습니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 설정 그룹의 제목을 표시하는 선택적 Composable
 * @param caption 설정 그룹의 캡션을 표시하는 선택적 Composable
 * @param content 실제 설정 항목을 포함하는 Composable 컨텐츠 블록
 */
@Composable
fun ColumnScope.PrefsGroup(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    AdaptiveSection(
        modifier = modifier,
        title = title,
        caption = caption,
        content = content
    )
}