package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIViewController
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertPositionInRootIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import zone.ien.hig.LocalCupertinoNativeBarsPadding
import zone.ien.utils.ui.foundation.IenTheme
import androidx.compose.foundation.text.BasicText
import kotlin.test.Test

/** compose-hig `LocalCupertinoNativeBarsPadding`: the platform draws the navigation bar above IEN screens. */
@OptIn(ExperimentalTestApi::class)
class IenNativeBarsTest {

    private val nativeBars = PaddingValues(top = 100.dp, bottom = 30.dp)

    @Composable
    private fun NativeBars(padding: PaddingValues? = nativeBars, content: @Composable () -> Unit) {
        IenTheme(darkTheme = false) {
            CompositionLocalProvider(
                LocalCupertinoNativeBarsPadding provides padding,
                LocalUIViewController provides remember { UIViewController(nibName = null, bundle = null) },
            ) {
                // Duo bar placement is not part of these tests.
                CompositionLocalProvider(LocalIenSideBarEnabled provides false, content = content)
            }
        }
    }

    @Composable
    private fun Screen(bottomBar: (@Composable () -> Unit)? = null) {
        IenScaffold(
            modifier = Modifier.size(240.dp, 400.dp),
            topBar = {
                IenTopAppBar(title = { BasicText("Title") }, modifier = Modifier.testTag("top-bar"))
                Box(Modifier.fillMaxWidth().height(44.dp).testTag("custom-top-bar"))
            },
            bottomBar = bottomBar,
            contentWindowInsets = WindowInsets(top = 20.dp, bottom = 10.dp),
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                Box(Modifier.fillMaxSize().testTag("body"))
            }
        }
    }

    @Test
    fun scaffoldLeavesTheTopBarToThePlatform() = runComposeUiTest {
        setContent { NativeBars { Screen() } }

        onNodeWithTag("top-bar").assertDoesNotExist()
        onNodeWithTag("custom-top-bar").assertDoesNotExist()
        // contentWindowInsets are replaced by the platform bars' padding.
        onNodeWithTag("body").assertPositionInRootIsEqualTo(0.dp, 100.dp)
        onNodeWithTag("body").assertHeightIsEqualTo(400.dp - 100.dp - 30.dp)
    }

    @Test
    fun aBottomBarStillTakesTheBottomPadding() = runComposeUiTest {
        setContent { NativeBars { Screen(bottomBar = { Box(Modifier.fillMaxWidth().height(50.dp)) }) } }

        onNodeWithTag("body").assertPositionInRootIsEqualTo(0.dp, 100.dp)
        onNodeWithTag("body").assertHeightIsEqualTo(400.dp - 100.dp - 50.dp)
    }

    @Test
    fun withoutPlatformBarsTheScaffoldIsUnchanged() = runComposeUiTest {
        setContent { NativeBars(padding = null) { Screen() } }

        onNodeWithTag("top-bar").assertExists()
        onNodeWithTag("custom-top-bar").assertExists()
    }
}
