package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import dev.gitlive.firebase.storage.StorageReference
import io.github.vinceglb.filekit.PlatformFile

expect suspend fun StorageReference.uploadFile(file: PlatformFile, metadata: FirebaseStorageMetadata? = null)