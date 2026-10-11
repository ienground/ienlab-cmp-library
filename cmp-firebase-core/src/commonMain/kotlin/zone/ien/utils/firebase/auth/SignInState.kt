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

/** 인증 작업을 코루틴으로 실행하는 [SignInState]입니다. */
class LaunchingSignInState(
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

/** 미리보기 등에서 인증 동작을 실행하지 않는 상태입니다. */
object NoOpSignInState : SignInState {
    override val isInProgress: Boolean = false
    override fun launch() = Unit
}

/** SDK 모듈에서 공통 인증 상태 구현을 호출하기 위한 함수입니다. */
@Composable
fun rememberFirebaseSignInStateCore(
    provider: AuthProvider,
    linkAccount: Boolean,
    authenticator: FirebaseSignInFlow?,
    createAuthenticator: () -> FirebaseSignInFlow,
    onResult: (FirebaseAuthResult) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val scope = rememberCoroutineScope()
    val rememberedAuthenticator = remember { createAuthenticator() }
    val resolvedAuthenticator = authenticator ?: rememberedAuthenticator
    val currentProvider by rememberUpdatedState(provider)
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, resolvedAuthenticator) {
        LaunchingSignInState(scope) {
            currentOnResult(
                resolvedAuthenticator.authenticate(
                    provider = currentProvider,
                    linkAccount = currentLinkAccount,
                )
            )
        }
    }
}
