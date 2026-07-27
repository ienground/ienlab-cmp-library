package zone.ien.utils.firebase.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

/** 버튼 등에 연결해 Firebase 인증 흐름을 시작하는 상태입니다. */
@Stable
interface SignInState {
    val isInProgress: Boolean
    fun launch()
}

internal class LaunchingSignInState(
    private val scope: CoroutineScope,
    private val block: suspend () -> Unit,
) : SignInState {
    override var isInProgress: Boolean by mutableStateOf(false)
        private set

    override fun launch() {
        if (isInProgress) return
        isInProgress = true
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                block()
            } finally {
                isInProgress = false
            }
        }
    }
}

private object NoOpSignInState : SignInState {
    override val isInProgress: Boolean = false
    override fun launch() = Unit
}

/**
 * KMPAuth의 state 기반 사용 경험을 유지하는 Firebase 인증 상태를 생성합니다.
 *
 * 공급자별 플랫폼 로그인은 [AuthProvider]에 캡슐화하며, 결과는 [onResult]로 전달합니다.
 */
@Composable
fun rememberFirebaseSignInState(
    provider: AuthProvider,
    linkAccount: Boolean = false,
    authenticator: FirebaseAuthenticator? = null,
    onResult: (FirebaseAuthResult) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val scope = rememberCoroutineScope()
    val resolvedAuthenticator = authenticator ?: remember { FirebaseAuthenticator() }
    val currentProvider by rememberUpdatedState(provider)
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, resolvedAuthenticator) {
        LaunchingSignInState(scope) {
            currentOnResult(
                resolvedAuthenticator.signIn(
                    provider = currentProvider,
                    linkAccount = currentLinkAccount,
                )
            )
        }
    }
}
