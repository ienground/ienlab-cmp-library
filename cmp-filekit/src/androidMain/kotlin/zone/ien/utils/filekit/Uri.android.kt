package zone.ien.utils.filekit

import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile

actual fun PlatformFile.getUri(): String {
    val file = androidFile
    return if (file is AndroidFile.UriWrapper) file.uri.toString() else ""
}