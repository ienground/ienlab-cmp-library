package zone.ien.utils.filekit

import android.net.Uri
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile
import zone.ien.firebase.storage.File

/**
 * PlatformFile을 Android File 객체로 변환합니다.
 * @return 변환된 File 객체 또는 null (변환 실패 시)
 */
actual fun PlatformFile.toFile(): File? {
    val file = androidFile
    return if (file is AndroidFile.UriWrapper) File(file.uri) else null
}

/**
 * 지정된 경로의 파일을 가져옵니다.
 * @param path 파일 경로
 * @return File 객체
 */
actual fun getFile(path: String): File {
    val uri = Uri.fromFile(java.io.File(path))
    return File(uri)
}

/**
 * File 객체를 URI 문자열로 변환합니다.
 * @return 파일의 URI 문자열
 */
actual fun File.toPath(): String = uri.toString()