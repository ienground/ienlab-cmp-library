package zone.ien.utils.utils

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformDependentTest {
    @Test
    fun exposes_non_negative_version_code() {
        assertTrue(PlatformDependent.versionCode >= 0)
    }
}
