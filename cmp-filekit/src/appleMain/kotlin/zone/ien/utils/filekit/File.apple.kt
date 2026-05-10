package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import platform.Foundation.NSURL

/**
 * PlatformFile을 Apple File 객체로 변환합니다.
 * @return 변환된 File 객체 또는 null (변환 실패 시)
 */
actual fun PlatformFile.toFile(): File? {
    val url = NSURL(fileURLWithPath = this.path)
    val file = File(url)
    return file
}

/**
 * 지정된 경로의 파일을 가져옵니다.
 * @param path 파일 경로
 * @return File 객체
 */
actual fun getFile(path: String): File {
    val url = NSURL(fileURLWithPath = path)
    return File(url)
}

/**
 * File을 파일 경로 문자열로 변환합니다.
 * @return 파일 경로 문자열
 */
actual fun File.toPath(): String = url.path.orEmpty()