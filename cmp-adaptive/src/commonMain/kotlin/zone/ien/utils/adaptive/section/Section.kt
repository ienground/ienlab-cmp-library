package zone.ien.utils.adaptive.section

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.section.CupertinoSection
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.LocalSectionStyle
import zone.ien.hig.section.ProvideSectionStyle
import zone.ien.hig.section.SectionScope
import zone.ien.hig.section.SectionState
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.section.rememberSectionState
import zone.ien.hig.section.sectionContainerBackground
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.LocalTopBarScaffoldScrollState
import zone.ien.utils.ui.screen.LocalIenScaffoldScrollState
import zone.ien.utils.ui.section.IenSection
import zone.ien.utils.ui.section.ienSectionBackground
import zone.ien.utils.ui.utils.conditional

/**
 * 섹션 배경 설정 확장 함수
 *
 * @param sectionStyle 섹션 스타일
 * @return Modifier - 배경 설정 적용된 Modifier
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun Modifier.sectionBackground(
    sectionStyle: SectionStyle
): Modifier {
    return this
        .conditional(currentTheme == Theme.Material3) { ienSectionBackground() }
        .conditional(currentTheme == Theme.Cupertino) { sectionContainerBackground(sectionStyle) }
}

/**
 * IEN 섹션 스타일과 Backdrop을 적용하는 컴포저블
 *
 * Backdrop을 내부 스크롤 modifier보다 먼저 적용합니다.
 *
 * @param backdrop 섹션에 적용할 Backdrop
 * @param modifier 섹션에 적용할 Modifier
 * @param fullHeight 섹션의 높이를 화면 전체로 설정할지 여부
 * @param scrollState 섹션 내부의 스크롤 상태를 관리하는 ScrollState
 * @param shape 섹션의 모양을 정의하는 Shape
 * @param title 섹션의 제목을 표시하는 컴포저블
 * @param content 섹션의 내용을 표시하는 컴포저블 블록
 */
@Composable
fun IenProvideSectionStyle(
    backdrop: LayerBackdrop,
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = LocalIenScaffoldScrollState.current ?: rememberScrollState(),
    shape: Shape = RectangleShape,
    title: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .conditional(fullHeight) { fillMaxHeight() }
            .layerBackdrop(backdrop)
            .conditional(scrollState != null) { scrollState?.let { this.verticalScroll(it) } ?: this }
            .clip(shape)
            .then(modifier),
        content = {
            if (title != null) {
                title()
            } else {
                Spacer(modifier = Modifier)
            }
            content()
            Spacer(modifier = Modifier)
        }
    )
}

/**
 * 적응형 섹션 스타일 제공 컴포저블
 *
 * @param style 섹션 스타일
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param fullHeight 전체 높이 여부
 * @param scrollState 스크롤 상태
 * @param shape 모양
 * @param title 섹션 제목
 * @param backdrop Backdrop 컴포넌트
 * @param content 섹션 내부에 표시할 콘텐츠
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveProvideSectionStyle(
    style: SectionStyle,
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = LocalTopBarScaffoldScrollState.current ?: rememberScrollState(),
    shape: Shape = RectangleShape,
    title: @Composable (() -> Unit)? = null,
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    content: @Composable ColumnScope.() -> Unit
) {
    AdaptiveWidget(
        material = {
            IenProvideSectionStyle(
                backdrop = backdrop,
                modifier = modifier,
                fullHeight = fullHeight,
                scrollState = scrollState,
                shape = shape,
                title = title,
                content = content
            )
        },
        cupertino = {
            ProvideSectionStyle(
                style = style,
            ) {
                Column(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .conditional(fullHeight) { fillMaxHeight() }
                        .conditional(scrollState != null) { scrollState?.let { this.verticalScroll(it) } ?: this }
                        .clip(shape)
                        .sectionBackground(style)
                        .then(modifier)
                    ,
                ) {
                    title?.invoke()
                    content()
                }
            }
        }
    )
}

/**
 * 적응형 섹션 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param style 섹션 스타일
 * @param state 섹션 상태
 * @param enterTransition 진입 전환 효과
 * @param exitTransition 퇴장 전환 효과
 * @param shape 모양
 * @param color 색상
 * @param dividerPadding 구분선 패딩
 * @param contentPadding 콘텐츠 패딩
 * @param title 섹션 제목
 * @param caption 섹션 캡션
 * @param content 섹션 내부에 표시할 콘텐츠
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveSection(
    modifier: Modifier = Modifier,
    style: SectionStyle = LocalSectionStyle.current,
    state: SectionState = rememberSectionState(canCollapse = true),
    enterTransition: EnterTransition = CupertinoSectionDefaults.EnterTransition,
    exitTransition: ExitTransition = CupertinoSectionDefaults.ExitTransition,
    shape: ContinuousRoundedRectangle = CupertinoSectionDefaults.shape(style),
    color: Color = if (style.grouped) CupertinoSectionDefaults.Color else Color.Transparent,
    dividerPadding: PaddingValues = PaddingValues(start = CupertinoSectionDefaults.DividerPadding),
    contentPadding : PaddingValues = CupertinoSectionDefaults.paddingValues(style = style, includePaddingBetweenSections = true),
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    AdaptiveWidget(
        material = {
            IenSection(
                modifier = modifier,
                title = title,
                caption = caption,
                content = content
            )
        },
        cupertino = {
            CupertinoSection(
                modifier = modifier,
                style = style,
                state = state,
                enterTransition = enterTransition,
                exitTransition = exitTransition,
                shape = shape,
                color = color,
                dividerPadding = dividerPadding,
                contentPadding = contentPadding,
                title = title,
                caption = caption,
                content = content
            )
        }
    )
}
