package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.filter

fun CollectionReference.getSnapshot(cache: Boolean = true) =
    snapshots(includeMetadataChanges = !cache)
        .filter { !it.metadata.isFromCache || cache }