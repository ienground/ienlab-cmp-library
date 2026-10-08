package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.chat_message_placeholder
import zone.ien.utils.cmp_ui.generated.resources.chat_send
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenFab
import zone.ien.utils.ui.interactive.IenFabSize
import zone.ien.utils.ui.interactive.IenFieldStatus
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.primitives.IenIcon

/**
 * 메시지 입력창과 전송 버튼을 조합한 채팅방 하단 바입니다.
 * [IenScaffold]의 bottomBar 슬롯에 배치할 수 있습니다.
 * 입력값, 첨부 상태, 실제 전송과 전송 성공 후 입력값 초기화는 호출자가 관리합니다.
 * 입력 줄이 늘어나도 한 줄 높이의 모서리 반경을 유지하며, 버튼은 하단에 정렬됩니다.
 *
 * @param value 입력한 메시지
 * @param onValueChange 메시지가 변경될 때 호출되는 콜백
 * @param onSend 전송 버튼 또는 키보드 전송 액션을 실행할 때 호출되는 콜백
 * @param modifier 안전 영역과 외부 여백을 포함한 하단 바 전체에 적용할 Modifier
 * @param placeholder 비어 있는 입력창에 표시할 안내 문구
 * @param leadingContent 입력창 앞에 배치할 첨부 버튼 등의 선택적인 콘텐츠
 * @param trailingContent 입력창 뒤, 전송 FAB 앞에 배치할 선택적인 콘텐츠
 * @param inputState 입력창의 활성화, 읽기 전용 및 오류 상태
 * @param sendState 전송 버튼의 활성화 및 로딩 상태. 기본값은 공백 외 텍스트가 있을 때만 활성화됩니다.
 * 첨부만 전송할 수 있는 경우 호출자가 enabled를 true로 지정할 수 있습니다.
 * @param maxLines 입력창이 표시할 최대 줄 수. 초과한 내용은 입력창 안에서 스크롤됩니다.
 * @param keyboardOptions 키보드 설정. 기본 전송 액션을 변경해 줄바꿈 등의 동작을 선택할 수 있습니다.
 * @param windowInsets 하단 바 표면 바깥에 적용하고 소비할 안전 영역 인셋
 * @param sendButtonContent 전송 FAB 안에 표시할 콘텐츠. 기본값은 위쪽 화살표 아이콘입니다.
 */
@Composable
fun IenChatBottomBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.chat_message_placeholder),
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    inputState: IenTextFieldState = IenTextFieldState(),
    sendState: IenButtonState = IenButtonState(enabled = value.isNotBlank()),
    maxLines: Int = 4,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Send,
    ),
    windowInsets: WindowInsets = IenBottomBarDefaults.windowInsets,
    sendButtonContent: @Composable () -> Unit = {
        IenIcon(
            imageVector = SystemIcons.ArrowDropUp,
            contentDescription = stringResource(Res.string.chat_send),
        )
    },
) {
    val actionHeight = 56.dp
    val cornerRadius = maxOf(
        IenBottomBarDefaults.ContentHeight,
        actionHeight + IenTheme.spacing.xs * 2,
    ) / 2
    val send: () -> Unit = {
        if (sendState.enabled && !sendState.loading) {
            onSend()
        }
    }

    IenBottomBar(
        modifier = modifier,
        windowInsets = windowInsets,
        shape = ContinuousRoundedRectangle(cornerRadius),
    ) {
        if (leadingContent != null) {
            Box(
                modifier = Modifier.align(Alignment.Bottom).heightIn(min = actionHeight),
                contentAlignment = Alignment.Center,
            ) {
                leadingContent()
            }
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .clipToBounds()
                .semantics {
                    if (inputState.status is IenFieldStatus.Error) {
                        error(inputState.status.message)
                    }
                },
            enabled = inputState.enabled,
            readOnly = inputState.readOnly,
            singleLine = false,
            minLines = 1,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onSend = { send() }),
            textStyle = IenTheme.typography.body2.copy(color = IenTheme.colors.textPrimary),
            cursorBrush = SolidColor(IenTheme.colors.brand),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textTertiary,
                        )
                    }
                    innerTextField()
                }
            },
        )
        if (trailingContent != null) {
            Box(
                modifier = Modifier.align(Alignment.Bottom).heightIn(min = actionHeight),
                contentAlignment = Alignment.Center,
            ) {
                trailingContent()
            }
        }
        IenFab(
            onClick = send,
            modifier = Modifier.align(Alignment.Bottom),
            size = IenFabSize.Regular,
            state = sendState,
            content = sendButtonContent,
        )
    }
}
