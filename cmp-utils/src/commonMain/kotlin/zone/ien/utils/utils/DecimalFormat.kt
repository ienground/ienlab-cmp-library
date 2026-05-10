package zone.ien.utils.utils

/**
 * 교차 플랫폼 소수점 형식화 유틸리티.
 * 
 * 이 클래스는 교차 플랫폼 방식으로 소수점 숫자를 형식화하는 기능을 제공하며,
 * Android와 iOS 각각에 맞춘 구현을 포함합니다.
 * 
 * @property DecimalFormat 소수점 숫자를 형식화하는 클래스입니다.
 * @property DoubleFormat 지정된 소수점 자릿수로 숫자를 형식화하는 클래스입니다.
 * 
 * @see DecimalFormat
 * @see DoubleFormat
 */
expect class DecimalFormat() {
    /**
     * 소수점 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 소수점 숫자
     * @return 형식화된 문자열 표현
     */
    fun format(number: Double): String
    
    /**
     * 정수 숫자를 문자열로 형식화합니다.
     * 
     * @param number 형식화할 정수 숫자
     * @return 형식화된 문자열 표현
     */
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