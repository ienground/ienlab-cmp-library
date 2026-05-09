package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import dev.gitlive.firebase.storage.StorageReference
import io.github.vinceglb.filekit.PlatformFile

/**
 * Firebase Storage에 파일을 업로드합니다.
 * @param file 업로드할 PlatformFile
 * @param metadata 파일 메타데이터 (옵션)
 */
actual suspend fun StorageReference.uploadFile(file: PlatformFile, metadata: FirebaseStorageMetadata?) {
    val f = file.toFile() ?: return

    putFile(f, metadata)
}