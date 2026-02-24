package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_utils.generated.resources.Res
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun String.checkDecimal() = matches(Regex("^\\d*$"))
fun String.checkDouble() = matches(Regex("^\\d*(\\.\\d*)?\$"))

fun String.toSafeInt(): Int = toIntOrNull() ?: 0

@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.ifEmptyOrNull(defaultValue: () -> String): String {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this.toString()
}