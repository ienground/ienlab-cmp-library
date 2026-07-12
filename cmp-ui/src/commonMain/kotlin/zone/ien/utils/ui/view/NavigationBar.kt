package zone.ien.utils.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Delete
import zone.ien.utils.icon.material.filled.Edit
import zone.ien.utils.icon.material.filled.Save
import zone.ien.utils.icon.material.filled.Schedule
import zone.ien.utils.ui.components.foundation.IenTheme

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
        containerColor: Color = IenTheme.colors.brand,
        selectedItemBackgroundColor: Color = MaterialTheme.colorScheme.surface,
        selectedIconColor: Color = IenTheme.colors.brand,
        selectedTextColor: Color = IenTheme.colors.brand,
        unselectedIconColor: Color = Color.White.copy(alpha = 0.72f),
        unselectedTextColor: Color = Color.White.copy(alpha = 0.72f),
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
                shape = RoundedCornerShape(999.dp),
                tonalElevation = 0.dp,
                shadowElevation = 18.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(78.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    content()
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
    alwaysShowLabel: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = LocalNavigationBarSelectedIndex.current
    val colors = LocalNavigationBarColors.current
    val selected = index == selectedIndex
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

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

    val weight by animateFloatAsState(
        targetValue = if (selected) 2.4f else 0.72f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "itemWeight"
    )
    val pressScale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.975f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "itemPressScale",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .weight(weight)
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            )
    ) {
        val pillBgColor by animateColorAsState(
            targetValue = if (selected) colors.selectedItemBackgroundColor else Color.Transparent,
            label = "pillBgColor"
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    color = pillBgColor,
                    shape = RoundedCornerShape(999.dp)
                )
                .fillMaxSize()
        ) {
            CompositionLocalProvider(LocalContentColor provides iconColor) {
                icon()
            }

            AnimatedVisibility(
                visible = alwaysShowLabel || selected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    ProvideTextStyle(
                        MaterialTheme.typography.labelMedium.copy(color = textColor)
                    ) {
                        label()
                    }
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
