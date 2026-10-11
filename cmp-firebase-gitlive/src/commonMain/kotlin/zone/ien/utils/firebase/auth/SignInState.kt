package zone.ien.utils.firebase.auth

import androidx.compose.runtime.Composable

/** GitLive Firebase 인증 SDK에 맞는 기본 인증기를 연결해 인증 상태를 생성합니다. */
@Composable
fun rememberFirebaseSignInState(
    provider: AuthProvider,
    linkAccount: Boolean = false,
    authenticator: FirebaseAuthenticator? = null,
    onResult: (FirebaseAuthResult) -> Unit,
): SignInState = rememberFirebaseSignInStateCore(
    provider = provider,
    linkAccount = linkAccount,
    authenticator = authenticator,
    createAuthenticator = { FirebaseAuthenticator() },
    onResult = onResult,
)
