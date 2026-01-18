package zone.ien.utils.utils

import kotlinx.io.files.Path

actual fun dataDirectory(appId: String): Path = applicationContext.applicationInfo.dataDir.toPath()

actual fun cacheDirectory(appId: String): Path = applicationContext.cacheDir.absolutePath.toPath()