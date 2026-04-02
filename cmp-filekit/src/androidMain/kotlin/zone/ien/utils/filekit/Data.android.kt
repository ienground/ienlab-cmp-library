package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.Data

actual suspend fun ByteArray.toStorageData(): Data = Data(this)