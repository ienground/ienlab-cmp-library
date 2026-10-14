package zone.ien.utils.firebase.firestore.utils

import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Timestamp
import zone.ien.utils.error.RichError
import zone.ien.utils.error.RichErrorException
import zone.ien.utils.firebase.kdatetime.toKZonedDateTime

inline fun <reified T> DocumentSnapshot.requireField(field: String): T =
    get(field) ?: error("필수 필드가 없습니다: $field ($id)")

/**
 * 필수 필드를 읽고 실패하면 지정한 오류 코드와 문서 문맥을 담아 던집니다.
 *
 * @param field 읽을 Firestore 필드 이름
 * @param errorCode 필드 읽기 실패에 사용할 오류 코드
 */
inline fun <reified T> DocumentSnapshot.requireField(
    field: String,
    errorCode: String,
): T = try {
    requireField<T>(field)
} catch (exception: Exception) {
    invalidSnapshot(errorCode, field, exception)
}

/**
 * 필수 Timestamp 필드를 로컬 시간대의 날짜와 시간으로 읽습니다.
 *
 * @param field 읽을 Firestore 필드 이름
 * @param errorCode 필드 읽기 또는 변환 실패에 사용할 오류 코드
 */
fun DocumentSnapshot.requireTimestamp(
    field: String,
    errorCode: String,
): KZonedDateTime = try {
    requireField<Timestamp>(field).toKZonedDateTime()
} catch (exception: Exception) {
    invalidSnapshot(errorCode, field, exception)
}

/**
 * 선택적 Timestamp 필드를 로컬 시간대의 날짜와 시간으로 읽습니다.
 *
 * @param field 읽을 Firestore 필드 이름
 * @param errorCode 필드 변환 실패에 사용할 오류 코드
 */
fun DocumentSnapshot.readOptionalTimestamp(
    field: String,
    errorCode: String,
): KZonedDateTime? = try {
    get<Timestamp?>(field)?.toKZonedDateTime()
} catch (exception: Exception) {
    invalidSnapshot(errorCode, field, exception)
}

/**
 * 문서 식별자와 필드 문맥, 원인 예외를 포함한 구조화 오류를 던집니다.
 *
 * @param code 발생시킬 구조화 오류 코드
 * @param field 오류가 발생한 Firestore 필드 이름
 * @param cause 변환 과정에서 발생한 원인 예외
 */
fun DocumentSnapshot.invalidSnapshot(
    code: String,
    field: String? = null,
    cause: Throwable? = null,
): Nothing {
    val context = buildMap {
        put("documentId", id)
        field?.let { put("field", it) }
    }
    throw RichErrorException(RichError(code, context), cause)
}
