package zone.ien.utils.filekit

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CompressTest {
    @Test
    fun `목표_크기를_달성하지_못하면_null을_반환한다`() = runBlocking<Unit> {
        val result = compressImageBytesToTarget(ByteArray(100), 10) {
            ByteArray(50)
        }

        assertNull(result)
    }

    @Test
    fun `압축_결과가_목표_크기_이하면_결과를_반환한다`() = runBlocking<Unit> {
        val compressedBytes = byteArrayOf(1, 2, 3)

        val result = compressImageBytesToTarget(ByteArray(100), 3) {
            compressedBytes
        }

        assertContentEquals(compressedBytes, assertNotNull(result))
    }

    @Test
    fun `파일_읽기_취소_예외를_다시_던진다`() = runBlocking<Unit> {
        assertFailsWith<CancellationException> {
            readBytesOrNull { throw CancellationException("cancelled") }
        }
    }

    @Test
    fun `일반_파일_읽기_실패는_null로_반환한다`() = runBlocking<Unit> {
        assertNull(readBytesOrNull { throw IllegalStateException("unreadable") })
    }
}
