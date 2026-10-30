package zone.ien.utils.firebase.firestore.utils

import dev.gitlive.firebase.firestore.FieldValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeletionTest {
    @Test
    fun `legacy_삭제와_복구가_boolean_delete_필드를_전송한다`() {
        val deleteMap = legacyDeletionUpdateMap(true)
        assertEquals(setOf("updateAt", "delete"), deleteMap.keys)
        assertTrue(deleteMap["updateAt"] is FieldValue)
        assertEquals(true, deleteMap["delete"])

        val undeleteMap = legacyDeletionUpdateMap(false)
        assertEquals(setOf("updateAt", "delete"), undeleteMap.keys)
        assertTrue(undeleteMap["updateAt"] is FieldValue)
        assertEquals(false, undeleteMap["delete"])
    }
}
