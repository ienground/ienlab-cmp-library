package zone.ien.utils.ui.menu

import zone.ien.utils.icon.IconData

/**
 * ActionMenuItem은 액션 메뉴 항목의 기본 인터페이스입니다.
 * 모든 메뉴 항목은 이 인터페이스를 구현해야 합니다.
 *
 * @property title 항목의 제목
 * @property onClick 항목 클릭 시 호출되는 콜백 함수
 * @property visible 항목의 표시 여부
 * @property enabled 항목의 활성화 여부
 */
sealed interface ActionMenuItem {
    val title: String
    val onClick: () -> Unit
    val visible: Boolean
    val enabled: Boolean

    /**
     * IconMenuItem은 아이콘을 포함하는 메뉴 항목의 인터페이스입니다.
     * 아이콘과 배지(badge)를 포함할 수 있습니다.
     *
     * @property icon 항목의 아이콘
     * @property badge 항목의 배지 숫자 (0이면 배지 표시 안 함)
     */
    sealed interface IconMenuItem : ActionMenuItem {
        val icon: IconData?
        val badge: Int

        /**
         * AlwaysShown은 항상 표시되는 아이콘 메뉴 항목입니다.
         *
         * @property title 항목의 제목
         * @property onClick 항목 클릭 시 호출되는 콜백 함수
         * @property icon 항목의 아이콘
         * @property badge 항목의 배지 숫자 (0이면 배지 표시 안 함)
         * @property visible 항목의 표시 여부
         * @property enabled 항목의 활성화 여부
         */
        data class AlwaysShown(
            override val title: String,
            override val onClick: () -> Unit,
            override val icon: IconData?,
            override val badge: Int = 0,
            override val visible: Boolean = true,
            override val enabled: Boolean = true
        ) : IconMenuItem

        /**
         * ShownIfRoom은 화면 공간이 허용될 때 표시되는 아이콘 메뉴 항목입니다.
         *
         * @property title 항목의 제목
         * @property onClick 항목 클릭 시 호출되는 콜백 함수
         * @property icon 항목의 아이콘
         * @property badge 항목의 배지 숫자 (0이면 배지 표시 안 함)
         * @property visible 항목의 표시 여부
         * @property enabled 항목의 활성화 여부
         */
        data class ShownIfRoom(
            override val title: String,
            override val onClick: () -> Unit,
            override val icon: IconData?,
            override val badge: Int = 0,
            override val visible: Boolean = true,
            override val enabled: Boolean = true
        ) : IconMenuItem
    }

    /**
     * NeverShown은 항상 숨겨진 메뉴 항목입니다.
     * 이 항목은 오버플로 메뉴에만 표시됩니다.
     *
     * @property title 항목의 제목
     * @property onClick 항목 클릭 시 호출되는 콜백 함수
     * @property visible 항목의 표시 여부
     * @property enabled 항목의 활성화 여부
     */
    data class NeverShown(
        override val title: String,
        override val onClick: () -> Unit,
        override val visible: Boolean = true,
        override val enabled: Boolean = true
    ): ActionMenuItem
}