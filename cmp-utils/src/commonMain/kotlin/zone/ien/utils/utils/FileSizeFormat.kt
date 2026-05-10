package zone.ien.utils.utils

import kotlin.math.pow

/**
 * 바이트 단위의 파일 크기를 적절한 단위로 표현하는 문자열로 변환합니다.
 * 
 * 이 함수는 바이트 단위의 파일 크기를 이진 (IEC) 단위(1024기반)로 변환하여 사용자 친화적인 형식으로 출력합니다.
 * 지정된 소수점 자리수로 출력됩니다.
 * 
 * @param bytes 파일 크기 (바이트 단위).
 * @param decimalPlaces 표시할 소수점 자리수 (기본값: 2).
 * @return 적절한 단위를 가진 파일 크기 문자열.
 * 
 * @see DoubleFormat
 */
fun formatFileSize(bytes: Long, decimalPlaces: Int = 2): String {
    val doubleFormat = DoubleFormat(decimalPlaces)
    // 0 바이트 처리
    if (bytes <= 0) return "0B"

    // 국제 표준 (IEC) 단위: 1024
    val unit = 1024

    // 단위 배열: Byte, KiloByte, MegaByte, GigaByte
    val units = arrayOf("B", "KB", "MB", "GB")

    // 현재 바이트 값에 가장 적합한 단위의 인덱스(i)를 찾습니다.
    val i = (kotlin.math.log(bytes.toDouble(), unit.toDouble())).toInt().coerceIn(0, units.lastIndex)

    // 계산된 값
    val value = bytes.toDouble() / unit.toDouble().pow(i)

    // 소수점 둘째 자리까지 포맷팅하여 단위와 함께 반환합니다.
    return "${doubleFormat.format(value)}${units[i]}"
}