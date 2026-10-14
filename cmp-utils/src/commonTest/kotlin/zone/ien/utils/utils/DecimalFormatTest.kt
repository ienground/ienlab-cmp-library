package zone.ien.utils.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalFormatTest {
    @Test
    fun optional_fraction_digits_follow_the_pattern() {
        val formatter = DecimalFormat("0.##")
        assertEquals("12.35", formatter.format(12.3456))
        assertEquals("12.3", formatter.format(12.3))
        assertEquals("12", formatter.format(12))
        assertEquals("0", formatter.format(0))
        assertEquals("-12.3", formatter.format(-12.3))
    }

    @Test
    fun required_digits_and_grouping_follow_the_pattern() {
        assertEquals("012345.67890", DecimalFormat("000000.00000").format(12345.6789))
        assertEquals("12,345.67890", DecimalFormat("#,###.00000").format(12345.6789))
        assertEquals("12,345", DecimalFormat().format(12345))
        assertEquals("12,346", DecimalFormat().format(12345.6789))
    }

    @Test
    fun patterns_support_affixes_percent_and_exponents() {
        assertEquals("+12.3", DecimalFormat("+0.##;-0.##").format(12.3))
        assertEquals("-12.3", DecimalFormat("+0.##;-0.##").format(-12.3))
        assertEquals("(12.30)", DecimalFormat("0.00;(0.00)").format(-12.3))
        assertEquals("12.5%", DecimalFormat("0.##%").format(0.125))
        assertEquals("1.235E4", DecimalFormat("0.###E0").format(12345.6789))
        assertEquals("#12.3", DecimalFormat("'#'0.##").format(12.3))
        assertEquals("12.3;x", DecimalFormat("0.##';x'").format(12.3))
        assertEquals("(12.3)", DecimalFormat("0.0;(#)").format(-12.3))
        assertEquals("12,345,678", DecimalFormat("#,##,##0").format(12345678))
    }

    @Test
    fun rounding_preserves_binary_double_boundaries() {
        assertEquals("2.67", DecimalFormat("0.##").format(2.675))
        assertEquals("-2.67", DecimalFormat("0.##").format(-2.675))
        assertEquals("1.01", DecimalFormat("0.##").format(1.015))
        assertEquals("1", DecimalFormat("0.##").format(1.005))
        assertEquals("2.67E0", DecimalFormat("0.##E0").format(2.675))
        assertEquals("2.67E", DecimalFormat("0.##'E'").format(2.675))
        assertEquals("0.01", DecimalFormat("0.##").format(0.005))
        assertEquals("0.0267", DecimalFormat("0.####").format(0.02675))
        assertEquals("0.100000000000000000", DecimalFormat("0.000000000000000000").format(0.1))
    }

    @Test
    fun rounding_uses_half_even() {
        assertEquals("2", DecimalFormat("0").format(2.5))
        assertEquals("4", DecimalFormat("0").format(3.5))
        assertEquals("1.12", DecimalFormat("0.00").format(1.125))
        assertEquals("1.38", DecimalFormat("0.00").format(1.375))
    }
}
