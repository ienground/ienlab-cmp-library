package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.permission_optional_section_title
import zone.ien.utils.cmp_ui.generated.resources.permission_required_section_title
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.list.IenListHeaderDefaults
import zone.ien.utils.ui.list.IenListRow
import zone.ien.utils.ui.list.IenListRowAssetFrame
import zone.ien.utils.ui.list.IenListRowAssetShape
import zone.ien.utils.ui.list.IenListRowAssetSize
import zone.ien.utils.ui.list.IenListRowBorder
import zone.ien.utils.ui.list.IenListRowPadding
import zone.ien.utils.ui.primitives.IenAssetFrame
import zone.ien.utils.ui.primitives.IenAssetFrameShape
import zone.ien.utils.ui.primitives.IenAssetFrameSize
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.toBold

/**
 * 권한 안내 화면에 표시할 권한 항목입니다.
 *
 * @property icon 항목을 설명하는 아이콘
 * @property title 항목 제목
 * @property description 항목 설명
 * @property iconContentDescription 아이콘의 접근성 설명. 장식용이면 `null`로 둡니다.
 * @property isRequired 필수 권한이면 `true`, 선택 권한이면 `false`입니다.
 */
@Immutable
data class IenPermissionItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val iconContentDescription: String? = null,
    val isRequired: Boolean = true,
)

/**
 * 앱에서 필요한 권한을 안내하는 본문 화면입니다.
 *
 * 권한 상태 조회와 실제 권한 요청, 계속하기 CTA는 호출자가 소유합니다. 화면의 문구와
 * 아이콘도 호출자가 주입하므로 앱별 리소스 및 플랫폼 권한 구현과 독립적으로 사용할
 * 수 있습니다. 하단 CTA는 [IenScaffold]의 `bottomBar`에 배치하는 것을 권장합니다.
 *
 * @param title 화면 제목
 * @param description 화면 설명
 * @param items 화면에 나열할 권한 항목
 * @param headerIcon 화면 상단 안내 아이콘
 * @param modifier 화면 루트에 적용할 [Modifier]
 * @param headerIconContentDescription 상단 아이콘의 접근성 설명. 장식용이면 `null`로 둡니다.
 * @param requiredSectionTitle 필수 권한 섹션 제목
 * @param optionalSectionTitle 선택 권한 섹션 제목
 */
@Composable
fun IenPermissionScreen(
    title: String,
    description: String,
    items: List<IenPermissionItem>,
    headerIcon: ImageVector,
    modifier: Modifier = Modifier,
    headerIconContentDescription: String? = null,
    requiredSectionTitle: String = stringResource(Res.string.permission_required_section_title),
    optionalSectionTitle: String = stringResource(Res.string.permission_optional_section_title),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = IenTheme.spacing.xxl),
    ) {
        IenTop(
            upperGap = IenTheme.spacing.xxl,
            lowerGap = 0.dp,
            upper = {
                IenTopUpperAssetContent {
                    IenAssetFrame(
                        size = IenAssetFrameSize.Large,
                        tone = IenSemanticTone.Brand,
                        shape = IenAssetFrameShape.Circle,
                        contentDescription = headerIconContentDescription,
                    ) {
                        IenIcon(
                            imageVector = headerIcon,
                            contentDescription = null,
                        )
                    }
                }
            },
            title = {
                IenTopTitleParagraph(
                    text = title,
                    size = IenTopTitleSize.Large,
                )
            },
            subtitleBottom = {
                IenTopSubtitleParagraph(
                    text = description,
                    size = IenTopSubtitleSize.Small,
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textTertiary,
                    modifier = Modifier.padding(top = IenTheme.spacing.md),
                )
            },
            contentPadding = PaddingValues(0.dp),
        )

        IenPermissionSection(
            title = requiredSectionTitle,
            items = items.filter { it.isRequired },
        )
        IenPermissionSection(
            title = optionalSectionTitle,
            items = items.filterNot { it.isRequired },
        )
    }
}

@Composable
private fun IenPermissionSection(
    title: String,
    items: List<IenPermissionItem>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = IenTheme.spacing.xxl),
    ) {
        IenListHeaderDefaults.Title(
            text = title,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            modifier = Modifier.padding(top = IenTheme.spacing.md),
        ) {
            items.forEach { item ->
                IenPermissionItemRow(item)
            }
        }
    }
}

@Composable
private fun IenPermissionItemRow(item: IenPermissionItem) {
    IenListRow(
        left = {
            IenListRowAssetFrame(
                shape = IenListRowAssetShape.Circle,
                size = IenListRowAssetSize.Medium,
                backgroundColor = IenTheme.colors.brandWeak,
            ) {
                IenIcon(
                    imageVector = item.icon,
                    contentDescription = item.iconContentDescription,
                    tint = IenTheme.colors.onBrandWeak,
                    size = 24.dp,
                )
            }
        },
        contents = {
            Column(
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
            ) {
                IenText(
                    text = item.title,
                    style = IenTheme.typography.title3.toBold(),
                )
                IenText(
                    text = item.description,
                    style = IenTheme.typography.label2,
                    color = IenTheme.colors.textTertiary,
                )
            }
        },
        border = IenListRowBorder.None,
        verticalPadding = IenListRowPadding.ExtraSmall,
        horizontalPadding = IenListRowPadding.ExtraSmall,
    )
}
