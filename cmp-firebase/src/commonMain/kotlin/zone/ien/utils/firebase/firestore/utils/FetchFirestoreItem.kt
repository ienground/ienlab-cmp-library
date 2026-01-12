package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FieldPath
import zone.ien.utils.firebase.firestore.model.FirestoreItem

suspend fun <T> fetchItem(
    collection: CollectionReference,
    transform: DocumentSnapshot.() -> T,
    cache: MutableMap<String, T>,
    ids: List<String>
): Map<String, T> where T: FirestoreItem {
    val cached = ids.mapNotNull { cache[it] }
    val missingIds = ids - cached.map { it.id }.toSet()
    val result = mutableMapOf<String, T>().apply { putAll(cached.associateBy { it.id }) }

    missingIds.chunked(30).forEach { chunk ->
        val documents = collection
            .where { FieldPath.documentId inArray chunk }
            .get().documents
        documents.forEach { document ->
            val item = document.transform()
            cache[document.id] = item
            result[document.id] = item
        }
    }

    return result
}