package zone.ien.utils.utils

/**
 * 전달받은 텍스트를 플랫폼별 공유 시트를 통해 공유합니다.
 *
 * 이 함수는 각 플랫폼의 네이티브 공유 메커니즘을 사용하여 텍스트 콘텐츠를 공유하는 교차 플랫폼 방식을 제공합니다.
 *
 * @param text 공유할 텍스트 내용
 */
expect fun shareText(text: String)