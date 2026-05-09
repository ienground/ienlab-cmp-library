package zone.ien.utils.navigation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember

/**
 * 네비게이션 결과를 저장하고 관리하는 저장소 클래스
 * Compose의 상태 관리 시스템을 사용하여 결과를 저장
 */
@Stable
class ResultStore {
    @PublishedApi
    internal val results = mutableStateMapOf<String, Any?>()

    /**
     * 저장된 결과를 가져오는 함수
     * @param resultKey 결과 키 (기본값은 T의 클래스 이름)
     * @return 결과 값 또는 null
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getResult(resultKey: String = T::class.toString()): T? {
        return results[resultKey] as? T
    }

    /**
     * 결과를 저장하는 함수
     * @param resultKey 결과 키 (기본값은 T의 클래스 이름)
     * @param result 저장할 결과 값
     */
    inline fun <reified T> setResult(resultKey: String = T::class.toString(), result: T) {
        results[resultKey] = result  // 직접 저장
    }

    /**
     * 저장된 결과를 제거하는 함수
     * @param resultKey 결과 키 (기본값은 T의 클래스 이름)
     */
    inline fun <reified T> removeResult(resultKey: String = T::class.toString()) {
        results.remove(resultKey)
    }

    /**
     * 저장된 결과를 가져온 후 제거하는 함수 (소비)
     * @param resultKey 결과 키 (기본값은 T의 클래스 이름)
     * @return 결과 값 또는 null
     */
    inline fun <reified T> consumeResult(resultKey: String = T::class.toString()): T? {
        val value = getResult<T>(resultKey)
        removeResult<T>(resultKey)
        return value
    }
}

/**
 * Composable 함수를 사용하여 ResultStore를 기억하는 함수
 * @return 기억된 ResultStore 인스턴스
 */
@Composable
fun rememberResultStore(): ResultStore {
    return remember { ResultStore() }  // rememberSaveable 제거
}