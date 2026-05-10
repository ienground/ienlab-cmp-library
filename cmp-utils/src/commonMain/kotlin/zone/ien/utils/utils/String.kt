package zone.ien.utils.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 문자열이 숫자만 포함하는지 확인합니다 (0-9).
 *
 * 이 함수는 문자열이 숫자만으로 구성되어 있는지 유효성을 검사합니다.
 * 다른 문자가 없는 경우 true를 반환합니다.
 *
 * @return 문자열이 숫자만 포함하면 true, 그렇지 않으면 false
 */
fun String.checkDecimal() = matches(Regex("^\\d*$"))

/**
 * 문자열이 유효한 10진수 숫자 형식인지 확인합니다.
 *
 * 이 함수는 문자열이 소수점과 소수 부분을 포함할 수 있는 유효한 10진수 형식인지
 * 유효성을 검사합니다.
 *
 * @return 문자열이 유효한 10진수 숫자이면 true, 그렇지 않으면 false
 */
fun String.checkDouble() = matches(Regex("^\\d*(\\.\\d*)?$"))

/**
 * 문자열을 정수로 변환하고, 변환에 실패하면 0을 반환합니다.
 *
 * 이 함수는 문자열을 정수로 파싱하려 시도합니다. 파싱에 실패하면 예외를 발생시키기보다는
 * 기본값 0을 반환합니다.
 *
 * @return 파싱된 정수 값 또는 파싱에 실패하면 0
 */
fun String.toSafeInt(): Int = toIntOrNull() ?: 0

/**
 * 문자열이 null 또는 비어 있지 않으면 원본 문자열을 반환하고, 그렇지 않으면 defaultValue의 결과를 반환합니다.
 *
 * 이 유틸리티 함수는 문자열을 다룰 때 null 또는 빈 문자열 검사를 피하는 데 도움을 줍니다.
 * 더 나은 최적화와 타입 추론을 위해 Kotlin 계약을 사용합니다.
 *
 * @param defaultValue null 또는 빈 문자열일 때 기본값을 반환하는 람다 함수
 * @return 원본 문자열이 null 또는 비어 있지 않으면 원본 문자열, 그렇지 않으면 defaultValue의 결과
 */
@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.ifEmptyOrNull(defaultValue: () -> String): String {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this.toString()
}