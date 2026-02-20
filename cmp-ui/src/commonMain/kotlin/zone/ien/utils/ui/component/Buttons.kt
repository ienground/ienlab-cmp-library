package zone.ien.utils.ui.component

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete
import zone.ien.utils.cmp_ui.generated.resources.edit
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.menu.ActionMenuItem

@Composable
fun m3DeleteButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.delete),
    icon = MaterialIcons.Delete,
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

@Composable
fun m3SaveButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.save),
    icon = MaterialIcons.Save,
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

@Composable
fun m3EditButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.edit),
    icon = MaterialIcons.Edit,
    onClick = onClick,
    visible = visible,
    enabled = enabled
)