package zone.ien.utils

/**
 * iOS 플랫폼 여부를 확인하는 함수
 *
 * 이 함수는 현재 실행 중인 플랫폼이 iOS인지 확인하는 expect 선언입니다.
 * 플랫폼별로 다른 구현이 필요합니다.
 *
 * @return iOS 플랫폼에서 true, 그렇지 않으면 false
 */
expect val isIos: Boolean