package zone.ien.utils.coil

import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun thumbnailCachePath(): Path? {
    val baseUrl = NSFileManager.defaultManager
        .URLsForDirectory(NSCachesDirectory, NSUserDomainMask)
        .filterIsInstance<NSURL>()
        .firstOrNull()
    val dir = baseUrl?.URLByAppendingPathComponent("thumbnail_cache")
    return dir?.path?.toPath()
}