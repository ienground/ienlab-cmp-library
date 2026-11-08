package zone.ien.utils.ui.foundation

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.interactive.IenAlphabetKeypad

@OptIn(ExperimentalTestApi::class)
class IenThemeCompositionTest {
    @Test
    fun themeAccessorsFollowDarkModeAndTokenChanges(): TestResult = runComposeUiTest {
        mainClock.autoAdvance = false
        val darkTheme = mutableStateOf(false)
        val tokens = mutableStateOf(defaultIenTokens())
        val observed = arrayOfNulls<IenTokens>(1)
        setContent {
            IenTheme(darkTheme = darkTheme.value, tokens = tokens.value) {
                val current = readThemeAccessors()
                SideEffect { observed[0] = current }
                IenAlphabetKeypad(
                    alphabets = listOf("A"),
                    modifier = Modifier.width(300.dp),
                    onKeyClick = {},
                    onBackspaceClick = {},
                )
            }
        }
        waitForIdle()
        assertEquals(tokens.value.copy(darkColors = tokens.value.lightColors), observed[0])

        runOnIdle { darkTheme.value = true }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(tokens.value.copy(lightColors = tokens.value.darkColors), observed[0])
        val darkKey = onNodeWithText("A").captureToImage().toPixelMap()

        runOnIdle {
            tokens.value = tokens.value.copy(darkColors = tokens.value.darkColors.copy(textPrimary = Color.Magenta))
        }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        val recoloredKey = onNodeWithText("A").captureToImage().toPixelMap()
        assertEquals(darkKey.width, recoloredKey.width)
        assertEquals(darkKey.height, recoloredKey.height)
        assertTrue(
            (0 until darkKey.height).any { y ->
                (0 until darkKey.width).any { x -> darkKey[x, y] != recoloredKey[x, y] }
            },
            "열린 키패드는 변경된 테마의 글자색으로 다시 그려야 합니다",
        )

        runOnIdle {
            val original = tokens.value
            tokens.value = original.copy(
                typography = original.typography.copy(body1 = original.typography.body1.copy(fontSize = 31.sp)),
                spacing = original.spacing.copy(md = 29.dp),
            )
        }
        mainClock.advanceTimeByFrame()
        waitForIdle()
        assertEquals(tokens.value.copy(lightColors = tokens.value.darkColors), observed[0])
    }
}

@Composable
private fun readThemeAccessors(): IenTokens = IenTokens(
    lightColors = IenTheme.colors,
    darkColors = IenTheme.colors,
    typography = IenTheme.typography,
    spacing = IenTheme.spacing,
    radius = IenTheme.radius,
    stroke = IenTheme.stroke,
    elevation = IenTheme.elevation,
    icon = IenTheme.icon,
    motion = IenTheme.motion,
    state = IenTheme.state,
)
