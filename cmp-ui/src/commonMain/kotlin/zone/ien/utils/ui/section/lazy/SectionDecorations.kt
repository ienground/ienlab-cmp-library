package zone.ien.utils.ui.section.lazy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle

@Composable
internal fun SectionTitle(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    IenProvideTextStyle(
        style = IenTheme.typography.label1,
        color = IenTheme.colors.textSecondary,
    ) {
        Box(
            modifier = modifier
                .padding(bottom = IenTheme.spacing.xs)
                .padding(horizontal = IenTheme.spacing.xl),
        ) { content(PaddingValues()) }
    }
}

@Composable
internal fun SectionCaption(
    content: @Composable () -> Unit,
) {
    IenProvideTextStyle(
        style = IenTheme.typography.caption,
        color = IenTheme.colors.textTertiary,
    ) {
        content.let {
            Box(
                modifier = Modifier
                    .padding(top = IenTheme.spacing.xs)
                    .padding(horizontal = IenTheme.spacing.xl),
            ) { it() }
        }
    }
}
