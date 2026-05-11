package zone.ien.utils.adaptive.view

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.hig.view.HigAsteriskTextWrapper
import zone.ien.utils.ui.view.M3AsteriskTextWrapper

/**
 * 별표 텍스트 래퍼 컴포저블
 * 
 * 텍스트에 별표 표시를 추가하는 래퍼 컴포저블입니다.
 * 
 * @param modifier 래퍼에 적용할 수정자
 * @param style 텍스트 스타일
 * @param content 래퍼에 적용할 콘텐츠 컴포저블
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AsteriskTextWrapper(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3AsteriskTextWrapper(
                modifier = modifier,
                style = style,
                content = content
            )
        },
        cupertino = {
            HigAsteriskTextWrapper(
                modifier = modifier,
                style = style,
                content = content
            )
        }
    )
}