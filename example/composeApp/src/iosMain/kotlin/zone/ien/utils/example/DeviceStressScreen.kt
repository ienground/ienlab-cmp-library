package zone.ien.utils.example

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.delay
import androidx.compose.runtime.withFrameNanos
import org.koin.compose.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSProcessInfo
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.component.AdaptiveCheckbox
import zone.ien.utils.adaptive.component.AdaptiveSegmentedControl
import zone.ien.utils.adaptive.component.AdaptiveSlider
import zone.ien.utils.adaptive.component.AdaptiveSwitch
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.example.di.KoinKey.DEFAULT_DATASTORE
import zone.ien.utils.example.ui.screens.designsystem.DesignSystemComponentPreview
import zone.ien.utils.example.ui.screens.designsystem.DesignSystemComponents
import zone.ien.utils.example.ui.screens.designsystem.playgroundControls
import zone.ien.utils.example.ui.screens.designsystem.stressAlternateValue
import zone.ien.utils.pref.rememberPreviewSettingsDataStore
import zone.ien.utils.ui.feedback.IenSkeleton
import zone.ien.utils.ui.feedback.IenSkeletonElement
import zone.ien.utils.ui.feedback.IenSkeletonRepeat
import zone.ien.utils.ui.feedback.IenToastState
import zone.ien.utils.ui.feedback.rememberIenToastState
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenSegmentedControlItem
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenScaffoldContentEdge

private const val DeviceStressCycles = 2
private const val CatalogDefaultDurationMillis = 800L
private const val CatalogAlternateDurationMillis = 800L
private const val CatalogUnmountDurationMillis = 400L

@Composable
internal fun DeviceStressScreen() {
    val previewDataStore = rememberPreviewSettingsDataStore()
    KoinApplication(
        application = {
            modules(
                module {
                    single<DataStore<Preferences>>(named(DEFAULT_DATASTORE)) { previewDataStore }
                },
            )
        },
    ) {
        DeviceStressContent()
    }
}

@Composable
private fun DeviceStressContent() {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val toastState = rememberIenToastState()
    val coroutineScope = rememberCoroutineScope()
    val mode = remember { mutableStateOf(DeviceStressMode.Catalog) }
    val componentId = remember { mutableStateOf<String?>(null) }
    val controlValues = remember { mutableStateOf(emptyMap<String, String>()) }
    val phaseText = remember { mutableStateOf("시작 대기") }
    val progressText = remember { mutableStateOf("") }
    val scaffoldBlurEnabled = remember { mutableStateOf(true) }
    val adaptiveSwitchChecked = remember { mutableStateOf(false) }
    val adaptiveCheckboxChecked = remember { mutableStateOf(false) }
    val adaptiveSegmentIndex = remember { mutableIntStateOf(0) }
    val adaptiveSliderValue = remember { mutableFloatStateOf(0.35f) }
    val webViewOnly = remember { NSProcessInfo.processInfo.arguments.contains("--ien-device-stress-webview-only") }
    var currentProgress = 0
    val totalCatalogCases = if (webViewOnly) 0 else DesignSystemComponents.size * DeviceStressCycles

    LaunchedEffect(Unit) {
        postDeviceStressPhase("warmup")
        phaseText.value = "실기기 준비 대기"
        progressText.value = "15초 warmup"
        delay(15_000)

        suspend fun enterPhase(case: String, progress: String, update: () -> Unit) {
            postDeviceStressPhase(case)
            update()
            phaseText.value = case
            progressText.value = progress
            scrollState.scrollTo(0)
            withFrameNanos { }
        }

        suspend fun runScrollPhase(
            durationMillis: Long,
            maximumScrollPx: Float? = null,
            onFrame: (() -> Unit)? = null,
        ) {
            scrollForDuration(scrollState, durationMillis, maximumScrollPx, onFrame)
        }

        for (cycle in 1..(if (webViewOnly) 0 else DeviceStressCycles)) {
            DesignSystemComponents.forEachIndexed { index, component ->
                val controls = playgroundControls(component.id)
                val defaultValues = controls.associate { control -> control.key to control.defaultValue }
                val alternateValues = controls.associate { control -> control.key to control.stressAlternateValue() }
                val componentProgress = "${cycle}/$DeviceStressCycles · ${index + 1}/${DesignSystemComponents.size} · ${component.id}"

                enterPhase(
                    case = "catalog/$cycle/${component.id}/default",
                    progress = "$componentProgress · 기본값",
                ) {
                    mode.value = DeviceStressMode.Catalog
                    componentId.value = component.id
                    controlValues.value = defaultValues
                }
                runScrollPhase(CatalogDefaultDurationMillis, maximumScrollPx = 160f)

                enterPhase(
                    case = "catalog/$cycle/${component.id}/alternate",
                    progress = "$componentProgress · 대체값",
                ) {
                    controlValues.value = alternateValues
                }
                runScrollPhase(CatalogAlternateDurationMillis, maximumScrollPx = 160f)

                enterPhase(
                    case = "catalog/$cycle/${component.id}/unmount",
                    progress = "$componentProgress · 해제",
                ) {
                    componentId.value = null
                    controlValues.value = emptyMap()
                }
                waitForRealtimeFrames(CatalogUnmountDurationMillis)
                currentProgress += 1
            }
        }

        if (!webViewOnly) {
            enterPhase("skeleton/64", "스켈레톤 64 블록") {
                mode.value = DeviceStressMode.Skeleton
                componentId.value = null
            }
            runScrollPhase(4_000)

            enterPhase("scaffold/blur-enabled", "Scaffold blur 활성") {
                mode.value = DeviceStressMode.Scaffold
                scaffoldBlurEnabled.value = true
            }
            runScrollPhase(4_000)

            enterPhase("scaffold/blur-disabled", "Scaffold blur 비활성") {
                scaffoldBlurEnabled.value = false
            }
            runScrollPhase(4_000)

            enterPhase("adaptive/cupertino", "Cupertino adaptive controls") {
                mode.value = DeviceStressMode.Adaptive
            }
            var adaptiveFrameCount = 0
            runScrollPhase(4_000) {
                adaptiveFrameCount += 1
                if (adaptiveFrameCount % 12 == 0) {
                    adaptiveSwitchChecked.value = !adaptiveSwitchChecked.value
                    adaptiveCheckboxChecked.value = !adaptiveCheckboxChecked.value
                    adaptiveSegmentIndex.intValue = 1 - adaptiveSegmentIndex.intValue
                    adaptiveSliderValue.floatValue = if (adaptiveSliderValue.floatValue < 0.5f) 0.68f else 0.32f
                }
            }

        }

        enterPhase("webview/scroll", "WKWebView 양방향 스크롤") {
            mode.value = DeviceStressMode.WebView
        }
        waitForRealtimeFrames(8_000)

        enterPhase("rest/final", "최종 해제 및 대기") {
            mode.value = DeviceStressMode.Rest
            componentId.value = null
            controlValues.value = emptyMap()
        }
        runScrollPhase(1_000)
        postDeviceStressComplete()
        phaseText.value = "완료"
        progressText.value = "$currentProgress/$totalCatalogCases 카탈로그 항목 완료"
    }

    IenTheme {
        Box(Modifier.fillMaxSize()) {
            when (mode.value) {
                DeviceStressMode.Catalog -> CatalogStressContent(
                    scrollState = scrollState,
                    componentId = componentId.value,
                    controlValues = controlValues.value,
                    snackbarHostState = snackbarHostState,
                    toastState = toastState,
                    coroutineScope = coroutineScope,
                )
                DeviceStressMode.Skeleton -> SkeletonStressContent(scrollState)
                DeviceStressMode.Scaffold -> ScaffoldStressContent(
                    scrollState = scrollState,
                    blurEnabled = scaffoldBlurEnabled.value,
                )
                DeviceStressMode.Adaptive -> CupertinoAdaptiveStressContent(
                    scrollState = scrollState,
                    switchChecked = adaptiveSwitchChecked.value,
                    checkboxChecked = adaptiveCheckboxChecked.value,
                    selectedIndex = adaptiveSegmentIndex.intValue,
                    sliderValue = adaptiveSliderValue.floatValue,
                )
                DeviceStressMode.WebView -> DeviceStressWebView()
                DeviceStressMode.Rest -> Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                ) {
                    Text("스트레스 루프 종료 대기")
                    Spacer(Modifier.height(12_000.dp))
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(IenTheme.colors.surface.copy(alpha = 0.94f))
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(phaseText.value, style = IenTheme.typography.caption)
                Text(progressText.value, style = IenTheme.typography.caption)
            }
        }
    }
}

private enum class DeviceStressMode {
    Catalog,
    Skeleton,
    Scaffold,
    Adaptive,
    WebView,
    Rest,
}

@Composable
private fun CatalogStressContent(
    scrollState: ScrollState,
    componentId: String?,
    controlValues: Map<String, String>,
    snackbarHostState: SnackbarHostState,
    toastState: IenToastState,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 240.dp, max = 280.dp)
                .background(IenTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            if (componentId == null) {
                Text("다음 카탈로그 항목 준비")
            } else {
                DesignSystemComponentPreview(
                    componentId = componentId,
                    snackbarHostState = snackbarHostState,
                    toastState = toastState,
                    coroutineScope = coroutineScope,
                    controlValues = controlValues,
                )
            }
        }
        Spacer(Modifier.height(12_000.dp))
    }
}

@Composable
private fun SkeletonStressContent(scrollState: ScrollState) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
    ) {
        IenSkeleton(
            modifier = Modifier.fillMaxWidth().padding(IenTheme.spacing.md),
            custom = List(64) { IenSkeletonElement.Block(height = 20.dp) },
            repeatLastItemCount = IenSkeletonRepeat.Count(1),
        )
        Spacer(Modifier.height(6_000.dp))
    }
}

@Composable
private fun ScaffoldStressContent(
    scrollState: ScrollState,
    blurEnabled: Boolean,
) {
    IenScaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().height(64.dp).background(Color.White),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text("Scaffold scroll blur", modifier = Modifier.padding(horizontal = 16.dp))
            }
        },
        contentEdge = IenScaffoldContentEdge(
            enabled = blurEnabled,
            scrollableState = scrollState,
            topHeight = 120.dp,
            bottomHeight = 72.dp,
            color = Color.White,
        ),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(contentPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(160) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .background(if (index % 2 == 0) Color(0xFFE9EEF5) else Color(0xFFF9FAFC)),
                ) {
                    Text("Scaffold row ${index + 1}", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
@OptIn(zone.ien.hig.adaptive.ExperimentalAdaptiveApi::class)
private fun CupertinoAdaptiveStressContent(
    scrollState: ScrollState,
    switchChecked: Boolean,
    checkboxChecked: Boolean,
    selectedIndex: Int,
    sliderValue: Float,
) {
    IenAdaptiveTheme(target = Theme.Cupertino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Cupertino adaptive controls")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("AdaptiveSwitch")
                AdaptiveSwitch(
                    checked = switchChecked,
                    onCheckedChange = {},
                )
            }
            AdaptiveCheckbox(
                checked = checkboxChecked,
                onCheckedChange = {},
                label = "AdaptiveCheckbox",
            )
            AdaptiveSegmentedControl(
                items = listOf(
                    IenSegmentedControlItem("first", "First"),
                    IenSegmentedControlItem("second", "Second"),
                ),
                value = if (selectedIndex == 0) "first" else "second",
                onChange = {},
            )
            AdaptiveSlider(
                value = sliderValue,
                onValueChange = {},
                label = "Slider",
                valueLabel = "${(sliderValue * 100).toInt()}%",
            )
            Spacer(Modifier.height(8_000.dp))
        }
    }
}

private suspend fun scrollForDuration(
    state: ScrollState,
    durationMillis: Long,
    maximumScrollPx: Float? = null,
    onFrame: (() -> Unit)? = null,
) {
    val durationNanos = durationMillis * 1_000_000L
    var direction = 1f
    state.scroll {
        val startNanos = withFrameNanos { it }
        while (true) {
            val frameNanos = withFrameNanos { it }
            if (frameNanos - startNanos >= durationNanos) break
            val maxValue = maximumScrollPx?.let { minOf(state.maxValue.toFloat(), it) }?.toInt()
                ?: state.maxValue
            if (maxValue > 0) {
                if (direction > 0f && state.value >= maxValue) direction = -1f
                if (direction < 0f && state.value <= 0) direction = 1f
                scrollBy(direction * 4f)
            }
            onFrame?.invoke()
        }
    }
}

private suspend fun waitForRealtimeFrames(durationMillis: Long) {
    val durationNanos = durationMillis * 1_000_000L
    val startNanos = withFrameNanos { it }
    while (true) {
        val frameNanos = withFrameNanos { it }
        if (frameNanos - startNanos >= durationNanos) return
    }
}

private fun postDeviceStressPhase(case: String) {
    NSNotificationCenter.defaultCenter.postNotificationName(
        aName = "IENDeviceStressPhase",
        `object` = null,
        userInfo = mapOf("case" to case),
    )
}

private fun postDeviceStressComplete() {
    NSNotificationCenter.defaultCenter.postNotificationName(
        aName = "IENDeviceStressComplete",
        `object` = null,
    )
}
