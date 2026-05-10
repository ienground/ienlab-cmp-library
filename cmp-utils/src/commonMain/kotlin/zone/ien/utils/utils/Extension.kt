package zone.ien.utils.utils

/**
 * Kotlin을 위한 유틸리티 확장 함수.
 * 
 * 두 개의 값을 모두 null이 아닌 경우에만 지정된 블록 함수를 실행합니다.
 * 
 * @param p1 첫 번째 값
 * @param p2 두 번째 값
 * @param block 실행할 블록 함수
 * @return 블록 함수의 결과 또는 null (값이 null인 경우)
 */
inline fun <T1 : Any, T2 : Any, R : Any> let2(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}