package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import dev.gitlive.firebase.storage.StorageReference
import io.github.vinceglb.filekit.PlatformFile

actual suspend fun StorageReference.uploadFile(file: PlatformFile, metadata: FirebaseStorageMetadata?) {
    val f = file.toFile() ?: return

    putFile(f, metadata)
}