package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleLarge
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIProgressView
import platform.UIKit.UIProgressViewStyle

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean,
    isWavyIndicator: Boolean
) {
    val viewController = LocalUIViewController.current

    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }

    LaunchedEffect(visible) {
        if (visible) {
            val alert = UIAlertController.alertControllerWithTitle("\n", null, UIAlertControllerStyleAlert)

            val loadingIndicator = UIActivityIndicatorView()

            loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyleLarge
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
        } else {
            alertRef?.dismissViewControllerAnimated(true, null)
            alertRef = null
        }
    }

}

@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean
) {
    val viewController = LocalUIViewController.current
    val currentProgress = progress()

    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }
    var progressViewRef by remember { mutableStateOf<UIProgressView?>(null) }

    LaunchedEffect(visible) {
        if (visible) {
            val alert = UIAlertController.alertControllerWithTitle("\n", null, UIAlertControllerStyleAlert)

            val margin = 16.0
            val progressView = UIProgressView(UIProgressViewStyle.UIProgressViewStyleDefault).apply {
                translatesAutoresizingMaskIntoConstraints = false
                tintColor = viewController.view.tintColor
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
        } else {
            alertRef?.dismissViewControllerAnimated(true, null)
            alertRef = null
            progressViewRef = null
        }
    }

    LaunchedEffect(currentProgress) {
        if (visible) {
            progressViewRef?.setProgress(currentProgress, animated = true)
        }
    }

}