package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenSemanticTone
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleLarge
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIProgressView
import platform.UIKit.UIProgressViewStyle
import zone.ien.hig.toUIColor
import zone.ien.utils.ui.interactive.toneColor

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean,
    isWavyIndicator: Boolean,
    tone: IenSemanticTone,
) {
    val viewController = LocalUIViewController.current
    val nativeTintColor = toneColor(tone).toUIColor()

    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }

    SideEffect {
        alertRef?.view?.tintColor = nativeTintColor
    }
    if (!visible) return

    DisposableEffect(viewController) {
        val alert = UIAlertController.alertControllerWithTitle("\n", null, UIAlertControllerStyleAlert)
        alert.view.tintColor = nativeTintColor

        val loadingIndicator = UIActivityIndicatorView()

        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyleLarge
        loadingIndicator.color = nativeTintColor
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.translatesAutoresizingMaskIntoConstraints = false
        loadingIndicator.startAnimating()

        alert.view.addSubview(loadingIndicator)
        NSLayoutConstraint.activateConstraints(listOf(
            loadingIndicator.centerXAnchor().constraintEqualToAnchor(alert.view.centerXAnchor),
            loadingIndicator.centerYAnchor().constraintEqualToAnchor(alert.view.centerYAnchor)
        ))

        alertRef = alert
        viewController.presentViewController(alert, animated = true, completion = null)
        onDispose {
            loadingIndicator.stopAnimating()
            if (viewController.presentedViewController == alert) {
                viewController.dismissViewControllerAnimated(true, null)
            }
            if (alertRef == alert) alertRef = null
        }
    }

}

@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean,
    tone: IenSemanticTone,
) {
    val viewController = LocalUIViewController.current
    val currentProgress by rememberUpdatedState(progress)
    val nativeTintColor = toneColor(tone).toUIColor()

    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }
    var progressViewRef by remember { mutableStateOf<UIProgressView?>(null) }

    SideEffect {
        alertRef?.view?.tintColor = nativeTintColor
        progressViewRef?.tintColor = nativeTintColor
    }

    LaunchedEffect(visible, progressViewRef) {
        val progressView = progressViewRef
        if (visible && progressView != null) {
            snapshotFlow { currentProgress() }.collect { progressView.setProgress(it, animated = true) }
        }
    }
    if (!visible) return

    DisposableEffect(viewController) {
        val alert = UIAlertController.alertControllerWithTitle("\n", null, UIAlertControllerStyleAlert)
        alert.view.tintColor = nativeTintColor

        val margin = 16.0
        val progressView = UIProgressView(UIProgressViewStyle.UIProgressViewStyleDefault).apply {
            translatesAutoresizingMaskIntoConstraints = false
            tintColor = nativeTintColor
        }

        alert.view.addSubview(progressView)

        alertRef = alert
        progressViewRef = progressView

        NSLayoutConstraint.activateConstraints(listOf(
            progressView.leadingAnchor.constraintEqualToAnchor(alert.view.leadingAnchor, margin),
            progressView.trailingAnchor.constraintEqualToAnchor(alert.view.trailingAnchor, -margin),
            progressView.centerYAnchor.constraintEqualToAnchor(alert.view.centerYAnchor)
        ))

        viewController.presentViewController(alert, animated = true, completion = null)
        onDispose {
            if (viewController.presentedViewController == alert) {
                viewController.dismissViewControllerAnimated(true, null)
            }
            if (alertRef == alert) {
                alertRef = null
                progressViewRef = null
            }
        }
    }

}
