package zone.ien.utils.utils

import android.app.ActivityManager
import android.content.Context

/**
 * Android에서의 moveToBackground 함수 구현.
 *
 * 이 함수는 ActivityManager를 사용하여 Android 애플리케이션을 백그라운드로 이동합니다.
 * 현재 애플리케이션 태스크를 종료하고 제거합니다.
 */
actual fun moveToBackground() {
    val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.appTasks.firstOrNull()?.finishAndRemoveTask()
}