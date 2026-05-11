package zone.ien.utils.hig.view.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.hig.CupertinoActivityIndicator
import zone.ien.hig.CupertinoButton
import zone.ien.hig.CupertinoButtonDefaults.plainButtonColors
import zone.ien.hig.CupertinoButtonSize
import zone.ien.hig.CupertinoIconDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.IconData

/**
 * HIG 텍스트 필드 아이콘 버튼 컴포저블
 * 
 * 텍스트 필드 내부에 사용되는 아이콘 버튼을 제공합니다.
 * 로딩 상태에 따라 아이콘과 로딩 인디케이터를 전환합니다.
 * 
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param onClick 버튼 클릭 시 실행할 함수
 * @param loading 로딩 상태 여부 (true일 경우 로딩 인디케이터 표시)
 * @param enabled 버튼 활성화 상태 여부
 * @param interactionSource 상호작용 소스
 * @param icon 표시할 아이콘
 * @param contentDescription 아이콘의 설명 텍스트
 * @return 텍스트 필드 아이콘 버튼 컴포저블
 */
@OptIn(ExperimentalCupertinoApi::class)
@Composable
fun HigTextFieldIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    loading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: IconData,
    contentDescription: String? = null
) {
    CupertinoButton(
        onClick = onClick,
        modifier = modifier.size(CupertinoIconDefaults.MediumSize),
        enabled = enabled && !loading,
        colors = plainButtonColors(
            contentColor = CupertinoTheme.colorScheme.tertiaryLabel,
            disabledContentColor = CupertinoTheme.colorScheme.tertiaryLabel.copy(alpha = 0.15f)
        ),
        size = CupertinoButtonSize.Regular,
        shape = CircleShape,
        border = null,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        content = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(CupertinoIconDefaults.MediumSize)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !loading,
                    enter = fadeIn(tween(700)),
                    exit = fadeOut(tween(700))
                ) {
                    ComplexIcon(
                        icon = icon,
                        contentDescription = contentDescription,
                        modifier = Modifier.size(CupertinoIconDefaults.MediumSize),
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = loading,
                    enter = fadeIn(tween(700)),
                    exit = fadeOut(tween(700))
                ) {
                    CupertinoActivityIndicator(
                        modifier = Modifier.size(CupertinoIconDefaults.MediumSize),
                    )
                }
            }
        }
    )
}

/**
 * HIG 텍스트 필드 클리어 버튼 컴포저블
 * 
 * 텍스트 필드에서 입력 내용을 삭제하는 데 사용되는 클리어 버튼을 제공합니다.
 *  
 * @param visible 버튼 표시 여부
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return 텍스트 필드 클리어 버튼 컴포저블
 */
@Composable
fun HigTextFieldClearButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring(1.2f)) + scaleIn(spring(1.2f), initialScale = 0.75f),
        exit = fadeOut(spring(1.2f)) + scaleOut(spring(1.2f), targetScale = 0.75f)
    ) {
        HigTextFieldIconButton(
            icon = IconData.Vector(M3SystemIcons.Cancel),
            onClick = onClick
        )
    }
}