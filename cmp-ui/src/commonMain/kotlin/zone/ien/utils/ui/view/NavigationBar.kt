package zone.ien.utils.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Delete
import zone.ien.utils.icon.material.filled.Edit
import zone.ien.utils.icon.material.filled.Save
import zone.ien.utils.icon.material.filled.Schedule

// ─── CompositionLocals ───────────────────────────────────────────────────────

internal val LocalNavigationBarSelectedIndex = compositionLocalOf { -1 }
internal val LocalNavigationBarColors = compositionLocalOf {
    CustomNavigationBarColors(
        containerColor = Color.Unspecified,
        selectedItemBackgroundColor = Color.Unspecified,
        selectedIconColor = Color.Unspecified,
        selectedTextColor = Color.Unspecified,
        unselectedIconColor = Color.Unspecified,
        unselectedTextColor = Color.Unspecified,
    )
}

// ─── Colors ──────────────────────────────────────────────────────────────────

@Immutable
data class CustomNavigationBarColors(
    val containerColor: Color,
    val selectedItemBackgroundColor: Color,
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
)

object CustomNavigationBarDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
        selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.primary,
        selectedIconColor: Color = MaterialTheme.colorScheme.onPrimary,
        selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ) = CustomNavigationBarColors(
        containerColor = containerColor,
        selectedItemBackgroundColor = selectedItemBackgroundColor,
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
    )
}

// ─── CustomNavigationBar ─────────────────────────────────────────────────────

/**
 * CustomNavigationBar는 사용자 정의 네비게이션 바를 표시하기 위한 컴포저블입니다.
 *
 * @param selectedIndex 선택된 항목 인덱스
 * @param itemCount 항목 개수
 * @param modifier 적용할 Modifier
 * @param colors 색상
 * @param windowInsets 윈도우 인셋
 * @param content 항목 내용
 */
@Composable
fun CustomNavigationBar(
    selectedIndex: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    colors: CustomNavigationBarColors = CustomNavigationBarDefaults.colors(),
    windowInsets: WindowInsets = WindowInsets.navigationBars,
    content: @Composable RowScope.() -> Unit
) {
    val navBarPadding = windowInsets.asPaddingValues()

    CompositionLocalProvider(
        LocalNavigationBarSelectedIndex provides selectedIndex,
        LocalNavigationBarColors provides colors,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = navBarPadding.calculateBottomPadding())
        ) {
            Surface(
                color = colors.containerColor,
                shape = RoundedCornerShape(36.dp),
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(72.dp)
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    val itemWidth = if (itemCount > 0) maxWidth / itemCount else 0.dp
                    val indicatorOffset by animateDpAsState(
                        targetValue = itemWidth * selectedIndex,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "indicatorOffset"
                    )

                    // 슬라이딩 배경
                    Surface(
                        color = colors.selectedItemBackgroundColor,
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .offset(x = indicatorOffset)
                            .width(itemWidth)
                            .fillMaxHeight()
                    ) {}

                    // 아이템들
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

// ─── CustomNavigationBarItem ─────────────────────────────────────────────────

/**
 * CustomNavigationBarItem는 네비게이션 바 항목을 표시하기 위한 컴포저블입니다.
 *
 * @param index 항목 인덱스
 * @param onClick 클릭 시 호출되는 콜백 함수
 * @param icon 아이콘
 * @param label 라벨
 * @param alwaysShowLabel 항상 라벨 표시 여부
 * @param enabled 활성화 여부
 * @param modifier 적용할 Modifier
 */
@Composable
fun RowScope.CustomNavigationBarItem(
    index: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    alwaysShowLabel: Boolean = true,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = LocalNavigationBarSelectedIndex.current
    val colors = LocalNavigationBarColors.current
    val selected = index == selectedIndex

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.unselectedIconColor.copy(alpha = 0.38f)
            selected -> colors.selectedIconColor
            else -> colors.unselectedIconColor
        },
        label = "iconColor"
    )
    val textColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.unselectedTextColor.copy(alpha = 0.38f)
            selected -> colors.selectedTextColor
            else -> colors.unselectedTextColor
        },
        label = "textColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides iconColor) {
                icon()
            }
            Spacer(Modifier.height(2.dp))
            AnimatedVisibility(
                visible = alwaysShowLabel || selected,
                enter = fadeIn(spring(1.2f)) + expandVertically(spring(1.2f)),
                exit = fadeOut(spring(1.2f)) + shrinkVertically(spring(1.2f))
            ) {
                ProvideTextStyle(
                    MaterialTheme.typography.labelSmall.copy(color = textColor)
                ) {
                    label()
                }
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8)
@Composable
private fun CustomNavigationBarPreview() {
    var selectedIndex by remember { mutableStateOf(0) }

    MaterialTheme {
        CustomNavigationBar(
            selectedIndex = selectedIndex,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            CustomNavigationBarItem(
                index = 0,
                onClick = { selectedIndex = 0 },
                icon = { Icon(M3SystemIcons.Filled.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            CustomNavigationBarItem(
                index = 1,
                onClick = { selectedIndex = 1 },
                icon = { Icon(M3SystemIcons.Filled.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            CustomNavigationBarItem(
                index = 2,
                onClick = { selectedIndex = 2 },
                icon = { Icon(M3SystemIcons.Filled.Schedule, contentDescription = null) },
                label = { Text("통계") }
            )
            CustomNavigationBarItem(
                index = 3,
                onClick = { selectedIndex = 3 },
                icon = { Icon(M3SystemIcons.Filled.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8, name = "Index 1 Selected")
@Composable
private fun CustomNavigationBarPreview1() {
    MaterialTheme {
        CustomNavigationBar(
            selectedIndex = 1,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            CustomNavigationBarItem(
                index = 0,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            CustomNavigationBarItem(
                index = 1,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            CustomNavigationBarItem(
                index = 2,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Schedule, contentDescription = null) },
                label = { Text("통계") }
            )
            CustomNavigationBarItem(
                index = 3,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF0F8, name = "Disabled Item")
@Composable
private fun CustomNavigationBarPreviewDisabled() {
    MaterialTheme {
        CustomNavigationBar(
            selectedIndex = 0,
            itemCount = 4,
            windowInsets = WindowInsets(0.dp)
        ) {
            CustomNavigationBarItem(
                index = 0,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Save, contentDescription = null) },
                label = { Text("홈") }
            )
            CustomNavigationBarItem(
                index = 1,
                onClick = {},
                enabled = false,
                icon = { Icon(M3SystemIcons.Filled.Edit, contentDescription = null) },
                label = { Text("기록") }
            )
            CustomNavigationBarItem(
                index = 2,
                onClick = {},
                enabled = false,
                icon = { Icon(M3SystemIcons.Filled.Schedule, contentDescription = null) },
                label = { Text("통계") }
            )
            CustomNavigationBarItem(
                index = 3,
                onClick = {},
                icon = { Icon(M3SystemIcons.Filled.Delete, contentDescription = null) },
                label = { Text("설정") }
            )
        }
    }
}