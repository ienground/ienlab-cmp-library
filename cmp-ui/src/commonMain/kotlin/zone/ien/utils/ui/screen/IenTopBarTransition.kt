package zone.ien.utils.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

/** 내비게이션이 제공하는 상단 바 영역별 전환입니다. */
interface IenTopBarTransition {
    /** 같은 역할의 영역을 연결할 식별자입니다. 화면별 식별자를 추가하지 않습니다. */
    enum class Slot { NavigationIcon, Actions, PrimaryAction }

    /** 화면 전환에 동기화된 [slot]의 Modifier를 반환합니다. */
    @Composable
    fun modifier(slot: Slot): Modifier
}

/** 전환 호스트가 없으면 상단 바는 기존 방식으로 표시됩니다. */
val LocalIenTopBarTransition = staticCompositionLocalOf<IenTopBarTransition?> { null }
