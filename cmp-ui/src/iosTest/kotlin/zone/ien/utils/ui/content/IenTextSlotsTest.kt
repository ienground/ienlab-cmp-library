package zone.ien.utils.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.TestResult
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.interactive.IenLineCheckbox
import zone.ien.utils.ui.interactive.IenRadioButton
import zone.ien.utils.ui.interactive.IenRangeSlider
import zone.ien.utils.ui.interactive.IenSlider
import zone.ien.utils.ui.interactive.IenStepper
import zone.ien.utils.ui.interactive.IenStepperRightButton
import zone.ien.utils.ui.interactive.IenStepperTexts
import zone.ien.utils.ui.interactive.IenStepperTextsType
import zone.ien.utils.ui.list.IenListRowAssetText

class IenTextSlotsTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun customTextSlotsAndStringOverloadsRenderTogether(): TestResult = runComposeUiTest {
        setContent {
            IenTheme {
                Column {
                    IenPost(
                        title = {
                            assertEquals(IenTheme.typography.title3.fontSize, LocalTextStyle.current.fontSize)
                            Text(text = "슬롯 제목")
                        },
                        author = { Text(text = "슬롯 작성자") },
                        description = {
                            assertEquals(IenTheme.colors.textSecondary, LocalContentColor.current)
                            Text(text = "슬롯 설명")
                        },
                    )
                    IenParagraph(
                        body = { Text(text = "슬롯 본문") },
                        footer = { Text(text = "슬롯 푸터") },
                    )
                    IenBadge(text = { Text(text = "슬롯 배지") })
                    IenBubble(text = { Text(text = "슬롯 말풍선") })
                    IenLineCheckbox(labelContent = { Text(text = "슬롯 체크박스") })
                    IenPost(title = "문자열 제목", description = "문자열 설명")
                    IenParagraph(body = "문자열 본문", emphasis = listOf("본문"))
                    IenBadge(text = "문자열 배지")
                }
            }
        }

        listOf(
            "슬롯 제목", "슬롯 작성자", "슬롯 설명", "슬롯 본문", "슬롯 푸터",
            "슬롯 배지", "슬롯 말풍선", "슬롯 체크박스", "문자열 제목",
            "문자열 설명", "문자열 본문", "문자열 배지",
        ).forEach { onNodeWithText(it).assertExists() }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun controlSlotsKeepLabelsAndInteractions(): TestResult = runComposeUiTest {
        var clicked = false
        setContent {
            IenTheme {
                Column {
                    IenSlider(
                        value = 0.5f,
                        onValueChange = {},
                        labelContent = { Text(text = "슬라이더") },
                        valueLabel = { Text(text = "현재 값") },
                    )
                    IenRangeSlider(
                        value = 0.2f..0.8f,
                        onValueChange = {},
                        labelContent = null,
                        valueLabel = { Text(text = "선택 범위") },
                    )
                    IenStepper(value = 1, onValueChange = {}, labelContent = { Text(text = "수량") })
                    IenStepperTexts(
                        type = IenStepperTextsType.A,
                        title = { Text(text = "단계 제목") },
                        description = { Text(text = "단계 설명") },
                    )
                    IenStepperRightButton(text = { Text(text = "단계 액션") }, onClick = { clicked = true })
                    IenListRowAssetText(text = { Text(text = "어셋") })
                    IenRadioButton(selected = true, onClick = {}, labelContent = { Text(text = "선택") })
                    IenCircleCheckbox(checked = true, labelContent = { Text(text = "원형 선택") })
                    IenDotCheckbox(checked = true, labelContent = { Text(text = "점 선택") })
                }
            }
        }
        listOf("슬라이더", "현재 값", "선택 범위", "수량", "단계 제목", "단계 설명", "어셋", "선택")
            .forEach { onNodeWithText(it).assertExists() }
        onNodeWithText("원형 선택").assertIsOn()
        onNodeWithText("점 선택").assertIsOn()
        onNodeWithText("단계 액션").performClick()
        assertEquals(true, clicked)
    }
}
