package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

/**
 * 키가 변경될 때 코드 블록을 실행하는 Composable 함수입니다.
 *
 * 이 함수는 초기 구성 이후에만 실행되는 사이드 이펙트를 제공합니다.
 * React의 useEffect와 유사한 방식입니다. 처음 실행을 제외하고 키 변경 시 트리거하는
 * "useEffect"의 단순화된 버전입니다.
 *
 * @param key1 변경을 감시할 키
 * @param block 키가 변경될 때 실행할 suspend 함수
 */
@Composable
fun UpdateEffect(key1: Any?, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(key1) {
        if (isTriggered) {
            block()
        } else {
            isTriggered = true
        }
    }
}

/**
 * 키 중 하나라도 변경될 때 코드 블록을 실행하는 Composable 함수입니다.
 *
 * 이 함수는 초기 구성 이후에만 실행되는 사이드 이펙트를 제공합니다.
 * React의 useEffect와 유사한 방식입니다. 처음 실행을 제외하고 키 변경 시 트리거하는
 * "useEffect"의 단순화된 버전입니다.
 *
 * @param key1 변경을 감시할 첫 번째 키
 * @param key2 변경을 감시할 두 번째 키
 * @param block 키가 변경될 때 실행할 suspend 함수
 */
@Composable
fun UpdateEffect(key1: Any?, key2: Any?, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(key1, key2) {
        if (isTriggered) {
            block()
        } else {
            isTriggered = true
        }
    }
}

/**
 * 키 중 하나라도 변경될 때 코드 블록을 실행하는 Composable 함수입니다.
 *
 * 이 함수는 초기 구성 이후에만 실행되는 사이드 이펙트를 제공합니다.
 * React의 useEffect와 유사한 방식입니다. 처음 실행을 제외하고 키 변경 시 트리거하는
 * "useEffect"의 단순화된 버전입니다.
 *
 * @param key1 변경을 감시할 첫 번째 키
 * @param key2 변경을 감시할 두 번째 키
 * @param key3 변경을 감시할 세 번째 키
 * @param block 키가 변경될 때 실행할 suspend 함수
 */
@Composable
fun UpdateEffect(key1: Any?, key2: Any?, key3: Any?, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(key1, key2, key3) {
        if (isTriggered) {
            block()
        } else {
            isTriggered = true
        }
    }
}

/**
 * 키 중 하나라도 변경될 때 코드 블록을 실행하는 Composable 함수입니다.
 *
 * 이 함수는 초기 구성 이후에만 실행되는 사이드 이펙트를 제공합니다.
 * React의 useEffect와 유사한 방식입니다. 처음 실행을 제외하고 키 변경 시 트리거하는
 * "useEffect"의 단순화된 버전입니다.
 *
 * @param key 변경을 감시할 키들
 * @param block 키가 변경될 때 실행할 suspend 함수
 */
@Composable
fun UpdateEffect(vararg key: Any?, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(key) {
        if (isTriggered) {
            block()
        } else {
            isTriggered = true
        }
    }
}