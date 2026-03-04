package zone.ien.utils.navigation

import androidx.navigationevent.NavigationEventInfo

data class ScreenInfo(
    val route: String,
    val title: String? = null,
): NavigationEventInfo()
