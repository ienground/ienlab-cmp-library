package zone.ien.utils.ui.utils

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * 텍스트 필드 다이얼로그 설정을 나타내는 데이터 클래스
 *
 * 이 클래스는 텍스트 필드 다이얼로그 컴포넌트의 설정 옵션을 제공합니다.
 * 초기 값, 값 변경 처리, 유효성 검사, 그리고 텍스트 필드의 various UI 관련 설정을 포함합니다.
 *
 * @property initialValue 텍스트 필드의 초기 값
 * @property onValueChange 텍스트 필드 값 변경을 처리하는 함수
 * @property valid 텍스트 필드 값 유효성 검사 함수
 * @property placeholder 텍스트 필드의 placeholder 텍스트
 * @property prefix 텍스트 필드 앞에 표시할 접두사
 * @property suffix 텍스트 필드 뒤에 표시할 접미사
 * @property keyboardType 입력을 위한 키보드 유형
 * @property imeAction IME 동작
 * @property maxLines 텍스트 필드의 최대 라인 수
 * @property minLines 텍스트 필드의 최소 라인 수
 */
data class TextFieldDialogData(
    val initialValue: String = "",
    val onValueChange: (String) -> String? = { it },
    val valid: (String) -> Boolean = { true },
    val placeholder: String = "",
    val prefix: String? = null,
    val suffix: String? = null,
    val keyboardType: KeyboardType = KeyboardType.Unspecified,
    val imeAction: ImeAction = ImeAction.Unspecified,
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1
)