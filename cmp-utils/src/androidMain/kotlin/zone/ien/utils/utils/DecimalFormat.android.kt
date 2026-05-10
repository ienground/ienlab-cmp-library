package zone.ien.utils.utils

import java.util.Locale

/**
 * Android용 소수점 형식화 클래스.
 * 
 * 이 클래스는 Android 플랫폼에서 소수점 숫자를 형식화하는 구현을 제공합니다.
 * 
 * @see DecimalFormat
 * @see DoubleFormat
 */
actual class DecimalFormat {
    /**
     * 소수점 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 소수점 숫자
     * @return 형식화된 문자열 표현
     */
    actual fun format(number: Double): String {
        val df = java.text.DecimalFormat("###,###")
        return df.format(number)
    }

    /**
     * 정수 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 정수 숫자
     * @return 형식화된 문자열 표현
     */
    actual fun format(number: Int): String {
        val df = java.text.DecimalFormat("###,###")
        return df.format(number)
    }
}

/**
 * Android용 소수점 형식화 클래스 (정밀도 지정).
 * 
 * 이 클래스는 지정된 소수점 자릿수로 숫자를 형식화하는 구현을 제공합니다.
 * 
 * @param decimalPlaces 표시할 소수점 자릿수
 * @see DecimalFormat
 * @see DoubleFormat
 */
actual class DoubleFormat actual constructor(decimalPlaces: Int) {
    private val formatString = "%.${decimalPlaces}f"

    /**
     * 지정된 소수점 자릿수로 소수점 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 소수점 숫자
     * @return 지정된 소수점 자릿수로 형식화된 문자열 표현
     */
    actual fun format(number: Double): String {
        return String.format(Locale.getDefault(), formatString, number)
    }
}