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
    putFile(checkNotNull(file.toFile()) { "PlatformFile을 Storage File로 변환할 수 없습니다." }, metadata)
}
