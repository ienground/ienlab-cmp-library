package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile

actual fun PlatformFile.toFile(): File? {
    val file = androidFile
    return if (file is AndroidFile.UriWrapper) File(file.uri) else null
}