package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * 다이얼로그 버튼의 배치 방향을 정의하는 열거형 클래스입니다.
 *
 * [Horizontal]은 가로 방향 배치, [Vertical]은 세로 방향 배치를 의미합니다.
 */
enum class IenDialogButtonLayout {
    Horizontal,
    Vertical,
}

/**
 * 간단한 경고/알림을 표시하기 위한 다이얼로그 컴포저블입니다.
 *
 * @param visible 다이얼로그의 표시 여부
 * @param title 다이얼로그의 제목 텍스트
 * @param message 다이얼로그의 본문 메시지 텍스트
 * @param onDismissRequest 다이얼로그가 닫힐 때(예: 바깥 화면 클릭) 호출되는 콜백 함수
 * @param modifier 다이얼로그 프레임에 적용할 Modifier
 * @param confirmText 확인 버튼에 표시될 텍스트
 * @param onConfirmClick 확인 버튼 클릭 시 호출되는 콜백 함수
 * @param tone 다이얼로그 버튼 및 전체적인 테마 톤 (예: Brand, Success, Danger 등)
 * @param closeOnDimmerClick 바깥 어두운 배경(Dimmer)을 클릭했을 때 다이얼로그를 닫을지 여부
 * @param closeOnBackEvent 뒤로가기 버튼을 눌렀을 때 다이얼로그를 닫을지 여부
 * @param onEntered 다이얼로그가 화면에 나타날 때 호출되는 콜백 함수
 * @param onExited 다이얼로그가 화면에서 사라질 때 호출되는 콜백 함수
 */
@Composable
fun IenAlertDialog(
    visible: Boolean,
    title: String,
    message: String? = null,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(Res.string.close),
    onConfirmClick: () -> Unit = onDismissRequest,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
) {
    IenAlertDialog(
        visible = visible,
        onClose = onDismissRequest,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
        title = {
            IenAlertDialogTitle(text = title)
        },
        description = message?.let {
            {
                IenAlertDialogDescription(text = it)
            }
        },
        alertButton = {
            IenAlertDialogAlertButton(
                text = confirmText,
                onClick = onConfirmClick,
                tone = tone,
            )
        },
    )
}

/**
 * 커스텀 Composable을 사용하여 경고/알림 다이얼로그를 구성하는 컴포저블입니다.
 *
 * @param visible 다이얼로그의 표시 여부
 * @param onClose 다이얼로그를 닫을 때 호출되는 콜백 함수
 * @param title 제목 영역에 표시될 Composable
 * @param alertButton 하단 버튼 영역에 표시될 Composable
 * @param modifier 다이얼로그 프레임에 적용할 Modifier
 * @param description 본문 내용 영역에 표시될 Composable (선택 사항)
 * @param closeOnDimmerClick 바깥 어두운 배경(Dimmer)을 클릭했을 때 다이얼로그를 닫을지 여부
 * @param closeOnBackEvent 뒤로가기 버튼을 눌렀을 때 다이얼로그를 닫을지 여부
 * @param onEntered 다이얼로그가 화면에 나타날 때 호출되는 콜백 함수
 * @param onExited 다이얼로그가 화면에서 사라질 때 호출되는 콜백 함수
 */
@Composable
fun IenAlertDialog(
    visible: Boolean,
    onClose: () -> Unit,
    title: @Composable () -> Unit,
    alertButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
) {
    IenDialogFrame(
        visible = visible,
        onClose = onClose,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
    ) {
        title()
        description?.invoke()
        alertButton()
    }
}

/**
 * 사용자의 동의나 확인을 받기 위해 취소와 확인 버튼을 제공하는 확인 다이얼로그 컴포저블입니다.
 *
 * @param visible 다이얼로그의 표시 여부
 * @param title 다이얼로그의 제목 텍스트
 * @param message 다이얼로그의 본문 메시지 텍스트
 * @param onDismissRequest 취소 또는 닫기 요청 시 호출되는 콜백 함수
 * @param onConfirmClick 확인 버튼 클릭 시 호출되는 콜백 함수
 * @param modifier 다이얼로그 프레임에 적용할 Modifier
 * @param confirmText 확인 버튼에 표시될 텍스트
 * @param dismissText 취소 버튼에 표시될 텍스트
 * @param destructive 확인 버튼의 동작이 파괴적인 작업(예: 삭제)인지 여부. true일 경우 Danger 톤 적용
 * @param closeOnDimmerClick 바깥 어두운 배경(Dimmer)을 클릭했을 때 다이얼로그를 닫을지 여부
 * @param closeOnBackEvent 뒤로가기 버튼을 눌렀을 때 다이얼로그를 닫을지 여부
 * @param onEntered 다이얼로그가 화면에 나타날 때 호출되는 콜백 함수
 * @param onExited 다이얼로그가 화면에서 사라질 때 호출되는 콜백 함수
 * @param buttonLayout 다이얼로그 하단 버튼의 배치 방향 (가로 또는 세로)
 */
@Composable
fun IenConfirmDialog(
    visible: Boolean,
    title: String,
    message: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(Res.string.ok),
    dismissText: String = stringResource(Res.string.cancel),
    destructive: Boolean = false,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    IenConfirmDialog(
        visible = visible,
        onClose = onDismissRequest,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
        title = {
            IenConfirmDialogTitle(text = title)
        },
        description = message?.let {
            {
                IenConfirmDialogDescription(text = it)
            }
        },
        cancelButton = {
            IenConfirmDialogCancelButton(
                text = dismissText,
                onClick = onDismissRequest,
            )
        },
        confirmButton = {
            IenConfirmDialogConfirmButton(
                text = confirmText,
                onClick = onConfirmClick,
                tone = if (destructive) IenSemanticTone.Danger else IenSemanticTone.Brand,
            )
        },
        buttonLayout = buttonLayout,
    )
}

/**
 * 커스텀 Composable을 사용하여 확인 다이얼로그를 구성하는 컴포저블입니다.
 *
 * @param visible 다이얼로그의 표시 여부
 * @param onClose 다이얼로그를 닫을 때 호출되는 콜백 함수
 * @param title 제목 영역에 표시될 Composable
 * @param cancelButton 취소 버튼 영역에 표시될 Composable
 * @param confirmButton 확인 버튼 영역에 표시될 Composable
 * @param modifier 다이얼로그 프레임에 적용할 Modifier
 * @param description 본문 내용 영역에 표시될 Composable (선택 사항)
 * @param closeOnDimmerClick 바깥 어두운 배경(Dimmer)을 클릭했을 때 다이얼로그를 닫을지 여부
 * @param closeOnBackEvent 뒤로가기 버튼을 눌렀을 때 다이얼로그를 닫을지 여부
 * @param onEntered 다이얼로그가 화면에 나타날 때 호출되는 콜백 함수
 * @param onExited 다이얼로그가 화면에서 사라질 때 호출되는 콜백 함수
 * @param buttonLayout 다이얼로그 하단 버튼의 배치 방향 (가로 또는 세로)
 */
@Composable
fun IenConfirmDialog(
    visible: Boolean,
    onClose: () -> Unit,
    title: @Composable () -> Unit,
    cancelButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    closeOnDimmerClick: Boolean = true,
    closeOnBackEvent: Boolean = true,
    onEntered: (() -> Unit)? = null,
    onExited: (() -> Unit)? = null,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    IenDialogFrame(
        visible = visible,
        onClose = onClose,
        modifier = modifier,
        closeOnDimmerClick = closeOnDimmerClick,
        closeOnBackEvent = closeOnBackEvent,
        onEntered = onEntered,
        onExited = onExited,
    ) {
        title()
        description?.invoke()
        when (buttonLayout) {
            IenDialogButtonLayout.Horizontal -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    cancelButton()
                }
                Box(modifier = Modifier.weight(1f)) {
                    confirmButton()
                }
            }
            IenDialogButtonLayout.Vertical -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            ) {
                confirmButton()
                cancelButton()
            }
        }
    }
}

/**
 * 다이얼로그에서 사용하는 표준 제목 컴포저블입니다.
 *
 * @param text 표시할 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param color 텍스트 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenAlertDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenText(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        textAlign = TextAlign.Center,
    )
}

/**
 * 확인 다이얼로그에서 사용하는 표준 제목 컴포저블입니다.
 *
 * @param text 표시할 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param color 텍스트 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenConfirmDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenAlertDialogTitle(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
    )
}

/**
 * 경고 다이얼로그에서 사용하는 본문 설명 텍스트 컴포저블입니다.
 *
 * @param text 표시할 설명 텍스트
 * @param modifier 적용할 Modifier
 * @param color 텍스트 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenAlertDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = IenTheme.typography.body2,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    IenText(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        textAlign = TextAlign.Center,
    )
}

/**
 * 확인 다이얼로그에서 사용하는 본문 설명 텍스트 컴포저블입니다.
 *
 * @param text 표시할 설명 텍스트
 * @param modifier 적용할 Modifier
 * @param color 텍스트 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenConfirmDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = IenTheme.typography.body2,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    IenAlertDialogDescription(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
    )
}

/**
 * 경고 다이얼로그에서 사용하는 표준 하단 경고/확인 버튼 컴포저블입니다.
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick BUTTON 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param tone 버튼의 의미 색상 톤
 * @param size 버튼의 크기
 * @param variant 버튼의 형태 종류 (예: Clear 등)
 */
@Composable
fun IenAlertDialogAlertButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
) {
    IenTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        tone = tone,
        variant = variant,
    )
}

/**
 * 확인 다이얼로그에서 사용하는 표준 취소 버튼 컴포저블입니다.
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param tone 버튼의 의미 색상 톤
 * @param variant 버튼의 형태 종류 (예: Weak 등)
 * @param size 버튼의 크기
 */
@Composable
fun IenConfirmDialogCancelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    size: IenButtonSize = IenButtonSize.Large,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        variant = variant,
        tone = tone,
        display = IenButtonDisplay.Block,
    )
}

/**
 * 확인 다이얼로그에서 사용하는 표준 확인 버튼 컴포저블입니다.
 *
 * @param text 버튼에 표시할 텍스트
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param tone 버튼의 의미 색상 톤
 * @param variant 버튼의 형태 종류 (예: Fill 등)
 * @param size 버튼의 크기
 */
@Composable
fun IenConfirmDialogConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    size: IenButtonSize = IenButtonSize.Large,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        size = size,
        variant = variant,
        tone = tone,
        display = IenButtonDisplay.Block,
    )
}

@Composable
private fun IenDialogFrame(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier,
    closeOnDimmerClick: Boolean,
    closeOnBackEvent: Boolean,
    onEntered: (() -> Unit)?,
    onExited: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(visible) {
        if (visible) {
            onEntered?.invoke()
        } else {
            onExited?.invoke()
        }
    }
    if (!visible) return
    Dialog(
        onDismissRequest = {
            if (closeOnDimmerClick || closeOnBackEvent) {
                onClose()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = closeOnBackEvent,
            dismissOnClickOutside = closeOnDimmerClick,
        ),
    ) {
        IenSurface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp),
            color = IenTheme.colors.surfaceRaised,
            shape = RoundedCornerShape(IenTheme.radius.xl),
            tonalElevation = IenTheme.elevation.overlay,
        ) {
            Column(
                modifier = Modifier
                    .padding(IenTheme.spacing.lg)
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                content = content,
            )
        }
    }
}
