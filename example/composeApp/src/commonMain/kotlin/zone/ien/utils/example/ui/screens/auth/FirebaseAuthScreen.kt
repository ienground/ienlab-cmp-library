package zone.ien.utils.example.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.firebase.auth.AuthCredential
import zone.ien.utils.firebase.auth.AuthProvider
import zone.ien.utils.firebase.auth.AuthProviderIds
import zone.ien.utils.firebase.auth.AuthProviderResult
import zone.ien.utils.firebase.auth.FirebaseAuthResult
import zone.ien.utils.firebase.auth.FirebaseAuthUser
import zone.ien.utils.firebase.auth.rememberFirebaseSignInState
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.TopBarMode

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun FirebaseAuthScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    val backdrop = rememberDefaultBackdrop()
    var lastResult by remember { mutableStateOf<FirebaseAuthResult?>(null) }
    var simulatedUser by remember { mutableStateOf<FirebaseAuthUser?>(null) }

    val googleProvider = remember {
        AuthProvider {
            AuthProviderResult.Authenticated(
                AuthCredential.IdToken(
                    providerId = AuthProviderIds.Google,
                    idToken = "sample-google-id-token",
                    accessToken = "sample-google-access-token",
                )
            )
        }
    }

    val appleProvider = remember {
        AuthProvider {
            AuthProviderResult.Authenticated(
                AuthCredential.IdToken(
                    providerId = AuthProviderIds.Apple,
                    idToken = "sample-apple-id-token",
                    rawNonce = "sample-raw-nonce",
                )
            )
        }
    }

    val googleSignInState = rememberFirebaseSignInState(
        provider = googleProvider,
        onResult = { result ->
            lastResult = result
            if (result is FirebaseAuthResult.Success) {
                simulatedUser = result.user
            }
        }
    )

    val appleSignInState = rememberFirebaseSignInState(
        provider = appleProvider,
        onResult = { result ->
            lastResult = result
            if (result is FirebaseAuthResult.Success) {
                simulatedUser = result.user
            }
        }
    )

    IenAdaptiveTheme(target = Theme.Material3) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = { IenText("Firebase Auth Sample") },
            adaptation = {
                material { mode = TopBarMode.Expanded }
                cupertino { this.backdrop = backdrop }
            },
            modifier = modifier,
        ) { pv, title ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(pv)
                    .padding(horizontal = 24.dp)
            ) {
                title()

                IenText("Firebase Kotlin Auth 대체 라이브러리 연동 샘플입니다.")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenButton(
                        onClick = { googleSignInState.launch() },
                        variant = IenButtonVariant.Fill,
                        display = IenButtonDisplay.Inline,
                    ) {
                        IenText("Google Sign-In")
                    }

                    if (googleSignInState.isInProgress) {
                        CircularProgressIndicator()
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IenButton(
                        onClick = { appleSignInState.launch() },
                        variant = IenButtonVariant.Weak,
                        display = IenButtonDisplay.Inline,
                    ) {
                        IenText("Apple Sign-In")
                    }

                    if (appleSignInState.isInProgress) {
                        CircularProgressIndicator()
                    }
                }

                simulatedUser?.let { user ->
                    Spacer(modifier = Modifier.height(16.dp))
                    IenText("현재 로그인된 사용자:")
                    IenText("UID: ${user.uid}")
                    IenText("Email: ${user.email ?: "N/A"}")
                    IenText("Provider: ${user.providerId ?: "N/A"}")

                    IenButton(
                        onClick = {
                            simulatedUser = null
                            lastResult = null
                        },
                        variant = IenButtonVariant.Fill,
                        display = IenButtonDisplay.Inline,
                    ) {
                        IenText("로그아웃")
                    }
                }

                lastResult?.let { res ->
                    Spacer(modifier = Modifier.height(8.dp))
                    when (res) {
                        is FirebaseAuthResult.Success -> IenText("로그인 성공: ${res.user.uid}")
                        FirebaseAuthResult.Canceled -> IenText("로그인 취소됨")
                        is FirebaseAuthResult.Failure -> IenText("로그인 실패: ${res.cause.message}")
                    }
                }
            }
        }
    }
}
