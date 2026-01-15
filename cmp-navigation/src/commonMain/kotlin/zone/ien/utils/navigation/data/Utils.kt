package zone.ien.utils.navigation.data

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun <T: NavKey> NavBackStack<T>.navigateBack(): T {
    return removeAt(lastIndex)
}

fun <T: NavKey> NavBackStack<T>.onBackPressed(
    back: () -> Unit
) {
    if (size > 1) {
        navigateBack()
    } else {
        back()
    }
}
fun <T: NavKey> NavBackStack<T>.popUpTo(route: T, inclusive: Boolean = false) {
    val bIndex = indexOfFirst { it == route }
    if (bIndex != -1) {
        repeat(size - bIndex) { navigateBack() }
    }
}