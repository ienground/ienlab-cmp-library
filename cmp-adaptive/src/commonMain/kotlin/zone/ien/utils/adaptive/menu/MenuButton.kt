package zone.ien.utils.adaptive.menu

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete
import zone.ien.utils.cmp_ui.generated.resources.edit
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.screen.initialNativeBarButton

/**
 * 삭제 메뉴 버튼 컴포저블
 *
 * @param visible 메뉴 버튼 표시 여부. 기본값은 true
 * @param enabled 메뉴 버튼 활성화 여부. 기본값은 true
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return ActionMenuItem - 삭제 메뉴 아이템
 */
@Composable
fun adaptiveDeleteButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.delete),
    icon = IconData.Paint(
        AdaptiveIcons.painter(
            material = { SystemIcons.Delete },
            cupertino = { "trash.fill" }
        )
    ),
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

/**
 * 저장 메뉴 버튼 컴포저블
 *
 * @param visible 메뉴 버튼 표시 여부. 기본값은 true
 * @param enabled 메뉴 버튼 활성화 여부. 기본값은 true
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return ActionMenuItem - 저장 메뉴 아이템
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun adaptiveSaveButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = saveAction(
    icon = IconData.Paint(
        AdaptiveIcons.painter(
            material = { SystemIcons.Save },
            cupertino = { SaveSystemImage }
        )
    ),
    onClick = onClick,
    visible = visible,
    enabled = enabled,
)

@Composable
private fun saveAction(
    icon: IconData?,
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit,
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.save),
    icon = icon,
    onClick = onClick,
    visible = visible,
    enabled = enabled,
)

private const val SaveSystemImage = "checkmark"

/** 저장 액션과 같은 제목·아이콘을 사용하는 네이티브 전환용 버튼입니다. */
@Composable
fun initialNativeSaveButton() = initialNativeBarButton(
    action = saveAction(icon = null) {},
    isPrimary = true,
    systemImage = SaveSystemImage,
)

/**
 * 편집 메뉴 버튼 컴포저블
 *
 * @param visible 메뉴 버튼 표시 여부. 기본값은 true
 * @param enabled 메뉴 버튼 활성화 여부. 기본값은 true
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return ActionMenuItem - 편집 메뉴 아이템
 */
@Composable
fun adaptiveEditButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.edit),
    icon = IconData.Paint(
        AdaptiveIcons.painter(
            material = { SystemIcons.Edit },
            cupertino = { "pencil" }
        )
    ),
    onClick = onClick,
    visible = visible,
    enabled = enabled
)