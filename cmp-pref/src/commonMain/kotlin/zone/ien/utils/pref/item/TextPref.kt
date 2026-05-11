package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionLink
import zone.ien.utils.adaptive.section.HigSectionItemAdaptation
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.M3SectionItemAdaptation
import zone.ien.utils.adaptive.section.M3SectionLinkAdaptation


/**
 * 클릭 가능한 링크가 포함된 텍스트 설정 항목을 생성하는 Composable 함수입니다.
 * 
 * 이 설정 항목은 제목과 선택적인 요약 텍스트를 표시합니다. 클릭 시 onClick 콜백을 트리거하며,
 * 다이얼로그를 열거나 화면을 이동할 때 유용합니다.
 * 
 * @param onClick 항목을 클릭했을 때 트리거되는 콜백 함수
 * @param modifier 레이아웃에 적용할 Modifier
 * @param enabled 설정의 활성화 여부
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param onClickLabel 클릭 동작에 대한 선택적 접근성 레이블
 * @param indication 상호작용에 대한 선택적 시각적 표시
 * @param interactionSource 상호작용 추적을 위한 선택적 소스
 * @param summary 표시할 선택적 요약 텍스트
 * @param chevron 끝에 쉐브론 아이콘을 표시하는 Composable
 * @param adaptation 플랫폼별(iOS/Android) 적응형 구성
 * @param title 이 설정 항목의 제목 텍스트
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextPref(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    summary: String? = null,
    chevron: @Composable () -> Unit = {
        AdaptiveWidget(
            material = {},
            cupertino = { CupertinoSectionDefaults.LabelChevron() }
        )
    },
    adaptation: AdaptationScope<HigSectionLinkAdaptation, M3SectionLinkAdaptation>.() -> Unit = {},
    title: String,
) {
    AdaptiveSectionLink(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        caption = summary?.let { { Text(text = it) } },
        trailingContent = chevron,
        adaptation = adaptation,
        title = { Text(text = title) }
    )
}

/**
 * 사용자 정의 콘텐츠가 포함된 텍스트 설정 항목을 생성하는 Composable 함수입니다.
 *
 * 이 변체는 상호작용이 없는 단순한 텍스트 항목을 표시하는 데 사용됩니다.
 * 정적인 정보를 표시하거나 설정 내의 그룹 헤더로 유용합니다.
 *
 * @param modifier 레이아웃에 적용할 [Modifier]
 * @param leadingContent 제목 앞에 표시할 선택적 콘텐츠
 * @param trailingContent 제목 뒤에 표시할 선택적 콘텐츠
 * @param summary 표시할 선택적 요약 텍스트
 * @param adaptation 플랫폼별(iOS/Android) 적응형 구성
 * @param title 이 설정 항목의 제목 텍스트
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextPref(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    summary: String? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, M3SectionItemAdaptation>.() -> Unit = {},
    title: String,
) {
    AdaptiveSectionItem(
        modifier = modifier,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        supportingContent = summary?.let { { Text(text = it) } },
        adaptation = adaptation,
        title = { Text(text = title) }
    )
}