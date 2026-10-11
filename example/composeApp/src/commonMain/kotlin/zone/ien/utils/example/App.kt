package zone.ien.utils.example

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
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
import zone.ien.utils.ui.feedback.IenHapticFeedbackProvider
import zone.ien.utils.ui.interactive.IenButton
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

    // Google Auth Provider 초기화 (앱 시작 시 1회)
    GoogleAuthProvider.create(
        credentials = GoogleAuthCredentials(serverId = BuildKonfig.GCP_WEB_CLIENT_ID)
    )

    val backStack = zone.ien.utils.navigation.rememberNavBackStack<RootRoute>(RootRoute.Home)
//    val backStack = rememberNavBackStack(getConfig<RootRoute>(), RootRoute.Home)
    var isMaterialTheme by remember { mutableStateOf(!isIos) }

    var iconStyle by remember { mutableStateOf<IconStyle>(IconStyle.Tabler) }

    CompositionLocalProvider(LocalIconStyle provides iconStyle) {
        IenAdaptiveTheme(
            target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
        ) {
            IenHapticFeedbackProvider {
                RootWrapper {
                    RootNavigationGraph(
                        modifier = it,
                        backStack = backStack,
                        iconStyle = iconStyle,
                        onIconStyleChange = { iconStyle = it },
                    )
                }
            }
        }
    }
}

@Composable
expect fun HapticTestScreen()