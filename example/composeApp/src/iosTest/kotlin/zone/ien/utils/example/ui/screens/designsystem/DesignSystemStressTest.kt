package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.TestResult
import org.koin.compose.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.UIKit.UIViewController
import zone.ien.utils.example.di.KoinKey.DEFAULT_DATASTORE
import zone.ien.utils.pref.rememberPreviewSettingsDataStore
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.initializeIenIos

class DesignSystemStressTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allCatalogComponentsMountTransitionScrollAndDisposeRepeatedly(): TestResult = runComposeUiTest {
        runOnUiThread { initializeIenIos() }
        mainClock.autoAdvance = false

        val components = DesignSystemComponents
        val componentIds = components.map { it.id }
        assertTrue(componentIds.isNotEmpty())
        assertEquals(componentIds.size, componentIds.distinct().size)
        println("iOS 디자인 시스템 카탈로그 컴포넌트 수: ${componentIds.size}")

        val activeComponentId = mutableStateOf<String?>(null)
        val controlValues = mutableStateOf(emptyMap<String, String>())
        var mountCount = 0
        var disposeCount = 0
        var composedComponentId: String? = null
        var composedControlValues: Map<String, String> = emptyMap()

        setContent {
            val componentId = activeComponentId.value
            val currentControlValues = controlValues.value
            if (componentId == null) {
                SideEffect {
                    composedComponentId = null
                    composedControlValues = emptyMap()
                }
                Text(text = "preview-unmounted")
            } else {
                key(componentId) {
                    DisposableEffect(componentId) {
                        mountCount += 1
                        onDispose { disposeCount += 1 }
                    }
                    SideEffect {
                        composedComponentId = componentId
                        composedControlValues = currentControlValues
                    }
                    IenTheme {
                        val scrollState = rememberScrollState()
                        val snackbarHostState = remember { SnackbarHostState() }
                        val coroutineScope = rememberCoroutineScope()
                        val dataStore = rememberPreviewSettingsDataStore()
                        val viewController = remember { UIViewController() }
                        CompositionLocalProvider(LocalUIViewController provides viewController) {
                            KoinApplication(
                                application = {
                                    modules(
                                        module {
                                            single<DataStore<Preferences>>(named(DEFAULT_DATASTORE)) { dataStore }
                                        },
                                    )
                                },
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 240.dp, max = 280.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        DesignSystemComponentPreview(
                                            componentId = componentId,
                                            snackbarHostState = snackbarHostState,
                                            toastState = null,
                                            coroutineScope = coroutineScope,
                                            controlValues = currentControlValues,
                                        )
                                    }
                                    Spacer(Modifier.height(1200.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        mainClock.advanceTimeByFrame()
        waitForIdle()

        var expectedMountCount = 0
        var expectedDisposeCount = 0
        repeat(2) { cycle ->
            components.forEach { component ->
                println("iOS 카탈로그 stress ${cycle + 1}/2: ${component.id}")
                val controls = playgroundControls(component.id)
                val defaultValues = controls.associate { it.key to it.defaultValue }
                val alternateValues = controls.associate { control ->
                    control.key to control.stressAlternateValue()
                }
                if (controls.any { it.stressAlternateValue() != it.defaultValue }) {
                    assertNotEquals(defaultValues, alternateValues, "${component.id} control 값 전환")
                }
                val initialValues = if (cycle == 0) defaultValues else alternateValues
                val transitionedValues = if (cycle == 0) alternateValues else defaultValues

                val mountFrameStart = mainClock.currentTime
                runOnIdle {
                    activeComponentId.value = component.id
                    controlValues.value = initialValues
                }
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > mountFrameStart, "${component.id} mount 프레임 전진")
                waitForIdle()

                expectedMountCount += 1
                runOnIdle {
                    assertEquals(expectedMountCount, mountCount, "${component.id} mount 횟수")
                    assertEquals(expectedDisposeCount, disposeCount, "${component.id} mount 후 dispose 횟수")
                    assertEquals(component.id, composedComponentId, "${component.id} 현재 미리보기")
                    assertEquals(initialValues, composedControlValues, "${component.id} 초기 control 값")
                }
                onNodeWithText("선택한 미리보기를 찾을 수 없습니다.").assertDoesNotExist()

                if (controls.isNotEmpty()) {
                    val transitionFrameStart = mainClock.currentTime
                    runOnIdle { controlValues.value = transitionedValues }
                    mainClock.advanceTimeByFrame()
                    assertTrue(mainClock.currentTime > transitionFrameStart, "${component.id} 전환 프레임 전진")
                    waitForIdle()
                    runOnIdle {
                        assertEquals(component.id, composedComponentId, "${component.id} 전환 후 미리보기")
                        assertEquals(transitionedValues, composedControlValues, "${component.id} 변경 control 값")
                        assertEquals(expectedMountCount, mountCount, "${component.id} control 전환 중 재-mount")
                        assertEquals(expectedDisposeCount, disposeCount, "${component.id} control 전환 중 dispose")
                    }
                }

                onAllNodes(isRoot()).onLast().performTouchInput { swipeUp() }
                mainClock.advanceTimeByFrame()
                waitForIdle()
                onAllNodes(isRoot()).onLast().performTouchInput { swipeDown() }
                mainClock.advanceTimeByFrame()
                waitForIdle()

                val disposeFrameStart = mainClock.currentTime
                runOnIdle { activeComponentId.value = null }
                mainClock.advanceTimeByFrame()
                assertTrue(mainClock.currentTime > disposeFrameStart, "${component.id} dispose 프레임 전진")
                waitForIdle()
                expectedDisposeCount += 1
                runOnIdle {
                    assertEquals(expectedMountCount, mountCount, "${component.id} unmount 후 mount 횟수")
                    assertEquals(expectedDisposeCount, disposeCount, "${component.id} dispose 횟수")
                    assertEquals(null, composedComponentId, "${component.id} unmount 상태")
                }
                onNodeWithText("preview-unmounted").assertExists()
            }
        }

        assertEquals(componentIds.size * 2, mountCount)
        assertEquals(componentIds.size * 2, disposeCount)
    }
}
