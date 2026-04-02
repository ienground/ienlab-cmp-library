package zone.ien.utils.filekit

import dev.gitlive.firebase.storage.Data

expect suspend fun ByteArray.toStorageData(): Data