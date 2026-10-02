package zone.ien.utils.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.compose.ui.graphics.Color
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.defaultIenTokens
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)
fun main() {
    val componentId = currentComponentId() ?: "button"
    val darkTheme = isDarkTheme()
    val tokens = defaultIenTokens()
    val colors = previewColors(if (darkTheme) tokens.darkColors else tokens.lightColors)

    ComposeViewport(viewportContainerId = "composeApplication") {
        DocsApp(componentId, darkTheme, colors)
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun currentComponentId(): String? =
    js("new URLSearchParams(window.location.search).get('component')")

@OptIn(ExperimentalWasmJsInterop::class)
private fun isDarkTheme(): Boolean =
    js("new URLSearchParams(window.location.search).get('theme') === 'dark'")

private fun previewColors(defaults: IenColorScheme): IenColorScheme = defaults.copy(
    background = previewColor("background", defaults.background),
    surface = previewColor("surface", defaults.surface),
    surfaceRaised = previewColor("surfaceRaised", defaults.surfaceRaised),
    surfaceWeak = previewColor("surfaceWeak", defaults.surfaceWeak),
    textPrimary = previewColor("textPrimary", defaults.textPrimary),
    textSecondary = previewColor("textSecondary", defaults.textSecondary),
    textTertiary = previewColor("textTertiary", defaults.textTertiary),
    textDisabled = previewColor("textDisabled", defaults.textDisabled),
    border = previewColor("border", defaults.border),
    borderStrong = previewColor("borderStrong", defaults.borderStrong),
    overlay = previewColor("overlay", defaults.overlay),
    surfaceVariant = previewColor("surfaceVariant", defaults.surfaceVariant),
    brand = previewColor("brand", defaults.brand),
    onBrand = previewColor("onBrand", defaults.onBrand),
    brandWeak = previewColor("brandWeak", defaults.brandWeak),
    onBrandWeak = previewColor("onBrandWeak", defaults.onBrandWeak),
    success = previewColor("success", defaults.success),
    onSuccess = previewColor("onSuccess", defaults.onSuccess),
    successWeak = previewColor("successWeak", defaults.successWeak),
    onSuccessWeak = previewColor("onSuccessWeak", defaults.onSuccessWeak),
    warning = previewColor("warning", defaults.warning),
    onWarning = previewColor("onWarning", defaults.onWarning),
    warningWeak = previewColor("warningWeak", defaults.warningWeak),
    onWarningWeak = previewColor("onWarningWeak", defaults.onWarningWeak),
    danger = previewColor("danger", defaults.danger),
    onDanger = previewColor("onDanger", defaults.onDanger),
    dangerWeak = previewColor("dangerWeak", defaults.dangerWeak),
    onDangerWeak = previewColor("onDangerWeak", defaults.onDangerWeak),
    info = previewColor("info", defaults.info),
    onInfo = previewColor("onInfo", defaults.onInfo),
    infoWeak = previewColor("infoWeak", defaults.infoWeak),
    onInfoWeak = previewColor("onInfoWeak", defaults.onInfoWeak),
)

private fun previewColor(role: String, fallback: Color): Color {
    val hex = currentColor(role) ?: return fallback
    if (!hex.matches(Regex("[A-Fa-f0-9]{8}"))) return fallback
    return Color(hex.toLong(16))
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun currentColor(role: String): String? =
    js("new URLSearchParams(window.location.search).get('color.' + role)")
