package zone.ien.utils.firebase.firestore.utils

import kotlinx.coroutines.flow.filter
import zone.ien.firebase.firestore.CollectionReference
import zone.ien.firebase.firestore.Query
import zone.ien.firebase.firestore.metadata
import zone.ien.firebase.firestore.snapshots


/**
 * CollectionReference에서 스냅샷을 구독하는 함수
 * @param cache 캐시 사용 여부 (기본값은 true)
 * @return QuerySnapshot의 Flow
 */
fun CollectionReference.getSnapshots(cache: Boolean = true) =
    snapshots(includeMetadataChanges = !cache)
        .filter { !it.metadata.isFromCache || cache }

/**
 * Query에서 스냅샷을 구독하는 함수
 * @param cache 캐시 사용 여부 (기본값은 true)
 * @return QuerySnapshot의 Flow
 */
fun Query.getSnapshots(cache: Boolean = true) =
    snapshots(!cache)
        .filter { !it.metadata.isFromCache || cache }