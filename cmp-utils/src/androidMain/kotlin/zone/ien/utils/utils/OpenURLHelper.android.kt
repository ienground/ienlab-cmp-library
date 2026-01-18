package zone.ien.utils.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.net.toUri

actual fun openUrl(url: String) {
    val context = applicationContext
    val uri = url.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)

    context.startActivity(intent)
}

actual fun openAppStoreUrl(android: String, ios: String) {
    val context = applicationContext
    val storeUrl = "https://play.google.com/store/apps/details?id=$android"
    val uri = storeUrl.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)

    intent.setPackage("com.android.vending")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri).apply { flags = FLAG_ACTIVITY_NEW_TASK })
    }
}
