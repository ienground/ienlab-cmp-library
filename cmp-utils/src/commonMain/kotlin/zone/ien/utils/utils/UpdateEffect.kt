package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

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