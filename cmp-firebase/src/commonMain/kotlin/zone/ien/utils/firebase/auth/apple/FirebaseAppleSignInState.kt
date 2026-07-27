package zone.ien.utils.firebase.auth.apple

import androidx.compose.runtime.Composable
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.SignInState
import zone.ien.utils.firebase.auth.ZoneFirebaseAuthGateway

/**
 * Apple Sign-In을 실행하고 Firebase 세션과 교환하는 Compose State 생성 함수입니다.
 * Apple 플랫폼은 별도의 serverId 초기화 설정 없이 곧바로 동작합니다.
 */
@Composable
expect fun rememberFirebaseAppleSignInState(
    requestScopes: List<AppleSignInRequestScope> = listOf(AppleSignInRequestScope.FullName, AppleSignInRequestScope.Email),
    linkAccount: Boolean = false,
    gateway: FirebaseAuthGateway = ZoneFirebaseAuthGateway(),
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState
