package zone.ien.utils.utils

import platform.UIKit.UIApplication

actual object BadgeController {
    actual fun updateBadge(badges: Int) {
        val count = if (badges < 0) 0L else badges.toLong()
        UIApplication.sharedApplication.setApplicationIconBadgeNumber(count)
    }
}