package zone.ien.utils.firebase.firestore.model

import dev.gitlive.firebase.firestore.DocumentSnapshot

/**
 * 무한 스크롤 상태 리스트 인터페이스
 * @param T 문서의 타입
 */
interface InfScrollStateList<T> {
    /**
     * 항목 리스트
     */
    val itemList: Map<String, T>
    
    /**
     * 마지막으로 보여진 문서
     */
    val lastVisibleDocument: DocumentSnapshot?
    
    /**
     * 초기화 상태
     */
    val isInitialized: Boolean
    
    /**
     * 로딩 상태
     */
    val isLoading: Boolean
    
    /**
     * 더 이상 데이터 존재 여부
     */
    val hasMore: Boolean
}