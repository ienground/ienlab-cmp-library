package zone.ien.utils.ui.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 라이브러리의 기본 테마가 입혀진 배경 판(Surface) 컴포저블입니다.
 *
 * 내부 콘텐츠의 색상 조절 및 그림자 효과를 처리합니다.
 *
 * @param modifier 레이아웃에 적용할 [Modifier]
 * @param color 배경 판에 채울 테마 색상
 * @param contentColor 내부 텍스트 및 자식 컴포저블에 기본 제공할 전경색
 * @param shape 모서리를 깎아줄 둥글기 모양 정의 ([Shape])
 * @param border 테두리에 적용할 외곽선 선 두께 및 색상 정보 ([BorderStroke])
 * @param tonalElevation 음영을 더해주는 톤 입체감 깊이 수준 ([Dp])
 * @param content 배경 판 내부에 배치할 자식 컴포저블 블록
 */
@Composable
fun IenSurface(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.surface,
    contentColor: Color = IenTheme.colors.textPrimary,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    border: BorderStroke? = null,
    tonalElevation: Dp = IenTheme.elevation.none,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = shape,
        border = border,
        tonalElevation = tonalElevation,
        content = content,
    )
}

/**
 * 라이브러리의 기본 타이포그래피 스타일을 기반으로 문구를 출력하는 기본 텍스트 컴포저블입니다.
 *
 * @param text 화면에 표시할 문자열
 * @param modifier 적용할 [Modifier]
 * @param style 적용할 글자 크기, 행간 등 스타일 명세 ([TextStyle])
 * @param color 글자 색상
 * @param fontWeight 글씨 두께 설정 ([FontWeight])
 * @param maxLines 줄 바꿈을 허용할 최대 라인 수
 * @param overflow 텍스트가 정해진 크기를 초과할 때 처리할 규칙 ([TextOverflow])
 * @param textAlign 텍스트 수평 정렬 방식 ([TextAlign])
 */
@Composable
fun IenText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
    )
}

/**
 * 내부 자식 컴포저블에 공통 텍스트 스타일([style]) 및 전경 컬러([color])를 주입해 주는 스타일 프로바이더 컴포저블입니다.
 *
 * @param style 자식 텍스트에 하위 전달할 [TextStyle]
 * @param color 자식 컴포저블의 기본 텍스트/전경 색상
 * @param content 스타일의 영향 범위 내부에 놓을 자식 컴포저블 블록
 */
@Composable
fun IenProvideTextStyle(
    style: TextStyle,
    color: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides color) {
        ProvideTextStyle(style, content)
    }
}

/**
 * 지정된 벡터 드로어블 이미지([imageVector])를 렌더링하는 기본 아이콘 컴포저블입니다.
 *
 * @param imageVector 화면에 그릴 벡터 그래픽 데이터 ([ImageVector])
 * @param contentDescription 시각장애인 접근성을 위한 스크린 리더용 설명문
 * @param modifier 적용할 [Modifier]
 * @param tint 아이콘 문양에 칠할 전경색
 * @param size 아이콘의 전체 크기 ([Dp])
 */
@Composable
fun IenIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = IenTheme.icon.md,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size),
    )
}

/**
 * 요소 간의 구분을 위해 제공하는 수평선(구분선) 컴포저블입니다.
 *
 * @param modifier 구분선의 레이아웃을 조절할 [Modifier]
 * @param color 구분선 선의 칠할 색상
 * @param thickness 수평선 선 굵기 ([Dp])
 */
@Composable
fun IenDivider(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.border,
    thickness: Dp = IenTheme.stroke.thin,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color),
    )
}

/**
 * 둥근 외곽 테두리를 두른 간단한 정보 영역을 쉽게 잡기 위해 설계된 박스 컴포저블입니다.
 *
 * @param modifier 박스 영역에 설정할 [Modifier]
 * @param shape 외곽선 형태를 제어할 모서리 모양 명세 ([Shape])
 * @param color 외곽선 선에 적용할 컬러
 * @param width 외곽선 테두리의 굵기 ([Dp])
 * @param padding 내부 콘텐츠 영역과의 간격 오프셋 ([PaddingValues])
 * @param content 내부 콘텐츠 컴포저블 블록
 */
@Composable
fun IenBorderBox(
    modifier: Modifier = Modifier,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    color: Color = IenTheme.colors.border,
    width: Dp = IenTheme.stroke.thin,
    padding: PaddingValues = PaddingValues(IenTheme.spacing.md),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .border(width, color, shape)
            .padding(padding),
    ) {
        content()
    }
}

/**
 * 라이브러리 규격의 최소 터치 타겟(Touch Target) 영역 크기를 보장하며, 잉크 리플이 없는 단순 클릭 이벤트를 연동하는 래퍼 컴포저블입니다.
 *
 * @param onClick 클릭되었을 때 수행할 콜백 함수
 * @param modifier 클릭 가능 영역에 전달할 [Modifier]
 * @param enabled 클릭 가능 여부 활성화 설정
 * @param role 스크린 리더에서 이 요소를 어떤 종류(예: 버튼 등)로 소통할지 정의하는 역할 명세 ([Role])
 * @param interactionSource 터치 인텐트 및 포커스 상태 변화를 추적하는 상호작용 소스 ([MutableInteractionSource])
 * @param content 클릭 가능 영역 내에 배치할 자식 컴포저블 블록
 */
@Composable
fun IenClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = role,
                onClick = onClick,
            ),
    ) {
        content()
    }
}

/**
 * 라이브러리 전용 기본 원형 회전식 인디케이터 기본 원형(Primitive) 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param color 원형 선에 채울 기본 색상
 * @param strokeWidth 선 굵기 ([Dp])
 */
@Composable
fun IenLoaderPrimitive(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    strokeWidth: Dp = 2.dp,
) {
    CircularProgressIndicator(
        modifier = modifier.size(IenTheme.icon.md),
        color = color,
        strokeWidth = strokeWidth,
    )
}
