package zone.ien.utils.example

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import zone.ien.utils.icon.IconStyle
import zone.ien.utils.icon.LocalIconStyle
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.adaptive.wrapper.RootWrapper
import zone.ien.utils.example.ui.navigation.RootNavigationGraph
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.firebase.auth.google.GoogleAuthCredentials
import zone.ien.utils.firebase.auth.google.GoogleAuthProvider
import zone.ien.utils.navigation.getConfig
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.IenSwitchColorPolicy
import zone.ien.utils.ui.foundation.IenTokens
import zone.ien.utils.ui.menu.IenMenu
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.foundation.defaultIenTokens
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.utils.Dlog



const val TAG = "CmpLibTAG"
expect val currentTheme: Theme
expect val isIos: Boolean

internal enum class SampleBrandColor(
    val label: String,
    val lightBrand: Color,
    val lightBrandWeak: Color,
    val darkBrand: Color,
    val darkBrandWeak: Color,
) {
    Blue("파랑", Color(0xFF3182F6), Color(0xFFE8F3FF), Color(0xFF64A8FF), Color(0xFF17365D)),
    Purple("보라", Color(0xFF6D28D9), Color(0xFFF2EAFF), Color(0xFFB89AFF), Color(0xFF35265F)),
    Green("초록", Color(0xFF15803D), Color(0xFFEAF7EE), Color(0xFF68D391), Color(0xFF173F2A)),
    Orange("주황", Color(0xFFC2410C), Color(0xFFFFF0E8), Color(0xFFFFA46B), Color(0xFF5C2D1B)),
    Pink("분홍", Color(0xFFBE185D), Color(0xFFFFEAF2), Color(0xFFFF80AB), Color(0xFF5C203C)),
    ;

    internal fun applyTo(tokens: IenTokens) = tokens.copy(
        lightColors = tokens.lightColors.copy(
            brand = lightBrand,
            onBrand = Color.White,
            brandWeak = lightBrandWeak,
            onBrandWeak = lightBrand,
        ),
        darkColors = tokens.darkColors.copy(
            brand = darkBrand,
            onBrand = Color.White,
            brandWeak = darkBrandWeak,
            onBrandWeak = darkBrand,
        ),
        switchColorPolicy = IenSwitchColorPolicy.Brand,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class,
    ExperimentalCupertinoApi::class, ExperimentalMaterial3ExpressiveApi::class
)
@Composable
@Preview
fun App() {
    Dlog.init(isDebug = true)

    // Google Auth Provider 초기화 (앱 시작 시 1회)
    GoogleAuthProvider.create(
        credentials = GoogleAuthCredentials(serverId = BuildKonfig.GCP_WEB_CLIENT_ID)
    )

    val backStack = zone.ien.utils.navigation.rememberNavBackStack<RootRoute>(RootRoute.Home)
//    val backStack = rememberNavBackStack(getConfig<RootRoute>(), RootRoute.Home)
    var isMaterialTheme by remember { mutableStateOf(!isIos) }

    var iconStyle by remember { mutableStateOf<IconStyle>(IconStyle.Tabler) }
    var selectedBrandColorName by rememberSaveable { mutableStateOf(SampleBrandColor.Blue.name) }
    val selectedBrandColor = SampleBrandColor.entries.firstOrNull { it.name == selectedBrandColorName }
        ?: SampleBrandColor.Blue
    var isBrandColorMenuOpen by remember { mutableStateOf(false) }
    val defaultTokens = remember { defaultIenTokens() }
    val tokens = remember(selectedBrandColor) {
        selectedBrandColor.applyTo(defaultTokens)
    }

    CompositionLocalProvider(LocalIconStyle provides iconStyle) {
        IenAdaptiveTheme(
            target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
            tokens = tokens,
        ) {
            RootWrapper(
    //            showKeyboardDirection = false
            ) {
    //            TextFieldScreen(
    //                modifier = it
    //            )

//                HapticTestScreen()
                Box(modifier = Modifier.fillMaxSize()) {
                    RootNavigationGraph(
                        modifier = it.fillMaxSize(),
                        backStack = backStack,
                        iconStyle = iconStyle,
                        onIconStyleChange = { iconStyle = it },
                    )
                    SampleBrandColorSelector(
                        selectedColor = selectedBrandColor,
                        expanded = isBrandColorMenuOpen,
                        onExpandedChange = { isBrandColorMenuOpen = it },
                        onColorSelected = { selectedBrandColorName = it.name },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                            .padding(IenTheme.spacing.md),
                    )
                }
            }
        }
    }
}

@Composable
private fun SampleBrandColorSelector(
    selectedColor: SampleBrandColor,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onColorSelected: (SampleBrandColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    IenMenu.Trigger(
        modifier = modifier,
        open = expanded,
        onOpen = { onExpandedChange(true) },
        onClose = { onExpandedChange(false) },
        placement = IenMenu.Placement.TopEnd,
        offset = DpOffset(IenTheme.spacing.none, IenTheme.spacing.xs),
        dropdown = {
            IenMenu.Dropdown(
                onDismissRequest = { onExpandedChange(false) },
                header = { IenMenu.Header(text = "브랜드 컬러") },
            ) {
                SampleBrandColor.entries.forEach { color ->
                    IenMenu.DropdownItem(
                        text = color.label,
                        selected = selectedColor == color,
                        onClick = {
                            onColorSelected(color)
                            onExpandedChange(false)
                        },
                        left = {
                            Box(
                                modifier = Modifier
                                    .size(IenTheme.icon.sm)
                                    .background(color.lightBrand, CircleShape),
                            )
                        },
                    )
                }
            }
        },
    ) {
        IenSurface(
            color = IenTheme.colors.brand,
            contentColor = IenTheme.colors.onBrand,
            tonalElevation = IenTheme.elevation.floating,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(IenTheme.icon.xs)
                        .background(IenTheme.colors.onBrand, CircleShape),
                )
                Text(
                    text = "브랜드 컬러",
                    style = IenTheme.typography.label1,
                    color = IenTheme.colors.onBrand,
                )
            }
        }
    }
}
