package zone.ien.utils.navigation.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
class ResultStore {
    @PublishedApi
    internal val results = mutableStateMapOf<String, Any?>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getResult(resultKey: String = T::class.toString()): T? {
        return results[resultKey] as? T
    }

    inline fun <reified T> setResult(resultKey: String = T::class.toString(), result: T) {
        results[resultKey] = result  // 직접 저장
    }

    inline fun <reified T> removeResult(resultKey: String = T::class.toString()) {
        results.remove(resultKey)
    }

    inline fun <reified T> consumeResult(resultKey: String = T::class.toString()): T? {
        val value = getResult<T>(resultKey)
        removeResult<T>(resultKey)
        return value
    }
}

@Composable
fun rememberResultStore(): ResultStore {
    return remember { ResultStore() }  // rememberSaveable 제거
}