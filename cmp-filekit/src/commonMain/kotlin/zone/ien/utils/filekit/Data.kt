package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.Data

/**
 * 바이트 배열을 Firebase Storage 데이터로 변환하는 예상 함수
 * @return Firebase Storage Data 객체
 */
expect suspend fun ByteArray.toStorageData(): Data