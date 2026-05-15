package zone.ien.utils.coil

import coil3.Bitmap
import coil3.asImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.decode.ImageSource
import coil3.request.Options
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import okio.use
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSData.Companion
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class HEICImageDecoder actual constructor(
    private val source: ImageSource,
    private val options: Options,
) : Decoder {

    actual override suspend fun decode(): DecodeResult? {
        val heicBytes = try {
            source.source().use { it.readByteArray() }
        } catch (_: Throwable) {
            return null
        }

        val uiImage = UIImage(data = heicBytes.toNSData())

        val jpegBytes = UIImageJPEGRepresentation(uiImage, 0.9)
            ?.toByteArray()
            ?: return null

        val skiaImage = try {
            Image.makeFromEncoded(jpegBytes)
        } catch (_: Throwable) {
            return null
        }

        return DecodeResult(
            image = Bitmap.makeFromImage(skiaImage).asImage(),
            isSampled = false,
        )
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData = usePinned {
    NSData.create(
        bytes = it.addressOf(0),
        length = this.size.convert(),
    )
}

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
private fun NSData.toByteArray(): ByteArray = let { nsData ->
    ByteArray(nsData.length.toInt()).apply {
        memcpy(this.refTo(0), nsData.bytes, nsData.length)
    }
}