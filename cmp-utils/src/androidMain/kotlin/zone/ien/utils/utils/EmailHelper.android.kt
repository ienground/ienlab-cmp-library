package zone.ien.utils.utils

import android.content.Intent
import zone.ien.utils.applicationContext

/**
 * Android용 이메일 전송 기능 구현.
 * 
 * 이 함수는 Android 네이티브 이메일 클라이언트를 사용하여 이메일을 전송하는 인텐트를 생성합니다.
 * 이메일 클라이언트에 수신자, 제목, 본문 정보를 채웁니다.
 * 
 * @param address 수신자 이메일 주소
 * @param subject 이메일 제목
 * @param body 이메일 본문 내용
 */
actual fun sendEmail(address: String, subject: String, body: String) {
    val context = applicationContext
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        type = "message/rfc822"
    }
    context.startActivity(Intent.createChooser(intent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}