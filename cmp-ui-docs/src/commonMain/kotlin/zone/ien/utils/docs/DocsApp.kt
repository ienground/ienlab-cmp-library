package zone.ien.utils.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Font
import zone.ien.utils.docs.generated.resources.Pretendard_Regular
import zone.ien.utils.docs.generated.resources.Res
import zone.ien.utils.example.ui.screens.designsystem.AgreementSection
import zone.ien.utils.example.ui.screens.designsystem.AssetSection
import zone.ien.utils.example.ui.screens.designsystem.BadgeSection
import zone.ien.utils.example.ui.screens.designsystem.BoardRowSection
import zone.ien.utils.example.ui.screens.designsystem.BorderSection
import zone.ien.utils.example.ui.screens.designsystem.BottomCTASection
import zone.ien.utils.example.ui.screens.designsystem.BottomInfoSection
import zone.ien.utils.example.ui.screens.designsystem.BottomSheetSection
import zone.ien.utils.example.ui.screens.designsystem.BubbleSection
import zone.ien.utils.example.ui.screens.designsystem.ButtonSection
import zone.ien.utils.example.ui.screens.designsystem.CheckboxSection
import zone.ien.utils.example.ui.screens.designsystem.DialogSection
import zone.ien.utils.example.ui.screens.designsystem.FabSection
import zone.ien.utils.example.ui.screens.designsystem.HighlightSection
import zone.ien.utils.example.ui.screens.designsystem.IconButtonSection
import zone.ien.utils.example.ui.screens.designsystem.KeypadSection
import zone.ien.utils.example.ui.screens.designsystem.ListFooterSection
import zone.ien.utils.example.ui.screens.designsystem.ListHeaderSection
import zone.ien.utils.example.ui.screens.designsystem.ListRowSection
import zone.ien.utils.example.ui.screens.designsystem.LoaderSection
import zone.ien.utils.example.ui.screens.designsystem.MenuSection
import zone.ien.utils.example.ui.screens.designsystem.ModalSection
import zone.ien.utils.example.ui.screens.designsystem.NumericSpinnerSection
import zone.ien.utils.example.ui.screens.designsystem.ParagraphSection
import zone.ien.utils.example.ui.screens.designsystem.PostSection
import zone.ien.utils.example.ui.screens.designsystem.PrimitivesSection
import zone.ien.utils.example.ui.screens.designsystem.ProgressBarSection
import zone.ien.utils.example.ui.screens.designsystem.ProgressStepperSection
import zone.ien.utils.example.ui.screens.designsystem.RatingSection
import zone.ien.utils.example.ui.screens.designsystem.ResultSection
import zone.ien.utils.example.ui.screens.designsystem.SearchFieldSection
import zone.ien.utils.example.ui.screens.designsystem.SegmentedControlSection
import zone.ien.utils.example.ui.screens.designsystem.SkeletonSection
import zone.ien.utils.example.ui.screens.designsystem.SliderSection
import zone.ien.utils.example.ui.screens.designsystem.SplitTextFieldSection
import zone.ien.utils.example.ui.screens.designsystem.StepperSection
import zone.ien.utils.example.ui.screens.designsystem.SwitchSection
import zone.ien.utils.example.ui.screens.designsystem.TabSection
import zone.ien.utils.example.ui.screens.designsystem.TableRowSection
import zone.ien.utils.example.ui.screens.designsystem.TextAreaSection
import zone.ien.utils.example.ui.screens.designsystem.TextButtonSection
import zone.ien.utils.example.ui.screens.designsystem.TextFieldSection
import zone.ien.utils.example.ui.screens.designsystem.ToastSection
import zone.ien.utils.example.ui.screens.designsystem.TooltipSection
import zone.ien.utils.example.ui.screens.designsystem.TopSection
import zone.ien.utils.ui.feedback.IenToast
import zone.ien.utils.ui.feedback.IenToastAction
import zone.ien.utils.ui.feedback.IenToastIcon
import zone.ien.utils.ui.feedback.IenToastPosition
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.IenTokens
import zone.ien.utils.ui.foundation.defaultIenTokens
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.getIenTypography

@Composable
fun DocsApp() {
    val docsTokens = docsIenTokens()

    IenTheme(
        tokens = docsTokens,
        darkTheme = false,
    ) {
        var showTopToast by remember { mutableStateOf(false) }
        var showBottomToast by remember { mutableStateOf(false) }
        var showIconToast by remember { mutableStateOf(false) }
        var showActionToast by remember { mutableStateOf(false) }
        var showCtaToast by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        IenProvideTextStyle(
            style = IenTheme.typography.body2,
            color = IenTheme.colors.textPrimary,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IenTheme.colors.background),
            ) {
                DocsSidebar()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 40.dp, vertical = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    DocsHero()
                    TokenSection()
                    ColorTokenSection("Light colors", docsTokens.lightColors)
                    ColorTokenSection("Dark colors", docsTokens.darkColors)
                    ComponentCatalogIntro()
                    BadgeSection()
                    BoardRowSection()
                    BorderSection()
                    BottomInfoSection()
                    BottomSheetSection()
                    BubbleSection()
                    ButtonSection()
                    CheckboxSection()
                    FabSection()
                    HighlightSection()
                    IconButtonSection()
                    ListFooterSection()
                    ListHeaderSection()
                    LoaderSection()
                    MenuSection()
                    ModalSection()
                    NumericSpinnerSection()
                    ParagraphSection()
                    PostSection()
                    ProgressBarSection()
                    ProgressStepperSection()
                    RatingSection()
                    ResultSection()
                    SearchFieldSection()
                    SegmentedControlSection()
                    SkeletonSection()
                    SliderSection()
                    StepperSection()
                    SwitchSection()
                    TabSection()
                    TableRowSection()
                    TextButtonSection()
                    ToastSection(
                        showTopToast = showTopToast,
                        showBottomToast = showBottomToast,
                        showIconToast = showIconToast,
                        showActionToast = showActionToast,
                        showCtaToast = showCtaToast,
                        onShowTopToastChange = { showTopToast = it },
                        onShowBottomToastChange = { showBottomToast = it },
                        onShowIconToastChange = { showIconToast = it },
                        onShowActionToastChange = { showActionToast = it },
                        onShowCtaToastChange = { showCtaToast = it },
                    )
                    TooltipSection()
                    TopSection()
                    AgreementSection()
                    AssetSection()
                    BottomCTASection()
                    DialogSection()
                    KeypadSection()
                    ListRowSection()
                    TextFieldSection()
                    SplitTextFieldSection()
                    TextAreaSection()
                    PrimitivesSection()
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

            IenToast(
                open = showTopToast,
                position = IenToastPosition.Top,
                text = "Top toast message",
                onClose = { showTopToast = false },
            )
            IenToast(
                open = showBottomToast,
                position = IenToastPosition.Bottom,
                text = "Bottom toast message",
                onClose = { showBottomToast = false },
            )
            IenToast(
                open = showIconToast,
                position = IenToastPosition.Top,
                text = "Toast with icon",
                leftAddon = { IenToastIcon(tone = IenSemanticTone.Success) },
                onClose = { showIconToast = false },
            )
            IenToast(
                open = showActionToast,
                position = IenToastPosition.Bottom,
                text = "Toast with action",
                button = IenToastAction("OK") { showActionToast = false },
                onClose = { showActionToast = false },
            )
            IenToast(
                open = showCtaToast,
                position = IenToastPosition.Bottom,
                text = "Toast above CTA",
                higherThanCTA = true,
                onClose = { showCtaToast = false },
            )
        }
    }
}

@Composable
private fun docsIenTokens(): IenTokens {
    val baseTokens = defaultIenTokens()
    val fontFamily = FontFamily(Font(Res.font.Pretendard_Regular))

    return baseTokens.copy(
        typography = getIenTypography(fontFamily),
    )
}

@Composable
private fun DocsSidebar() {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(IenTheme.colors.surfaceWeak)
            .border(width = 1.dp, color = IenTheme.colors.border)
            .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        IenText("CMP UI", style = IenTheme.typography.title1)
        IenText(
            text = "Wasm design system specification",
            style = IenTheme.typography.body2,
            color = IenTheme.colors.textSecondary,
        )
        IenDivider()
        SidebarGroup("Foundations", listOf("Colors", "Typography", "Spacing", "Radius", "Stroke", "Elevation"))
        SidebarGroup(
            title = "Components",
            items = listOf(
                "Badge",
                "Button",
                "Input",
                "Feedback",
                "Navigation",
                "List",
                "Screen",
                "Dialog",
                "Primitive",
            ),
        )
    }
}

@Composable
private fun SidebarGroup(
    title: String,
    items: List<String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        IenText(
            text = title.uppercase(),
            style = IenTheme.typography.caption,
            color = IenTheme.colors.textTertiary,
            fontWeight = FontWeight.Bold,
        )
        items.forEach { item ->
            IenText(
                text = item,
                style = IenTheme.typography.label1,
                color = IenTheme.colors.textSecondary,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun DocsHero() {
    IenSurface(
        modifier = Modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
        tonalElevation = IenTheme.elevation.raised,
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            IenText("CMP UI Docs", style = IenTheme.typography.display)
            IenText(
                text = "A full Compose Multiplatform Wasm page for foundations, color samples, and the complete component catalog from DesignSystemScreen.",
                style = IenTheme.typography.body1,
                color = IenTheme.colors.textSecondary,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoPill("Compose Wasm")
                InfoPill("Real cmp-ui")
                InfoPill("DesignSystemScreen")
                InfoPill("Tokens")
            }
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(IenTheme.radius.full))
            .background(IenTheme.colors.brandWeak)
            .padding(horizontal = 12.dp, vertical = 7.dp),
    ) {
        IenText(text, style = IenTheme.typography.label2, color = IenTheme.colors.brand)
    }
}

@Composable
private fun TokenSection() {
    DocsSection(title = "Foundation tokens") {
        TokenRows(
            title = "Typography",
            rows = listOf(
                "display" to "30 / 38 Bold",
                "title1" to "24 / 32 Bold",
                "title2" to "20 / 28 SemiBold",
                "title3" to "18 / 26 SemiBold",
                "body1" to "16 / 24 Regular",
                "body2" to "15 / 22 Regular",
                "label1" to "14 / 20 SemiBold",
                "label2" to "13 / 18 SemiBold",
                "caption" to "12 / 16 Regular",
            ),
        )
        TokenRows(
            title = "Spacing",
            rows = listOf(
                "none" to "0dp",
                "xxxs" to "2dp",
                "xxs" to "4dp",
                "xs" to "8dp",
                "sm" to "12dp",
                "md" to "16dp",
                "lg" to "20dp",
                "xl" to "24dp",
                "xxl" to "32dp",
                "xxxl" to "40dp",
            ),
        )
        TokenRows(
            title = "Radius",
            rows = listOf(
                "none" to "0dp",
                "xs" to "4dp",
                "sm" to "8dp",
                "default" to "12dp",
                "md" to "12dp",
                "lg" to "16dp",
                "xl" to "24dp",
                "full" to "999dp",
            ),
        )
        TokenRows(
            title = "Stroke / Elevation / Icon",
            rows = listOf(
                "stroke.hairline" to "0.5dp",
                "stroke.thin" to "1dp",
                "stroke.medium" to "1.5dp",
                "stroke.thick" to "2dp",
                "elevation.raised" to "4dp",
                "elevation.floating" to "12dp",
                "icon.md" to "20dp",
                "icon.xl" to "32dp",
            ),
        )
    }
}

@Composable
private fun TokenRows(
    title: String,
    rows: List<Pair<String, String>>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        IenText(title, style = IenTheme.typography.title3)
        rows.chunked(2).forEach { line ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                line.forEach { (name, value) ->
                    TokenCell(name = name, value = value, modifier = Modifier.weight(1f))
                }
                if (line.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TokenCell(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(IenTheme.radius.sm))
            .background(IenTheme.colors.surfaceWeak)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenText(name, style = IenTheme.typography.label1)
        IenText(value, style = IenTheme.typography.caption, color = IenTheme.colors.textSecondary)
    }
}

@Composable
private fun ColorTokenSection(
    title: String,
    colors: IenColorScheme,
) {
    DocsSection(title = title) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            colorSamples(colors).forEach { sample ->
                ColorSample(sample)
            }
        }
    }
}

@Composable
private fun ColorSample(sample: ColorSample) {
    Column(
        modifier = Modifier
            .width(190.dp)
            .clip(RoundedCornerShape(IenTheme.radius.md))
            .background(IenTheme.colors.surfaceWeak)
            .border(1.dp, IenTheme.colors.border, RoundedCornerShape(IenTheme.radius.md))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(IenTheme.radius.sm))
                .background(sample.color)
                .border(1.dp, IenTheme.colors.borderStrong, RoundedCornerShape(IenTheme.radius.sm)),
        )
        IenText(sample.name, style = IenTheme.typography.label1)
        IenText(sample.hex, style = IenTheme.typography.caption, color = IenTheme.colors.textSecondary)
    }
}

@Composable
private fun ComponentCatalogIntro() {
    DocsSection(title = "Component catalog") {
        IenText(
            text = "The sections below are rendered from the same DesignSystemScreen component samples used by the example app.",
            style = IenTheme.typography.body1,
            color = IenTheme.colors.textSecondary,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                "Badge",
                "BoardRow",
                "Button",
                "Checkbox",
                "FAB",
                "Input",
                "Menu",
                "Progress",
                "Rating",
                "Sheet",
                "Toast",
                "Tooltip",
                "Top",
                "Dialog",
                "Keypad",
                "ListRow",
                "Primitive",
            ).forEach { InfoPill(it) }
        }
    }
}

@Composable
private fun DocsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    IenSurface(
        modifier = Modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
        tonalElevation = IenTheme.elevation.raised,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(IenTheme.colors.brand),
                )
                IenText(title, style = IenTheme.typography.title2)
            }
            IenDivider()
            content()
        }
    }
}

private data class ColorSample(
    val name: String,
    val color: Color,
    val hex: String,
)

private fun colorSamples(colors: IenColorScheme) = listOf(
    ColorSample("background", colors.background, "#FFFFFFFF / #FF101318"),
    ColorSample("surface", colors.surface, "#FFFFFFFF / #FF171B22"),
    ColorSample("surfaceRaised", colors.surfaceRaised, "#FFFFFFFF / #FF202631"),
    ColorSample("surfaceWeak", colors.surfaceWeak, "#FFF9FAFB / #FF11151B"),
    ColorSample("surfaceVariant", colors.surfaceVariant, "#FFF2F4F6 / #FF202632"),
    ColorSample("textPrimary", colors.textPrimary, "#FF191F28 / #FFF2F4F6"),
    ColorSample("textSecondary", colors.textSecondary, "#FF4E5968 / #FFD1D6DB"),
    ColorSample("textTertiary", colors.textTertiary, "#FF8B95A1 / #FF8B95A1"),
    ColorSample("textDisabled", colors.textDisabled, "#FFB0B8C1 / #FF6B7684"),
    ColorSample("border", colors.border, "#FFE5E8EB / #FF333D4B"),
    ColorSample("borderStrong", colors.borderStrong, "#FFD1D6DB / #FF4E5968"),
    ColorSample("overlay", colors.overlay, "#99000000 / #B3000000"),
    ColorSample("brand", colors.brand, "#FF3182F6 / #FF64A8FF"),
    ColorSample("onBrand", colors.onBrand, "#FFFFFFFF / #FFFFFFFF"),
    ColorSample("brandWeak", colors.brandWeak, "#FFE8F3FF / #FF17365D"),
    ColorSample("onBrandWeak", colors.onBrandWeak, "#FF3182F6 / #FF64A8FF"),
    ColorSample("success", colors.success, "#FF03B26C / #FF3FD599"),
    ColorSample("onSuccess", colors.onSuccess, "#FFFFFFFF / #FFFFFFFF"),
    ColorSample("successWeak", colors.successWeak, "#FFF0FAF6 / #FF113B2B"),
    ColorSample("onSuccessWeak", colors.onSuccessWeak, "#FF03B26C / #FF3FD599"),
    ColorSample("warning", colors.warning, "#FFFE9800 / #FFFFBD51"),
    ColorSample("onWarning", colors.onWarning, "#FFFFFFFF / #FFFFFFFF"),
    ColorSample("warningWeak", colors.warningWeak, "#FFFFF3E0 / #FF4A3211"),
    ColorSample("onWarningWeak", colors.onWarningWeak, "#FFFE9800 / #FFFFBD51"),
    ColorSample("danger", colors.danger, "#FFF04452 / #FFFB8890"),
    ColorSample("onDanger", colors.onDanger, "#FFFFFFFF / #FFFFFFFF"),
    ColorSample("dangerWeak", colors.dangerWeak, "#FFFFEEEE / #FF4A1D22"),
    ColorSample("onDangerWeak", colors.onDangerWeak, "#FFF04452 / #FFFB8890"),
    ColorSample("info", colors.info, "#FF18A5A5 / #FF58C7C7"),
    ColorSample("onInfo", colors.onInfo, "#FFFFFFFF / #FFFFFFFF"),
    ColorSample("infoWeak", colors.infoWeak, "#FFEDF8F8 / #FF123A3A"),
    ColorSample("onInfoWeak", colors.onInfoWeak, "#FF18A5A5 / #FF58C7C7"),
)
