package zone.ien.utils.coil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import okio.FileSystem
import okio.Path
import okio.SYSTEM

expect fun thumbnailCachePath(): Path?

@Composable
fun rememberThumbnailImageLoader(): ImageLoader {
    val context = LocalPlatformContext.current

    return remember {
        ImageLoader.Builder(context)
            .crossfade(false)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.08)
                    .build()
            }
            .let {
                val path = thumbnailCachePath()
                if (path != null) {
                    it.diskCache {
                        DiskCache.Builder()
                            .fileSystem(FileSystem.SYSTEM)
                            .directory(path)
                            .maxSizeBytes(20L * 1024 * 1024)
                            .build()
                    }
                } else it
            }
            .build()
    }
}