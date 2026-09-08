package zone.ien.utils.firebase.firestore.utils

import zone.ien.firebase.firestore.DocumentSnapshot
import zone.ien.firebase.firestore.get

inline fun <reified T> DocumentSnapshot.requireField(field: String): T =
    get(field) ?: error("필수 필드가 없습니다: $field ($id)")
