package zone.ien.utils.utils.tracker

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 딥링크 데이터 인터페이스.
 * 
 * 딥링크로 전달되는 데이터를 정의하는 인터페이스입니다.
 * 
 * @property type 딥링크 타입을 나타내는 문자열.
 */
interface DeeplinkData {
    var type: String
}

/**
 * 딥링크 데이터를 저장하고 관리하는 호ル더 클래스.
 * 
 * 이 클래스는 딥링크 데이터를 Flow를 통해 관리하며, 데이터 업데이트를 지원합니다.
 * 제네릭 타입 T는 DeeplinkData 인터페이스를 구현한 데이터 클래스여야 합니다.
 * 
 * @param T 딥링크 데이터 타입 (DeeplinkData 인터페이스를 구현한 클래스)
 */
class DeeplinkHolder<T: DeeplinkData> {
    private val _deepLinkData = MutableStateFlow<T?>(null)
    val deepLinkData: StateFlow<T?> = _deepLinkData.asStateFlow()

    /**
     * 딥링크 데이터를 업데이트합니다.
     * 
     * @param data 업데이트할 딥링크 데이터
     */
    fun update(data: T) {
        _deepLinkData.update { data }
    }
}