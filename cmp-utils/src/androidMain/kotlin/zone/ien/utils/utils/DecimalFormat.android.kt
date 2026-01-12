package zone.ien.utils.utils

import java.util.Locale

actual class DecimalFormat {
    actual fun format(number: Double): String {
        val df = java.text.DecimalFormat("###,###")
        return df.format(number)
    }

    actual fun format(number: Int): String {
        val df = java.text.DecimalFormat("###,###")
        return df.format(number)
    }
}

actual class DoubleFormat actual constructor(decimalPlaces: Int) {
    private val formatString = "%.${decimalPlaces}f"

    actual fun format(number: Double): String {
        return String.format(Locale.getDefault(), formatString, number)
    }
}