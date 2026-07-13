package zone.ien.utils.ui.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.clear
import zone.ien.utils.cmp_ui.generated.resources.done
import zone.ien.utils.cmp_ui.generated.resources.english
import zone.ien.utils.cmp_ui.generated.resources.korean
import zone.ien.utils.cmp_ui.generated.resources.space
import zone.ien.utils.cmp_ui.generated.resources.special
import zone.ien.utils.cmp_ui.generated.resources.submit
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import kotlin.random.Random

/**
 * 키패드 및 키보드 입력 시 발생하는 개별 액션을 정의하는 실드 인터페이스.
 */
sealed interface IenKeyboardAction {
    /** 일반 텍스트 입력 액션 */
    data class Input(val text: String) : IenKeyboardAction
    /** 백스페이스(한 글자 지우기) 액션 */
    data object Backspace : IenKeyboardAction
    /** 띄어쓰기(공백 추가) 액션 */
    data object Space : IenKeyboardAction
    /** 입력 텍스트 전체 지우기 액션 */
    data object Clear : IenKeyboardAction
    /** 입력 완료(확인) 액션 */
    data object Done : IenKeyboardAction
}

/**
 * 보안 키보드([IenFullSecureKeyboard])에서 사용되는 입력 언어 종류 열거형 클래스.
 */
enum class IenSecureKeyboardLanguage {
    /** 영문 입력 모드 */
    English,
    /** 한글 입력 모드 */
    Korean,
}

/**
 * 보안 키보드([IenFullSecureKeyboard])의 현재 텍스트 입력 상태를 관리하는 데이터 클래스.
 *
 * @property value 현재 입력된 평문 텍스트 문자열.
 * @property language 현재 선택된 입력 언어 모드.
 * @property maskValue 텍스트를 마스킹하여 감출지 여부. 기본값은 true.
 */
@Immutable
data class IenSecureKeyboardState(
    val value: String,
    val language: IenSecureKeyboardLanguage = IenSecureKeyboardLanguage.English,
    val maskValue: Boolean = true,
)

/**
 * 풀 보안 키패드([IenFullSecureKeypad])의 개별 키 정보를 정의하는 데이터 클래스.
 *
 * @property value 키가 눌렸을 때 전달되는 실제 문자 값.
 * @property label 키 겉면에 기본으로 노출될 텍스트 라벨.
 * @property secondaryLabel 키 우측 등에 작게 보조로 노출될 추가 텍스트 라벨 (예: 한글/영문 병기).
 */
@Immutable
data class IenFullSecureKey(
    val value: String,
    val label: String = value,
    val secondaryLabel: String? = null,
)

/**
 * 풀 보안 키패드([IenFullSecureKeypad]) 내 무작위로 섞이는 빈 칸 셀의 상태 정보를 관리하는 상태 클래스.
 */
@Stable
class IenFullSecureKeypadState internal constructor(
    private val initialSeed: Int,
) {
    private var seed by mutableStateOf(initialSeed)

    fun reorderEmptyCells() {
        seed = Random.nextInt()
    }

    internal fun emptyCellIndexes(
        rowId: String,
        totalCells: Int,
        keyCount: Int,
    ): Set<Int> {
        val emptyCount = (totalCells - keyCount).coerceAtLeast(0)
        if (emptyCount == 0) return emptySet()
        return (0 until totalCells)
            .shuffled(Random(seed xor rowId.hashCode()))
            .take(emptyCount)
            .toSet()
    }
}

/**
 * 풀 보안 키패드([IenFullSecureKeypad])의 무작위 셀 섞기 상태 객체인 [IenFullSecureKeypadState]를 기억하고 생성하는 헬퍼 컴포저블.
 */
@Composable
fun rememberIenFullSecureKeypadState(): IenFullSecureKeypadState = remember {
    IenFullSecureKeypadState(Random.nextInt())
}

/**
 * [IenAlphabetKeypad]의 기본 값(알파벳 목록)을 제공하는 설정 객체.
 */
object IenAlphabetKeypadDefaults {
    val Alphabets: List<String> = ('A'..'Z').map { it.toString() }
}

/**
 * [IenNumberKeypad]의 기본 값(숫자 목록)을 제공하는 설정 객체.
 */
object IenNumberKeypadDefaults {
    val Numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
}

/**
 * [IenFullSecureKeypad]의 기본 키 맵핑 정보들을 제공하는 설정 객체.
 */
object IenFullSecureKeypadDefaults {
    val NumberKeys: List<IenFullSecureKey> = (1..9).map { IenFullSecureKey(it.toString()) } +
        IenFullSecureKey("0")
    val FirstAlphabetRow: List<IenFullSecureKey> = listOf(
        IenFullSecureKey("q", secondaryLabel = "ㅂ"),
        IenFullSecureKey("w", secondaryLabel = "ㅈ"),
        IenFullSecureKey("e", secondaryLabel = "ㄷ"),
        IenFullSecureKey("r", secondaryLabel = "ㄱ"),
        IenFullSecureKey("t", secondaryLabel = "ㅅ"),
        IenFullSecureKey("y", secondaryLabel = "ㅛ"),
        IenFullSecureKey("u", secondaryLabel = "ㅕ"),
        IenFullSecureKey("i", secondaryLabel = "ㅑ"),
        IenFullSecureKey("o", secondaryLabel = "ㅐ"),
        IenFullSecureKey("p", secondaryLabel = "ㅔ"),
    )
    val SecondAlphabetRow: List<IenFullSecureKey> = listOf(
        IenFullSecureKey("a", secondaryLabel = "ㅁ"),
        IenFullSecureKey("s", secondaryLabel = "ㄴ"),
        IenFullSecureKey("d", secondaryLabel = "ㅇ"),
        IenFullSecureKey("f", secondaryLabel = "ㄹ"),
        IenFullSecureKey("g", secondaryLabel = "ㅎ"),
        IenFullSecureKey("h", secondaryLabel = "ㅗ"),
        IenFullSecureKey("j", secondaryLabel = "ㅓ"),
        IenFullSecureKey("k", secondaryLabel = "ㅏ"),
        IenFullSecureKey("l", secondaryLabel = "ㅣ"),
    )
    val ThirdAlphabetRow: List<IenFullSecureKey> = listOf(
        IenFullSecureKey("z", secondaryLabel = "ㅋ"),
        IenFullSecureKey("x", secondaryLabel = "ㅌ"),
        IenFullSecureKey("c", secondaryLabel = "ㅊ"),
        IenFullSecureKey("v", secondaryLabel = "ㅍ"),
        IenFullSecureKey("b", secondaryLabel = "ㅠ"),
        IenFullSecureKey("n", secondaryLabel = "ㅜ"),
        IenFullSecureKey("m", secondaryLabel = "ㅡ"),
    )
}

/**
 * 영문 알파벳 목록을 격자 형태로 나열하여 입력을 받을 수 있게 해주는 알파벳 키패드 컴포저블.
 *
 * @param onKeyClick 키가 눌렸을 때 눌린 글자 값을 전달하는 콜백 함수.
 * @param onBackspaceClick 백스페이스가 눌렸을 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param alphabets 키패드에 배치할 알파벳 문자 목록. 기본값은 A부터 Z까지입니다.
 * @param enabled 활성화 여부.
 * @param columns 가로 방향 열의 개수. 기본값은 3.
 * @param keyHeight 개별 키의 높이 규격. 기본값은 56.dp.
 */
@Composable
fun IenAlphabetKeypad(
    onKeyClick: (value: String) -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    alphabets: List<String> = IenAlphabetKeypadDefaults.Alphabets,
    enabled: Boolean = true,
    columns: Int = 3,
    keyHeight: Dp = 56.dp,
) {
    val safeColumns = columns.coerceAtLeast(1)
    val rows = remember(alphabets, safeColumns) {
        alphabets.filter { it.isNotBlank() }.chunked(safeColumns)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                repeat(safeColumns) { index ->
                    val value = row.getOrNull(index)
                    if (value == null) {
                        Box(modifier = Modifier.weight(1f))
                    } else {
                        IenKeypadKey(
                            label = value,
                            onClick = { onKeyClick(value) },
                            modifier = Modifier.weight(1f),
                            enabled = enabled,
                            height = keyHeight,
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            repeat(safeColumns - 1) {
                Box(modifier = Modifier.weight(1f))
            }
            IenKeypadKey(
                label = "⌫",
                onClick = onBackspaceClick,
                modifier = Modifier.weight(1f),
                enabled = enabled,
                height = keyHeight,
            )
        }
    }
}

/**
 * 숫자를 입력할 수 있는 3x4 격자 형태의 숫자 전용 키패드 컴포저블.
 *
 * @param onKeyClick 숫자 키가 눌렸을 때 눌린 숫자 문자 값을 전달하는 콜백 함수.
 * @param onBackspaceClick 백스페이스가 눌렸을 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param numbers 키패드에 표시할 숫자 목록.
 * @param secure 보안 모드 적용 여부. true일 경우 터치 흔적 방지를 위해 가짜 노이즈 키 클릭 콜백을 유발합니다.
 * @param enabled 활성화 여부.
 * @param keyHeight 개별 키의 높이 규격. 기본값은 64.dp.
 * @param onSecureNoiseKeyClick 보안 모드 활성화 시 가짜 클릭 효과를 주기 위해 선택된 임의의 노이즈 키 값들을 전달하는 콜백 함수.
 */
@Composable
fun IenNumberKeypad(
    onKeyClick: (value: String) -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    numbers: List<Int> = IenNumberKeypadDefaults.Numbers,
    secure: Boolean = false,
    enabled: Boolean = true,
    keyHeight: Dp = 64.dp,
    onSecureNoiseKeyClick: (value: String) -> Unit = {},
) {
    val safeNumbers = remember(numbers) {
        numbers.filter { it in 0..9 }.ifEmpty { IenNumberKeypadDefaults.Numbers }
    }
    val rows = remember(safeNumbers) {
        buildNumberKeypadRows(safeNumbers)
    }
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surfaceWeak,
        shape = RoundedCornerShape(IenTheme.radius.default),
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    row.forEach { cell ->
                        when {
                            cell == null -> Box(modifier = Modifier.weight(1f))
                            cell.isBackspace -> IenNumberKeypadActionKey(
                                label = "⌫",
                                onClick = onBackspaceClick,
                                modifier = Modifier.weight(1f),
                                enabled = enabled,
                                height = keyHeight,
                            )
                            cell.number != null -> IenNumberKeypadDigitKey(
                                number = cell.number,
                                onClick = {
                                    onKeyClick(cell.number.toString())
                                    if (secure) {
                                        findSecureNoiseNumbers(
                                            number = cell.number,
                                            rows = rows,
                                        ).forEach { noise ->
                                            onSecureNoiseKeyClick(noise.toString())
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = enabled,
                                height = keyHeight,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 입력 순서 해킹 방지 등을 위해 알파벳, 숫자, 빈 칸 셀이 무작위로 배치되는 고보안성 풀 보안 키패드 컴포저블.
 *
 * @param onKeyClick 키가 눌렸을 때 해당 키의 문자 값을 전달하는 콜백 함수.
 * @param onBackspaceClick 백스페이스가 눌렸을 때 호출되는 콜백 함수.
 * @param onSpaceClick 스페이스바가 눌렸을 때 호출되는 콜백 함수.
 * @param onSubmit 확인/전송 버튼이 눌렸을 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param state 키패드의 무작위 셀 배치 및 재생성 시드를 관리하는 상태 객체 ([IenFullSecureKeypadState]).
 * @param submitDisabled 전송 버튼의 비활성화 여부.
 * @param submitButtonText 전송 버튼에 표시할 텍스트.
 * @param enabled 활성화 여부.
 * @param onSpecialClick 특수 기호 전환 등 커스텀 액션을 유발할 추가 기능 키의 클릭 콜백 함수 (null일 경우 비활성화).
 * @param keyHeight 개별 키의 높이 규격. 기본값은 44.dp.
 */
@Composable
fun IenFullSecureKeypad(
    onKeyClick: (value: String) -> Unit,
    onBackspaceClick: () -> Unit,
    onSpaceClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenFullSecureKeypadState = rememberIenFullSecureKeypadState(),
    submitDisabled: Boolean = false,
    submitButtonText: String = stringResource(Res.string.submit),
    enabled: Boolean = true,
    onSpecialClick: (() -> Unit)? = null,
    keyHeight: Dp = 44.dp,
) {
    val totalCells = 10
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surfaceWeak,
        shape = RoundedCornerShape(IenTheme.radius.default),
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.xs),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            IenFullSecureKeypadRow(
                keys = IenFullSecureKeypadDefaults.NumberKeys,
                rowId = "numbers",
                totalCells = totalCells,
                state = state,
                enabled = enabled,
                keyHeight = keyHeight,
                onKeyClick = onKeyClick,
            )
            IenFullSecureKeypadRow(
                keys = IenFullSecureKeypadDefaults.FirstAlphabetRow,
                rowId = "firstAlphabet",
                totalCells = totalCells,
                state = state,
                enabled = enabled,
                keyHeight = keyHeight,
                onKeyClick = onKeyClick,
            )
            IenFullSecureKeypadRow(
                keys = IenFullSecureKeypadDefaults.SecondAlphabetRow,
                rowId = "secondAlphabet",
                totalCells = totalCells,
                state = state,
                enabled = enabled,
                keyHeight = keyHeight,
                onKeyClick = onKeyClick,
            )
            IenFullSecureKeypadRow(
                keys = IenFullSecureKeypadDefaults.ThirdAlphabetRow,
                rowId = "thirdAlphabet",
                totalCells = totalCells,
                state = state,
                enabled = enabled,
                keyHeight = keyHeight,
                onKeyClick = onKeyClick,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                IenFullSecureActionKey(
                    label = stringResource(Res.string.special),
                    onClick = { onSpecialClick?.invoke() },
                    modifier = Modifier.weight(1.2f),
                    enabled = enabled && onSpecialClick != null,
                    height = keyHeight,
                )
                IenFullSecureActionKey(
                    label = "Space",
                    onClick = onSpaceClick,
                    modifier = Modifier.weight(2.4f),
                    enabled = enabled,
                    height = keyHeight,
                )
                IenFullSecureActionKey(
                    label = "⌫",
                    onClick = onBackspaceClick,
                    modifier = Modifier.weight(1.2f),
                    enabled = enabled,
                    height = keyHeight,
                )
                IenFullSecureActionKey(
                    label = submitButtonText,
                    onClick = onSubmit,
                    modifier = Modifier.weight(2.4f),
                    enabled = enabled && !submitDisabled,
                    height = keyHeight,
                    filled = true,
                )
            }
        }
    }
}

/**
 * 알파벳 쿼티 자판 배열 형태로 구성되며, 띄어쓰기 및 백스페이스 처리를 포함하는 통합 키보드 컴포저블.
 *
 * @param onAction 키 입력에 따라 유발되는 통합 액션 ([IenKeyboardAction]) 전달 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param randomized 키 배열 순서를 무작위로 섞을지 여부.
 * @param enabled 활성화 여부.
 */
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

/**
 * 완료(Done) 및 지우기(Clear) 기능을 통합하여 하나의 액션 콜백으로 제어할 수 있는 숫자 키패드 컴포저블.
 *
 * @param onAction 키 입력에 따라 유발되는 통합 액션 ([IenKeyboardAction]) 전달 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param randomized 숫자 키 배열 순서를 무작위로 섞을지 여부.
 * @param enabled 활성화 여부.
 */
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

/**
 * 보안 텍스트 입력을 위해 입력 필드 마스킹, 영/한 언어 전환 및 무작위 숫자 배열 키패드를 한 번에 제공하는 통합 보안 키보드 컴포저블.
 *
 * @param state 현재 입력 정보 및 노출 설정을 포함하는 보안 키보드 상태 객체 ([IenSecureKeyboardState]).
 * @param onAction 키 입력에 따라 유발되는 통합 액션 ([IenKeyboardAction]) 전달 콜백 함수.
 * @param onLanguageChange 입력 언어가 전환될 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param randomized 키배열 무작위 셔플 활성화 여부. 기본값은 true.
 * @param enabled 활성화 여부.
 */
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
                    text = stringResource(if (state.language == IenSecureKeyboardLanguage.English) Res.string.korean else Res.string.english),
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

private data class IenNumberKeypadCell(
    val number: Int? = null,
    val isBackspace: Boolean = false,
)

private fun buildNumberKeypadRows(numbers: List<Int>): List<List<IenNumberKeypadCell?>> {
    val rows = numbers.map { IenNumberKeypadCell(number = it) }.chunked(3).map { row ->
        row.map<IenNumberKeypadCell, IenNumberKeypadCell?> { it }
    }.toMutableList()
    val lastRow = rows.removeLastOrNull()
    if (lastRow == null) {
        rows += listOf(null, null, IenNumberKeypadCell(isBackspace = true))
        return rows
    }
    if (lastRow.size == 3) {
        rows += lastRow
        rows += listOf(null, null, IenNumberKeypadCell(isBackspace = true))
        return rows
    }
    val paddedLastRow = when (lastRow.size) {
        1 -> listOf(null, lastRow.first(), IenNumberKeypadCell(isBackspace = true))
        2 -> listOf(lastRow[0], lastRow[1], IenNumberKeypadCell(isBackspace = true))
        else -> lastRow
    }
    rows += paddedLastRow
    return rows
}

private fun findSecureNoiseNumbers(
    number: Int,
    rows: List<List<IenNumberKeypadCell?>>,
): List<Int> {
    val positions = rows.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { columnIndex, cell ->
            cell?.number?.let { value ->
                value to (rowIndex to columnIndex)
            }
        }
    }.toMap()
    val selectedPosition = positions[number] ?: return emptyList()
    val blockedPositions = setOf(
        selectedPosition,
        selectedPosition.first - 1 to selectedPosition.second,
        selectedPosition.first + 1 to selectedPosition.second,
        selectedPosition.first to selectedPosition.second - 1,
        selectedPosition.first to selectedPosition.second + 1,
    )
    return positions
        .filterValues { it !in blockedPositions }
        .keys
        .shuffled()
        .take(2)
}

@Composable
private fun IenNumberKeypadDigitKey(
    number: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 64.dp,
) {
    IenSurface(
        modifier = modifier,
        color = if (enabled) IenTheme.colors.surfaceRaised else IenTheme.colors.surface,
        contentColor = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.default),
        tonalElevation = if (enabled) IenTheme.elevation.raised else IenTheme.elevation.none,
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = number.toString(),
                style = IenTheme.typography.title2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
private fun IenNumberKeypadActionKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 64.dp,
) {
    IenSurface(
        modifier = modifier,
        color = if (enabled) IenTheme.colors.surfaceRaised else IenTheme.colors.surface,
        contentColor = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.default),
        tonalElevation = if (enabled) IenTheme.elevation.raised else IenTheme.elevation.none,
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = label,
                style = IenTheme.typography.title3,
                color = if (enabled) IenTheme.colors.textSecondary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
private fun IenFullSecureKeypadRow(
    keys: List<IenFullSecureKey>,
    rowId: String,
    totalCells: Int,
    state: IenFullSecureKeypadState,
    enabled: Boolean,
    keyHeight: Dp,
    onKeyClick: (String) -> Unit,
) {
    val emptyIndexes = remember(keys, rowId, totalCells, state.emptyCellIndexes(rowId, totalCells, keys.size)) {
        state.emptyCellIndexes(rowId, totalCells, keys.size)
    }
    var keyIndex = 0
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        repeat(totalCells) { cellIndex ->
            val key = if (cellIndex in emptyIndexes) {
                null
            } else {
                keys.getOrNull(keyIndex).also {
                    if (it != null) keyIndex += 1
                }
            }
            if (key == null) {
                Box(modifier = Modifier.weight(1f))
            } else {
                IenFullSecureInputKey(
                    key = key,
                    onClick = { onKeyClick(key.value) },
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    height = keyHeight,
                )
            }
        }
    }
}

@Composable
private fun IenFullSecureInputKey(
    key: IenFullSecureKey,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 44.dp,
) {
    IenSurface(
        modifier = modifier,
        color = if (enabled) IenTheme.colors.surfaceRaised else IenTheme.colors.surface,
        contentColor = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.sm),
        tonalElevation = if (enabled) IenTheme.elevation.raised else IenTheme.elevation.none,
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = key.label,
                    style = IenTheme.typography.label1,
                    color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
                )
                key.secondaryLabel?.let {
                    IenText(
                        text = it,
                        style = IenTheme.typography.caption,
                        color = if (enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled,
                    )
                }
            }
        }
    }
}

@Composable
private fun IenFullSecureActionKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 44.dp,
    filled: Boolean = false,
) {
    val background: Color = when {
        filled && enabled -> IenTheme.colors.brand
        enabled -> IenTheme.colors.surfaceRaised
        else -> IenTheme.colors.surface
    }
    val content: Color = when {
        filled && enabled -> IenTheme.colors.onBrand
        enabled -> IenTheme.colors.textPrimary
        else -> IenTheme.colors.textDisabled
    }
    IenSurface(
        modifier = modifier,
        color = background,
        contentColor = content,
        shape = RoundedCornerShape(IenTheme.radius.sm),
        tonalElevation = if (enabled) IenTheme.elevation.raised else IenTheme.elevation.none,
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = label,
                style = IenTheme.typography.label1.copy(fontWeight = FontWeight.Bold),
                color = content,
            )
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

@Composable
private fun IenKeypadKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 56.dp,
) {
    IenSurface(
        modifier = modifier,
        color = if (enabled) IenTheme.colors.surfaceRaised else IenTheme.colors.surfaceWeak,
        contentColor = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.default),
        tonalElevation = if (enabled) IenTheme.elevation.raised else IenTheme.elevation.none,
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = label,
                style = IenTheme.typography.title3,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
private fun String.label(): String = when (this) {
    "space" -> stringResource(Res.string.space)
    "backspace" -> "⌫"
    "clear" -> stringResource(Res.string.clear)
    "done" -> stringResource(Res.string.done)
    else -> this
}
