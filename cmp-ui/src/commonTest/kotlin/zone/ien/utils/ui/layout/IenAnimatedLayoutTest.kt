package zone.ien.utils.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals

class IenAnimatedLayoutTest {
    private data class Item(
        val id: String,
        val label: String,
    )

    @Test
    fun `새 항목은 입력 순서로 추가되고 기존 항목은 최신 값으로 갱신된다`() {
        val currentItems = listOf(
            AnimatedLayoutItem("first", Item("first", "이전 값"), visible = true),
        )

        val result = mergeAnimatedLayoutItems(
            currentItems = currentItems,
            incomingItems = listOf(
                Item("first", "최신 값"),
                Item("second", "새 값"),
            ),
            itemKey = Item::id,
        )

        assertEquals(
            listOf("first", "second"),
            result.map { it.key },
        )
        assertEquals(Item("first", "최신 값"), result.first().value)
        assertEquals(true, result.first().visible)
    }

    @Test
    fun `삭제된 항목은 종료 애니메이션을 위해 보이지 않는 상태로 유지된다`() {
        val currentItems = listOf(
            AnimatedLayoutItem("first", Item("first", "첫 번째"), visible = true),
            AnimatedLayoutItem("second", Item("second", "두 번째"), visible = true),
        )

        val result = mergeAnimatedLayoutItems(
            currentItems = currentItems,
            incomingItems = listOf(Item("first", "첫 번째")),
            itemKey = Item::id,
        )

        assertEquals(listOf("first", "second"), result.map { it.key })
        assertEquals(false, result.last().visible)
    }

    @Test
    fun `새 상태가 추가되면 이전 상태는 종료 대기 상태가 된다`() {
        val result = mergeAnimatedContentItems(
            currentItems = listOf(
                AnimatedContentItem(value = "a", visible = true),
            ),
            targetState = "b",
        )

        assertEquals(
            listOf("a", "b"),
            result.map { it.value },
        )
        assertEquals(
            listOf(false, true),
            result.map { it.visible },
        )
    }

    @Test
    fun `종료 중인 기존 상태로 돌아오면 상태 항목을 재사용한다`() {
        val currentItems = listOf(
            AnimatedContentItem(value = "a", visible = false),
            AnimatedContentItem(value = "b", visible = true),
        )

        val result = mergeAnimatedContentItems(
            currentItems = currentItems,
            targetState = "a",
        )

        assertEquals(listOf("a", "b"), result.map { it.value })
        assertEquals(listOf(true, false), result.map { it.visible })
    }

    @Composable
    private fun compileIenAnimatedContentWithContentAlignment() {
        IenAnimatedContent(
            targetState = true,
            contentAlignment = Alignment.Center,
        ) { state ->
            if (state) {
                Box(modifier = Modifier)
            } else {
                Box(modifier = Modifier)
            }
        }
    }
}
