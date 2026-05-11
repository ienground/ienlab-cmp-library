package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.AlertDialogDefaults.iconContentColor
import androidx.compose.material3.AlertDialogDefaults.textContentColor
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * BaseDialog은 AlertDialog의 기본 구성 요소로, 기본적인 다이얼로그 레이아웃을 제공합니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param content 다이얼로그의 내용을 나타내는 Composable
 * @param onCancel 다이얼로그를 닫기 위한 콜백 함수
 * @param buttons 다이얼로그의 버튼을 나타내는 Composable
 */
@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    content: @Composable (() -> Unit)?,
    onCancel: (() -> Unit)?,
    buttons: @Composable ((modifier: Modifier) -> Unit)?
) {
    val dialogShape = LocalDialogShape.current ?: LocalDialogProviderDefault.Shape
    val dialogBorder = LocalDialogBorder.current
    val dialogBackgroundColor = LocalDialogBackgroundColor.current ?: LocalDialogProviderDefault.BackgroundColor

    Dialog(
        onDismissRequest = onCancel ?: {},
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Surface(
            shape = dialogShape,
            color = dialogBackgroundColor,
            border = dialogBorder,
            tonalElevation = 6.dp,
            modifier = modifier.height(IntrinsicSize.Min)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                        Box(Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)) {
                            icon()
                        }
                    }
                }
                title?.let {
                    ProvideTextStyle(
                        value = MaterialTheme.typography.headlineSmall.copy(color = titleContentColor),
                    ) {
                        Box(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
                                .align(
                                    if (icon == null) {
                                        Alignment.Start
                                    } else {
                                        Alignment.CenterHorizontally
                                    }
                                )
                        ) {
                            Text(
                                text = it,
                                autoSize = TextAutoSize.StepBased(maxFontSize = 24.sp),
                                maxLines = 1
                            )
                        }
                    }
                }
                ProvideTextStyle(
                    value = MaterialTheme.typography.bodyMedium.copy(color = textContentColor),
                ) {
                    Box(
                        Modifier.weight(weight = 1f, fill = false)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp)
                            .align(Alignment.Start)
                    ) {
                        content?.invoke()
                    }
                }
                buttons?.invoke(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(40.dp)
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}