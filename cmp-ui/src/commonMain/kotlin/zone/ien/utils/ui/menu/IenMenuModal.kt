package zone.ien.utils.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * [IenMenu]에서 사용하는 메뉴 항목의 데이터를 정의하는 불변(Immutable) 데이터 클래스입니다.
 *
 * @property title 메뉴 항목에 표시될 제목
 * @property onClick 메뉴 항목 클릭 시 실행할 콜백 함수
 * @property description 메뉴 항목 아래에 표시될 부연 설명 (선택 사항)
 * @property enabled 메뉴 항목의 활성화 여부
 * @property tone 메뉴 항목의 의미적 색상 톤 (예: Danger, Warning, Neutral 등)
 * @property leading 메뉴 항목 왼쪽에 배치할 컴포저블 (예: 아이콘)
 * @property trailing 메뉴 항목 오른쪽에 배치할 컴포저블
 */
@Immutable
data class IenMenuItem(
    val title: String,
    val onClick: () -> Unit,
    val description: String? = null,
    val enabled: Boolean = true,
    val tone: IenSemanticTone = IenSemanticTone.Neutral,
    val leading: (@Composable () -> Unit)? = null,
    val trailing: (@Composable () -> Unit)? = null,
)

/**
 * 여러 개의 [IenMenuItem] 리스트를 받아 세로 목록 형태의 메뉴를 구성하는 컴포저블입니다.
 *
 * @param items 메뉴에 표시할 항목 리스트
 * @param modifier 메뉴 전체 컨테이너에 적용할 Modifier
 * @param header 메뉴 상단에 추가로 렌더링할 헤더 영역 컴포저블
 * @param footer 메뉴 하단에 추가로 렌더링할 푸터 영역 컴포저블
 */
@Composable
fun IenMenu(
    items: List<IenMenuItem>,
    modifier: Modifier = Modifier,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    IenSurface(
        modifier = modifier.widthIn(min = IenTheme.state.minimumTouchTarget * 4),
        color = IenTheme.colors.surfaceRaised,
        shape = ContinuousRoundedRectangle(IenTheme.radius.lg),
        tonalElevation = IenTheme.elevation.floating,
    ) {
        Column(Modifier.padding(vertical = IenTheme.spacing.xs)) {
            if (header != null) {
                Column(Modifier.padding(IenTheme.spacing.md), content = header)
                IenDivider()
            }
            items.forEach { item ->
                IenMenuItemRow(item)
            }
            if (footer != null) {
                IenDivider()
                Column(Modifier.padding(IenTheme.spacing.md), content = footer)
            }
        }
    }
}

@Composable
private fun IenMenuItemRow(item: IenMenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled, role = Role.Button, onClick = item.onClick)
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item.leading?.invoke()
        Column(Modifier.weight(1f)) {
            IenText(
                text = item.title,
                style = IenTheme.typography.body2,
                color = if (item.enabled) toneColor(item.tone) else IenTheme.colors.textDisabled,
            )
            if (item.description != null) {
                IenText(
                    text = item.description,
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textTertiary,
                )
            }
        }
        item.trailing?.invoke()
    }
}

/**
 * 다이얼로그 형태로 화면 위에 모달 창을 표시하는 컴포저블입니다.
 *
 * @param open 모달의 표시 여부
 * @param onOpenChange 모달의 열림 상태 변경 시 호출되는 콜백 함수
 * @param modifier 모달의 전체 컨테이너 Box에 적용할 Modifier
 * @param onExited 모달이 닫히고 사라진 후에 호출되는 콜백 함수
 * @param properties 다이얼로그의 속성 (기본값: 플랫폼 기본 너비를 사용하지 않도록 설정)
 * @param content 모달 내부의 레이아웃을 채울 컴포저블
 */
@Composable
fun IenModal(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onExited: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable BoxScope.() -> Unit,
) {
    if (!open) {
        onExited?.invoke()
        return
    }
    Dialog(
        onDismissRequest = { onOpenChange(false) },
        properties = properties,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            content = content,
        )
    }
}

/**
 * 모달 창 내부에 사용할 공통적인 서브 컴포저블(Overlay, Content)을 포함하는 오브젝트입니다.
 */
object IenModal {
    /**
     * 모달 뒷배경을 어둡게 처리하여 모달에 집중할 수 있도록 만드는 오버레이 컴포저블입니다.
     *
     * @param modifier 오버레이 컨테이너 Box에 적용할 Modifier
     * @param color 오버레이 배경 색상 및 알파 값 (기본값: 검은색 42% 불투명도)
     * @param onClick 배경 터치 시 호출할 콜백 함수 (제공되는 경우 클릭 이벤트 활성화)
     */
    @Composable
    fun Overlay(
        modifier: Modifier = Modifier,
        color: Color = Color.Black.copy(alpha = 0.42f),
        onClick: (() -> Unit)? = null,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(
                    enabled = onClick != null,
                    role = Role.Button,
                    onClick = { onClick?.invoke() },
                ),
        ) {
            IenSurface(
                modifier = Modifier.fillMaxSize(),
                color = color,
                contentColor = color,
            ) {}
        }
    }

    /**
     * 모달 내부에 카드 형태로 콘텐츠를 구성할 수 있도록 배경과 패딩을 설정해주는 컴포저블입니다.
     *
     * @param modifier 콘텐츠 영역에 적용할 Modifier
     * @param shape 모서리의 둥글기 모양
     * @param content 내부에 들어갈 컴포저블 내용
     */
    @Composable
    fun Content(
        modifier: Modifier = Modifier,
        shape: ContinuousRoundedRectangle = ContinuousRoundedRectangle(IenTheme.radius.xl),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        IenSurface(
            modifier = modifier.fillMaxWidth(),
            color = IenTheme.colors.surfaceRaised,
            shape = shape,
            tonalElevation = IenTheme.elevation.overlay,
        ) {
            Column(
                modifier = Modifier.padding(IenTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                content = content,
            )
        }
    }
}
