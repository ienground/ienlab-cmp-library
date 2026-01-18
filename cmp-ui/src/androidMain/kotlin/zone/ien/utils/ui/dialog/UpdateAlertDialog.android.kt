package zone.ien.utils.ui.dialog

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.move_to_play_store

internal actual val updateAlertDismissText @Composable get() = stringResource(Res.string.move_to_play_store)