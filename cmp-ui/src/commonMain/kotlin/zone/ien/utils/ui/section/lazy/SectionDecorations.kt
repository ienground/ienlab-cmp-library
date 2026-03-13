package zone.ien.utils.ui.section.lazy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SectionTitle(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = modifier
                .padding(top = 4.dp, bottom = 8.dp)
                .padding(horizontal = 22.dp),
        ) { content(PaddingValues(0.dp)) }
    }
}

@Composable
internal fun SectionCaption(
    content: @Composable () -> Unit,
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
    ) {
        content.let {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 22.dp),
            ) { it() }
        }
    }
}