package zone.ien.utils.filekit

import zone.ien.firebase.storage.Data

/**
 * 바이트 배열을 Firebase Storage 데이터로 변환합니다.
 * @return Firebase Storage Data 객체
 */
actual suspend fun ByteArray.toStorageData(): Data = Data(this)