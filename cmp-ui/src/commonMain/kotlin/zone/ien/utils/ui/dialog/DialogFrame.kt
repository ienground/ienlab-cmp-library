package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface

/**
 * 다이얼로그의 일관된 프레임 레이아웃을 제공하는 내부 컴포저블입니다.
 *
 * 배경 영역, 둥근 모서리, 여백 및 너비 설정과 뒤로가기/외부 클릭 시 닫기 동작을 제어합니다.
 *
 * @param visible 다이얼로그의 표시 여부
 * @param onDismiss 다이얼로그를 닫을 때 호출되는 콜백 함수
 * @param modifier 프레임 레이아웃에 적용할 Modifier
 * @param maxWidth 다이얼로그의 최대 너비
 * @param contentPadding 내부 콘텐츠의 패딩 값
 * @param dismissOnBackPress 뒤로가기 버튼 입력 시 닫기 여부
 * @param dismissOnClickOutside 다이얼로그 외부 영역 클릭 시 닫기 여부
 * @param usePlatformDefaultWidth 플랫폼 기본 너비 제약 사용 여부
 * @param horizontalMargin 다이얼로그 양옆의 최소 마진
 * @param fixedWidth 고정 너비 값 (지정하지 않을 경우 가로 꽉 차게 혹은 최대 너비 제한)
 * @param content 다이얼로그 내부에 표시할 콘텐츠 Composable
 */
@Composable
internal fun IenDialogFrame(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 320.dp,
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.lg),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    horizontalMargin: Dp = 24.dp,
    fixedWidth: Dp? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = usePlatformDefaultWidth,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalMargin),
        ) {
            IenSurface(
                modifier = modifier
                    .align(Alignment.Center)
                    .then(fixedWidth?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth())
                    .widthIn(max = maxWidth),
                color = IenTheme.colors.surfaceRaised,
                shape = RoundedCornerShape(IenTheme.radius.xl),
                tonalElevation = IenTheme.elevation.overlay,
            ) {
                Column(
                    modifier = Modifier.padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    content = content,
                )
            }
        }
    }
}
