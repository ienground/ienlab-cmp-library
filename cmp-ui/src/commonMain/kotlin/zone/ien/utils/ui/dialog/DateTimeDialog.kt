package zone.ien.utils.ui.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.icon.MaterialIcons
import zone.ien.utils.ui.utils.rememberMyDatePickerState
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3DatePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long? = null,
    initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    val dialogShape = LocalDialogShape.current ?: LocalDialogProviderDefault.Shape
    val dialogBorder = LocalDialogBorder.current
    val dialogBackgroundColor = LocalDialogBackgroundColor.current ?: LocalDialogProviderDefault.BackgroundColor
    val dialogContentColor = LocalDialogContentColor.current ?: LocalDialogProviderDefault.ContentColor

    if (visible) {
        val datePickerState = rememberMyDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
            initialDisplayedMonthMillis = initialDisplayedMonthMillis,
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
            selectableDates = selectableDates
        )

        BasicAlertDialog(
            onDismissRequest = onDismiss,
            modifier = modifier.wrapContentHeight(),
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Surface(
                shape = dialogShape,
                color = dialogBackgroundColor,
                border = dialogBorder,
                modifier = Modifier
                    .requiredWidth(360.dp)
                    .heightIn(max = 568.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(containerColor = dialogBackgroundColor),
                            title = {
                                Text(
                                    text = title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                                )
                            },
                            modifier = Modifier
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(DialogButtonsPadding)
                    ) {
                        val mergedStyle = LocalTextStyle.current.merge(MaterialTheme.typography.labelLarge)
                        CompositionLocalProvider(
                            LocalContentColor provides dialogContentColor,
                            LocalTextStyle provides mergedStyle,
                        ) {
                            AlertDialogFlowRow(
                                mainAxisSpacing = DialogButtonsMainAxisSpacing,
                                crossAxisSpacing = DialogButtonsCrossAxisSpacing
                            ) {
                                TextButton(onClick = onDismiss) {
                                    Text(text = stringResource(Res.string.cancel))
                                }
                                TextButton(
                                    enabled = datePickerState.selectedDateMillis != null,
                                    onClick = { onConfirm(datePickerState.selectedDateMillis ?: 0L) }
                                ) {
                                    Text(text = stringResource(Res.string.ok))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TimePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean = false,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    if (visible) {
        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = is24Hour,
        )
        var isTimePickerDial by remember { mutableStateOf(true) }

        Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = true)) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = modifier
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    ),
            ) {
                Column {
                    Text(text = title, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp))
                    if (isTimePickerDial) {
                        TimePicker(state = timePickerState, modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp))
                    } else {
                        TimeInput(state = timePickerState, modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp))
                    }
                    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                        androidx.compose.material3.IconButton(onClick = {
                            isTimePickerDial = !isTimePickerDial
                        }) {
                            AnimatedContent(
                                targetState = if (isTimePickerDial) MaterialIcons.Keyboard else MaterialIcons.Schedule,
                                label = "time_picker_dial"
                            ) {
                                Icon(imageVector = it, contentDescription = "", tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = onDismiss) {
                            Text(text = stringResource(Res.string.cancel))
                        }
                        TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                            Text(text = stringResource(Res.string.ok))
                        }
                    }
                }
            }
        }
    }
}


@Composable
internal fun AlertDialogFlowRow(
    mainAxisSpacing: Dp,
    crossAxisSpacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(content) { measurables, constraints ->
        val sequences = mutableListOf<List<Placeable>>()
        val crossAxisSizes = mutableListOf<Int>()
        val crossAxisPositions = mutableListOf<Int>()

        var mainAxisSpace = 0
        var crossAxisSpace = 0

        val currentSequence = mutableListOf<Placeable>()
        var currentMainAxisSize = 0
        var currentCrossAxisSize = 0

        // Return whether the placeable can be added to the current sequence.
        fun canAddToCurrentSequence(placeable: Placeable) =
            currentSequence.isEmpty() ||
                    currentMainAxisSize + mainAxisSpacing.roundToPx() + placeable.width <=
                    constraints.maxWidth

        // Store current sequence information and start a new sequence.
        fun startNewSequence() {
            if (sequences.isNotEmpty()) {
                crossAxisSpace += crossAxisSpacing.roundToPx()
            }
            // Ensures that confirming actions appear above dismissive actions.
            @Suppress("ListIterator") sequences.add(0, currentSequence.toList())
            crossAxisSizes += currentCrossAxisSize
            crossAxisPositions += crossAxisSpace

            crossAxisSpace += currentCrossAxisSize
            mainAxisSpace = max(mainAxisSpace, currentMainAxisSize)

            currentSequence.clear()
            currentMainAxisSize = 0
            currentCrossAxisSize = 0
        }

        measurables.fastForEach { measurable ->
            // Ask the child for its preferred size.
            val placeable = measurable.measure(constraints)

            // Start a new sequence if there is not enough space.
            if (!canAddToCurrentSequence(placeable)) startNewSequence()

            // Add the child to the current sequence.
            if (currentSequence.isNotEmpty()) {
                currentMainAxisSize += mainAxisSpacing.roundToPx()
            }
            currentSequence.add(placeable)
            currentMainAxisSize += placeable.width
            currentCrossAxisSize = max(currentCrossAxisSize, placeable.height)
        }

        if (currentSequence.isNotEmpty()) startNewSequence()

        val mainAxisLayoutSize = max(mainAxisSpace, constraints.minWidth)

        val crossAxisLayoutSize = max(crossAxisSpace, constraints.minHeight)

        val layoutWidth = mainAxisLayoutSize

        val layoutHeight = crossAxisLayoutSize

        layout(layoutWidth, layoutHeight) {
            sequences.fastForEachIndexed { i, placeables ->
                val childrenMainAxisSizes =
                    IntArray(placeables.size) { j ->
                        placeables[j].width +
                                if (j < placeables.lastIndex) mainAxisSpacing.roundToPx() else 0
                    }
                val arrangement = Arrangement.End
                val mainAxisPositions = IntArray(childrenMainAxisSizes.size)
                with(arrangement) {
                    arrange(
                        mainAxisLayoutSize,
                        childrenMainAxisSizes,
                        layoutDirection,
                        mainAxisPositions
                    )
                }
                placeables.fastForEachIndexed { j, placeable ->
                    placeable.place(x = mainAxisPositions[j], y = crossAxisPositions[i])
                }
            }
        }
    }
}

internal val DialogButtonsPadding = PaddingValues(bottom = 8.dp, end = 6.dp)
internal val DialogButtonsMainAxisSpacing = 8.dp
internal val DialogButtonsCrossAxisSpacing = 12.dp