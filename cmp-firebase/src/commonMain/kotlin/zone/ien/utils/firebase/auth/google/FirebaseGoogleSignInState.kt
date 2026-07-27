package zone.ien.utils.firebase.auth.google

import androidx.compose.runtime.Composable
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.SignInState
import zone.ien.utils.firebase.auth.ZoneFirebaseAuthGateway

/**
 * Google Sign-In을 실행하고 Firebase 세션과 교환하는 Compose State 생성 함수입니다.
 * 앱 시작 시 `GoogleAuthProvider.create(GoogleAuthCredentials(serverId = ...))`가 호출되어 있어야 합니다.
 */
@Composable
expect fun rememberFirebaseGoogleSignInState(
    linkAccount: Boolean = false,
    /** Android Credential Manager 전용 (iOS는 무시됨) */
    filterByAuthorizedAccounts: Boolean = false,
    /** Android Credential Manager 전용 (iOS는 무시됨) */
    isAutoSelectEnabled: Boolean = true,
    scopes: List<String> = listOf("email", "profile"),
    gateway: FirebaseAuthGateway = ZoneFirebaseAuthGateway(),
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState
