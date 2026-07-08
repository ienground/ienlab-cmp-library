package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FieldValue

/**
 * 문서를 삭제 상태로 변경하는 함수
 * delete 필드를 true로 설정하고 updateAt을 서버 타임스탬프로 업데이트
 */
suspend fun DocumentReference.del() {
    update(DeleteHashMap)
}

/**
 * 문서의 삭제 상태를 되돌리는 함수
 * delete 필드를 false로 설정하고 updateAt을 서버 타임스탬프로 업데이트
 */
suspend fun DocumentReference.undel() {
    update(UndeleteHashMap)
}

/**
 * 삭제 상태로 설정할 데이터 맵
 * @property updateAt 서버 타임스탬프
 * @property deletedAt 삭제 서버 타임스탬프
 */
private val DeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "deletedAt" to FieldValue.serverTimestamp
)

/**
 * 삭제 상태를 되돌릴 데이터 맵
 * @property updateAt 서버 타임스탬프
 * @property deletedAt 삭제 일시 (null)
 */
private val UndeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "deletedAt" to null
)

/**
 * 문서를 삭제 상태로 변경하는 함수
 * delete 필드를 true로 설정하고 updateAt을 서버 타임스탬프로 업데이트
 */
suspend fun DocumentReference.delLegacy() {
    update(DeleteHashMap)
}

/**
 * 문서의 삭제 상태를 되돌리는 함수
 * delete 필드를 false로 설정하고 updateAt을 서버 타임스탬프로 업데이트
 */
suspend fun DocumentReference.undelLegacy() {
    update(UndeleteHashMap)
}

/**
 * 삭제 상태로 설정할 데이터 맵
 * @property updateAt 서버 타임스탬프
 * @property delete 삭제 상태 (true)
 */
private val LegacyDeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "delete" to true
)

/**
 * 삭제 상태를 되돌릴 데이터 맵
 * @property updateAt 서버 타임스탬프
 * @property delete 삭제 상태 (false)
 */
private val LegacyUndeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "delete" to false
)