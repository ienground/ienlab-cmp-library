package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import platform.Foundation.NSURL

actual fun PlatformFile.toFile(): File? {
    val url = NSURL(string = "file://${this.path}")
    val file = File(url)
    return file
}