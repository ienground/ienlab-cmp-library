package zone.ien.utils.utils

import androidx.compose.ui.graphics.ImageBitmap

/**
 * 교차 플랫폼 이미지 비트맵 유틸리티.
 * 
 * 이 모듈은 다양한 플랫폼에서 이미지 비트맵을 다루는 유틸리티를 제공하며,
 * 특히 인코딩 작업에 적합합니다.
 * 
 * @see ImageBitmap.encodeToByteArray
 */
expect fun ImageBitmap.encodeToByteArray(quality: Int): ByteArray