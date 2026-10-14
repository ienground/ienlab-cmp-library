package zone.ien.utils.pref

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.LocalSectionStyle
import zone.ien.hig.section.SectionScope
import zone.ien.hig.section.SectionState
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.section.rememberSectionState
import zone.ien.utils.adaptive.section.AdaptiveSection
import zone.ien.utils.ui.section.IenSection

/**
 * 기존 위치 인자 호출을 유지하는 설정 그룹입니다.
 */
@Composable
fun ColumnScope.PrefsGroup(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    PrefsGroup(
        modifier = modifier,
        title = title,
        caption = caption,
        content = content,
        style = LocalSectionStyle.current,
    )
}

/**
 * 제목과 캡션이 있는 설정 그룹을 생성하는 Composable 함수입니다.
 *
 * 이 함수는 설정 화면에서 여러 설정 항목을 포함할 수 있는 섹션을 생성합니다.
 * 사용자 경험을 개선하기 위해 관련 설정을 함께 구성하도록 설계되었습니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 설정 그룹의 제목을 표시하는 선택적 Composable
 * @param caption 설정 그룹의 캡션을 표시하는 선택적 Composable
 * @param style Cupertino 섹션 스타일
 * @param state Cupertino 섹션 접힘 상태
 * @param enterTransition Cupertino 섹션 진입 효과
 * @param exitTransition Cupertino 섹션 퇴장 효과
 * @param shape Cupertino 섹션 모양
 * @param color Cupertino 섹션 배경색
 * @param dividerPadding Cupertino 구분선 여백
 * @param contentPadding Cupertino 콘텐츠 여백
 * @param content 실제 설정 항목을 포함하는 Composable 컨텐츠 블록
 */
@OptIn(ExperimentalCupertinoApi::class)
@Composable
fun ColumnScope.PrefsGroup(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    style: SectionStyle = LocalSectionStyle.current,
    state: SectionState = rememberSectionState(canCollapse = true),
    enterTransition: EnterTransition = CupertinoSectionDefaults.EnterTransition,
    exitTransition: ExitTransition = CupertinoSectionDefaults.ExitTransition,
    shape: ContinuousRoundedRectangle = CupertinoSectionDefaults.shape(style),
    color: Color = if (style.grouped) CupertinoSectionDefaults.Color else Color.Transparent,
    dividerPadding: PaddingValues = PaddingValues(start = CupertinoSectionDefaults.DividerPadding),
    contentPadding: PaddingValues = CupertinoSectionDefaults.paddingValues(style, includePaddingBetweenSections = true),
    content: @Composable SectionScope.() -> Unit
) {
    if (!LocalPrefsAdaptive.current) {
        IenSection(
            modifier = modifier,
            title = title,
            caption = caption,
            content = content,
        )
        return
    }
    AdaptiveSection(
        style = style,
        state = state,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        shape = shape,
        color = color,
        dividerPadding = dividerPadding,
        contentPadding = contentPadding,
        modifier = modifier,
        title = title,
        caption = caption,
        content = content
    )
}
