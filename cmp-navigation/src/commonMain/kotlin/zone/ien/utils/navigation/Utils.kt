package zone.ien.utils.navigation

import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * 백스택의 마지막 요소를 제거하고 반환합니다.
 *
 * @return 제거된 요소
 * @throws IllegalStateException 백스택이 비어 있는 경우
 */
fun <T : NavKey> NavBackStack<T>.navigateBack(): T {
    check(isNotEmpty()) {
        "비어 있는 백스택에서는 뒤로 이동할 수 없습니다."
    }

    return removeAt(lastIndex)
}

/**
 * 뒤로가기를 처리합니다.
 *
 * 백스택에 이전 화면이 있으면 마지막 요소를 제거하고,
 * 루트 화면이라면 [back]을 호출합니다.
 *
 * @param back 루트 화면에서 호출할 플랫폼 뒤로가기 콜백
 */
fun <T : NavKey> NavBackStack<T>.onBackPressed(
    back: () -> Unit,
) {
    if (size > 1) {
        navigateBack()
    } else {
        back()
    }
}

/**
 * 지정한 Route까지 백스택을 제거합니다.
 *
 * 같은 Route가 여러 개 존재하면 현재 위치에서 가장 가까운 Route를 기준으로 합니다.
 *
 * @param route 이동할 Route
 * @param inclusive true이면 지정한 Route도 함께 제거합니다.
 * @return 지정한 Route를 찾았는지 여부
 */
fun <T : NavKey> NavBackStack<T>.popUpTo(
    route: T,
    inclusive: Boolean = false,
): Boolean {
    val targetIndex = indexOfLast { it == route }

    if (targetIndex == -1) {
        return false
    }

    val destinationIndex = if (inclusive) {
        targetIndex - 1
    } else {
        targetIndex
    }

    Snapshot.withMutableSnapshot {
        while (lastIndex > destinationIndex) {
            removeAt(lastIndex)
        }
    }

    return true
}

/**
 * 현재 화면을 새 Route로 교체합니다.
 */
fun <T : NavKey> NavBackStack<T>.replaceTop(
    route: T,
) {
    Snapshot.withMutableSnapshot {
        if (isEmpty()) {
            add(route)
        } else {
            this[lastIndex] = route
        }
    }
}

/**
 * 기존 백스택을 모두 제거하고 지정한 Route부터 다시 시작합니다.
 *
 * 로그인 완료나 로그아웃처럼 이전 화면으로 돌아가면 안 되는 전환에 사용합니다.
 */
fun <T : NavKey> NavBackStack<T>.resetTo(
    route: T,
) {
    Snapshot.withMutableSnapshot {
        clear()
        add(route)
    }
}

/**
 * 현재 최상단 Route와 다를 때만 새 Route를 추가합니다.
 */
fun <T : NavKey> NavBackStack<T>.navigateSingleTop(
    route: T,
) {
    if (lastOrNull() != route) {
        add(route)
    }
}

/**
 * 첫 번째 Route만 남기고 모두 제거합니다.
 */
fun <T : NavKey> NavBackStack<T>.popUpToRoot() {
    if (size <= 1) {
        return
    }

    Snapshot.withMutableSnapshot {
        while (size > 1) {
            removeAt(lastIndex)
        }
    }
}


/**
 * 네비게이션 설정을 생성하는 함수 (사용된 타입 T를 포함한 polymorphic 설정)
 * @return SavedStateConfiguration
 */
@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : NavKey> getConfig() = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<T>()
        }
        // T가 NavKey가 아닌 경우(RootRoute 등) T scope도 추가 등록
        if (T::class != NavKey::class) {
            polymorphic(T::class) {
                subclassesOfSealed<T>()
            }
        }
    }
}