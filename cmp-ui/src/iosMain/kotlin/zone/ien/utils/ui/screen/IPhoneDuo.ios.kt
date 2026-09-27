package zone.ien.utils.ui.screen

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.uname
import platform.posix.utsname

private const val IPhoneDuoModelIdentifier = "iPhone19,4"

@OptIn(ExperimentalForeignApi::class)
internal actual fun isIPhoneDuo(): Boolean = memScoped {
    val systemInfo = alloc<utsname>()
    uname(systemInfo.ptr) == 0 && systemInfo.machine.toKString() == IPhoneDuoModelIdentifier
}
