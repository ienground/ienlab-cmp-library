package zone.ien.utils.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowRightS
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenText

enum class IenListRowBorder {
    Indented,
    None,
}

enum class IenListRowDisabledStyle {
    Type1,
    Type2,
}

enum class IenListRowPadding {
    ExtraSmall,
    Small,
    Medium,
    Large,
    XLarge,
}

enum class IenListRowAlignment {
    Top,
    Center,
}

enum class IenListRowLoaderType {
    Square,
    Circle,
    Bar,
}

enum class IenListRowAssetShape {
    Original,
    Squircle,
    Card,
    Square,
    Circle,
}

enum class IenListRowAssetSize {
    XSmall,
    Small,
    Medium,
}

enum class IenListRowTextsType {
    OneRowTypeA,
    OneRowTypeB,
    OneRowTypeC,
    RightOneRowTypeA,
    RightOneRowTypeB,
    RightOneRowTypeC,
    RightOneRowTypeD,
    RightOneRowTypeE,
    TwoRowTypeA,
    TwoRowTypeB,
    TwoRowTypeC,
    TwoRowTypeD,
    TwoRowTypeE,
    TwoRowTypeF,
    RightTwoRowTypeA,
    RightTwoRowTypeB,
    RightTwoRowTypeC,
    RightTwoRowTypeD,
    RightTwoRowTypeE,
    ThreeRowTypeA,
    ThreeRowTypeB,
    ThreeRowTypeC,
    ThreeRowTypeD,
    ThreeRowTypeE,
    ThreeRowTypeF,
}

@Composable
fun IenListRow(
    contents: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    left: (@Composable () -> Unit)? = null,
    right: (@Composable RowScope.() -> Unit)? = null,
    border: IenListRowBorder = IenListRowBorder.Indented,
    disabled: Boolean = false,
    disabledStyle: IenListRowDisabledStyle = IenListRowDisabledStyle.Type1,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
    horizontalPadding: IenListRowPadding = IenListRowPadding.Medium,
    leftAlignment: IenListRowAlignment = IenListRowAlignment.Center,
    rightAlignment: IenListRowAlignment = IenListRowAlignment.Center,
    withArrow: Boolean = false,
    withTouchEffect: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val enabled = !disabled
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val hasTouchEffect = withTouchEffect || onClick != null
    val rowBackground by animateColorAsState(
        targetValue = when {
            disabled && disabledStyle == IenListRowDisabledStyle.Type2 -> IenTheme.colors.surfaceWeak
            pressed && hasTouchEffect && enabled -> IenTheme.colors.surfaceWeak
            else -> Color.Transparent
        },
        label = "ienListRowBackground",
    )
    val contentAlpha = if (disabled) 0.45f else 1f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(rowBackground)
            .then(
                if (onClick != null || withTouchEffect) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { onClick?.invoke() },
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        if (border == IenListRowBorder.Indented) {
            Box(
                modifier = Modifier
                    .padding(start = horizontalPadding.value + 20.dp)
                    .fillMaxWidth()
                    .height(IenTheme.stroke.hairline)
                    .background(IenTheme.colors.border),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                .padding(
                    horizontal = horizontalPadding.value,
                    vertical = verticalPadding.value,
                )
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            left?.let {
                Box(
                    modifier = Modifier.align(leftAlignment.toRowAlignment()),
                    contentAlignment = leftAlignment.toBoxAlignment(),
                ) {
                    it()
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                contents()
            }
            if (right != null || withArrow) {
                Row(
                    modifier = Modifier.align(rightAlignment.toRowAlignment()),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    verticalAlignment = rightAlignment.toRowAlignment(),
                ) {
                    right?.invoke(this)
                    if (withArrow) {
                        CompositionLocalProvider(
                            LocalContentColor provides IenTheme.colors.textTertiary
                        ) {
                            Icon(
                                imageVector = RemixIcons.Line.ArrowRightS,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IenListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    border: IenListRowBorder = IenListRowBorder.Indented,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
    horizontalPadding: IenListRowPadding = IenListRowPadding.Medium,
    withArrow: Boolean = false,
) {
    IenListRow(
        modifier = modifier,
        left = leading,
        right = trailing,
        border = border,
        disabled = !enabled,
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        withArrow = withArrow,
        onClick = onClick,
        contents = {
            IenListRowTexts(
                type = if (subtitle == null) IenListRowTextsType.OneRowTypeA else IenListRowTextsType.TwoRowTypeA,
                top = title,
                bottom = subtitle,
                topColor = if (selected) IenTheme.colors.brand else null,
            )
        },
    )
}

@Composable
fun IenListRowTexts(
    top: String,
    modifier: Modifier = Modifier,
    type: IenListRowTextsType = IenListRowTextsType.OneRowTypeA,
    middle: String? = null,
    bottom: String? = null,
    topColor: Color? = null,
    middleColor: Color? = null,
    bottomColor: Color? = null,
    maxLines: Int = 1,
) {
    val rightAligned = type.name.startsWith("Right")
    val textAlign = if (rightAligned) TextAlign.End else TextAlign.Start
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (rightAligned) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
    ) {
        IenText(
            text = top,
            style = type.topStyle(),
            color = topColor ?: type.topColor(),
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        if (middle != null && type.rowCount >= 3) {
            IenText(
                text = middle,
                style = type.middleStyle(),
                color = middleColor ?: type.middleColor(),
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (bottom != null && type.rowCount >= 2) {
            IenText(
                text = bottom,
                style = type.bottomStyle(),
                color = bottomColor ?: type.bottomColor(),
                textAlign = textAlign,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun IenListRowAssetText(
    text: String,
    modifier: Modifier = Modifier,
    shape: IenListRowAssetShape = IenListRowAssetShape.Squircle,
    size: IenListRowAssetSize = IenListRowAssetSize.Medium,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    contentColor: Color = IenTheme.colors.textPrimary,
) {
    Box(
        modifier = modifier
            .size(size.value)
            .clip(shape.toShape())
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        IenText(
            text = text,
            style = IenTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun IenListRowAssetFrame(
    modifier: Modifier = Modifier,
    shape: IenListRowAssetShape = IenListRowAssetShape.Squircle,
    size: IenListRowAssetSize = IenListRowAssetSize.Medium,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size.value)
            .clip(shape.toShape())
            .background(if (shape == IenListRowAssetShape.Original) Color.Transparent else backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun IenListRowLoader(
    modifier: Modifier = Modifier,
    type: IenListRowLoaderType = IenListRowLoaderType.Square,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
) {
    IenListRow(
        modifier = modifier,
        border = IenListRowBorder.None,
        verticalPadding = verticalPadding,
        horizontalPadding = IenListRowPadding.Medium,
        left = if (type == IenListRowLoaderType.Bar) {
            null
        } else {
            {
                when (type) {
                    IenListRowLoaderType.Square -> IenListRowLoaderBlock(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(IenTheme.radius.lg),
                    )

                    IenListRowLoaderType.Circle -> IenListRowLoaderBlock(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                    )

                    IenListRowLoaderType.Bar -> Unit
                }
            }
        },
        contents = {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                IenListRowLoaderBlock(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp),
                    shape = RoundedCornerShape(IenTheme.radius.full),
                )
                IenListRowLoaderBlock(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp),
                    shape = RoundedCornerShape(IenTheme.radius.full),
                )
            }
        },
    )
}

@Composable
private fun IenListRowLoaderBlock(
    modifier: Modifier,
    shape: Shape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(IenTheme.colors.surfaceWeak),
    )
}

private val IenListRowPadding.value: Dp
    get() = when (this) {
        IenListRowPadding.ExtraSmall -> 4.dp
        IenListRowPadding.Small -> 8.dp
        IenListRowPadding.Medium -> 12.dp
        IenListRowPadding.Large -> 16.dp
        IenListRowPadding.XLarge -> 24.dp
    }

private val IenListRowAssetSize.value: Dp
    get() = when (this) {
        IenListRowAssetSize.XSmall -> 32.dp
        IenListRowAssetSize.Small -> 40.dp
        IenListRowAssetSize.Medium -> 52.dp
    }

@Composable
private fun IenListRowAssetShape.toShape(): Shape = when (this) {
    IenListRowAssetShape.Original -> RoundedCornerShape(0.dp)
    IenListRowAssetShape.Squircle -> RoundedCornerShape(IenTheme.radius.lg)
    IenListRowAssetShape.Card -> RoundedCornerShape(IenTheme.radius.default)
    IenListRowAssetShape.Square -> RoundedCornerShape(IenTheme.radius.sm)
    IenListRowAssetShape.Circle -> CircleShape
}

private fun IenListRowAlignment.toRowAlignment(): Alignment.Vertical = when (this) {
    IenListRowAlignment.Top -> Alignment.Top
    IenListRowAlignment.Center -> Alignment.CenterVertically
}

private fun IenListRowAlignment.toBoxAlignment(): Alignment = when (this) {
    IenListRowAlignment.Top -> Alignment.TopCenter
    IenListRowAlignment.Center -> Alignment.Center
}

private val IenListRowTextsType.rowCount: Int
    get() = when {
        name.startsWith("Three") -> 3
        name.startsWith("Two") || name.startsWith("RightTwo") -> 2
        else -> 1
    }

@Composable
private fun IenListRowTextsType.topStyle(): TextStyle = when (this) {
    IenListRowTextsType.OneRowTypeB,
    IenListRowTextsType.OneRowTypeC,
    IenListRowTextsType.TwoRowTypeD,
    IenListRowTextsType.TwoRowTypeE,
    IenListRowTextsType.ThreeRowTypeC,
    IenListRowTextsType.ThreeRowTypeD -> IenTheme.typography.label1
    else -> IenTheme.typography.body1
}

@Composable
private fun IenListRowTextsType.middleStyle(): TextStyle = IenTheme.typography.body2

@Composable
private fun IenListRowTextsType.bottomStyle(): TextStyle = when (this) {
    IenListRowTextsType.TwoRowTypeF,
    IenListRowTextsType.ThreeRowTypeF -> IenTheme.typography.label2
    else -> IenTheme.typography.caption
}

@Composable
private fun IenListRowTextsType.topColor(): Color = when {
    name.startsWith("Right") -> IenTheme.colors.textSecondary
    this == IenListRowTextsType.OneRowTypeC -> IenTheme.colors.textSecondary
    else -> IenTheme.colors.textPrimary
}

@Composable
private fun IenListRowTextsType.middleColor(): Color = IenTheme.colors.textSecondary

@Composable
private fun IenListRowTextsType.bottomColor(): Color = when (this) {
    IenListRowTextsType.TwoRowTypeC,
    IenListRowTextsType.TwoRowTypeF,
    IenListRowTextsType.RightTwoRowTypeD,
    IenListRowTextsType.RightTwoRowTypeE -> IenTheme.colors.textPrimary
    else -> IenTheme.colors.textSecondary
}

enum class IenTableRowAlign {
    Left,
    SpaceBetween,
}

@Immutable
data class IenTableRowScope(
    val align: IenTableRowAlign,
)

@Composable
fun IenTableRow(
    modifier: Modifier = Modifier,
    left: String? = null,
    right: String? = null,
    label: String? = null,
    value: String? = null,
    description: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    align: IenTableRowAlign = IenTableRowAlign.SpaceBetween,
    leftRatio: Int? = null,
) {
    val resolvedLeft = left ?: label.orEmpty()
    val resolvedRight = right ?: value.orEmpty()
    val hasLegacyAffordance = label != null || value != null || description != null || leading != null || trailing != null

    IenTableRow(
        left = {
            if (hasLegacyAffordance) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    verticalAlignment = Alignment.Top,
                ) {
                    leading?.invoke()
                    Column {
                        IenText(resolvedLeft, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
                        if (description != null) {
                            IenText(description, style = IenTheme.typography.caption, color = IenTheme.colors.textTertiary)
                        }
                    }
                }
            } else {
                IenText(
                    text = resolvedLeft,
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        right = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = resolvedRight,
                    style = IenTheme.typography.body2,
                    color = if (hasLegacyAffordance) IenTheme.colors.textPrimary else IenTheme.colors.textSecondary,
                    textAlign = if (align == IenTableRowAlign.SpaceBetween) TextAlign.End else TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                trailing?.invoke(this)
            }
        },
        modifier = modifier,
        align = align,
        leftRatio = leftRatio,
    )
}

@Composable
fun IenTableRow(
    left: @Composable IenTableRowScope.() -> Unit,
    right: @Composable IenTableRowScope.() -> Unit,
    modifier: Modifier = Modifier,
    align: IenTableRowAlign = IenTableRowAlign.SpaceBetween,
    leftRatio: Int? = null,
) {
    val scope = IenTableRowScope(align = align)
    val safeLeftRatio = leftRatio?.coerceIn(1, 99)
    val leftWeight = safeLeftRatio?.toFloat()
    val rightWeight = safeLeftRatio?.let { (100 - it).coerceAtLeast(0).toFloat() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = when (align) {
            IenTableRowAlign.Left -> Arrangement.spacedBy(IenTheme.spacing.md)
            IenTableRowAlign.SpaceBetween -> Arrangement.SpaceBetween
        },
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = when {
                leftWeight != null && leftWeight <= 0f -> Modifier.weight(0.0001f)
                leftWeight != null -> Modifier.weight(leftWeight)
                align == IenTableRowAlign.SpaceBetween -> Modifier.weight(1f)
                else -> Modifier
            }
        ) {
            scope.left()
        }
        Box(
            modifier = when {
                rightWeight != null && rightWeight <= 0f -> Modifier.weight(0.0001f)
                rightWeight != null -> Modifier.weight(rightWeight)
                align == IenTableRowAlign.SpaceBetween -> Modifier.weight(1f)
                else -> Modifier
            },
            contentAlignment = if (align == IenTableRowAlign.SpaceBetween) Alignment.TopEnd else Alignment.TopStart,
        ) {
            scope.right()
        }
    }
}
