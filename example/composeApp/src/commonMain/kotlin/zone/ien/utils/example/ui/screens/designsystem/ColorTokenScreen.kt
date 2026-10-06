package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.launch
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.screen.IenTopBar
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.utils.toClipEntry

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ColorTokenScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    var darkTheme by remember { mutableStateOf(false) }
    var appName by remember { mutableStateOf("Lovehero") }
    var copied by remember { mutableStateOf(false) }
    var copyFailed by remember { mutableStateOf(false) }
    val defaultTokens = remember { defaultIenTokens() }
    var lightOverrides by remember {
        mutableStateOf(ColorOverrides.from(defaultTokens.lightColors))
    }
    var darkOverrides by remember {
        mutableStateOf(ColorOverrides.from(defaultTokens.darkColors))
    }
    val tokens = remember(defaultTokens, lightOverrides, darkOverrides) {
        defaultTokens.copy(
            lightColors = lightOverrides.toColorScheme(defaultTokens.lightColors),
            darkColors = darkOverrides.toColorScheme(defaultTokens.darkColors),
        )
    }
    val code = remember(appName, lightOverrides, darkOverrides) {
        generateColorSchemeCode(appName, lightOverrides, darkOverrides)
    }
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    IenTheme(tokens = tokens, darkTheme = darkTheme) {
        val scrollState = rememberScrollState()

        IenScaffold(
            modifier = modifier,
            contentEdge = IenScaffoldContentEdge(
                scrollState = scrollState,
            ),
            topBar = {
                IenTopBar(
                    title = "컬러 스킴 제작기",
                    subtitle = if (darkTheme) "Dark theme" else "Light theme",
                    navigationIcon = {
                        IenTextButton(onClick = navigateBack) {
                            Text("닫기")
                        }
                    },
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IenTheme.colors.background)
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                    .padding(IenTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            ) {
                IenSurface(
                    modifier = Modifier.fillMaxWidth(),
                    color = IenTheme.colors.surface,
                    border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
                ) {
                    Row(
                        modifier = Modifier.padding(IenTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("테마 모드", style = IenTheme.typography.label1)
                            Text(
                                text = "색상 토큰이 라이트/다크에서 어떻게 바뀌는지 확인합니다.",
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.textSecondary,
                            )
                        }
                        IenSwitch(
                            checked = darkTheme,
                            onCheckedChange = { darkTheme = it },
                        )
                    }
                }

                IenSurface(
                    modifier = Modifier.fillMaxWidth(),
                    color = IenTheme.colors.surface,
                    border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
                ) {
                    Column(
                        modifier = Modifier.padding(IenTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    ) {
                        Text("컬러 스킴 제작기", style = IenTheme.typography.title3)
                        IenTextField(
                            value = appName,
                            onValueChange = {
                                appName = it
                                copied = false
                                copyFailed = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = "앱 이름",
                            placeholder = "예: Lovehero",
                        )
                        Text(
                            "${if (darkTheme) "다크" else "라이트"} 모드 색상을 HEX로 조정하면 미리보기와 Kotlin 코드가 함께 바뀝니다.",
                            style = IenTheme.typography.caption,
                            color = IenTheme.colors.textSecondary,
                        )
                        val currentOverrides = if (darkTheme) darkOverrides else lightOverrides
                        listOf(
                            "background" to currentOverrides.background,
                            "surface" to currentOverrides.surface,
                            "surfaceRaised" to currentOverrides.surfaceRaised,
                        ).forEach { (role, value) ->
                            ColorOverrideRow(
                                role = role,
                                value = value,
                                onValueChange = { next ->
                                    val update: (ColorOverrides) -> ColorOverrides = { previous ->
                                        when (role) {
                                            "background" -> previous.copy(background = next)
                                            "surface" -> previous.copy(surface = next)
                                            else -> previous.copy(surfaceRaised = next)
                                        }
                                    }
                                    if (darkTheme) darkOverrides = update(darkOverrides)
                                    else lightOverrides = update(lightOverrides)
                                    copied = false
                                    copyFailed = false
                                },
                            )
                        }
                    }
                }

                ColorTokenGroup(
                    title = "Background / Surface",
                    tokens = listOf(
                        ColorToken("background", IenTheme.colors.background),
                        ColorToken("surface", IenTheme.colors.surface),
                        ColorToken("surfaceRaised", IenTheme.colors.surfaceRaised),
                        ColorToken("surfaceWeak", IenTheme.colors.surfaceWeak),
                        ColorToken("overlay", IenTheme.colors.overlay),
                    ),
                )

                ColorTokenGroup(
                    title = "Text",
                    tokens = listOf(
                        ColorToken("textPrimary", IenTheme.colors.textPrimary),
                        ColorToken("textSecondary", IenTheme.colors.textSecondary),
                        ColorToken("textTertiary", IenTheme.colors.textTertiary),
                        ColorToken("textDisabled", IenTheme.colors.textDisabled),
                    ),
                )

                ColorTokenGroup(
                    title = "Border",
                    tokens = listOf(
                        ColorToken("border", IenTheme.colors.border),
                        ColorToken("borderStrong", IenTheme.colors.borderStrong),
                    ),
                )

                ColorTokenGroup(
                    title = "Brand / Status / On Semantic",
                    tokens = listOf(
                        ColorToken("brand", IenTheme.colors.brand),
                        ColorToken("onBrand", IenTheme.colors.onBrand),
                        ColorToken("brandWeak", IenTheme.colors.brandWeak),
                        ColorToken("onBrandWeak", IenTheme.colors.onBrandWeak),

                        ColorToken("success", IenTheme.colors.success),
                        ColorToken("onSuccess", IenTheme.colors.onSuccess),
                        ColorToken("successWeak", IenTheme.colors.successWeak),
                        ColorToken("onSuccessWeak", IenTheme.colors.onSuccessWeak),

                        ColorToken("warning", IenTheme.colors.warning),
                        ColorToken("onWarning", IenTheme.colors.onWarning),
                        ColorToken("warningWeak", IenTheme.colors.warningWeak),
                        ColorToken("onWarningWeak", IenTheme.colors.onWarningWeak),

                        ColorToken("danger", IenTheme.colors.danger),
                        ColorToken("onDanger", IenTheme.colors.onDanger),
                        ColorToken("dangerWeak", IenTheme.colors.dangerWeak),
                        ColorToken("onDangerWeak", IenTheme.colors.onDangerWeak),

                        ColorToken("info", IenTheme.colors.info),
                        ColorToken("onInfo", IenTheme.colors.onInfo),
                        ColorToken("infoWeak", IenTheme.colors.infoWeak),
                        ColorToken("onInfoWeak", IenTheme.colors.onInfoWeak),
                    ),
                )

                IenSurface(
                    modifier = Modifier.fillMaxWidth(),
                    color = IenTheme.colors.surface,
                    border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
                ) {
                    Column(
                        modifier = Modifier.padding(IenTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Kotlin 테마 코드", style = IenTheme.typography.title3)
                            IenButton(
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            clipboard.setClipEntry(code.toClipEntry())
                                            copied = true
                                            copyFailed = false
                                        } catch (_: Exception) {
                                            copied = false
                                            copyFailed = true
                                        }
                                    }
                                },
                                state = IenButtonState(
                                    enabled = lightOverrides.isValid && darkOverrides.isValid,
                                ),
                            ) {
                                Text(if (copied) "복사 완료" else "코드 복사")
                            }
                        }
                        if (!lightOverrides.isValid || !darkOverrides.isValid) {
                            Text(
                                "HEX 색상을 #RRGGBB 또는 #RRGGBBAA 형식으로 입력해 주세요.",
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.danger,
                            )
                        }
                        if (copied) {
                            Text(
                                "Kotlin 코드를 클립보드에 복사했어요.",
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.success,
                            )
                        }
                        if (copyFailed) {
                            Text(
                                "클립보드에 복사하지 못했어요.",
                                style = IenTheme.typography.caption,
                                color = IenTheme.colors.danger,
                            )
                        }
                        Text(
                            text = highlightKotlin(code),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(IenTheme.colors.surfaceWeak, RoundedCornerShape(12.dp))
                                .padding(IenTheme.spacing.md),
                            style = IenTheme.typography.caption.copy(
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp,
                            ),
                        )
                    }
                }

                Spacer(Modifier.height(IenTheme.spacing.md))
            }
        }
    }
}

@Immutable
private data class ColorToken(
    val name: String,
    val color: Color,
)

@Immutable
private data class ColorOverrides(
    val background: String,
    val surface: String,
    val surfaceRaised: String,
) {
    val isValid: Boolean
        get() = listOf(background, surface, surfaceRaised).all(::isHexColor)

    fun toColorScheme(defaults: IenColorScheme): IenColorScheme = defaults.copy(
        background = background.toComposeColor(defaults.background),
        surface = surface.toComposeColor(defaults.surface),
        surfaceRaised = surfaceRaised.toComposeColor(defaults.surfaceRaised),
    )

    companion object {
        fun from(colors: IenColorScheme) = ColorOverrides(
            background = colors.background.hexString(),
            surface = colors.surface.hexString(),
            surfaceRaised = colors.surfaceRaised.hexString(),
        )
    }
}

@Composable
private fun ColorOverrideRow(
    role: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    val valid = isHexColor(value)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    value.toComposeColor(IenTheme.colors.surfaceWeak),
                    ContinuousRoundedRectangle(IenTheme.radius.default),
                ),
        )
        IenTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            label = role,
            hasError = !valid,
            help = if (valid) null else "#RRGGBB 또는 #RRGGBBAA",
        )
    }
}

private fun isHexColor(value: String): Boolean =
    value.matches(Regex("#[0-9A-Fa-f]{6}(?:[0-9A-Fa-f]{2})?"))

private fun String.toComposeColor(fallback: Color): Color {
    if (!isHexColor(this)) return fallback
    val hex = removePrefix("#")
    val argb = if (hex.length == 6) "FF$hex" else hex.takeLast(2) + hex.dropLast(2)
    return Color(argb.toLong(16))
}

private fun generateColorSchemeCode(
    appName: String,
    light: ColorOverrides,
    dark: ColorOverrides,
): String {
    val name = appName
        .split(Regex("[^A-Za-z0-9]+"))
        .filter(String::isNotEmpty)
        .joinToString("") { part -> part.replaceFirstChar(Char::uppercase) }
        .ifEmpty { "App" }
        .let { if (it.first().isDigit()) "App$it" else it }

    fun declaration(mode: String, colors: ColorOverrides) = buildString {
        appendLine("internal val ${name}${mode}Colors = DefaultColors.${mode.lowercase()}Colors.copy(")
        appendLine("    background = Color(${colors.background.toColorLiteral()}),")
        appendLine("    surface = Color(${colors.surface.toColorLiteral()}),")
        appendLine("    surfaceRaised = Color(${colors.surfaceRaised.toColorLiteral()}),")
        append(")")
    }

    return """import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens

private val DefaultColors = defaultIenTokens()

${declaration("Light", light)}

${declaration("Dark", dark)}

private val ${name}Tokens = DefaultColors.copy(
    lightColors = ${name}LightColors,
    darkColors = ${name}DarkColors,
)

@Composable
fun ${name}Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    IenTheme(tokens = ${name}Tokens, darkTheme = darkTheme, content = content)
}
""".trimIndent()
}

private fun String.toColorLiteral(): String {
    val hex = removePrefix("#")
    val argb = if (hex.length == 6) "FF$hex" else hex.takeLast(2) + hex.dropLast(2)
    return "0x${argb.uppercase()}"
}

private fun highlightKotlin(source: String): AnnotatedString = buildAnnotatedString {
    append(source)
    val styles = listOf(
        Regex("\\b(private|internal|val|fun)\\b") to Color(0xFFC678DD),
        Regex("@[A-Za-z_][A-Za-z0-9_]*") to Color(0xFFE5C07B),
        Regex("\\b[A-Z][A-Za-z0-9_]*\\b") to Color(0xFF61AFEF),
        Regex("0x[0-9A-Fa-f]+") to Color(0xFFD19A66),
    )
    styles.forEach { (pattern, color) ->
        pattern.findAll(source).forEach { match ->
            addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
        }
    }
}

@Composable
private fun ColorTokenGroup(
    title: String,
    tokens: List<ColorToken>,
    modifier: Modifier = Modifier,
) {
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        ) {
            Text(title, style = IenTheme.typography.title3)
            IenDivider()
            tokens.forEach { token ->
                ColorTokenRow(token)
            }
        }
    }
}

@Composable
private fun ColorTokenRow(
    token: ColorToken,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(token.color, ContinuousRoundedRectangle(IenTheme.radius.default)),
        )
        Column(Modifier.weight(1f)) {
            Text(token.name, style = IenTheme.typography.label1)
            Text(
                text = token.color.hexString(),
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

private fun Color.hexString(): String {
    val argb = toArgb()
    val rgb = argb and 0x00FFFFFF
    return "#${rgb.toString(16).padStart(6, '0').uppercase()}"
}
