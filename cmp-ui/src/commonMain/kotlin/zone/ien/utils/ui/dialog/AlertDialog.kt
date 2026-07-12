package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.components.composite.IenAlertDialog
import zone.ien.utils.ui.components.composite.IenAlertDialogDescription
import zone.ien.utils.ui.components.composite.IenAlertDialogTitle
import zone.ien.utils.ui.components.composite.IenConfirmDialog
import zone.ien.utils.ui.components.composite.IenConfirmDialogCancelButton
import zone.ien.utils.ui.components.composite.IenDialogButtonLayout
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonState
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.components.interactive.IenTextButton

/**
 * M3BaseAlertDialog는 AlertDialog의 기본 구조를 정의하는 컴포저블로, IEN 다이얼로그 프레임을 사용합니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param buttons 다이얼로그의 버튼을 나타내는 Composable
 */
@Composable
fun M3BaseAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    onDismiss: () -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    M3IenAlertDialogFrame(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss,
        tone = IenSemanticTone.Brand,
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                content = buttons,
            )
        },
    )
}

/**
 * M3AlertDialog은 간단한 확인/취소 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textDismiss 취소 버튼의 텍스트
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param tone 버튼과 아이콘에 적용할 의미 색상
 * @param isDestructive true이면 Danger 톤으로 표시
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.close),
    onDismiss: (() -> Unit)?,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    isDestructive: Boolean = false,
) {
    val resolvedTone = M3AlertDialogTone(tone = tone, isDestructive = isDestructive)
    M3IenAlertDialogFrame(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss ?: {},
        tone = resolvedTone,
        actions = {
            onDismiss?.let {
                IenTextButton(
                    text = textDismiss,
                    onClick = it,
                    modifier = Modifier.fillMaxWidth(),
                    tone = resolvedTone,
                )
            }
        },
    )
}

/**
 * M3AlertDialog은 확인/취소 다이얼로그를 제공하는 컴포저블로, 확인 버튼이 있는 형태입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textDismiss 취소 버튼의 텍스트
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param textConfirm 확인 버튼의 텍스트
 * @param onConfirm 확인 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledConfirm 확인 버튼의 활성화 여부
 * @param tone 확인 버튼과 아이콘에 적용할 의미 색상
 * @param isDestructive true이면 확인 버튼을 Danger 톤으로 표시
 * @param buttonLayout 확인/취소 버튼의 배치 방향
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    isDestructive: Boolean = false,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    val resolvedTone = M3AlertDialogTone(tone = tone, isDestructive = isDestructive)
    IenConfirmDialog(
        visible = visible,
        onClose = onDismiss,
        modifier = modifier,
        title = {
            M3AlertDialogHeader(
                icon = icon,
                title = title,
                tone = resolvedTone,
            )
        },
        description = message?.let {
            {
                IenAlertDialogDescription(text = it)
            }
        },
        cancelButton = {
            IenConfirmDialogCancelButton(
                text = textDismiss,
                onClick = onDismiss,
            )
        },
        confirmButton = {
            M3IenDialogButton(
                text = textConfirm,
                onClick = onConfirm,
                enabled = enabledConfirm,
                tone = resolvedTone,
                variant = IenButtonVariant.Fill,
            )
        },
        buttonLayout = buttonLayout,
    )
}

/**
 * M3AlertDialog은 세 가지 버튼(중립, 부정, 긍정)을 포함한 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textNeutral 중립 버튼의 텍스트
 * @param onNeutral 중립 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledNeutral 중립 버튼의 활성화 여부
 * @param textNegative 부정 버튼의 텍스트
 * @param onNegative 부정 버튼을 누를 때 호출되는 콜백 함수
 * @param textPositive 긍정 버튼의 텍스트
 * @param onPositive 긍정 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledPositive 긍정 버튼의 활성화 여부
 * @param tone 긍정 버튼과 아이콘에 적용할 의미 색상
 * @param isDestructive true이면 긍정 버튼을 Danger 톤으로 표시
 * @param buttonLayout 긍정/부정 버튼의 배치 방향
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textNeutral: String = stringResource(Res.string.close),
    onNeutral: () -> Unit,
    enabledNeutral: Boolean = true,
    textNegative: String = stringResource(Res.string.cancel),
    onNegative: () -> Unit,
    textPositive: String = stringResource(Res.string.ok),
    onPositive: () -> Unit,
    enabledPositive: Boolean = true,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    isDestructive: Boolean = false,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Horizontal,
) {
    val resolvedTone = M3AlertDialogTone(tone = tone, isDestructive = isDestructive)
    M3IenAlertDialogFrame(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onNegative,
        tone = resolvedTone,
        actions = {
            IenTextButton(
                text = textNeutral,
                onClick = onNeutral,
                modifier = Modifier.fillMaxWidth(),
                tone = IenSemanticTone.Neutral,
                state = IenButtonState(enabled = enabledNeutral),
            )
            M3IenDialogButtonGroup(
                buttonLayout = buttonLayout,
                cancelButton = {
                    IenConfirmDialogCancelButton(
                        text = textNegative,
                        onClick = onNegative,
                    )
                },
                confirmButton = {
                    M3IenDialogButton(
                        text = textPositive,
                        onClick = onPositive,
                        enabled = enabledPositive,
                        tone = resolvedTone,
                        variant = IenButtonVariant.Fill,
                    )
                },
            )
        },
    )
}

@Composable
private fun M3IenAlertDialogFrame(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    onDismiss: () -> Unit,
    tone: IenSemanticTone,
    actions: @Composable () -> Unit,
) {
    IenAlertDialog(
        visible = visible,
        onClose = onDismiss,
        modifier = modifier,
        title = {
            M3AlertDialogHeader(
                icon = icon,
                title = title,
                tone = tone,
            )
        },
        description = message?.let {
            {
                IenAlertDialogDescription(text = it)
            }
        },
        alertButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                actions()
            }
        },
    )
}

@Composable
private fun M3AlertDialogHeader(
    icon: @Composable (() -> Unit)?,
    title: String?,
    tone: IenSemanticTone,
) {
    if (icon == null && title == null) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        icon?.let {
            CompositionLocalProvider(LocalContentColor provides M3AlertDialogToneColor(tone)) {
                Box(contentAlignment = Alignment.Center) {
                    it()
                }
            }
        }
        title?.let {
            IenAlertDialogTitle(text = it)
        }
    }
}

@Composable
private fun M3IenDialogButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    tone: IenSemanticTone,
    variant: IenButtonVariant,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        size = IenButtonSize.Large,
        variant = variant,
        tone = tone,
        state = IenButtonState(enabled = enabled),
        display = IenButtonDisplay.Block,
    )
}

@Composable
private fun M3IenDialogButtonGroup(
    buttonLayout: IenDialogButtonLayout,
    cancelButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
) {
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

private fun M3AlertDialogTone(
    tone: IenSemanticTone,
    isDestructive: Boolean,
): IenSemanticTone {
    return if (isDestructive) IenSemanticTone.Danger else tone
}

@Composable
private fun M3AlertDialogToneColor(tone: IenSemanticTone): Color {
    return when (tone) {
        IenSemanticTone.Neutral -> IenTheme.colors.textPrimary
        IenSemanticTone.Brand -> IenTheme.colors.brand
        IenSemanticTone.Success -> IenTheme.colors.success
        IenSemanticTone.Warning -> IenTheme.colors.warning
        IenSemanticTone.Danger -> IenTheme.colors.danger
        IenSemanticTone.Info -> IenTheme.colors.info
    }
}
