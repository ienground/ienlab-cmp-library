package zone.ien.utils.filekit

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes
import kotlin.coroutines.cancellation.CancellationException

private const val QUALITY_STEP = 5

/**
 * 이미지(PlatformFile)를 지정된 크기 이하로 압축합니다.
 * @param targetSize 압축 후 목표로 하는 최대 크기 (Byte 단위)
 * @return 목표 크기 이하의 바이트 배열 또는 파일 읽기 실패·목표 크기 미달성 시 null
 * @throws CancellationException 파일 읽기가 취소된 경우
 */
suspend fun PlatformFile.compressFile(targetSize: Long): ByteArray? {
    val originalBytes = readBytesOrNull { readBytes() } ?: return null

    return compressImageBytesToTarget(originalBytes, targetSize) { quality ->
        FileKit.compressImage(
            bytes = originalBytes,
            quality = quality,
            imageFormat = ImageFormat.JPEG
        )
    }
}

internal suspend fun readBytesOrNull(read: suspend () -> ByteArray): ByteArray? = try {
    read()
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    null
}

internal suspend fun compressImageBytesToTarget(
    originalBytes: ByteArray,
    targetSize: Long,
    compress: suspend (quality: Int) -> ByteArray,
): ByteArray? {
    var compressionQuality = 90
    var currentBytes = originalBytes

    while (currentBytes.size > targetSize && compressionQuality >= QUALITY_STEP) {
        compressionQuality -= QUALITY_STEP
        currentBytes = compress(compressionQuality.coerceAtLeast(1))
    }

    return currentBytes.takeIf { it.size <= targetSize }
}
