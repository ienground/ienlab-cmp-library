package zone.ien.utils.utils

/** 한국어와 영어 지역화 값에서 지원하는 언어. */
enum class LocalizedLocale(val code: String) {
    /** 한국어. */
    KO("ko"),

    /** 영어. */
    EN("en"),
}

private var localeResolver: (() -> String?)? = null

/**
 * [Localized.get]에서 호출할 언어 결정 함수를 등록합니다.
 *
 * 함수는 조회할 때마다 호출되며 `ko`, `en`만 지원합니다.
 * null을 전달하면 등록을 해제하고 각 조회의 기본 언어를 사용합니다.
 */
fun setLocalizedLocaleResolver(resolver: (() -> String?)?) {
    localeResolver = resolver
}

/**
 * 한국어와 영어 값을 보관합니다. 동등성은 두 언어의 값을 모두 비교합니다.
 *
 * @param ko 한국어 값.
 * @param en 영어 값.
 */
data class Localized<T>(val ko: T, val en: T) {
    /**
     * 등록된 함수가 결정한 언어의 값을 반환합니다.
     *
     * 언어가 없거나 지원하지 않으면 [fallback]을 사용합니다.
     * 선택한 값이 null이거나 공백뿐인 문자열이면 기본 언어의 값을 그대로 반환합니다.
     *
     * @param fallback 기본 언어. 기본값은 한국어입니다.
     */
    fun get(fallback: LocalizedLocale = LocalizedLocale.KO): T {
        val resolved = localeResolver?.invoke()
        val locale = LocalizedLocale.entries.firstOrNull { it.code == resolved } ?: fallback
        val current = value(locale)
        return if (current == null || (current is String && current.isBlank())) {
            value(fallback)
        } else {
            current
        }
    }

    private fun value(locale: LocalizedLocale): T = when (locale) {
        LocalizedLocale.KO -> ko
        LocalizedLocale.EN -> en
    }
}
