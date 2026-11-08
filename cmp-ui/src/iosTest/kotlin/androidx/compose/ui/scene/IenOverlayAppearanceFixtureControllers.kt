package androidx.compose.ui.scene

import platform.UIKit.UIViewController

internal data class AppearanceCallbacks(
    var willAppearCount: Int = 0,
    var didAppearCount: Int = 0,
    var willDisappearCount: Int = 0,
    var didDisappearCount: Int = 0,
)

internal class ComposeLayersViewControllerAppearanceFixture(
    val callbacks: AppearanceCallbacks = AppearanceCallbacks(),
) : UIViewController(nibName = null, bundle = null) {
    override fun viewWillAppear(animated: Boolean) {
        callbacks.willAppearCount++
        super.viewWillAppear(animated)
    }

    override fun viewDidAppear(animated: Boolean) {
        callbacks.didAppearCount++
        super.viewDidAppear(animated)
    }

    override fun viewWillDisappear(animated: Boolean) {
        callbacks.willDisappearCount++
        super.viewWillDisappear(animated)
    }

    override fun viewDidDisappear(animated: Boolean) {
        callbacks.didDisappearCount++
        super.viewDidDisappear(animated)
    }
}

internal class UnrelatedViewControllerAppearanceFixture(
    val callbacks: AppearanceCallbacks = AppearanceCallbacks(),
) : UIViewController(nibName = null, bundle = null) {
    override fun viewWillAppear(animated: Boolean) {
        callbacks.willAppearCount++
        super.viewWillAppear(animated)
    }

    override fun viewDidAppear(animated: Boolean) {
        callbacks.didAppearCount++
        super.viewDidAppear(animated)
    }

    override fun viewWillDisappear(animated: Boolean) {
        callbacks.willDisappearCount++
        super.viewWillDisappear(animated)
    }

    override fun viewDidDisappear(animated: Boolean) {
        callbacks.didDisappearCount++
        super.viewDidDisappear(animated)
    }
}
