package zone.ien.utils.utils

fun String.checkDecimal() = matches(Regex("^\\d*$"))
fun String.checkDouble() = matches(Regex("^\\d*(\\.\\d*)?\$"))

fun String.toSafeInt(): Int = toIntOrNull() ?: 0