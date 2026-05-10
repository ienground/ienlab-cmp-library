package zone.ien.utils.utils

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * 교차 플랫폼 파일 시스템 유틸리티.
 * 
 * 이 모듈은 애플리케이션의 데이터와 캐시 디렉토리에 접근하기 위한 플랫폼별 파일 시스템 작업을 제공합니다.
 * 
 * @see dataDirectory
 * @see cacheDirectory
 * @see getDataDirectory
 * @see getCacheDirectory
 */
internal expect fun dataDirectory(appId: String): Path

/**
 * 애플리케이션의 캐시 디렉토리 경로를 가져옵니다.
 * 
 * @param appId 디렉토리 경로를 생성하는 데 사용되는 애플리케이션 식별자.
 * @return 캐시 디렉토리를 나타내는 Path 객체.
 */
internal expect fun cacheDirectory(appId: String): Path

/**
 * 애플리케이션의 데이터 디렉토리 경로를 가져옵니다.
 * 
 * @param appId 디렉토리 경로를 생성하는 데 사용되는 애플리케이션 식별자.
 * @param createDir 디렉토리가 존재하지 않을 경우 생성할지 여부 (기본값: true).
 * @return 데이터 디렉토리를 나타내는 Path 객체.
 */
fun getDataDirectory(appId: String, createDir: Boolean = true): Path = dataDirectory(appId).also {
    if (createDir) {
        SystemFileSystem.createDirectories(it)
    }
}

/**
 * 애플리케이션의 캐시 디렉토리 경로를 가져옵니다.
 * 
 * @param appId 디렉토리 경로를 생성하는 데 사용되는 애플리케이션 식별자.
 * @param createDir 디렉토리가 존재하지 않을 경우 생성할지 여부 (기본값: true).
 * @return 캐시 디렉토리를 나타내는 Path 객체.
 */
fun getCacheDirectory(appId: String, createDir: Boolean = true): Path = cacheDirectory(appId).also {
    if (createDir) {
        SystemFileSystem.createDirectories(it)
    }
}