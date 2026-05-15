package zone.ien.utils.coil

import okio.Path
import okio.Path.Companion.toOkioPath
import zone.ien.utils.applicationContext

actual fun thumbnailCachePath(): Path? {
    return applicationContext.cacheDir.resolve("thumbnail_cache").toOkioPath()
}