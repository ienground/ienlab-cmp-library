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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.composite.IenScaffold
import zone.ien.utils.ui.components.composite.IenScaffoldContentEdge
import zone.ien.utils.ui.components.composite.IenTopBar
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenSwitch
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun ColorTokenScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    var darkTheme by remember { mutableStateOf(false) }

    IenTheme(darkTheme = darkTheme) {
        val scrollState = rememberScrollState()

        IenScaffold(
            modifier = modifier,
            contentEdge = IenScaffoldContentEdge(
                topProgress = (scrollState.value / 48f).coerceIn(0f, 1f),
            ),
            topBar = {
                IenTopBar(
                    title = "Color Tokens",
                    subtitle = if (darkTheme) "Dark theme" else "Light theme",
                    navigationIcon = { IenTextButton(text = "닫기", onClick = navigateBack) },
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
                            IenText("테마 모드", style = IenTheme.typography.label1)
                            IenText(
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
            IenText(title, style = IenTheme.typography.title3)
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
                .background(token.color, RoundedCornerShape(IenTheme.radius.default)),
        )
        Column(Modifier.weight(1f)) {
            IenText(token.name, style = IenTheme.typography.label1)
            IenText(
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
