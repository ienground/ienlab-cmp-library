package zone.ien.utils.utils

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class LocalizedTest {
    @AfterTest
    fun reset_resolver() {
        setLocalizedLocaleResolver(null)
    }

    @Test
    fun supported_locale_selects_its_value_on_each_call() {
        var locale = "ko"
        setLocalizedLocaleResolver { locale }
        val value = Localized(ko = "안녕", en = "Hello")

        assertEquals("안녕", value.get())
        locale = "en"
        assertEquals("Hello", value.get())
        assertEquals("안녕", value.ko)
    }

    @Test
    fun missing_or_unsupported_locale_uses_the_requested_fallback() {
        val value = Localized(ko = "안녕", en = "Hello")
        assertEquals("안녕", value.get())
        assertEquals("Hello", value.get(fallback = LocalizedLocale.EN))

        for (locale in listOf(null, "fr", "en-US", "")) {
            setLocalizedLocaleResolver { locale }
            assertEquals("안녕", value.get())
            assertEquals("Hello", value.get(fallback = LocalizedLocale.EN))
        }
    }

    @Test
    fun blank_strings_and_null_values_use_the_fallback() {
        setLocalizedLocaleResolver { "en" }
        for (text in listOf("", " \t\n")) {
            assertEquals("안녕", Localized(ko = "안녕", en = text).get())
        }
        assertEquals("안녕", Localized<String?>(ko = "안녕", en = null).get())
        assertNull(Localized<String?>(ko = null, en = null).get())
        assertEquals("", Localized(ko = "", en = "").get())
        assertEquals(" Hello ", Localized(ko = "안녕", en = " Hello ").get())

        setLocalizedLocaleResolver { "ko" }
        assertEquals("Hello", Localized(ko = " ", en = "Hello").get(LocalizedLocale.EN))
    }

    @Test
    fun generic_values_preserve_zero_false_and_collections() {
        setLocalizedLocaleResolver { "en" }
        assertEquals(0, Localized(ko = 1, en = 0).get())
        assertEquals(false, Localized(ko = true, en = false).get())
        assertEquals(emptyList(), Localized(ko = listOf(1), en = emptyList()).get())
        assertEquals(1, Localized<Int?>(ko = 1, en = null).get())
    }

    @Test
    fun equality_compares_both_languages() {
        val value = Localized(ko = "안녕", en = "Hello")
        assertEquals(value, Localized(ko = "안녕", en = "Hello"))
        assertNotEquals(value, value.copy(ko = "다름"))
        assertNotEquals(value, value.copy(en = "Different"))
    }
}
