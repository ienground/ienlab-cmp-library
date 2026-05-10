package zone.ien.utils.utils

import kotlinx.io.files.Path

/**
 * 교차 플랫폼 경로 유틸리티.
 * 
 * 이 모듈은 kotlinx.io.files.Path를 사용하여 파일 경로를 교차 플랫폼 방식으로 처리하는
 * 확장 함수를 제공합니다.
 * 
 * @see toPath
 * @see Path.div
 */
inline fun String.toPath(): Path = Path(this)

/**
 * 경로를 자식 문자열로 나누어 새로운 경로를 생성합니다.
 * 
 * @param child 추가할 하위 경로 구성 요소.
 * @return 하위 경로를 나타내는 새로운 경로.
 */
operator fun Path.div(child: String): Path = Path(this, child)