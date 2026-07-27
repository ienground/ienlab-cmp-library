package zone.ien.utils.firebase.firestore.utils

import kotlinx.coroutines.flow.filter
import zone.ien.firebase.firestore.DocumentReference
import zone.ien.firebase.firestore.metadata
import zone.ien.firebase.firestore.snapshots

/**
 * DocumentReference에서 스냅샷을 구독하는 함수
 * @param cache 캐시 사용 여부 (기본값은 true)
 * @return DocumentSnapshot의 Flow
 */
fun DocumentReference.getSnapshots(cache: Boolean = true) =
    snapshots(includeMetadataChanges = !cache)
        .filter { it?.metadata?.isFromCache == false || cache }

