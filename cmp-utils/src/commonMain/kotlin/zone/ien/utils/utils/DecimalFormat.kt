package zone.ien.utils.utils

/**
 * Java DecimalFormat 형식의 패턴으로 숫자를 표시합니다.
 *
 * `0`은 필수 자릿수, `#`은 선택 자릿수, `,`는 그룹 구분, `.`은 소수점입니다.
 * 예를 들어 `DecimalFormat("0.##").format(12.3456)`은 `"12.35"`를 반환합니다.
 * 구분 기호와 통화 기호는 현재 로케일을 따르며, 반올림은 HALF_EVEN을 사용합니다.
 * 인자 없는 생성자는 기존 천 단위 정수 표시를 유지합니다.
 */
expect class DecimalFormat {
    /** 천 단위로 구분하고 소수점 없이 표시합니다. */
    constructor()

    /**
     * 지정한 숫자 패턴으로 표시합니다.
     *
     * @param pattern 숫자 자릿수와 접두사, 접미사를 지정하는 패턴
     */
    constructor(pattern: String)

    /** 소수점 숫자를 패턴에 맞춰 표시합니다. */
    fun format(number: Double): String

    /** 정수를 패턴에 맞춰 표시합니다. */
    fun format(number: Int): String
}

/**
 * 교차 플랫폼 소수점 형식화 유틸리티 (정밀도 조정 가능).
 * 
 * 이 클래스는 지정된 소수점 자릿수를 기준으로 소수점 숫자를 형식화하는 기능을 제공합니다.
 * 
 * @param decimalPlaces 표시할 소수점 자릿수
 * 
 * @property decimalPlaces 표시할 소수점 자릿수
 * 
 * @see DecimalFormat
 */
expect class DoubleFormat(decimalPlaces: Int) {
    // 💡 수정됨: 소수점 이하 자릿수를 인자로 받음
    /**
     * 지정된 소수점 자릿수로 소수점 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 소수점 숫자
     * @return 지정된 소수점 자릿수로 형식화된 문자열 표현
     */
    fun format(number: Double): String
}