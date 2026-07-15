package zone.ien.utils.utils

class DecimalFormat {
    fun format(number: Double): String = format(number.toLong())

    fun format(number: Int): String = format(number.toLong())

    private fun format(number: Long): String {
        val sign = if (number < 0) "-" else ""
        val digits = number.toString().removePrefix("-")
        return sign + digits
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    }
}
