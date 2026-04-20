package zone.ien.utils.filekit

import android.net.Uri
import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile

actual fun PlatformFile.toFile(): File? {
    val file = androidFile
    return if (file is AndroidFile.UriWrapper) File(file.uri) else null
}

actual fun getFile(path: String): File {
    val uri = Uri.fromFile(java.io.File(path))
    return File(uri)
}

actual fun File.toPath(): String = uri.toString()