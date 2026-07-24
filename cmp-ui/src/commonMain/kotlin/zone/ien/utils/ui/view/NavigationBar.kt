package zone.ien.utils.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Delete
import zone.ien.utils.icon.material.filled.Edit
import zone.ien.utils.icon.material.filled.Save
import zone.ien.utils.icon.material.filled.Schedule
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneGradientBrush

// ─── CompositionLocals ───────────────────────────────────────────────────────

/**
 * 현재 선택된 네비게이션 바 항목의 인덱스를 제공하는 CompositionLocal입니다.
 */
internal val LocalNavigationBarSelectedIndex = compositionLocalOf { -1 }

/**
 * 네비게이션 바 및 하위 항목에서 사용할 색상 구성을 제공하는 CompositionLocal입니다.
 */
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

/**
 * 네비게이션 바에서 각 항목의 가로 위치 정보(왼쪽 시작 지점과 너비)를 나타내는 데이터 클래스입니다.
 *
 * @property left 항목의 왼쪽 시작 지점 (Dp)
 * @property width 항목의 너비 (Dp)
 */
internal data class NavigationBarItemBounds(
    val left: Dp,
    val width: Dp,
)

/**
 * 네비게이션 바 항목의 경계 정보(위치 및 너비)를 업데이트하는 콜백 함수를 제공하는 CompositionLocal입니다.
 */
internal val LocalNavigationBarItemBoundsUpdater = compositionLocalOf<(Int, NavigationBarItemBounds) -> Unit> {
    { _, _ -> }
}

/**
 * 네비게이션 바 항목의 아이콘과 라벨 배치 방향입니다.
 */
enum class CustomNavigationBarItemDirection {
    Horizontal,
    Vertical,
}

// ─── Colors ──────────────────────────────────────────────────────────────────

/**
 * 사용자 정의 네비게이션 바([CustomNavigationBar])의 색상 구성 정보를 담는 데이터 클래스입니다.
 *
 * @property containerColor 네비게이션 바의 배경색
 * @property selectedItemBackgroundColor 선택된 항목의 배경색 (인디케이터 색상)
 * @property selectedIconColor 선택된 항목의 아이콘 색상
 * @property selectedTextColor 선택된 항목의 텍스트 색상
 * @property unselectedIconColor 선택되지 않은 항목의 아이콘 색상
 * @property unselectedTextColor 선택되지 않은 항목의 텍스트 색상
 */
@Immutable
data class CustomNavigationBarColors(
    val containerColor: Color,
    val selectedItemBackgroundColor: Color,
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
)

/**
 * 사용자 정의 네비게이션 바의 기본값 및 색상 생성을 위한 유틸리티 객체입니다.
 */
object CustomNavigationBarDefaults {
    /**
     * [CustomNavigationBar]에 적용할 색상 구성을 생성합니다.
     *
     * @param containerColor 네비게이션 바의 배경색
     * @param selectedItemBackgroundColor 선택된 항목의 배경색
     * @param selectedIconColor 선택된 항목의 아이콘 색상
     * @param selectedTextColor 선택된 항목의 텍스트 색상
     * @param unselectedIconColor 선택되지 않은 항목의 아이콘 색상
     * @param unselectedTextColor 선택되지 않은 항목의 텍스트 색상
     */
    @Composable
    fun colors(
        containerColor: Color = IenTheme.colors.brand,
        selectedItemBackgroundColor: Color = IenTheme.colors.surface,
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
    val itemBounds = remember { mutableStateMapOf<Int, NavigationBarItemBounds>() }
    val selectedBounds = itemBounds[selectedIndex]
    val indicatorOffset by animateDpAsState(
        targetValue = selectedBounds?.left ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "navigationBarIndicatorOffset",
    )
    val indicatorWidth by animateDpAsState(
        targetValue = selectedBounds?.width ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "navigationBarIndicatorWidth",
    )

    CompositionLocalProvider(
        LocalNavigationBarSelectedIndex provides selectedIndex,
        LocalNavigationBarColors provides colors,
        LocalNavigationBarItemBoundsUpdater provides { index, bounds -> itemBounds[index] = bounds },
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = navBarPadding.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                val containerShape = ContinuousCapsule()
                Surface(
                    color = Color.Transparent,
                    shape = containerShape,
                    tonalElevation = 0.dp,
                    shadowElevation = 18.dp,
                    modifier = Modifier
                        .height(78.dp)
                        .background(
                            brush = if (colors.containerColor == IenTheme.colors.brand) {
                                toneGradientBrush(IenSemanticTone.Brand)
                            } else {
                                androidx.compose.ui.graphics.SolidColor(colors.containerColor)
                            },
                            shape = containerShape,
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                    ) {
                        if (selectedBounds != null) {
                            Box(
                                modifier = Modifier
                                    .offset(x = indicatorOffset)
                                    .width(indicatorWidth)
                                    .fillMaxHeight()
                                    .background(
                                        color = colors.selectedItemBackgroundColor,
                                        shape = ContinuousCapsule(),
                                    )
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            content()
                        }
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
 * @param direction 아이콘과 라벨 배치 방향
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
    direction: CustomNavigationBarItemDirection = CustomNavigationBarItemDirection.Horizontal,
    alwaysShowLabel: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = LocalNavigationBarSelectedIndex.current
    val colors = LocalNavigationBarColors.current
    val updateItemBounds = LocalNavigationBarItemBoundsUpdater.current
    val density = LocalDensity.current
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
            .then(
                if (selected || alwaysShowLabel) Modifier else Modifier.width(48.dp)
            )
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                updateItemBounds(
                    index,
                    NavigationBarItemBounds(
                        left = with(density) { coordinates.positionInParent().x.toDp() },
                        width = with(density) { coordinates.size.width.toDp() },
                    )
                )
            }
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
        val showLabel = alwaysShowLabel || selected

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .then(
                    if (showLabel) {
                        Modifier.padding(horizontal = 24.dp)
                    } else {
                        Modifier.width(48.dp)
                    }
                )
                .fillMaxHeight()
        ) {
            when (direction) {
                CustomNavigationBarItemDirection.Horizontal -> {
                    CompositionLocalProvider(LocalContentColor provides iconColor) {
                        icon()
                    }

                    if (showLabel) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(IntrinsicSize.Min),
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            ProvideTextStyle(
                                IenTheme.typography.label2.copy(color = textColor)
                            ) {
                                label()
                            }
                        }
                    }
                }

                CustomNavigationBarItemDirection.Vertical -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CompositionLocalProvider(LocalContentColor provides iconColor) {
                            icon()
                        }

                        if (showLabel) {
                            Spacer(modifier = Modifier.height(3.dp))
                            ProvideTextStyle(
                                IenTheme.typography.label2.copy(color = textColor)
                            ) {
                                label()
                            }
                        }
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

    IenTheme {
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
    IenTheme {
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
    IenTheme {
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
