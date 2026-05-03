package zone.ien.utils.utils

import android.app.ActivityManager
import android.content.Context

actual fun moveToBackground() {
    val activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.appTasks.firstOrNull()?.finishAndRemoveTask()
}