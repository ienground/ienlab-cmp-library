package zone.ien.utils.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Build
import zone.ien.utils.applicationContext

private val initializedApplicationContext: Context?
    get() = runCatching { applicationContext }.getOrNull()

private val installedPackageInfo: PackageInfo?
    get() = initializedApplicationContext?.let { context ->
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }.getOrNull()
    }

/** Android 런타임 정보를 제공합니다. */
actual object PlatformDependent {
    actual val isDebug: Boolean
        get() = initializedApplicationContext?.let { context ->
            context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } ?: false

    actual val isIos: Boolean = false

    actual val appId: String
        get() = initializedApplicationContext?.packageName.orEmpty()

    actual val versionName: String
        get() = installedPackageInfo?.versionName.orEmpty()

    actual val versionCode: Int
        get() = installedPackageInfo?.longVersionCode
            ?.coerceIn(0L, Int.MAX_VALUE.toLong())
            ?.toInt()
            ?: 0

    actual val deviceName: String
        get() = "${Build.BRAND} ${Build.MODEL}".trim()

    actual val deviceOS: String
        get() = Build.VERSION.RELEASE.orEmpty()
}
