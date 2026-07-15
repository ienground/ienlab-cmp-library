package zone.ien.utils.adaptive.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zone.ien.hig.CupertinoSegmentedControl
import zone.ien.hig.CupertinoSegmentedControlTab
import zone.ien.hig.CupertinoText
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.interactive.IenSegmentedControl
import zone.ien.utils.ui.interactive.IenSegmentedControlAlignment
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.interactive.IenSegmentedControlSize

/**
 * Material 분기에서 [IenSegmentedControl]을 사용하는 적응형 세그먼티드 컨트롤 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveSegmentedControl(
    items: List<IenSegmentedControlItem>,
    modifier: Modifier = Modifier,
    value: String? = null,
    defaultValue: String? = null,
    onChange: (String) -> Unit = {},
    size: IenSegmentedControlSize = IenSegmentedControlSize.Small,
    alignment: IenSegmentedControlAlignment = IenSegmentedControlAlignment.Fixed,
    enabled: Boolean = true,
) {
    var localValue by remember(items, defaultValue) {
        mutableStateOf(defaultValue ?: items.firstOrNull { it.enabled }?.value ?: items.firstOrNull()?.value.orEmpty())
    }
    val selectedValue = value ?: localValue

    AdaptiveWidget(
        material = {
            IenSegmentedControl(
                items = items,
                modifier = modifier,
                value = value,
                defaultValue = defaultValue,
                onChange = onChange,
                size = size,
                alignment = alignment,
                enabled = enabled,
            )
        },
        cupertino = {
            CupertinoSegmentedControl(
                selectedTabIndex = items.indexOfFirst { it.value == selectedValue }.coerceAtLeast(0),
                modifier = modifier.then(if (alignment == IenSegmentedControlAlignment.Fixed) Modifier.fillMaxWidth() else Modifier),
            ) {
                items.forEach { item ->
                    val itemEnabled = enabled && item.enabled
                    CupertinoSegmentedControlTab(
                        onClick = {
                            if (itemEnabled && item.value != selectedValue) {
                                if (value == null) {
                                    localValue = item.value
                                }
                                onChange(item.value)
                            }
                        },
                        isSelected = item.value == selectedValue,
                    ) {
                        CupertinoText(item.label)
                    }
                }
            }
        },
    )
}

/**
 * 문자열 목록으로 구성하는 인덱스 기반 적응형 세그먼티드 컨트롤 컴포저블.
 */
@Composable
fun AdaptiveSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AdaptiveSegmentedControl(
        items = items.mapIndexed { index, label ->
            IenSegmentedControlItem(
                value = index.toString(),
                label = label,
            )
        },
        modifier = modifier,
        value = selectedIndex.toString(),
        onChange = { value -> onSelectedIndexChange(value.toInt()) },
        enabled = enabled,
    )
}
