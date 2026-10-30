package zone.ien.utils.adaptive.view

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationBarTest {
    @Test
    fun duoUsesComposableTabsEvenWhenNativeTabsAreRequested() {
        assertFalse(shouldUseNativeNavigationBar(isNative = true, hasBadges = false, isSideBar = true))
    }

    @Test
    fun regularLayoutKeepsRequestedNativeTabs() {
        assertTrue(shouldUseNativeNavigationBar(isNative = true, hasBadges = false, isSideBar = false))
    }

    @Test
    fun badgesAndExplicitComposableTabsKeepComposableTabs() {
        assertFalse(shouldUseNativeNavigationBar(isNative = true, hasBadges = true, isSideBar = false))
        assertFalse(shouldUseNativeNavigationBar(isNative = false, hasBadges = false, isSideBar = false))
    }
}
