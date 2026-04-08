package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FieldPath
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import zone.ien.utils.firebase.firestore.model.FirestoreItem

suspend fun <T : FirestoreItem> fetchItems(
    collection: CollectionReference,
    transform: DocumentSnapshot.() -> T,
    cache: MutableMap<String, T>,
    ids: List<String>
): Map<String, T> {
    if (ids.isEmpty()) return emptyMap()

    val cached = ids.mapNotNull { cache[it] }.associateBy { it.id }
    val missingIds = ids - cached.keys

    // 병렬 fetch
    val fetched = missingIds
        .chunked(30)
        .map { chunk ->
            coroutineScope {
                async {
                    collection
                        .where { FieldPath.documentId inArray chunk }
                        .get()
                        .documents
                        .map { it.transform() }
                }
            }
        }
        .awaitAll()
        .flatten()
        .associateBy { it.id }

    // 캐시 업데이트
    cache.putAll(fetched)

    return cached + fetched
}

fun <T : FirestoreItem> fetchItemsAsFlow(
    collection: CollectionReference,
    transform: DocumentSnapshot.() -> T,
    ids: List<String>
): Flow<Map<String, T>> {
    if (ids.isEmpty()) return flowOf(emptyMap())

    return channelFlow {
        ids.chunked(30).map { chunk ->
            collection
                .where { FieldPath.documentId inArray chunk }
                .getSnapshots()
                .map { snapshot ->
                    snapshot.documents.map { it.transform() }.associateBy { it.id }
                }
        }.merge()
            .collect { partialMap ->
                send(partialMap)
            }
    }
}