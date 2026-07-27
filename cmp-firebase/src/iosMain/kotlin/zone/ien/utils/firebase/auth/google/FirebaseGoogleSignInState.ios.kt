package zone.ien.utils.firebase.auth.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuth
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthDataResult
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthUIDelegateProtocol
import swiftPMImport.zone.ien.firebase.firebase.auth.FIROAuthProvider
import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.auth
import zone.ien.utils.firebase.auth.AuthCredential
import zone.ien.utils.firebase.auth.AuthProviderIds
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.LaunchingSignInState
import zone.ien.utils.firebase.auth.NoOpSignInState
import zone.ien.utils.firebase.auth.SignInState
import kotlin.coroutines.resume

@Composable
actual fun rememberFirebaseGoogleSignInState(
    linkAccount: Boolean,
    /** Android Credential Manager 전용 (iOS는 무시됨) */
    filterByAuthorizedAccounts: Boolean,
    /** Android Credential Manager 전용 (iOS는 무시됨) */
    isAutoSelectEnabled: Boolean,
    scopes: List<String>,
    gateway: FirebaseAuthGateway,
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val scope = rememberCoroutineScope()
    val googleAuthProvider = remember { GoogleAuthProvider.get() }
    val currentScopes by rememberUpdatedState(scopes)
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, googleAuthProvider, gateway) {
        LaunchingSignInState(scope) {
            val serverId = googleAuthProvider.credentials.serverId
            try {
                val userResult = signInWithGoogleIos(serverId, currentScopes, currentLinkAccount)
                currentOnResult(userResult)
            } catch (e: Exception) {
                currentOnResult(Result.failure(e))
            }
        }
    }
}
@OptIn(ExperimentalForeignApi::class)
internal class IosAuthUIDelegate : FIRAuthUIDelegateProtocol, NSObject() {
    override fun presentViewController(
        viewControllerToPresent: UIViewController,
        animated: Boolean,
        completion: (() -> Unit)?,
    ) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(viewControllerToPresent, animated = animated, completion = completion)
    }

    override fun dismissViewControllerAnimated(
        flag: Boolean,
        completion: (() -> Unit)?,
    ) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.dismissViewControllerAnimated(flag, completion = completion)
    }
}

@OptIn(ExperimentalForeignApi::class)
private suspend fun signInWithGoogleIos(
    serverId: String,
    scopes: List<String>,
    linkAccount: Boolean,
): Result<FirebaseUser?> = suspendCancellableCoroutine { continuation ->
    val provider = FIROAuthProvider.providerWithProviderID("google.com")
    val customParams = mutableMapOf<Any?, Any?>()
    if (serverId.isNotBlank()) {
        customParams["client_id"] = serverId
    }
    provider.setCustomParameters(customParams)
    provider.setScopes(scopes)

    val handleResult: (FIRAuthDataResult?, NSError?) -> Unit = { result, error ->
        if (continuation.isActive) {
            if (error != null || result == null) {
                val message = buildString {
                    append("Google Sign-In failed")
                    error?.let { e ->
                        append(" [domain=${e.domain}:code=${e.code}]")
                        e.localizedDescription.let { append(": $it") }
                    }
                }
                continuation.resume(Result.failure(IllegalStateException(message)))
            } else {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    continuation.resume(Result.success(currentUser))
                } else {
                    continuation.resume(Result.failure(IllegalStateException(
                        "Sign-in succeeded but currentUser is null"
                    )))
                }
            }
        }
    }

    val uiDelegate = IosAuthUIDelegate()
    provider.getCredentialWithUIDelegate(uiDelegate) { credential, error ->
        if (error != null || credential == null) {
            if (continuation.isActive) {
                val message = buildString {
                    append("Failed to get Google credential")
                    error?.let { e ->
                        append(" [domain=${e.domain}:code=${e.code}]")
                        e.localizedDescription.let { append(": $it") }
                    }
                }
                continuation.resume(Result.failure(IllegalStateException(message)))
            }
            return@getCredentialWithUIDelegate
        }

        val iosAuth = FIRAuth.auth()
        val currentUser = iosAuth.currentUser()
        if (linkAccount && currentUser != null) {
            currentUser.linkWithCredential(credential, handleResult)
        } else {
            iosAuth.signInWithCredential(credential, handleResult)
        }
    }
}
