package zone.ien.utils.utils

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun String.checkDecimal() = matches(Regex("^\\d*$"))
fun String.checkDouble() = matches(Regex("^\\d*(\\.\\d*)?\$"))

fun String.toSafeInt(): Int = toIntOrNull() ?: 0

fun String?.ifEmptyOrNull(defaultValue: () -> String) = if (isNullOrEmpty()) defaultValue() else this
