package zone.ien.utils.utils

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

internal expect fun dataDirectory(appId: String): Path

internal expect fun cacheDirectory(appId: String): Path

fun getDataDirectory(appId: String, createDir: Boolean = true): Path = dataDirectory(appId).also {
    if (createDir) {
        SystemFileSystem.createDirectories(it)
    }
}

fun getCacheDirectory(appId: String, createDir: Boolean = true): Path = cacheDirectory(appId).also {
    if (createDir) {
        SystemFileSystem.createDirectories(it)
    }
}