package zone.ien.utils.utils

/**
 * 교차 플랫폼 URL 열기 유틸리티.
 * 
 * 이 모듈은 시스템 브라우저나 앱스토어에서 URL을 여는 기능을 제공합니다.
 * 다양한 플랫폼에서 작동하는 유틸리티입니다.
 * 
 * @see openUrl
 * @see openAppStoreUrl
 */
expect fun openUrl(url: String)

/**
 * 애플리케이션의 페이지를 해당 앱스토어에서 엽니다.
 * 
 * @param android 안드로이드 앱스토어 URL.
 * @param ios iOS 앱스토어 URL.
 */
expect fun openAppStoreUrl(android: String, ios: String)