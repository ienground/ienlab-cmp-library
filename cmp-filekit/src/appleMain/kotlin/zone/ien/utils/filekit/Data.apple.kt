package zone.ien.utils.filekit

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import zone.ien.firebase.storage.Data

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual suspend fun ByteArray.toStorageData(): Data {
    usePinned { pinned ->
        val data = NSData.create(
            bytes = pinned.addressOf(0),
            length = size.toULong()
        )

        return Data(data)
    }
}