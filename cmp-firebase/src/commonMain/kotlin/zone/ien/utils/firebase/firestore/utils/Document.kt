package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.filter

fun DocumentReference.getSnapshot(cache: Boolean = true) =
    snapshots(includeMetadataChanges = !cache)
        .filter { !it.metadata.isFromCache || cache }