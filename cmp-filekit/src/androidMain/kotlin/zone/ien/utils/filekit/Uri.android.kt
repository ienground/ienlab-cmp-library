package zone.ien.utils.filekit

import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile

/**
 * PlatformFile에서 URI 문자열을 얻습니다.
 * @return 파일의 URI 문자열
 */
actual fun PlatformFile.getUri(): String {
    val file = androidFile
    return if (file is AndroidFile.UriWrapper) file.uri.toString() else ""
}