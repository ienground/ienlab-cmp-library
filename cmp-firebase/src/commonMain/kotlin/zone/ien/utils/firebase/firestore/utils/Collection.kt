package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Query
import kotlinx.coroutines.flow.filter

fun CollectionReference.getSnapshots(cache: Boolean = true) =
    snapshots(includeMetadataChanges = !cache)
        .filter { !it.metadata.isFromCache || cache }

fun Query.getSnapshots(cache: Boolean = true) =
    snapshots(!cache)
        .filter { !it.metadata.isFromCache || cache }