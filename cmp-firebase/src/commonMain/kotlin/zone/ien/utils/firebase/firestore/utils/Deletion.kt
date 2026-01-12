package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FieldValue

suspend fun DocumentReference.del() {
    update(DeleteHashMap)
}

suspend fun DocumentReference.undel() {
    update(UndeleteHashMap)
}

private val DeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "delete" to true
)

private val UndeleteHashMap = hashMapOf(
    "updateAt" to FieldValue.serverTimestamp,
    "delete" to false
)