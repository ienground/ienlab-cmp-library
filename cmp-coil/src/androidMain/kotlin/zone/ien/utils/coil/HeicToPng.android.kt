package zone.ien.utils.coil

import coil3.decode.BitmapFactoryDecoder
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.decode.ImageSource
import coil3.request.Options

// androidMain
actual class HEICImageDecoder actual constructor(
    private val source: ImageSource,
    private val options: Options,
) : Decoder {
    // Android는 기본 BitmapDecoder가 처리하므로 그냥 위임
    private val delegate = BitmapFactoryDecoder(source, options)
    actual override suspend fun decode(): DecodeResult? = delegate.decode()
}