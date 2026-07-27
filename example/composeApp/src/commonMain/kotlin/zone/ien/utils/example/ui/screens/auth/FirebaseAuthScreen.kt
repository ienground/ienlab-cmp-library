package zone.ien.utils.example.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.firebase.auth.apple.rememberFirebaseAppleSignInState
import zone.ien.utils.firebase.auth.google.rememberFirebaseGoogleSignInState
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.TopBarMode

import zone.ien.utils.example.TAG

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun FirebaseAuthScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    val backdrop = rememberDefaultBackdrop()
    var lastResult by remember { mutableStateOf<Result<FirebaseUser?>?>(null) }
    var currentUser by remember { mutableStateOf<FirebaseUser?>(null) }

    // 1. KMPAuth 호환 구글 로그인 State
    val googleSignInState = rememberFirebaseGoogleSignInState(
        onResult = { result ->
            lastResult = result
            currentUser = result.getOrNull()
        }
    )

    // 2. KMPAuth 호환 애플 로그인 State (iOS 전용/서버 ID 불필요)
    val appleSignInState = rememberFirebaseAppleSignInState(
        onResult = { result ->
            lastResult = result
            currentUser = result.getOrNull()
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

                IenText("KMPAuth 스타일 Google 및 Apple Sign-In 연동 샘플입니다.")

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

                currentUser?.let { user ->
                    Spacer(modifier = Modifier.height(16.dp))
                    IenText("현재 로그인된 사용자:")
                    IenText("UID: ${user.uid}")
                    IenText("Email: ${user.email ?: "N/A"}")

                    IenButton(
                        onClick = {
                            currentUser = null
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
                    val user = res.getOrNull()
                    val error = res.exceptionOrNull()
                    if (user != null) {
                        IenText("로그인 성공: ${user.uid}")
                    } else if (error != null) {
                        IenText("로그인 결과: ${error.message}")
                    }
                }
            }
        }
    }
}
