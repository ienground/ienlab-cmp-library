package zone.ien.utils.firebase.firestore.model

import com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime
import dev.gitlive.firebase.firestore.DocumentReference

interface FirestoreItem {
    val id: String
    val ref: DocumentReference?
    val createAt: KZonedDateTime
    val updateAt: KZonedDateTime
    val delete: Boolean
}