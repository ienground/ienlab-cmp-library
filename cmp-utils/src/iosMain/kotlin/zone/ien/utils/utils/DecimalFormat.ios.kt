package zone.ien.utils.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual class DecimalFormat {
    actual fun format(number: Double): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 2u
        formatter.numberStyle = 1u //Decimal
        return formatter.stringFromNumber(NSNumber(number))!!
    }

    actual fun format(number: Int): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 2u
        formatter.numberStyle = 1u //Decimal
        return formatter.stringFromNumber(NSNumber(number))!!
    }
}

actual class DoubleFormat actual constructor(decimalPlaces: Int) {
    private val formatter = NSNumberFormatter()
    init {
        formatter.numberStyle = NSNumberFormatterDecimalStyle

        formatter.minimumFractionDigits = decimalPlaces.toULong()
        formatter.maximumFractionDigits = decimalPlaces.toULong()
        formatter.usesGroupingSeparator = false // 천 단위 구분 기호는 사용하지 않는 것이 %.2f 포맷과 유사
    }
    actual fun format(number: Double): String {
        return formatter.stringFromNumber(NSNumber(double = number))!!
    }
}