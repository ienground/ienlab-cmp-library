package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import zone.ien.utils.ui.foundation.IenTheme

class IenDuoLayoutTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun topBarRespectsHorizontalWindowInsets(): TestResult = runComposeUiTest {
        val direction = mutableStateOf(LayoutDirection.Ltr)
        setContent {
            IenTheme {
                CompositionLocalProvider(LocalLayoutDirection provides direction.value) {
                    IenTopBar(
                        title = "Duo",
                        modifier = Modifier.fillMaxWidth().testTag("bar"),
                        windowInsets = WindowInsets(left = 80, top = 0, right = 100, bottom = 0),
                        navigationIcon = { Box(Modifier.size(48.dp).testTag("navigation")) },
                        actions = { Box(Modifier.size(48.dp).testTag("action")) },
                    )
                }
            }
        }
        for (layoutDirection in LayoutDirection.entries) {
            runOnIdle { direction.value = layoutDirection }
            val bar = onNodeWithTag("bar").fetchSemanticsNode().boundsInRoot
            for (tag in listOf("navigation", "action")) {
                val button = onNodeWithTag(tag).fetchSemanticsNode().boundsInRoot
                assertTrue(button.left >= bar.left + 80, "$layoutDirection $tag left")
                assertTrue(button.right <= bar.right - 100, "$layoutDirection $tag right")
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun changingBarGeometryDoesNotRecomposeUnrelatedContent(): TestResult = runComposeUiTest {
        val placement = mutableStateOf(IenBarPlacement(sideWidth = 84.dp))
        var unrelatedCompositions = 0
        var observedWidth = 0.dp
        setContent {
            val content = remember {
                @Composable {
                    UnrelatedContent { unrelatedCompositions++ }
                    BarGeometryReader { observedWidth = it }
                }
            }
            CompositionLocalProvider(LocalIenBarPlacement provides placement.value, content = content)
        }
        var initialCompositions = 0
        runOnIdle {
            initialCompositions = unrelatedCompositions
            placement.value = IenBarPlacement(sideWidth = 76.dp)
        }
        runOnIdle {
            assertEquals(76.dp, observedWidth)
            assertEquals(initialCompositions, unrelatedCompositions)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rotatingBetweenSideAndHorizontalBarsPreservesSlotState(): TestResult = runComposeUiTest {
        val placement = mutableStateOf(IenBarPlacement())
        val slots = mutableMapOf<String, Any>()
        setContent {
            IenTheme {
                val controller = remember { UIViewController() }
                CompositionLocalProvider(
                    LocalUIViewController provides controller,
                    LocalIenBarPlacement provides placement.value,
                ) {
                    IenScaffold(
                        topBar = { RememberedSlot { slots["top"] = it } },
                        bottomBar = { RememberedSlot { slots["bottom"] = it } },
                        floating = { RememberedSlot { slots["floating"] = it } },
                        snackbarHost = { RememberedSlot { slots["snackbar"] = it } },
                    ) {
                        RememberedSlot { slots["content"] = it }
                    }
                }
            }
        }
        var initialSlots = emptyMap<String, Any>()
        runOnIdle {
            initialSlots = slots.toMap()
            assertEquals(setOf("top", "bottom", "floating", "snackbar", "content"), initialSlots.keys)
            placement.value = IenBarPlacement(sideWidth = 84.dp, controlsTop = 120.dp, controlsBottom = 24.dp)
        }
        runOnIdle {
            initialSlots.forEach { (name, token) -> assertSame(token, slots[name], name) }
            placement.value = IenBarPlacement()
        }
        runOnIdle {
            initialSlots.forEach { (name, token) -> assertSame(token, slots[name], name) }
        }
    }
}

@Composable
private fun UnrelatedContent(onCompose: () -> Unit) {
    SideEffect(onCompose)
}

@Composable
private fun BarGeometryReader(onWidth: (Dp) -> Unit) {
    val width = LocalIenBarPlacement.current?.sideWidth ?: 0.dp
    SideEffect { onWidth(width) }
}

@Composable
private fun RememberedSlot(onToken: (Any) -> Unit) {
    val token = remember { Any() }
    SideEffect { onToken(token) }
    Box(Modifier.height(48.dp))
}
