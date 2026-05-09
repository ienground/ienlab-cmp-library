package zone.ien.utils.filekit

import io.github.vinceglb.filekit.PlatformFile

/**
 * PlatformFile에서 URI 문자열을 얻는 예상 함수
 * @return 파일의 URI 문자열
 */
expect fun PlatformFile.getUri(): String