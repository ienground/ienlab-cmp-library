package zone.ien.utils.ui.menu

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete
import zone.ien.utils.cmp_ui.generated.resources.edit
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.IconData

/**
 * m3DeleteButton은 삭제 버튼을 생성하기 위한 컴포저블 함수입니다.
 *
 * @param visible 버튼의 표시 여부
 * @param enabled 버튼의 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 * @return 삭제 메뉴 항목
 */
@Composable
fun m3DeleteButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.delete),
    icon = IconData.Vector(M3SystemIcons.Delete),
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

/**
 * m3SaveButton은 저장 버튼을 생성하기 위한 컴포저블 함수입니다.
 *
 * @param visible 버튼의 표시 여부
 * @param enabled 버튼의 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 * @return 저장 메뉴 항목
 */
@Composable
fun m3SaveButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.save),
    icon = IconData.Vector(M3SystemIcons.Save),
    onClick = onClick,
    visible = visible,
    enabled = enabled
)

/**
 * m3EditButton은 편집 버튼을 생성하기 위한 컴포저블 함수입니다.
 *
 * @param visible 버튼의 표시 여부
 * @param enabled 버튼의 활성화 여부
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 * @return 편집 메뉴 항목
 */
@Composable
fun m3EditButton(
    visible: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit
) = ActionMenuItem.IconMenuItem.ShownIfRoom(
    title = stringResource(Res.string.edit),
    icon = IconData.Vector(M3SystemIcons.Edit),
    onClick = onClick,
    visible = visible,
    enabled = enabled
)