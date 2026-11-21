package zone.ien.utils.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NavigationBackStackTest {
    private data object Root : NavKey
    private data object Detail : NavKey
    private data class Item(val id: String) : NavKey

    @Test
    fun navigateToAddsRouteOnlyOnce() {
        val backStack = NavBackStack<NavKey>(Root)

        backStack.navigateTo(Detail)
        backStack.navigateTo(Detail)

        assertEquals(listOf(Root, Detail), backStack.toList())
    }

    @Test
    fun navigateToPopsToExistingRouteAndDistinguishesArguments() {
        val first = Item("first")
        val second = Item("second")
        val backStack = NavBackStack<NavKey>(Root, first)

        backStack.navigateTo(second)
        assertEquals(listOf(Root, first, second), backStack.toList())

        backStack.navigateTo(Item("first"))
        assertEquals(listOf(Root, first), backStack.toList())
    }

    @Test
    fun navigateBackRemovesAndReturnsTopEntry() {
        val backStack = NavBackStack<NavKey>(Root, Detail)

        assertEquals(Detail, backStack.navigateBack())
        assertEquals(listOf(Root), backStack.toList())
    }

    @Test
    fun navigateBackPreservesRoot() {
        val backStack = NavBackStack<NavKey>(Root)

        assertNull(backStack.navigateBack())
        assertEquals(listOf(Root), backStack.toList())
    }

    @Test
    fun repeatedNavigateBackPreservesRoot() {
        val backStack = NavBackStack<NavKey>(Root, Detail)

        backStack.navigateBack()
        repeat(100) {
            assertNull(backStack.navigateBack())
            assertEquals(listOf(Root), backStack.toList())
        }
    }

    @Test
    fun navigateBackOnEmptyStackReturnsNull() {
        val backStack = NavBackStack<NavKey>()

        assertNull(backStack.navigateBack())
        assertEquals(emptyList(), backStack.toList())
    }

    @Test
    fun onBackPressedPopsBeforeDelegatingToParent() {
        val backStack = NavBackStack<NavKey>(Root, Detail)
        var parentBackCalls = 0

        backStack.onBackPressed { parentBackCalls++ }

        assertEquals(listOf(Root), backStack.toList())
        assertEquals(0, parentBackCalls)

        backStack.onBackPressed { parentBackCalls++ }

        assertEquals(listOf(Root), backStack.toList())
        assertEquals(1, parentBackCalls)
    }
}
