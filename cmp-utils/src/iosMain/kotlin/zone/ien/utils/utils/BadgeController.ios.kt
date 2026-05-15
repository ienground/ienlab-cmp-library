package zone.ien.utils.utils

import platform.UIKit.UIApplication

actual class BadgeController actual constructor() {
    actual fun updateBadge(badges: Int) {
        UIApplication.sharedApplication.setApplicationIconBadgeNumber(badges.toLong())
    }
}