package zone.ien.utils.firebase.firestore.model

import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import dev.gitlive.firebase.firestore.DocumentReference

interface BaseFirestoreItem {
    /**
     * 문서의 고유 ID
     */
    val id: String

    /**
     * 문서 참조 (DocumentReference)
     */
    val ref: DocumentReference?

    /**
     * 문서 생성 시간
     */
    val createAt: KZonedDateTime

    /**
     * 문서 마지막 수정 시간
     */
    val updateAt: KZonedDateTime
}

/**
 * Firestore 문서를 나타내는 인터페이스
 * 모든 Firestore 문서는 이 인터페이스를 구현해야 합니다
 */
interface FirestoreItem: BaseFirestoreItem {
    /**
     * 문서 삭제 시간
     */
    val deletedAt: KZonedDateTime?
}

/**
 * 이전 Firestore 문서를 나타내는 인터페이스
 * delete가 있음
 */
interface LegacyFirestoreItem: BaseFirestoreItem {
    /**
     * 문서 삭제 상태
     */
    val delete: Boolean
}