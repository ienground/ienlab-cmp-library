package zone.ien.utils.filekit

import android.net.Uri
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile

/**
 * PlatformFile에서 URI 문자열을 얻습니다.
 * @return 파일의 URI 문자열
 */
actual fun PlatformFile.getUri(): String {
    return when (val file = androidFile) {
        is AndroidFile.UriWrapper -> file.uri.toString()
        is AndroidFile.FileWrapper -> Uri.fromFile(file.file).toString()
    }
}
