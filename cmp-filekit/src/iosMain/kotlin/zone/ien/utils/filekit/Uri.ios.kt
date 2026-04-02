package zone.ien.utils.filekit

import io.github.vinceglb.filekit.PlatformFile

actual fun PlatformFile.getUri(): String = nsUrl.toString()