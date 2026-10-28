package zone.ien.utils.example.ui.screens.bottomcta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.screen.AdaptiveScaffold
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBar
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenCircleCheckbox
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
import zone.ien.utils.ui.screen.isIenSideBarAvailable
import zone.ien.utils.ui.screen.TopBarMode

@Composable
fun BottomCTATestScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    var renderer by rememberSaveable { mutableStateOf("adaptive") }
    var accessory by rememberSaveable { mutableStateOf(false) }
    var acknowledged by rememberSaveable { mutableStateOf(false) }
    var type by rememberSaveable { mutableStateOf("single") }
    var placement by rememberSaveable { mutableStateOf("scaffold") }
    var loading by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var showIcon by rememberSaveable { mutableStateOf(true) }
    var rtl by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("아직 누른 버튼이 없습니다.") }
    val state = IenButtonState(enabled = enabled && (!accessory || acknowledged), loading = loading)
    val topAccessory: (@Composable () -> Unit)? = if (accessory) {
        {
            IenCircleCheckbox(
                checked = acknowledged,
                onCheckedChange = { acknowledged = it },
                label = "안내 내용을 확인했으며 이 작업을 진행하는 데 동의합니다.",
            )
        }
    } else null
    val icon: (@Composable () -> Unit)? = if (showIcon) {
        { IenIcon(icon = IconData.Vector(SystemIcons.Check), contentDescription = null) }
    } else null
    val primaryClick = { result = "확인 버튼을 눌렀습니다." }
    val secondaryClick = { result = "취소 버튼을 눌렀습니다." }

    IenTheme {
        CompositionLocalProvider(LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
            BottomCTATestScaffold(
                modifier = modifier,
                renderer = renderer,
                navigateBack = navigateBack,
                bottomBar = if (placement == "scaffold") {
                    {
                        if (type == "single") {
                            IenBottomCTA(text = "확인", onClick = primaryClick, state = state, icon = icon, topAccessory = topAccessory)
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
                                topAccessory = topAccessory,
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
                            if (isIenSideBarAvailable) "현재 배치: Duo 오른쪽 세로 CTA" else "현재 배치: 일반 하단 CTA",
                            style = IenTheme.typography.title3,
                        )
                        Text("Duo를 접고 펼쳐 배치 전환과 시스템 표시기·카메라 영역의 겹침을 확인하세요.")
                        IenSegmentedControl(
                            items = listOf(
                                IenSegmentedControlItem("adaptive", "Adaptive 상단 바"),
                                IenSegmentedControlItem("plain", "Adaptive 기본"),
                                IenSegmentedControlItem("ien", "IenScaffold"),
                            ),
                            value = renderer,
                            onChange = { renderer = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
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
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("동의 액세서리 표시", Modifier.weight(1f))
                            IenSwitch(checked = accessory, onCheckedChange = { accessory = it })
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
                                topAccessory = topAccessory,
                            )
                        } else {
                            IenFixedDoubleBottomCTA(
                                topAccessory = topAccessory,
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


@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
private fun BottomCTATestScaffold(
    modifier: Modifier,
    renderer: String,
    navigateBack: () -> Unit,
    bottomBar: (@Composable () -> Unit)?,
    content: @Composable (PaddingValues) -> Unit,
) {
    if (renderer == "adaptive") {
        AdaptiveTopAppBarScaffold(
            modifier = modifier,
            title = { Text("BottomCTA 테스트") },
            navigationIcon = { AdaptiveBackButton(onClick = navigateBack) },
            topBarMode = TopBarMode.Static,
            backdrop = rememberDefaultBackdrop(),
            bottomBar = bottomBar,
        ) { padding, _ -> content(padding) }
    } else if (renderer == "plain") {
        AdaptiveScaffold(
            modifier = modifier,
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text("BottomCTA 테스트") },
                    navigationIcon = { AdaptiveBackButton(onClick = navigateBack) },
                )
            },
            bottomBar = bottomBar,
            content = content,
        )
    } else {
        IenScaffold(
            modifier = modifier,
            topBar = {
                IenTopBar(
                    title = "BottomCTA 테스트",
                    navigationIcon = { IenCloseButton(onClick = navigateBack) },
                )
            },
            bottomBar = bottomBar,
            content = content,
        )
    }
}
