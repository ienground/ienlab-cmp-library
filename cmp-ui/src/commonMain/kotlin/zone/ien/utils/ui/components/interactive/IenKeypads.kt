package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

sealed interface IenKeyboardAction {
    data class Input(val text: String) : IenKeyboardAction
    data object Backspace : IenKeyboardAction
    data object Space : IenKeyboardAction
    data object Clear : IenKeyboardAction
    data object Done : IenKeyboardAction
}

enum class IenSecureKeyboardLanguage {
    English,
    Korean,
}

@Immutable
data class IenSecureKeyboardState(
    val value: String,
    val language: IenSecureKeyboardLanguage = IenSecureKeyboardLanguage.English,
    val maskValue: Boolean = true,
)

@Composable
fun IenAlphabetKeyboard(
    onAction: (IenKeyboardAction) -> Unit,
    modifier: Modifier = Modifier,
    randomized: Boolean = false,
    enabled: Boolean = true,
) {
    val rows = remember(randomized) {
        listOf(
            "qwertyuiop".map { it.toString() },
            "asdfghjkl".map { it.toString() },
            "zxcvbnm".map { it.toString() },
        ).let { rows ->
            if (randomized) rows.map { it.shuffled() } else rows
        }
    }
    IenKeyboardRows(
        rows = rows + listOf(listOf("space", "backspace", "done")),
        onKeyClick = { key ->
            when (key) {
                "space" -> onAction(IenKeyboardAction.Space)
                "backspace" -> onAction(IenKeyboardAction.Backspace)
                "done" -> onAction(IenKeyboardAction.Done)
                else -> onAction(IenKeyboardAction.Input(key))
            }
        },
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun IenNumberKeypad(
    onAction: (IenKeyboardAction) -> Unit,
    modifier: Modifier = Modifier,
    randomized: Boolean = false,
    enabled: Boolean = true,
) {
    val numbers = remember(randomized) {
        (1..9).map { it.toString() }.let { if (randomized) it.shuffled() else it }
    }
    val rows = listOf(
        numbers.subList(0, 3),
        numbers.subList(3, 6),
        numbers.subList(6, 9),
        listOf("clear", "0", "backspace"),
        listOf("done"),
    )
    IenKeyboardRows(
        rows = rows,
        onKeyClick = { key ->
            when (key) {
                "clear" -> onAction(IenKeyboardAction.Clear)
                "backspace" -> onAction(IenKeyboardAction.Backspace)
                "done" -> onAction(IenKeyboardAction.Done)
                else -> onAction(IenKeyboardAction.Input(key))
            }
        },
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun IenFullSecureKeyboard(
    state: IenSecureKeyboardState,
    onAction: (IenKeyboardAction) -> Unit,
    onLanguageChange: (IenSecureKeyboardLanguage) -> Unit,
    modifier: Modifier = Modifier,
    randomized: Boolean = true,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        IenSurface(
            modifier = Modifier.fillMaxWidth(),
            color = IenTheme.colors.surfaceWeak,
            shape = RoundedCornerShape(IenTheme.radius.default),
            border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
        ) {
            Row(
                modifier = Modifier.padding(IenTheme.spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = if (state.maskValue) "•".repeat(state.value.length) else state.value,
                    modifier = Modifier.weight(1f),
                    style = IenTheme.typography.title3,
                    color = IenTheme.colors.textPrimary,
                )
                IenTextButton(
                    text = if (state.language == IenSecureKeyboardLanguage.English) "한글" else "EN",
                    onClick = {
                        onLanguageChange(
                            if (state.language == IenSecureKeyboardLanguage.English) {
                                IenSecureKeyboardLanguage.Korean
                            } else {
                                IenSecureKeyboardLanguage.English
                            },
                        )
                    },
                )
            }
        }
        when (state.language) {
            IenSecureKeyboardLanguage.English -> IenAlphabetKeyboard(
                onAction = onAction,
                randomized = randomized,
                enabled = enabled,
            )

            IenSecureKeyboardLanguage.Korean -> IenKoreanKeyboard(
                onAction = onAction,
                randomized = randomized,
                enabled = enabled,
            )
        }
        IenNumberKeypad(
            onAction = onAction,
            randomized = randomized,
            enabled = enabled,
        )
    }
}

@Composable
private fun IenKoreanKeyboard(
    onAction: (IenKeyboardAction) -> Unit,
    modifier: Modifier = Modifier,
    randomized: Boolean = false,
    enabled: Boolean = true,
) {
    val rows = remember(randomized) {
        listOf(
            listOf("ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ"),
            listOf("ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ"),
            listOf("ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ"),
        ).let { rows ->
            if (randomized) rows.map { it.shuffled() } else rows
        }
    }
    IenKeyboardRows(
        rows = rows + listOf(listOf("space", "backspace", "done")),
        onKeyClick = { key ->
            when (key) {
                "space" -> onAction(IenKeyboardAction.Space)
                "backspace" -> onAction(IenKeyboardAction.Backspace)
                "done" -> onAction(IenKeyboardAction.Done)
                else -> onAction(IenKeyboardAction.Input(key))
            }
        },
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
private fun IenKeyboardRows(
    rows: List<List<String>>,
    onKeyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                row.forEach { key ->
                    KeyboardKey(
                        label = key.label(),
                        onClick = { onKeyClick(key) },
                        modifier = Modifier.weight(if (key == "space" || key == "done") 2f else 1f),
                        enabled = enabled,
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyboardKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenSurface(
        modifier = modifier,
        color = if (enabled) IenTheme.colors.surfaceRaised else IenTheme.colors.surfaceWeak,
        contentColor = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.sm),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Box(
            modifier = Modifier
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                .padding(vertical = IenTheme.spacing.sm),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = label,
                style = IenTheme.typography.label1,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

private fun String.label(): String = when (this) {
    "space" -> "공백"
    "backspace" -> "⌫"
    "clear" -> "초기화"
    "done" -> "완료"
    else -> this
}
