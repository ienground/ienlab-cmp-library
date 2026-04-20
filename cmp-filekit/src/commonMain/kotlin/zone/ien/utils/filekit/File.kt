package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes

expect fun PlatformFile.toFile(): File?
expect fun getFile(path: String): File
expect fun File.toPath(): String


private const val QUALITY_STEP = 5
suspend fun PlatformFile.compressFile(targetSize: Long): ByteArray? {
// 1. 원본 이미지 데이터 읽기
    val originalBytes = try {
        readBytes()
    } catch (e: Exception) {
        // 파일 읽기 실패 처리
        return null
    }

    // 2. 초기 품질 및 현재 데이터 설정
    var compressionQuality = 90 // 초기 품질 (0~100)
    var currentBytes: ByteArray = originalBytes

    // 3. 반복 압축 루프 시작
    while (currentBytes.size > targetSize && compressionQuality >= QUALITY_STEP) {
        // 품질 감소
        compressionQuality -= QUALITY_STEP

        // 품질이 0 미만이 되지 않도록 최소값 보정
        val effectiveQuality = compressionQuality.coerceAtLeast(1)

        // 4. FileKit의 압축 함수 호출 (새로운 바이트 데이터 생성)
        currentBytes = FileKit.compressImage(
            bytes = originalBytes, // 원본 데이터를 계속 사용하거나
            // 이전 압축 데이터를 사용해도 되지만, 일반적으로 원본에서 시작하는 것이 좋습니다.
            quality = effectiveQuality,
            imageFormat = ImageFormat.JPEG // JPEG는 손실 압축이므로 크기 감소에 유리
        )

        // 디버깅 용 콘솔 출력 (선택 사항)
        println("품질: $effectiveQuality, 현재 크기: ${currentBytes.size / 1024}KB")
    }



    // 5. 결과 반환
    if (currentBytes.size <= targetSize) {
        return currentBytes
    } else {
        // 루프를 돌았는데도 목표 크기를 달성하지 못한 경우 (품질 1%까지 낮췄는데도 실패)
        // 이 경우, 해상도(Resizing)를 함께 시도하는 로직이 추가로 필요합니다.
        return null
    }
}