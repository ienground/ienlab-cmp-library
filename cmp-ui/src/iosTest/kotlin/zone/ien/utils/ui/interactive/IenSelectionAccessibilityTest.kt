package zone.ien.utils.ui.interactive

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme

class IenSelectionAccessibilityTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkboxSemanticsExposeStateAndEnabledStatus(): TestResult = runComposeUiTest {
        var lineChecked: Boolean? = null

        setContent {
            IenTheme {
                Column {
                    IenCircleCheckbox(checked = true, label = "원형")
                    IenDotCheckbox(checked = false, enabled = false, label = "점")
                    IenLineCheckbox(
                        checked = false,
                        onCheckedChange = { lineChecked = it },
                        label = "라인",
                    )
                }
            }
        }

        onNodeWithText("원형").assertIsOn().assertIsEnabled()
        onNodeWithText("점").assertIsOff().assertIsNotEnabled()
        onNodeWithText("라인").assertIsOff().performClick()
        assertEquals(true, lineChecked)
    }
}
