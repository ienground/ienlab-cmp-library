package zone.ien.utils.utils

import android.content.Intent
import zone.ien.utils.applicationContext

/**
 * Android용 텍스트 공유 기능 구현.
 * 
 * 이 함수는 Android 네이티브 공유 시스템을 사용하여 텍스트를 공유하는 인텐트를 생성합니다.
 * Intent.ACTION_SEND와 text/plain 타입을 사용하며 공유 선택 다이얼로그를 표시합니다.
 * 
 * @param text 시스템 공유 메커니즘을 통해 공유할 텍스트
 */
actual fun shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    applicationContext.startActivity(
        Intent.createChooser(intent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )
}