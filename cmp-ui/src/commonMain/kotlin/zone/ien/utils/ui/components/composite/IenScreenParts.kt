package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenCheckbox
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

enum class IenTopBarTitleAlignment {
    Start,
    Center,
}

@Composable
fun IenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floating: @Composable () -> Unit = {},
    containerColor: Color = IenTheme.colors.background,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floating,
        containerColor = containerColor,
        contentColor = IenTheme.colors.textPrimary,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Composable
fun IenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = true,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
) {
    val insetPadding = windowInsets.asPaddingValues()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(IenTheme.colors.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = insetPadding.calculateTopPadding())
                .defaultMinSize(minHeight = 56.dp)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon?.invoke()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (titleAlignment == IenTopBarTitleAlignment.Center) Alignment.CenterHorizontally else Alignment.Start,
            ) {
                IenText(
                    text = title,
                    style = IenTheme.typography.title3,
                    textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                )
                if (subtitle != null) {
                    IenText(
                        text = subtitle,
                        style = IenTheme.typography.caption,
                        color = IenTheme.colors.textSecondary,
                        textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                    )
                }
            }
            actions?.invoke(this)
        }
        if (showDivider) {
            IenDivider()
        }
    }
}

@Composable
fun IenTop(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigation?.invoke()
        Column(Modifier.weight(1f)) {
            IenText(title, style = IenTheme.typography.title2)
            if (subtitle != null) {
                IenText(subtitle, style = IenTheme.typography.caption, color = IenTheme.colors.textSecondary)
            }
        }
        actions?.invoke(this)
    }
}

@Composable
fun IenTooltip(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    anchor: (@Composable BoxScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        anchor?.let {
            Box(content = it)
        }
        IenSurface(
            color = if (tone == IenSemanticTone.Neutral) IenTheme.colors.textPrimary else zone.ien.utils.ui.components.interactive.toneColor(tone),
            contentColor = IenTheme.colors.surface,
            shape = RoundedCornerShape(IenTheme.radius.sm),
        ) {
            IenText(
                text = text,
                modifier = Modifier.padding(horizontal = IenTheme.spacing.sm, vertical = IenTheme.spacing.xs),
                style = IenTheme.typography.caption,
                color = IenTheme.colors.surface,
            )
        }
    }
}

@Immutable
data class IenAgreementItemV4(
    val id: String,
    val title: String,
    val checked: Boolean,
    val required: Boolean = false,
    val description: String? = null,
)

@Composable
fun IenAgreementV4(
    items: List<IenAgreementItemV4>,
    onItemCheckedChange: (id: String, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "약관 동의",
    onAllCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val allChecked = items.isNotEmpty() && items.all { it.checked }
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
        shape = RoundedCornerShape(IenTheme.radius.lg),
    ) {
        Column(Modifier.padding(IenTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm)) {
            IenCheckbox(
                checked = allChecked,
                onCheckedChange = { checked ->
                    if (onAllCheckedChange != null) {
                        onAllCheckedChange(checked)
                    } else {
                        items.forEach { onItemCheckedChange(it.id, checked) }
                    }
                },
                label = title,
            )
            IenDivider()
            items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        IenCheckbox(
                            checked = item.checked,
                            onCheckedChange = { onItemCheckedChange(item.id, it) },
                            label = "${if (item.required) "[필수] " else "[선택] "}${item.title}",
                        )
                        if (item.description != null) {
                            IenText(
                                text = item.description,
                                modifier = Modifier.padding(start = IenTheme.state.minimumTouchTarget),
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.textTertiary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IenBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: IenButtonVariant = IenButtonVariant.Fill,
) {
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
    ) {
        IenButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.padding(IenTheme.spacing.md),
            fullWidth = true,
            state = zone.ien.utils.ui.components.interactive.IenButtonState(enabled = enabled),
            variant = variant,
        )
    }
}

@Composable
fun IenDoubleBottomCTA(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
    secondaryEnabled: Boolean = true,
) {
    IenSurface(modifier = modifier.fillMaxWidth(), color = IenTheme.colors.surface) {
        Row(
            modifier = Modifier.padding(IenTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            IenButton(
                text = secondaryText,
                onClick = onSecondaryClick,
                modifier = Modifier.weight(1f),
                size = IenButtonSize.Large,
                variant = IenButtonVariant.Weak,
                state = zone.ien.utils.ui.components.interactive.IenButtonState(enabled = secondaryEnabled),
            )
            IenButton(
                text = primaryText,
                onClick = onPrimaryClick,
                modifier = Modifier.weight(1f),
                size = IenButtonSize.Large,
                state = zone.ien.utils.ui.components.interactive.IenButtonState(enabled = primaryEnabled),
            )
        }
    }
}

@Composable
fun BoxScope.IenFixedBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.md),
    enabled: Boolean = true,
) {
    IenSurface(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(),
        color = IenTheme.colors.surface,
        tonalElevation = IenTheme.elevation.floating,
    ) {
        IenButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.padding(contentPadding),
            fullWidth = true,
            state = zone.ien.utils.ui.components.interactive.IenButtonState(enabled = enabled),
        )
    }
}
