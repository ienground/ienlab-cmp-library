package zone.ien.utils.filekit

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes

/**
 * 이미지(PlatformFile)를 지정된 크기 이하로 압축하는 함수입니다.
 * @param targetSize 압축 후 목표로 하는 최대 크기 (Byte 단위)
 * @return 압축된 바이트 배열 또는 파일 읽기 실패 시 null
 */
private const val QUALITY_STEP = 5
suspend fun PlatformFile.compressFile(targetSize: Long): ByteArray? {
    val originalBytes = try {
        readBytes()
    } catch (e: Exception) {
        return null
    }

    var compressionQuality = 90
    var currentBytes = originalBytes

    while (currentBytes.size > targetSize && compressionQuality >= QUALITY_STEP) {
        compressionQuality -= QUALITY_STEP
        currentBytes = FileKit.compressImage(
            bytes = originalBytes,
            quality = compressionQuality.coerceAtLeast(1),
            imageFormat = ImageFormat.JPEG
        )
    }

    return currentBytes
}
