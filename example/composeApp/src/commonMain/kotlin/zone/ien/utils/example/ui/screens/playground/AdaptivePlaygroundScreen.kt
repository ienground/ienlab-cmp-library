package zone.ien.utils.example.ui.screens.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.component.AdaptiveButton
import zone.ien.utils.adaptive.component.AdaptiveCheckbox
import zone.ien.utils.adaptive.component.AdaptiveDotCheckbox
import zone.ien.utils.adaptive.component.AdaptiveExtendedFloatingActionButton
import zone.ien.utils.adaptive.component.AdaptiveFilledIconButton
import zone.ien.utils.adaptive.component.AdaptiveIconButton
import zone.ien.utils.adaptive.component.AdaptiveIconToggleButton
import zone.ien.utils.adaptive.component.AdaptiveLineCheckbox
import zone.ien.utils.adaptive.component.AdaptiveMediumFloatingActionButton
import zone.ien.utils.adaptive.component.AdaptiveSegmentedControl
import zone.ien.utils.adaptive.component.AdaptiveSlider
import zone.ien.utils.adaptive.component.AdaptiveSwitch
import zone.ien.utils.adaptive.component.AdaptiveTextButton
import zone.ien.utils.adaptive.component.AdaptiveToggleButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveCircularProgressIndicator
import zone.ien.utils.adaptive.view.AdaptiveLoadingIndicator
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenIconPlacement
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.interactive.IenToggleButton
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.TopBarMode

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptivePlaygroundScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    val backdrop = rememberDefaultBackdrop()
    val scrollState = rememberScrollState()
    var isMaterialTheme by remember { mutableStateOf(true) }
    var darkTheme by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var switchChecked by remember { mutableStateOf(true) }
    var checkboxChecked by remember { mutableStateOf(true) }
    var dotChecked by remember { mutableStateOf(false) }
    var lineChecked by remember { mutableStateOf(true) }
    var toggleChecked by remember { mutableStateOf(true) }
    var iconToggleChecked by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0.64f) }
    var segmentedIndex by remember { mutableIntStateOf(0) }
    var topBarMode by remember { mutableStateOf(TopBarMode.Expanded) }

    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
        darkTheme = darkTheme,
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = {
                AdaptiveBackButton(
                    backdrop = backdrop,
                    onClick = navigateBack,
                )
            },
            title = { IenText("Adaptive Playground") },
            subtitle = { IenText(if (isMaterialTheme) "Material3 + Ien" else "Cupertino + HIG") },
            modifier = modifier,
            adaptation = {
                material {
                    mode = topBarMode
                }
                cupertino {
                    this.backdrop = backdrop
                    mode = topBarMode
                }
            },
        ) { paddingValues, title ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop)
                    .background(IenTheme.colors.background)
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                title()

                PlaygroundGroup(title = "Theme") {
                    PlaygroundSwitchRow(
                        text = "Material theme",
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it },
                    )
                    PlaygroundSwitchRow(
                        text = "Dark theme",
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it },
                    )
                    PlaygroundSwitchRow(
                        text = "Components enabled",
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                    )
                    AdaptiveSegmentedControl(
                        items = listOf(
                            IenSegmentedControlItem("static", "Static"),
                            IenSegmentedControlItem("expanded", "Expanded"),
                        ),
                        value = when (topBarMode) {
                            TopBarMode.Static -> "static"
                            TopBarMode.Expanded -> "expanded"
                        },
                        onChange = { value ->
                            topBarMode = when (value) {
                                "static" -> TopBarMode.Static
                                else -> TopBarMode.Expanded
                            }
                        },
                    )
                }

                PlaygroundGroup(title = "Buttons") {
                    AdaptiveButton(
                        text = "Primary action",
                        onClick = {},
                        enabled = enabled,
                        display = IenButtonDisplay.Full,
                        icon = { SampleIcon() },
                    )
                    AdaptiveButton(
                        text = "Weak action",
                        onClick = {},
                        enabled = enabled,
                        variant = IenButtonVariant.Weak,
                        tone = IenSemanticTone.Success,
                        display = IenButtonDisplay.Full,
                        icon = { SampleIcon() },
                        iconPlacement = IenIconPlacement.End,
                    )
                    AdaptiveToggleButton(
                        checked = toggleChecked,
                        onCheckedChange = { toggleChecked = it },
                        text = if (toggleChecked) "Toggle enabled" else "Toggle disabled",
                        enabled = enabled,
                        display = IenButtonDisplay.Full,
                        shapes = IenToggleButton.Default.shapes(
                            checked = ContinuousCapsule(),
                            unchecked = ContinuousRoundedRectangle(IenTheme.radius.default),
                        ),
                        colors = IenToggleButton.Default.colors(
                            checkedTone = IenSemanticTone.Success,
                            uncheckedTone = IenSemanticTone.Neutral,
                            checkedBackgroundBrush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF34D399),
                                    Color(0xFF10B981),
                                    Color(0xFF2DD4BF),
                                )
                            ),
                        ),
                        icon = { SampleIcon() },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdaptiveTextButton(
                            text = "Text button",
                            onClick = {},
                            disabled = !enabled,
                        )
                        AdaptiveIconButton(
                            onClick = {},
                            enabled = enabled,
                            size = IenButtonSize.Medium,
                        ) {
                            SampleIcon()
                        }
                        AdaptiveFilledIconButton(
                            onClick = {},
                            enabled = enabled,
                            size = IenButtonSize.Medium,
                        ) {
                            SampleIcon()
                        }
                        AdaptiveIconToggleButton(
                            checked = iconToggleChecked,
                            onCheckedChange = { iconToggleChecked = it },
                            enabled = enabled,
                            size = IenButtonSize.Medium,
                            shapes = IenToggleButton.Default.shapes(
                                checked = CircleShape,
                                unchecked = ContinuousRoundedRectangle(IenTheme.radius.sm),
                            ),
                        ) {
                            SampleIcon()
                        }
                    }
                    AdaptiveExtendedFloatingActionButton(
                        text = "Extended floating action",
                        onClick = {},
                        enabled = enabled,
                        icon = { SampleIcon() },
                    )
                    AdaptiveMediumFloatingActionButton(
                        onClick = {},
                        content = { SampleIcon() },
                    )
                }

                PlaygroundGroup(title = "Selection") {
                    PlaygroundSwitchRow(
                        text = "AdaptiveSwitch",
                        checked = switchChecked,
                        enabled = enabled,
                        onCheckedChange = { switchChecked = it },
                    )
                    AdaptiveSegmentedControl(
                        items = listOf("One", "Two", "Three"),
                        selectedIndex = segmentedIndex,
                        onSelectedIndexChange = { segmentedIndex = it },
                        enabled = enabled,
                    )
                    AdaptiveSegmentedControl(
                        items = listOf(
                            IenSegmentedControlItem("small", "Small"),
                            IenSegmentedControlItem("regular", "Regular"),
                            IenSegmentedControlItem("large", "Large", enabled = false),
                        ),
                        value = when (segmentedIndex) {
                            0 -> "small"
                            1 -> "regular"
                            else -> "large"
                        },
                        onChange = { value ->
                            segmentedIndex = when (value) {
                                "small" -> 0
                                "regular" -> 1
                                else -> 2
                            }
                        },
                        enabled = enabled,
                    )
                    AdaptiveCheckbox(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                        enabled = enabled,
                        label = "Circle checkbox",
                    )
                    AdaptiveDotCheckbox(
                        checked = dotChecked,
                        onCheckedChange = { dotChecked = it },
                        enabled = enabled,
                        label = "Dot checkbox",
                    )
                    AdaptiveLineCheckbox(
                        checked = lineChecked,
                        onCheckedChange = { lineChecked = it },
                        enabled = enabled,
                        label = "Line checkbox",
                    )
                }

                PlaygroundGroup(title = "Feedback") {
                    AdaptiveSlider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        enabled = enabled,
                        label = "Slider",
                        valueLabel = "${(sliderValue * 100).toInt()}%",
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AdaptiveCircularProgressIndicator()
                        AdaptiveLoadingIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PlaygroundGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IenText(
            text = title,
            style = IenTheme.typography.title3,
            color = IenTheme.colors.textPrimary,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

@Composable
private fun PlaygroundSwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenText(text = text)
        AdaptiveSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }
}

@Composable
private fun SampleIcon() {
    IenIcon(
        imageVector = M3SystemIcons.Save,
        contentDescription = null,
        modifier = Modifier.size(20.dp),
    )
}

@Preview
@Composable
private fun AdaptivePlaygroundScreenPreview() {
    AdaptivePlaygroundScreen()
}
