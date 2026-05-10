package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes

/**
 * PlatformFile을 파일 객체로 변환하는 Expect 함수입니다.
 * @return 변환된 File 객체 또는 null
 */
expect fun PlatformFile.toFile(): File?

/**
 * 파일 경로를 사용하여 파일 객체를 얻는 예상 함수
 * @param path 파일 경로
 * @return 파일 객체
 */
expect fun getFile(path: String): File

/**
 * File을 파일 경로로 변환하는 예상 함수
 * @return 파일 경로 문자열
 */
expect fun File.toPath(): String


/**
 * 이미지(PlatformFile)를 지정된 크기 이하로 압축하는 함수입니다.
 * @param targetSize 압축 후 목표로 하는 최대 크기 (Byte 단위)
 * @return 압축된 바이트 배열 또는 파일 읽기 실패 시 null
 */
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
    }

    // 5. 결과 반환
    return currentBytes
}