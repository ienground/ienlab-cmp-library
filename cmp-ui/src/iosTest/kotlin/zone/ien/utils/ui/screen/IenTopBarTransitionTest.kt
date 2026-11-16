package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIViewController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.hig.LocalTopBarTransition
import zone.ien.hig.TopBarTransition
import zone.ien.hig.adaptive.AdaptiveTheme
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.MaterialThemeSpec
import zone.ien.utils.ui.foundation.IenTheme

class IenTopBarTransitionTest {
    @OptIn(ExperimentalTestApi::class, ExperimentalAdaptiveApi::class)
    @Test
    fun hiddenControlsDoNotLeaveShadows(): TestResult = runComposeUiTest {
        val controlsVisible = mutableStateOf(true)
        setContent {
            CompositionLocalProvider(LocalUIViewController provides UIViewController()) {
                AdaptiveTheme(target = Theme.Material3, material = MaterialThemeSpec.Default()) {
                    IenTheme {
                        Box(Modifier.fillMaxWidth().height(160.dp).background(Color.White).testTag("bar")) {
                            IenTopBar(
                                title = {},
                                navigationIcon = { if (controlsVisible.value) IenBackButton(onClick = {}) },
                                actions = { if (controlsVisible.value) Text(text = "액션") },
                                windowInsets = WindowInsets(0),
                            )
                        }
                    }
                }
            }
        }
        val visible = onNodeWithTag("bar").captureToImage().toPixelMap()
        assertTrue((0 until visible.height).any { y ->
            (0 until visible.width).any { x -> visible[x, y] != Color.White }
        })
        runOnIdle { controlsVisible.value = false }
        val empty = onNodeWithTag("bar").captureToImage().toPixelMap()
        assertTrue((0 until empty.height).all { y ->
            (0 until empty.width).all { x -> empty[x, y] == Color.White }
        }, "버튼 콘텐츠가 없어졌을 때 빈 배경과 그림자가 남으면 안 됩니다.")
        runOnIdle { controlsVisible.value = true }
        val restored = onNodeWithTag("bar").captureToImage().toPixelMap()
        assertTrue((0 until visible.height).all { y ->
            (0 until visible.width).all { x -> visible[x, y] == restored[x, y] }
        }, "버튼이 다시 나타나면 배경과 그림자도 원래대로 복원되어야 합니다.")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun ienControlsIgnoreNavigationTopBarTransition(): TestResult = runComposeUiTest {
        val enabled = mutableStateOf(false)
        val transition = object : TopBarTransition {
            @Composable
            override fun modifier(slot: TopBarTransition.Slot): Modifier = Modifier.offset(
                x = if (slot == TopBarTransition.Slot.NavigationIcon) 12.dp else 24.dp,
            )
        }
        setContent {
            IenTheme {
                CompositionLocalProvider(LocalTopBarTransition provides transition.takeIf { enabled.value }) {
                    Column {
                        IenTopBarTitleAlignment.entries.forEach { alignment ->
                            IenTopBar(
                                title = { Text(text = "제목", modifier = Modifier.testTag("$alignment-title")) },
                                navigationIcon = { Text(text = "뒤로", modifier = Modifier.testTag("$alignment-back")) },
                                actions = { Text(text = "액션", modifier = Modifier.testTag("$alignment-actions")) },
                                primaryAction = { Text(text = "저장", modifier = Modifier.testTag("$alignment-primary")) },
                                titleAlignment = alignment,
                                windowInsets = WindowInsets(0),
                            )
                        }
                    }
                }
            }
        }
        val initialBounds = IenTopBarTitleAlignment.entries.associateWith { alignment ->
            listOf("title", "back", "actions", "primary").associateWith { slot ->
                onNodeWithTag("$alignment-$slot").getUnclippedBoundsInRoot()
            }
        }
        runOnIdle { enabled.value = true }
        IenTopBarTitleAlignment.entries.forEach { alignment ->
            listOf("title", "back", "actions", "primary").forEach { slot ->
                val before = initialBounds.getValue(alignment).getValue(slot)
                val after = onNodeWithTag("$alignment-$slot").getUnclippedBoundsInRoot()
                assertEquals(before.left, after.left, "$alignment-$slot")
                assertEquals(before.top, after.top, "$alignment-$slot")
                assertEquals(before.right, after.right, "$alignment-$slot")
                assertEquals(before.bottom, after.bottom, "$alignment-$slot")
            }
        }
    }
}
