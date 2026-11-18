package zone.ien.utils.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class NativeNavigationParentTest {
    private data class Route(val name: String) : NavKey

    private fun entry(name: String): Pair<NavKey, NavEntry<NavKey>> =
        Route(name).let { it to NavEntry<NavKey>(it, contentKey = name) {} }

    @Test
    fun `상위 그래프의 화면은 하위 상세 위에 쌓이고 가장 위부터 제거된다`() {
        val main = mutableListOf("상세")
        val root = mutableListOf("로그인", "약관")
        fun hierarchy() = nativeParentEntries(
            localEntries = main.map(::entry),
            localOnBack = { main.removeLastOrNull() },
            parent = nativeParentEntries(root.map(::entry), { root.removeLastOrNull() }, null),
        )

        assertEquals(listOf("상세", "로그인", "약관"), hierarchy().entries.map { it.contentKey })
        hierarchy().onBack()
        assertEquals(listOf("로그인"), root)
        assertEquals(listOf("상세"), main)
        hierarchy().onBack()
        hierarchy().onBack()
        assertTrue(root.isEmpty())
        assertTrue(main.isEmpty())
    }

    @Test
    fun `빈 상위 스택은 하위 화면의 뒤로가기를 가로채지 않는다`() {
        var calls = 0
        val parent = nativeParentEntries(emptyList(), { error("빈 상위 스택") }, null)
        nativeParentEntries(listOf(entry("편집")), { calls++ }, parent).onBack()
        assertEquals(1, calls)
    }

    @Test
    fun `그래프 메타데이터는 일반 화면과 구분된다`() {
        assertTrue(BaseNavDisplay.navigationGraph().isNotEmpty())
        assertTrue(isNavigationGraph(NavEntry(Route("메인"), metadata = BaseNavDisplay.navigationGraph()) {}))
        assertEquals(false, isNavigationGraph(entry("일반").second))
    }

    @Test
    fun `서로 다른 그래프가 같은 라우트 키를 사용하면 조용히 덮어쓰지 않는다`() {
        val parent = nativeParentEntries(listOf(entry("중복")), {}, null)
        assertFailsWith<IllegalArgumentException> {
            nativeParentEntries(listOf(entry("중복")), {}, parent)
        }
    }


    @Test
    fun `한 번에 여러 화면을 pop해도 최신 부모 스택부터 제거한다`() {
        val main = mutableListOf(entry("상세"))
        val root = mutableListOf(entry("로그인"), entry("약관"))
        val parent = nativeParentEntries(root, { root.removeLastOrNull() }, null)
        val combined = nativeParentEntries(main, { main.removeLastOrNull() }, parent)
        repeat(3) { combined.onBack() }
        assertTrue(root.isEmpty())
        assertTrue(main.isEmpty())
    }

    @Test
    fun `탭과 공유 스택 사이의 중복 라우트 키는 거부한다`() {
        assertFailsWith<IllegalArgumentException> {
            nativeParentEntries(listOf(entry("중복"), entry("중복")), {}, null)
        }
    }
}
