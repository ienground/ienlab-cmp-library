package zone.ien.utils.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

/**
 * iOS용 데이터 디렉토리 경로 가져오기.
 * 
 * 이 함수는 지정된 애플리케이션 ID에 대한 데이터 디렉토리 경로를 반환합니다.
 * 
 * @param appId 애플리케이션 ID
 * @return 데이터 디렉토리 경로
 */
actual fun dataDirectory(appId: String): Path =
    NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
        .firstOrNull()?.toString()?.toPath()
        ?.let { it / appId } ?: error("Unable to get 'NSApplicationSupportDirectory'")

/**
 * iOS용 캐시 디렉토리 경로 가져오기.
 * 
 * 이 함수는 지정된 애플리케이션 ID에 대한 캐시 디렉토리 경로를 반환합니다.
 * 
 * @param appId 애플리케이션 ID
 * @return 캐시 디렉토리 경로
 */
actual fun cacheDirectory(appId: String): Path {
    val cachesDirectory = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true)
        .firstOrNull()?.toString()?.toPath() ?: error("Unable to get 'NSCachesDirectory'")

    return cachesDirectory / appId
}

@OptIn(ExperimentalForeignApi::class)
fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}