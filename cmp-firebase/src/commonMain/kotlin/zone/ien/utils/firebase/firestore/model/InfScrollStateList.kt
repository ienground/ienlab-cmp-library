package zone.ien.utils.firebase.firestore.model

import dev.gitlive.firebase.firestore.DocumentSnapshot

interface InfScrollStateList<T> {
    val itemList: Map<String, T>
    val lastVisibleDocument: DocumentSnapshot?
    val isInitialized: Boolean
    val isLoading: Boolean
    val hasMore: Boolean
}