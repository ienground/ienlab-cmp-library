package zone.ien.utils.coil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.decode.ImageSource
import coil3.fetch.SourceFetchResult
import coil3.request.Options

expect class HEICImageDecoder(
    source: ImageSource,
    options: Options,
): Decoder {
    override suspend fun decode(): DecodeResult?
}

class HEICImageDecoderFactory: Decoder.Factory {
    override fun create(
        result: SourceFetchResult,
        options: Options,
        imageLoader: ImageLoader,
    ): Decoder? {
        val mime = result.mimeType
            ?.substringBefore(";")
            ?.trim()
            ?.lowercase()

        if (mime == "image/heic" || mime == "image/heif") {
            return HEICImageDecoder(result.source, options)
        }

        val header = try {
            result.source.source().peek().readByteArray(64)
        } catch (_: Throwable) {
            null
        } ?: return null

        if (!header.isHeifLike()) return null

        return HEICImageDecoder(result.source, options)
    }
}

@Composable
fun SetHeicImageDecoder() {
    val context = LocalPlatformContext.current
    LaunchedEffect(Unit) {
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(context)
                .components {
                    add(HEICImageDecoderFactory())
                }
                .build()
        }
    }
}

private val HEIF_BRANDS = setOf("mif1", "msf1", "heic", "heix", "hevc", "hevx")
private fun ByteArray.isHeifLike(): Boolean {
    if (size < 16) return false
    fun ascii(start: Int, len: Int): String {
        if (start + len > size) return ""
        return decodeToString(start, start + len)
    }
    if (ascii(4, 4) != "ftyp") return false
    if (ascii(8, 4).lowercase() in HEIF_BRANDS) return true
    for (i in 16..size - 4 step 4) {
        if (ascii(i, 4).lowercase() in HEIF_BRANDS) return true
    }
    return false
}