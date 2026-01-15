package zone.ien.utils.navigation.data

import androidx.navigationevent.NavigationEventInfo

data class ScreenInfo(
    val route: String,
    val title: String? = null,
): NavigationEventInfo()
