package zone.ien.utils.firebase.auth.apple

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.firebase.auth.FirebaseAuth as AndroidFirebaseAuth
import com.google.firebase.auth.OAuthProvider as AndroidOAuthProvider
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.auth
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.LaunchingSignInState
import zone.ien.utils.firebase.auth.NoOpSignInState
import zone.ien.utils.firebase.auth.SignInState

@Composable
actual fun rememberFirebaseAppleSignInState(
    requestScopes: List<AppleSignInRequestScope>,
    linkAccount: Boolean,
    gateway: FirebaseAuthGateway,
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentScopes by rememberUpdatedState(requestScopes)
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, gateway) {
        LaunchingSignInState(scope) {
            val activity = context.findActivity()
            if (activity == null) {
                currentOnResult(Result.failure(IllegalStateException("Activity is null")))
                return@LaunchingSignInState
            }

            try {
                val androidAuth = AndroidFirebaseAuth.getInstance()
                val providerBuilder = AndroidOAuthProvider.newBuilder("apple.com")
                val scopesList = currentScopes.map {
                    when (it) {
                        AppleSignInRequestScope.Email -> "email"
                        AppleSignInRequestScope.FullName -> "name"
                    }
                }
                providerBuilder.scopes = scopesList

                val pendingResult = androidAuth.pendingAuthResult
                if (pendingResult != null) {
                    pendingResult.await()
                } else {
                    val currentUser = androidAuth.currentUser
                    if (currentLinkAccount && currentUser != null) {
                        currentUser.startActivityForLinkWithProvider(activity, providerBuilder.build()).await()
                    } else {
                        androidAuth.startActivityForSignInWithProvider(activity, providerBuilder.build()).await()
                    }
                }
                currentOnResult(Result.success(Firebase.auth.currentUser))
            } catch (e: Throwable) {
                currentOnResult(Result.failure(e))
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
