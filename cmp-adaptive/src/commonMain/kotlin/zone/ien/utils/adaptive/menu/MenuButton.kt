package zone.ien.utils.adaptive.menu

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete
import zone.ien.utils.cmp_ui.generated.resources.edit
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.IconData

@Composable
fun adaptiveDeleteButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.delete),
    icon = MaterialIcons.Delete.let { IconData.Paint(
        AdaptiveIcons.painter(
            material = { it },
            cupertino = { "trash.fill" }
        )
    ) },
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun adaptiveSaveButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) =  ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.save),
    icon = MaterialIcons.Save.let { IconData.Paint(
        AdaptiveIcons.painter(
            material = { it },
            cupertino = { "checkmark" }
        )
    ) },
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

@Composable
fun adaptiveEditButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.edit),
    icon = MaterialIcons.Edit.let { IconData.Paint(
        AdaptiveIcons.painter(
            material = { it },
            cupertino = { "pencil" }
        )
    ) },
    onClick = onClick,
    visible = visible,
    enabled = enabled
)