package zone.ien.utils.ui.menu

import zone.ien.utils.icon.IconData

sealed interface ActionMenuItem {
    val title: String
    val onClick: () -> Unit
    val visible: Boolean
    val enabled: Boolean

    sealed interface IconMenuItem : ActionMenuItem {
        val icon: IconData?
        val badge: Int

        data class AlwaysShown(
            override val title: String,
            override val onClick: () -> Unit,
            override val icon: IconData?,
            override val badge: Int = 0,
            override val visible: Boolean = true,
            override val enabled: Boolean = true
        ) : IconMenuItem

        data class ShownIfRoom(
            override val title: String,
            override val onClick: () -> Unit,
            override val icon: IconData?,
            override val badge: Int = 0,
            override val visible: Boolean = true,
            override val enabled: Boolean = true
        ) : IconMenuItem
    }

    data class NeverShown(
        override val title: String,
        override val onClick: () -> Unit,
        override val visible: Boolean = true,
        override val enabled: Boolean = true
    ): ActionMenuItem
}