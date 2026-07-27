package zone.ien.utils.filekit

import io.github.vinceglb.filekit.PlatformFile
import zone.ien.firebase.storage.FirebaseStorageMetadata
import zone.ien.firebase.storage.StorageReference

/**
 * Firebase Storage에 파일을 업로드하는 예상 함수
 * @param file 업로드할 PlatformFile
 * @param metadata 파일 메타데이터 (옵션)
 */
expect suspend fun StorageReference.uploadFile(file: PlatformFile, metadata: FirebaseStorageMetadata? = null)