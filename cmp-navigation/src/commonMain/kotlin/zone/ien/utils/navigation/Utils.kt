package zone.ien.utils.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * 백스택에서 마지막 요소를 제거하고 반환하는 함수
 * @return 제거된 요소
 */
fun <T: NavKey> NavBackStack<T>.navigateBack(): T {
    return removeAt(lastIndex)
}

/**
 * 뒤로가기 처리 함수
 * 백스택에 요소가 있을 경우 이전 화면으로 이동하고, 없을 경우 back 콜백 호출
 * @param back 뒤로가기 처리 시 호출할 콜백 함수
 */
fun <T: NavKey> NavBackStack<T>.onBackPressed(
    back: () -> Unit
) {
    if (size > 1) {
        navigateBack()
    } else {
        back()
    }
}

/**
 * 지정된 라우트까지 백스택을 pop-up 처리하는 함수
 * @param route 백스택에서 제거할 라우트
 * @param inclusive 포함 여부 (기본값은 false)
 */
fun <T: NavKey> NavBackStack<T>.popUpTo(route: T, inclusive: Boolean = false) {
    val bIndex = indexOfFirst { it == route }
    if (bIndex != -1) {
        val popCount = if (inclusive) size - bIndex else size - bIndex - 1
        repeat(popCount) { navigateBack() }
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