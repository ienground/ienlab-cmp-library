package zone.ien.utils.firebase.auth

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FirebaseAuthenticatorTest {
    @Test
    fun `공급자 credential을 Firebase에 전달해 로그인한다`() = runBlocking {
        val credential = AuthCredential.IdToken(
            providerId = AuthProviderIds.Google,
            idToken = "secret-id-token",
            accessToken = "secret-access-token",
        )
        val gateway = FakeFirebaseAuthGateway(
            user = FirebaseAuthUser(
                uid = "user-id",
                email = "user@example.com",
                displayName = "사용자",
                photoUrl = null,
                providerId = AuthProviderIds.Google,
            )
        )
        val authenticator = FirebaseAuthenticator(gateway)

        val result = authenticator.signIn(
            provider = AuthProvider { AuthProviderResult.Authenticated(credential) },
            linkAccount = true,
        )

        val success = assertIs<FirebaseAuthResult.Success>(result)
        assertEquals("user-id", success.user.uid)
        assertSame(credential, gateway.credential)
        assertTrue(gateway.linkAccount)
    }

    @Test
    fun `공급자 취소는 Firebase를 호출하지 않고 취소 결과를 반환한다`() = runBlocking {
        val gateway = FakeFirebaseAuthGateway()
        val authenticator = FirebaseAuthenticator(gateway)

        val result = authenticator.signIn(AuthProvider { AuthProviderResult.Canceled })

        assertIs<FirebaseAuthResult.Canceled>(result)
        assertFalse(gateway.wasCalled)
    }

    @Test
    fun `공급자 오류는 원인을 보존한다`() = runBlocking {
        val expected = IllegalStateException("provider failed")
        val authenticator = FirebaseAuthenticator(FakeFirebaseAuthGateway())

        val result = authenticator.signIn(
            AuthProvider { AuthProviderResult.Failure(expected) }
        )

        val failure = assertIs<FirebaseAuthResult.Failure>(result)
        assertSame(expected, failure.cause)
    }

    @Test
    fun `Firebase 오류는 실패 결과로 반환한다`() = runBlocking {
        val expected = IllegalArgumentException("firebase failed")
        val gateway = FakeFirebaseAuthGateway(error = expected)
        val authenticator = FirebaseAuthenticator(gateway)

        val result = authenticator.signIn(
            AuthProvider {
                AuthProviderResult.Authenticated(
                    AuthCredential.IdToken(AuthProviderIds.Apple, "secret", rawNonce = "nonce")
                )
            }
        )

        val failure = assertIs<FirebaseAuthResult.Failure>(result)
        assertSame(expected, failure.cause)
    }

    @Test
    fun `코루틴 취소는 인증 실패로 변환하지 않는다`() = runBlocking {
        val authenticator = FirebaseAuthenticator(FakeFirebaseAuthGateway())

        assertFailsWith<CancellationException> {
            authenticator.signIn(AuthProvider { throw CancellationException("cancel") })
        }
        Unit
    }

    @Test
    fun `credential 문자열은 민감한 값을 노출하지 않는다`() {
        val credential = AuthCredential.IdToken(
            providerId = AuthProviderIds.Apple,
            idToken = "id-token-secret",
            accessToken = "access-token-secret",
            rawNonce = "nonce-secret",
        )

        val text = credential.toString()

        assertFalse(text.contains("id-token-secret"))
        assertFalse(text.contains("access-token-secret"))
        assertFalse(text.contains("nonce-secret"))
        assertTrue(text.contains(AuthProviderIds.Apple))
    }

    private class FakeFirebaseAuthGateway(
        private val user: FirebaseAuthUser = FirebaseAuthUser("user-id"),
        private val error: Throwable? = null,
    ) : FirebaseAuthGateway {
        var wasCalled: Boolean = false
            private set
        var credential: AuthCredential? = null
            private set
        var linkAccount: Boolean = false
            private set

        override suspend fun signIn(
            credential: AuthCredential,
            linkAccount: Boolean,
        ): FirebaseAuthUser {
            wasCalled = true
            this.credential = credential
            this.linkAccount = linkAccount
            error?.let { throw it }
            return user
        }

        override fun currentUser(): FirebaseAuthUser? = null

        override fun signOut() = Unit
    }
}
