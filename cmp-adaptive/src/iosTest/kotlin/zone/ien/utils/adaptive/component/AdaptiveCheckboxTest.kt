package zone.ien.utils.adaptive.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme

class AdaptiveCheckboxTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Cupertino 체크박스는 라벨과 상태 접근성 정보를 제공한다`(): TestResult = runComposeUiTest {
        val changedValues = mutableListOf<Boolean>()

        setContent {
            CompositionLocalProvider(LocalUIViewController provides UIViewController()) {
                IenAdaptiveTheme(target = Theme.Cupertino) {
                    Column {
                        AdaptiveCheckbox(
                            checked = false,
                            onCheckedChange = { changedValues += it },
                            label = "원형",
                        )
                        AdaptiveDotCheckbox(
                            checked = false,
                            onCheckedChange = { changedValues += it },
                            enabled = false,
                            label = "점",
                        )
                        AdaptiveLineCheckbox(
                            checked = true,
                            onCheckedChange = { changedValues += it },
                            label = "라인",
                        )
                    }
                }
            }
        }

        onNodeWithText("원형").assertIsOff().assertIsEnabled().performTouchInput {
            click(centerRight)
        }
        onNodeWithText("점").assertIsOff().assertIsNotEnabled()
        onNodeWithText("라인").assertIsOn().assertIsEnabled().performTouchInput {
            click(centerRight)
        }
        assertEquals(listOf(true, false), changedValues)
    }
}
