package zone.ien.utils.utils

import androidx.compose.ui.text.intl.Locale

/**
 * 교차 플랫폼 로케일 유틸리티.
 * 
 * 이 모듈은 교차 플랫폼 방식으로 로케일을 다루는 유틸리티를 제공하며,
 * 특히 한국어 로케일 지원에 중점을 둡니다.
 * 
 * @see Locale.Companion.KOREA
 * @see Pair.locale
 */
val Locale.Companion.KOREA: Locale get() = Locale("ko-KR")

/**
 * 현재 로케일에 따라 적절한 문자열을 선택합니다.
 * 
 * 현재 로케일이 한국어이면 첫 번째 문자열을 반환하고, 그렇지 않으면 두 번째 문자열을 반환합니다.
 * 첫 번째 문자열이 null이고 두 번째가 null이 아니면 두 번째 문자열을 반환합니다.
 * 두 문자열 모두 null이면 빈 문자열을 반환합니다.
 * 
 * @receiver 한국어용 첫 번째 문자열과 다른 언어용 두 번째 문자열로 구성된 Pair입니다.
 * @return 현재 로케일에 따라 선택된 문자열.
 */
fun Pair<String?, String?>.locale() = (if (Locale.current == Locale.KOREA) first else second).orEmpty()