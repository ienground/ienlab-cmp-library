package zone.ien.utils.filekit

import io.github.vinceglb.filekit.PlatformFile

/**
 * PlatformFile에서 URI 문자열을 얻습니다.
 * @return 파일의 URI 문자열
 */
actual fun PlatformFile.getUri(): String = nsUrl.toString()