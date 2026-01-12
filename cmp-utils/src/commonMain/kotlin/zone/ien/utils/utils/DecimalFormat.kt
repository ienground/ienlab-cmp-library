package zone.ien.utils.utils

expect class DecimalFormat() {
    fun format(number: Double): String
    fun format(number: Int): String
}

expect class DoubleFormat(decimalPlaces: Int) {
    // 💡 수정됨: 소수점 이하 자릿수를 인자로 받음
    fun format(number: Double): String
}