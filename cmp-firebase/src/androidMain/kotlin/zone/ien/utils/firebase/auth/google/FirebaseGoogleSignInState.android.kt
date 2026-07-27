package zone.ien.utils.firebase.auth.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.auth
import zone.ien.utils.firebase.auth.AuthCredential
import zone.ien.utils.firebase.auth.AuthProviderIds
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.LaunchingSignInState
import zone.ien.utils.firebase.auth.NoOpSignInState
import zone.ien.utils.firebase.auth.SignInState

@Composable
actual fun rememberFirebaseGoogleSignInState(
    linkAccount: Boolean,
    filterByAuthorizedAccounts: Boolean,
    isAutoSelectEnabled: Boolean,
    scopes: List<String>,
    gateway: FirebaseAuthGateway,
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleAuthProvider = remember { GoogleAuthProvider.get() }
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentFilter by rememberUpdatedState(filterByAuthorizedAccounts)
    val currentAutoSelect by rememberUpdatedState(isAutoSelectEnabled)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, googleAuthProvider, gateway) {
        LaunchingSignInState(scope) {
            val serverId = googleAuthProvider.credentials.serverId
            try {
                val credentialManager = CredentialManager.create(context)
                val cred = try {
                    val option = GetGoogleIdOption.Builder()
                        .setServerClientId(serverId)
                        .setFilterByAuthorizedAccounts(currentFilter)
                        .setAutoSelectEnabled(currentAutoSelect)
                        .build()
                    val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
                    credentialManager.getCredential(context = context, request = request).credential
                } catch (e: NoCredentialException) {
                    if (currentFilter) {
                        val option = GetGoogleIdOption.Builder()
                            .setServerClientId(serverId)
                            .setFilterByAuthorizedAccounts(false)
                            .setAutoSelectEnabled(false)
                            .build()
                        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
                        credentialManager.getCredential(context = context, request = request).credential
                    } else {
                        throw e
                    }
                }

                if (cred is CustomCredential && cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(cred.data)
                    val realIdToken = googleIdTokenCredential.idToken

                    val firebaseCredential = AuthCredential.IdToken(
                        providerId = AuthProviderIds.Google,
                        idToken = realIdToken,
                    )
                    gateway.signIn(firebaseCredential, currentLinkAccount)
                    val signedInUser = Firebase.auth.currentUser
                    if (signedInUser != null) {
                        currentOnResult(Result.success(signedInUser))
                    } else {
                        currentOnResult(Result.failure(IllegalStateException(
                            "Sign-in succeeded but currentUser is null"
                        )))
                    }
                } else {
                    currentOnResult(Result.failure(IllegalStateException("Unexpected credential type: ${cred.type}")))
                }
            } catch (e: Throwable) {
                currentOnResult(Result.failure(e))
            }
        }
    }
}
