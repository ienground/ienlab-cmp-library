package zone.ien.utils.example

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.example.ui.theme.SampleIenTheme
import zone.ien.utils.adaptive.wrapper.RootWrapper
import zone.ien.utils.example.ui.navigation.RootNavigationGraph
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.navigation.getConfig
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

    val backStack = zone.ien.utils.navigation.rememberNavBackStack<RootRoute>(RootRoute.Home)
//    val backStack = rememberNavBackStack(getConfig<RootRoute>(), RootRoute.Home)
    var isMaterialTheme by remember { mutableStateOf(!isIos) }

    SampleIenTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
    ) {
        RootWrapper(
//            showKeyboardDirection = false,
            enableImePadding = true
        ) {
//            TextFieldScreen(
//                modifier = it
//            )
            RootNavigationGraph(
                modifier = it,
                backStack = backStack
            )
        }
    }

}