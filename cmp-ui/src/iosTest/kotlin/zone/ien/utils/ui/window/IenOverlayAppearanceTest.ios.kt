package zone.ien.utils.ui.window

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.scene.AppearanceCallbacks
import androidx.compose.ui.scene.ComposeLayersViewControllerAppearanceFixture
import androidx.compose.ui.scene.UnrelatedViewControllerAppearanceFixture
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.test.TestResult
import platform.UIKit.UIViewController
import platform.UIKit.beginAppearanceTransition
import platform.UIKit.endAppearanceTransition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zone.ien.utils.initializeIenIos
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestBeginCount
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestCreateOwnedOverrideController
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestInstallBeginCounter
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestIsComposeLayersController
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestOwnedBeginCount
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestOwnedEndCount
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestRestoreBeginCounter
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestSimulateAutomaticCompletion
import zone.ien.utils.ui.appearance.test.IENOverlayAppearanceTestSimulateAutomaticAppearance

@OptIn(ExperimentalForeignApi::class, ExperimentalTestApi::class, ExperimentalFoundationApi::class)
class IenOverlayAppearanceTest {
    @Test
    fun initializationEnablesTextMenusAndGuardsAutomaticAppearance(): TestResult = runOnUIKitMain {
        ComposeFoundationFlags.isNewContextMenuEnabled = false
        initializeIenIos()
        initializeIenIos()
        assertTrue(ComposeFoundationFlags.isNewContextMenuEnabled)
        val controller = ComposeLayersViewControllerAppearanceFixture()
        assertTrue(IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            IENOverlayAppearanceTestSimulateAutomaticAppearance(controller, appearing = true)
            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()
            IENOverlayAppearanceTestSimulateAutomaticCompletion(controller)

            assertEquals(1, IENOverlayAppearanceTestBeginCount())
            assertEquals(1, controller.callbacks.willAppearCount)
            assertEquals(1, controller.callbacks.didAppearCount)
        }
    }

    @Test
    fun unrelatedViewControllersKeepTheirAppearanceBehavior(): TestResult = runOnUIKitMain {
        assertTrue(enableIenOverlayAppearanceFix())
        val controller = UnrelatedViewControllerAppearanceFixture()
        assertFalse(IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            IENOverlayAppearanceTestSimulateAutomaticAppearance(controller, appearing = true)
            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()
            IENOverlayAppearanceTestSimulateAutomaticCompletion(controller)

            assertEquals(2, IENOverlayAppearanceTestBeginCount())
        }
    }

    @Test
    fun manualOnlyTransitionsStillReachUIKit(): TestResult = runOnUIKitMain {
        assertTrue(enableIenOverlayAppearanceFix())
        val controller = ComposeLayersViewControllerAppearanceFixture()
        assertTrue(IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()
            assertEquals(1, IENOverlayAppearanceTestBeginCount())

            controller.beginAppearanceTransition(isAppearing = false, animated = false)
            controller.endAppearanceTransition()
            assertEquals(2, IENOverlayAppearanceTestBeginCount())
            assertEquals(1, controller.callbacks.willAppearCount)
            assertEquals(1, controller.callbacks.didAppearCount)
            assertEquals(1, controller.callbacks.willDisappearCount)
            assertEquals(1, controller.callbacks.didDisappearCount)
        }
    }

    @Test
    fun endingAnAutomaticTransitionClearsItsPendingDirection(): TestResult = runOnUIKitMain {
        assertTrue(enableIenOverlayAppearanceFix())
        val controller = ComposeLayersViewControllerAppearanceFixture()
        assertTrue(IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            IENOverlayAppearanceTestSimulateAutomaticAppearance(controller, appearing = true)
            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()
            IENOverlayAppearanceTestSimulateAutomaticCompletion(controller)
            assertEquals(1, IENOverlayAppearanceTestBeginCount())
            assertEquals(1, controller.callbacks.willAppearCount)
            assertEquals(1, controller.callbacks.didAppearCount)

            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()
            assertEquals(2, IENOverlayAppearanceTestBeginCount())
        }
    }

    @Test
    fun pendingAppearanceDoesNotSuppressTheOppositeDirection(): TestResult = runOnUIKitMain {
        assertTrue(enableIenOverlayAppearanceFix())
        val baseline = runOppositeDirectionTransition(isComposeLayers = false)
        val guarded = runOppositeDirectionTransition(isComposeLayers = true)
        assertEquals(baseline, guarded)
    }

    @Test
    fun controllersWithTheirOwnAppearanceOverridesKeepThem(): TestResult = runOnUIKitMain {
        assertTrue(enableIenOverlayAppearanceFix())
        val controller = assertNotNull(IENOverlayAppearanceTestCreateOwnedOverrideController())
        assertTrue(IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            controller.beginAppearanceTransition(isAppearing = true, animated = false)
            controller.endAppearanceTransition()

            assertEquals(1, IENOverlayAppearanceTestOwnedBeginCount())
            assertEquals(1, IENOverlayAppearanceTestOwnedEndCount())
            assertEquals(1, IENOverlayAppearanceTestBeginCount())
        }
    }

    private fun runOnUIKitMain(block: () -> Unit): TestResult =
        runComposeUiTest { runOnUiThread(block) }

    private fun withBeginCounter(
        controller: UIViewController,
        block: () -> Unit,
    ) {
        assertTrue(IENOverlayAppearanceTestInstallBeginCounter(controller))
        try {
            block()
        } finally {
            IENOverlayAppearanceTestRestoreBeginCounter()
        }
    }

    private fun runOppositeDirectionTransition(
        isComposeLayers: Boolean,
    ): AppearanceCallbacks {
        val callbacks = AppearanceCallbacks()
        val controller: UIViewController = if (isComposeLayers) {
            ComposeLayersViewControllerAppearanceFixture(callbacks)
        } else {
            UnrelatedViewControllerAppearanceFixture(callbacks)
        }
        assertEquals(isComposeLayers, IENOverlayAppearanceTestIsComposeLayersController(controller))

        withBeginCounter(controller) {
            IENOverlayAppearanceTestSimulateAutomaticAppearance(controller, appearing = true)
            controller.beginAppearanceTransition(isAppearing = false, animated = false)
            controller.endAppearanceTransition()
            IENOverlayAppearanceTestSimulateAutomaticCompletion(controller)

            assertEquals(2, IENOverlayAppearanceTestBeginCount())
        }

        return callbacks.copy()
    }
}
