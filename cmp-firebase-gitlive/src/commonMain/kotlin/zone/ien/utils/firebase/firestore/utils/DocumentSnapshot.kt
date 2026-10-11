package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.DocumentSnapshot

inline fun <reified T> DocumentSnapshot.requireField(field: String): T =
    get(field) ?: error("필수 필드가 없습니다: $field ($id)")
