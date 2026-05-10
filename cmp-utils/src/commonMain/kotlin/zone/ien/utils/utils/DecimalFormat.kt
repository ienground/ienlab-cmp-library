package zone.ien.utils.utils

/**
 * Cross-platform decimal formatting utility.
 * 
 * This class provides formatting capabilities for decimal numbers across
 * different platforms. It uses platform-specific implementations to ensure
 * consistent formatting behavior on each platform.
 * 
 * @property DecimalFormat Represents a class for formatting decimal numbers.
 * @property DoubleFormat Represents a formatter that allows specifying the number of decimal places.
 * 
 * @see DecimalFormat
 * @see DoubleFormat
 */
expect class DecimalFormat() {
    /**
     * Formats a double number into a string representation.
     * 
     * @param number The double number to format.
     * @return The formatted string representation of the number.
     */
    fun format(number: Double): String
    
    /**
     * Formats an integer number into a string representation.
     * 
     * @param number The integer number to format.
     * @return The formatted string representation of the number.
     */
    fun format(number: Int): String
}

/**
 * Cross-platform decimal format with customizable precision.
 * 
 * This class provides formatting capabilities for decimal numbers with 
 * a specified number of decimal places.
 * 
 * @param decimalPlaces The number of decimal places to display.
 * 
 * @property decimalPlaces The number of decimal places to display.
 * 
 * @see DecimalFormat
 */
expect class DoubleFormat(decimalPlaces: Int) {
    // 💡 수정됨: 소수점 이하 자릿수를 인자로 받음
    /**
     * Formats a double number into a string representation with specified decimal places.
     * 
     * @param number The double number to format.
     * @return The formatted string representation of the number with the specified decimal places.
     */
    fun format(number: Double): String
}