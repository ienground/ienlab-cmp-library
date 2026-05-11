package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 날짜 선택 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param initialSelectedDateMillis 초기 선택된 날짜 (밀리초)
 * @param initialDisplayedMonthMillis 초기 표시되는 월 (밀리초)
 * @param yearRange 연도 범위
 * @param initialDisplayMode 초기 표시 방식
 * @param selectableDates 선택 가능 날짜
 * @param title 제목
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수 (선택된 날짜 밀리초)
 * @return 날짜 선택 다이얼로그 컴포저블
 */
@Composable
expect fun DatePickerDialog(
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
)

/**
 * 시간 선택 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param initialHour 초기 선택된 시간
 * @param initialMinute 초기 선택된 분
 * @param is24Hour 24시간 형식 여부 (iOS에서는 시스템 설정에 따라 달라짐)
 * @param title 제목
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수 (시, 분)
 * @return 시간 선택 다이얼로그 컴포저블
 */
@Composable
expect fun TimePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean = false,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
)