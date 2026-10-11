package zone.ien.utils.filekit

import io.github.vinceglb.filekit.PlatformFile
import zone.ien.firebase.storage.File

/**
 * PlatformFile을 파일 객체로 변환하는 Expect 함수입니다.
 * @return 변환된 File 객체 또는 null
 */
expect fun PlatformFile.toFile(): File?

/**
 * 파일 경로를 사용하여 파일 객체를 얻는 예상 함수
 * @param path 파일 경로
 * @return 파일 객체
 */
expect fun getFile(path: String): File

/**
 * File을 파일 경로로 변환하는 예상 함수
 * @return 파일 경로 문자열
 */
expect fun File.toPath(): String
