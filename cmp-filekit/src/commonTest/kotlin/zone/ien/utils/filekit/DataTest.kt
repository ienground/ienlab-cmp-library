package zone.ien.utils.filekit

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull

class DataTest {
    @Test
    fun convertsEmptyAndNonEmptyByteArraysToStorageData() = runBlocking<Unit> {
        assertNotNull(byteArrayOf().toStorageData())
        assertNotNull(byteArrayOf(1, 2, 3).toStorageData())
    }
}
