package zone.ien.utils.model


interface InfScrollStateList<T> {
    val itemList: Map<String, T>
    val lastItemId: Long?
    val isInitialized: Boolean
    val isLoading: Boolean
    val hasMore: Boolean
}