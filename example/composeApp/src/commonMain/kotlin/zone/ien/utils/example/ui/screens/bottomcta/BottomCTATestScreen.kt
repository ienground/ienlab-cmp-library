package zone.ien.utils.example.ui.screens.bottomcta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenSegmentedControl
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenBottomCTA
import zone.ien.utils.ui.screen.IenBottomCTAButton
import zone.ien.utils.ui.screen.IenCloseButton
import zone.ien.utils.ui.screen.IenDoubleBottomCTA
import zone.ien.utils.ui.screen.IenFixedBottomCTA
import zone.ien.utils.ui.screen.IenFixedDoubleBottomCTA
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopBar
import zone.ien.utils.ui.screen.isIenSideBarLayout

@Composable
fun BottomCTATestScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    var type by rememberSaveable { mutableStateOf("single") }
    var placement by rememberSaveable { mutableStateOf("scaffold") }
    var loading by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var showIcon by rememberSaveable { mutableStateOf(true) }
    var rtl by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("아직 누른 버튼이 없습니다.") }
    val state = IenButtonState(enabled = enabled, loading = loading)
    val icon: (@Composable () -> Unit)? = if (showIcon) {
        { IenIcon(icon = IconData.Vector(SystemIcons.Check), contentDescription = null) }
    } else null
    val primaryClick = { result = "확인 버튼을 눌렀습니다." }
    val secondaryClick = { result = "취소 버튼을 눌렀습니다." }

    IenTheme {
        CompositionLocalProvider(LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
            IenScaffold(
                modifier = modifier,
                topBar = {
                    IenTopBar(
                        title = "BottomCTA 테스트",
                        navigationIcon = { IenCloseButton(onClick = navigateBack) },
                    )
                },
                bottomBar = if (placement == "scaffold") {
                    {
                        if (type == "single") {
                            IenBottomCTA(text = "확인", onClick = primaryClick, state = state, icon = icon)
                        } else {
                            IenDoubleBottomCTA(
                                primaryText = "확인",
                                onPrimaryClick = primaryClick,
                                secondaryText = "취소",
                                onSecondaryClick = secondaryClick,
                                primaryState = state,
                                secondaryState = state,
                                primaryIcon = icon,
                                primaryButtonWeight = 2f,
                            )
                        }
                    }
                } else null,
            ) { contentPadding ->
                Box(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(contentPadding)
                            .padding(IenTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                    ) {
                        Text(
                            if (isIenSideBarLayout) "현재 배치: Duo 오른쪽 세로 CTA" else "현재 배치: 일반 하단 CTA",
                            style = IenTheme.typography.title3,
                        )
                        Text("Duo를 접고 펼쳐 배치 전환과 시스템 표시기·카메라 영역의 겹침을 확인하세요.")
                        IenSegmentedControl(
                            items = listOf(
                                IenSegmentedControlItem("single", "단일 CTA"),
                                IenSegmentedControlItem("double", "이중 CTA"),
                            ),
                            value = type,
                            onChange = { type = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        IenSegmentedControl(
                            items = listOf(
                                IenSegmentedControlItem("scaffold", "Scaffold 바"),
                                IenSegmentedControlItem("fixed", "Box 고정"),
                            ),
                            value = placement,
                            onChange = { placement = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("로딩", Modifier.weight(1f))
                            IenSwitch(checked = loading, onCheckedChange = { loading = it })
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("버튼 활성화", Modifier.weight(1f))
                            IenSwitch(checked = enabled, onCheckedChange = { enabled = it })
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("아이콘 표시", Modifier.weight(1f))
                            IenSwitch(checked = showIcon, onCheckedChange = { showIcon = it })
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("RTL 배치", Modifier.weight(1f))
                            IenSwitch(checked = rtl, onCheckedChange = { rtl = it })
                        }
                        IenTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = "키보드 테스트",
                            placeholder = "Box 고정 CTA가 키보드 위로 이동하는지 확인하세요.",
                        )
                        Text(result, color = IenTheme.colors.textSecondary)
                        repeat(12) { index ->
                            Text("스크롤 확인 항목 ${index + 1}", modifier = Modifier.padding(vertical = IenTheme.spacing.md))
                        }
                    }
                    if (placement == "fixed") {
                        if (type == "single") {
                            IenFixedBottomCTA(
                                text = "확인",
                                onClick = primaryClick,
                                state = state,
                                icon = icon,
                                fixedAboveKeyboard = true,
                            )
                        } else {
                            IenFixedDoubleBottomCTA(
                                leftButtonWeight = 1f,
                                rightButtonWeight = 2f,
                                leftButton = {
                                    IenBottomCTAButton(
                                        text = "취소", onClick = secondaryClick, state = state,
                                        variant = if (isIenSideBarLayout) IenButtonVariant.Line else IenButtonVariant.Weak,
                                        tone = IenSemanticTone.Neutral,
                                    )
                                },
                                rightButton = {
                                    IenBottomCTAButton(text = "확인", onClick = primaryClick, state = state, icon = icon)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
