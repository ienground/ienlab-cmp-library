package zone.ien.utils.utils

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVarOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreGraphics.CGBitmapInfo
import platform.CoreGraphics.CGColorRenderingIntent
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCreateWithCFData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreate
import platform.CoreGraphics.kCGBitmapByteOrder32Little
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImageOrientation
import platform.UIKit.UIScreen
import platform.posix.memcpy

private fun ImageBitmap.toPixelsIntArray(): IntArray {
    val width = this.width
    val height = this.height

    // 1. ImageBitmap 크기만큼의 IntArray 버퍼를 준비합니다.
    val buffer = IntArray(width * height)

    // 2. readPixels 함수를 호출하여 픽셀 데이터를 버퍼에 복사합니다.
    // 인자를 모두 기본값으로 사용하여 전체 이미지를 복사합니다.
    this.readPixels(
        buffer = buffer,
        startX = 0,
        startY = 0,
        width = width,
        height = height,
        bufferOffset = 0,
        stride = width
    )

    return buffer
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ImageBitmap.toUIImage(): UIImage {
    // 1. ImageBitmap의 속성 추출
    val width = this.width
    val height = this.height

    // 2. 픽셀 데이터를 IntArray로 가져옵니다. (RGBA 값)
    val pixels = toPixelsIntArray() // Compose Multiplatform 내부 API 가정

    memScoped {
        // 3. IntArray를 NSData로 변환

//        val data: NSData = NSData.dataWithBytes(pixels.addressOf(0), (width * height * 4).toULong())
//            ?: throw IllegalStateException("Failed to create NSData from pixels.")

        val data: NSData = pixels.usePinned { pinned ->
            // IntArray의 바이트 길이: Int당 4바이트
            val byteLength = (width * height * 4).toULong()

            NSData.create(
                bytes = pinned.addressOf(0),
                length = byteLength
            )
        }
        val bytesPointer = data.bytes
        val typedBytesPointer: CPointer<UByteVarOf<UByte>>? = bytesPointer?.reinterpret()
        val cfData = CFDataCreate(kCFAllocatorDefault, typedBytesPointer, data.length.toLong())
        // 4. CGDataProvider 및 CGImage 생성 설정
        val colorSpace = CGColorSpaceCreateDeviceRGB()
        val alphaInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
        val byteOrderInfo: UInt = kCGBitmapByteOrder32Little
        val bitmapInfo: CGBitmapInfo = byteOrderInfo or alphaInfo

        val provider = CGDataProviderCreateWithCFData(cfData)

        // 5. CGImage 생성
        val cgImage = CGImageCreate(
            width = width.toULong(),
            height = height.toULong(),
            bitsPerComponent = 8u,
            bitsPerPixel = 32u,
            bytesPerRow = (width * 4).toULong(), // 4 bytes per pixel (RGBA)
            space = colorSpace,
            bitmapInfo = bitmapInfo,
            provider = provider,
            decode = null,
            shouldInterpolate = true,
            intent = CGColorRenderingIntent.kCGRenderingIntentDefault
        ) ?: throw IllegalStateException("Failed to create CGImage.")

        // 6. CGImage를 UIImage로 변환 (Scale은 Main Screen Scale 사용)
        return UIImage.imageWithCGImage(cgImage, scale = UIScreen.mainScreen.scale, orientation = UIImageOrientation.UIImageOrientationUp)
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun ImageBitmap.encodeToByteArray(quality: Int): ByteArray {
    // 1. ImageBitmap을 UIImage로 변환
    val uiImage = this.toUIImage()

    // 2. UIImage를 JPEG 데이터(NSData)로 인코딩합니다.
    // quality는 0.0 ~ 1.0 사이의 Float이므로 변환이 필요합니다.
    val jpegData: NSData = UIImageJPEGRepresentation(uiImage, quality.toDouble() / 100)
        ?: throw IllegalStateException("Failed to encode UIImage to JPEG data.")

    // 3. NSData를 Kotlin ByteArray로 변환합니다.
    return ByteArray(jpegData.length.toInt()).apply {
        memcpy(this.refTo(0), jpegData.bytes, jpegData.length)
    }
}