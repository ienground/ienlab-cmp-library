package zone.ien.utils.adaptive.section

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.shapes.RoundedRectangle
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
import zone.ien.utils.ui.section.M3ProvideSectionStyle
import zone.ien.utils.ui.section.M3Section
import zone.ien.utils.ui.section.m3SectionBackground
import zone.ien.utils.ui.utils.conditional

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun Modifier.sectionBackground(
    sectionStyle: SectionStyle
): Modifier {
    return this
        .conditional(currentTheme == Theme.Material3) { m3SectionBackground() }
        .conditional(currentTheme == Theme.Cupertino) { sectionContainerBackground(sectionStyle) }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveProvideSectionStyle(
    style: SectionStyle,
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = rememberScrollState(),
    shape: Shape = RectangleShape,
    title: @Composable (() -> Unit)? = null,
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    content: @Composable ColumnScope.() -> Unit
) {
    AdaptiveWidget(
        material = {
            M3ProvideSectionStyle(
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

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveSection(
    modifier: Modifier = Modifier,
    style: SectionStyle = LocalSectionStyle.current,
    state: SectionState = rememberSectionState(canCollapse = true),
    enterTransition: EnterTransition = CupertinoSectionDefaults.EnterTransition,
    exitTransition: ExitTransition = CupertinoSectionDefaults.ExitTransition,
    shape: RoundedRectangle = CupertinoSectionDefaults.shape(style),
    color: Color = if (style.grouped) CupertinoSectionDefaults.Color else Color.Transparent,
    dividerPadding: PaddingValues = PaddingValues(start = CupertinoSectionDefaults.DividerPadding),
    contentPadding : PaddingValues = CupertinoSectionDefaults.paddingValues(style = style, includePaddingBetweenSections = true),
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    AdaptiveWidget(
        material = {
            M3Section(
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