package zone.ien.utils.filekit

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.vinceglb.filekit.PlatformFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlatformFileConversionTest {
    @Test
    fun pathFileConvertsToStorageFileAndUri() {
        val pathFile = java.io.File("/tmp/image.jpg")
        val platformFile = PlatformFile(pathFile)
        val expectedUri = Uri.fromFile(pathFile).toString()

        assertEquals(expectedUri, platformFile.getUri())
        assertEquals(expectedUri, platformFile.toFile()?.toPath())
    }

    @Test
    fun contentUriConvertsToStorageFileAndUri() {
        val uri = Uri.parse("content://example.provider/images/42")
        val platformFile = PlatformFile(uri)
        val storageFile = platformFile.toFile()

        assertNotNull(storageFile)
        assertEquals(uri.toString(), platformFile.getUri())
        assertEquals(uri.toString(), storageFile?.toPath())
    }
}
