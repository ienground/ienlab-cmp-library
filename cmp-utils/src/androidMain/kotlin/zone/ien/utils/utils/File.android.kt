package zone.ien.utils.utils

import kotlinx.io.files.Path
import zone.ien.utils.applicationContext

/**
 * Android용 데이터 디렉토리 경로 가져오기.
 * 
 * 이 함수는 지정된 애플리케이션 ID에 대한 데이터 디렉토리 경로를 반환합니다.
 * 
 * @param appId 애플리케이션 ID
 * @return 데이터 디렉토리 경로
 */
actual fun dataDirectory(appId: String): Path = applicationContext.applicationInfo.dataDir.toPath()

/**
 * Android용 캐시 디렉토리 경로 가져오기.
 * 
 * 이 함수는 지정된 애플리케이션 ID에 대한 캐시 디렉토리 경로를 반환합니다.
 * 
 * @param appId 애플리케이션 ID
 * @return 캐시 디렉토리 경로
 */
actual fun cacheDirectory(appId: String): Path = applicationContext.cacheDir.absolutePath.toPath()