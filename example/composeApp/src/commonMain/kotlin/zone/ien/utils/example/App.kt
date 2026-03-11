package zone.ien.utils.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
import com.kyant.backdrop.backdrops.layerBackdrop
import kotlinx.coroutines.launch
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoText
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.MenuAction
import zone.ien.hig.MenuSection
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.AdaptiveTextButton
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.icons.CupertinoIcons
import zone.ien.hig.icons.outlined.CheckmarkCircle
import zone.ien.hig.icons.outlined.PersonCropCircle
import zone.ien.hig.icons.outlined.Pin
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.component.AdaptiveMediumFloatingActionButton
import zone.ien.utils.adaptive.menu.adaptiveSaveButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle
import zone.ien.utils.adaptive.section.AdaptiveSection
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionSecureTextField
import zone.ien.utils.adaptive.section.AdaptiveSectionSlider
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.adaptive.section.AdaptiveSectionTextField
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenu
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.adaptive.view.DropdownMenuSection.Action
import zone.ien.utils.adaptive.view.textfield.AdaptiveTextFieldClearButton
import zone.ien.utils.adaptive.wrapper.RootWrapper
import zone.ien.utils.example.ui.navigation.RootNavigationGraph
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.example.ui.navigation.rootConfig
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.TopBarSize
import zone.ien.utils.ui.utils.IconData
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.utils.Dlog

const val TAG = "CmpLibTAG"
expect val currentTheme: Theme
expect val isIos: Boolean

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class,
    ExperimentalCupertinoApi::class, ExperimentalMaterial3ExpressiveApi::class
)
@Composable
@Preview
fun App() {
    Dlog.init(isDebug = true)

    val backStack = rememberNavBackStack(rootConfig, RootRoute.Home)
    var isMaterialTheme by remember { mutableStateOf(false) }

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
    ) {
        RootWrapper(
            showKeyboardDirection = false
        ) {
            RootNavigationGraph(
                modifier = it,
                backStack = backStack
            )
        }
    }

}