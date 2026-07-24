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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
import zone.ien.utils.example.ui.screens.designsystem.SnackbarSection
import zone.ien.utils.example.ui.screens.designsystem.TooltipSection
import zone.ien.utils.example.ui.screens.designsystem.TopSection
import zone.ien.utils.ui.feedback.IenSnackbarHost
import zone.ien.utils.ui.feedback.showIenSnackbar
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
import kotlinx.coroutines.launch

@Composable
fun DocsApp() {
    val docsTokens = docsIenTokens()

    IenTheme(
        tokens = docsTokens,
        darkTheme = false,
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        IenProvideTextStyle(
            style = IenTheme.typography.body2,
            color = IenTheme.colors.textPrimary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IenTheme.colors.background),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
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
                        SnackbarSection(
                            onShowBasic = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar("기본 스낵바 메시지예요")
                                }
                            },
                            onShowSuccess = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "성공 상태 스낵바예요",
                                        tone = IenSemanticTone.Success,
                                    )
                                }
                            },
                            onShowAction = {
                                coroutineScope.launch {
                                    val result = snackbarHostState.showIenSnackbar(
                                        message = "버튼이 포함된 스낵바예요",
                                        actionLabel = "확인",
                                        duration = SnackbarDuration.Long,
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        snackbarHostState.showIenSnackbar("확인을 눌렀어요")
                                    }
                                }
                            },
                            onShowCompact = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "최대 240",
                                        minWidth = null,
                                        maxWidth = 240.dp,
                                        fillMaxWidth = false,
                                    )
                                }
                            },
                            onShowQueued = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar("첫 번째 스낵바예요")
                                }
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar("두 번째는 조금 더 긴 메시지예요")
                                }
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "세 번째 성공 상태 스낵바예요",
                                        tone = IenSemanticTone.Success,
                                    )
                                }
                            },
                            onShowShortDuration = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "Short duration",
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            },
                            onShowLongDuration = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "Long duration",
                                        duration = SnackbarDuration.Long,
                                    )
                                }
                            },
                            onShowIndefiniteDuration = {
                                coroutineScope.launch {
                                    snackbarHostState.showIenSnackbar(
                                        message = "직접 닫을 때까지 유지돼요",
                                        actionLabel = "닫기",
                                        duration = SnackbarDuration.Indefinite,
                                    )
                                }
                            },
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
                IenSnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                )
            }
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
        TypographyTokenPreview()
        SpacingTokenPreview()
        RadiusTokenPreview()
        StrokeTokenPreview()
        ElevationTokenPreview()
        IconTokenPreview()
    }
}

@Composable
private fun TypographyTokenPreview() {
    TokenPreviewGroup(title = "Typography") {
        typographySamples().forEach { sample ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(IenTheme.radius.sm))
                    .background(IenTheme.colors.surfaceWeak)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                IenText(
                    text = sample.preview,
                    style = sample.style,
                    color = IenTheme.colors.textPrimary,
                )
                TokenNameValue(name = sample.name, value = sample.value)
            }
        }
    }
}

@Composable
private fun SpacingTokenPreview() {
    TokenPreviewGroup(title = "Spacing") {
        spacingSamples().forEach { sample ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(IenTheme.radius.sm))
                    .background(IenTheme.colors.surfaceWeak)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.width(82.dp)) {
                    IenText(sample.name, style = IenTheme.typography.label1)
                    IenText(sample.value, style = IenTheme.typography.caption, color = IenTheme.colors.textSecondary)
                }
                Box(
                    modifier = Modifier
                        .width(240.dp)
                        .height(34.dp)
                        .clip(RoundedCornerShape(IenTheme.radius.xs))
                        .background(IenTheme.colors.surfaceVariant),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .width(if (sample.size == 0.dp) 1.dp else sample.size)
                            .fillMaxHeight()
                            .background(IenTheme.colors.brand),
                    )
                }
            }
        }
    }
}

@Composable
private fun RadiusTokenPreview() {
    TokenPreviewGroup(title = "Radius") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            radiusSamples().forEach { sample ->
                Column(
                    modifier = Modifier
                        .width(136.dp)
                        .clip(RoundedCornerShape(IenTheme.radius.sm))
                        .background(IenTheme.colors.surfaceWeak)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 88.dp, height = 58.dp)
                            .clip(RoundedCornerShape(sample.size))
                            .background(IenTheme.colors.brandWeak)
                            .border(
                                width = IenTheme.stroke.thin,
                                color = IenTheme.colors.brand,
                                shape = RoundedCornerShape(sample.size),
                            ),
                    )
                    TokenNameValue(name = sample.name, value = sample.value)
                }
            }
        }
    }
}

@Composable
private fun StrokeTokenPreview() {
    TokenPreviewGroup(title = "Stroke") {
        strokeSamples().forEach { sample ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(IenTheme.radius.sm))
                    .background(IenTheme.colors.surfaceWeak)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.width(112.dp)) {
                    IenText(sample.name, style = IenTheme.typography.label1)
                    IenText(sample.value, style = IenTheme.typography.caption, color = IenTheme.colors.textSecondary)
                }
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(34.dp)
                        .border(sample.size, IenTheme.colors.brand, RoundedCornerShape(IenTheme.radius.xs)),
                )
            }
        }
    }
}

@Composable
private fun ElevationTokenPreview() {
    TokenPreviewGroup(title = "Elevation") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            elevationSamples().forEach { sample ->
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(IenTheme.radius.sm))
                        .background(IenTheme.colors.surfaceWeak)
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    val shape = RoundedCornerShape(IenTheme.radius.md)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(sample.size, shape, clip = false)
                            .clip(shape)
                            .background(IenTheme.colors.surface),
                    )
                    TokenNameValue(name = sample.name, value = sample.value)
                }
            }
        }
    }
}

@Composable
private fun IconTokenPreview() {
    TokenPreviewGroup(title = "Icon") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            iconSamples().forEach { sample ->
                Column(
                    modifier = Modifier
                        .width(122.dp)
                        .clip(RoundedCornerShape(IenTheme.radius.sm))
                        .background(IenTheme.colors.surfaceWeak)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(IenTheme.radius.sm))
                            .background(IenTheme.colors.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(sample.size)
                                .clip(CircleShape)
                                .background(IenTheme.colors.brand),
                        )
                    }
                    TokenNameValue(name = sample.name, value = sample.value)
                }
            }
        }
    }
}

@Composable
private fun TokenPreviewGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        IenText(title, style = IenTheme.typography.title3)
        content()
    }
}

@Composable
private fun TokenNameValue(
    name: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        IenText(
            text = name,
            style = IenTheme.typography.label1,
            color = IenTheme.colors.textPrimary,
        )
        IenText(
            text = value,
            style = IenTheme.typography.caption,
            color = IenTheme.colors.textSecondary,
        )
    }
}

private data class TypographySample(
    val name: String,
    val value: String,
    val preview: String,
    val style: TextStyle,
)

private data class SizeSample(
    val name: String,
    val value: String,
    val size: Dp,
)

@Composable
private fun typographySamples() = listOf(
    TypographySample("display", "30 / 38 Bold", "디스플레이 제목 Display", IenTheme.typography.display),
    TypographySample("title1", "24 / 32 Bold", "큰 제목 Title 1", IenTheme.typography.title1),
    TypographySample("title2", "20 / 28 SemiBold", "중간 제목 Title 2", IenTheme.typography.title2),
    TypographySample("title3", "18 / 26 SemiBold", "작은 제목 Title 3", IenTheme.typography.title3),
    TypographySample("body1", "16 / 24 Regular", "본문 텍스트 Body 1 - 읽기 좋은 기본 문장입니다.", IenTheme.typography.body1),
    TypographySample("body2", "15 / 22 Regular", "보조 본문 Body 2 - 설명과 안내문에 사용합니다.", IenTheme.typography.body2),
    TypographySample("label1", "14 / 20 SemiBold", "레이블 Label 1", IenTheme.typography.label1),
    TypographySample("label2", "13 / 18 SemiBold", "작은 레이블 Label 2", IenTheme.typography.label2),
    TypographySample("caption", "12 / 16 Regular", "캡션 Caption", IenTheme.typography.caption),
)

@Composable
private fun spacingSamples() = listOf(
    SizeSample("none", "0dp", IenTheme.spacing.none),
    SizeSample("xxxs", "2dp", IenTheme.spacing.xxxs),
    SizeSample("xxs", "4dp", IenTheme.spacing.xxs),
    SizeSample("xs", "8dp", IenTheme.spacing.xs),
    SizeSample("sm", "12dp", IenTheme.spacing.sm),
    SizeSample("md", "16dp", IenTheme.spacing.md),
    SizeSample("lg", "20dp", IenTheme.spacing.lg),
    SizeSample("xl", "24dp", IenTheme.spacing.xl),
    SizeSample("xxl", "32dp", IenTheme.spacing.xxl),
    SizeSample("xxxl", "40dp", IenTheme.spacing.xxxl),
)

@Composable
private fun radiusSamples() = listOf(
    SizeSample("none", "0dp", IenTheme.radius.none),
    SizeSample("xs", "4dp", IenTheme.radius.xs),
    SizeSample("sm", "8dp", IenTheme.radius.sm),
    SizeSample("default", "12dp", IenTheme.radius.default),
    SizeSample("md", "12dp", IenTheme.radius.md),
    SizeSample("lg", "16dp", IenTheme.radius.lg),
    SizeSample("xl", "24dp", IenTheme.radius.xl),
    SizeSample("full", "999dp", IenTheme.radius.full),
)

@Composable
private fun strokeSamples() = listOf(
    SizeSample("hairline", "0.5dp", IenTheme.stroke.hairline),
    SizeSample("thin", "1dp", IenTheme.stroke.thin),
    SizeSample("medium", "1.5dp", IenTheme.stroke.medium),
    SizeSample("thick", "2dp", IenTheme.stroke.thick),
)

@Composable
private fun elevationSamples() = listOf(
    SizeSample("none", "0dp", IenTheme.elevation.none),
    SizeSample("raised", "4dp", IenTheme.elevation.raised),
    SizeSample("floating", "12dp", IenTheme.elevation.floating),
    SizeSample("overlay", "24dp", IenTheme.elevation.overlay),
)

@Composable
private fun iconSamples() = listOf(
    SizeSample("xs", "12dp", IenTheme.icon.xs),
    SizeSample("sm", "16dp", IenTheme.icon.sm),
    SizeSample("md", "20dp", IenTheme.icon.md),
    SizeSample("lg", "24dp", IenTheme.icon.lg),
    SizeSample("xl", "32dp", IenTheme.icon.xl),
)

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
                "Snackbar",
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
