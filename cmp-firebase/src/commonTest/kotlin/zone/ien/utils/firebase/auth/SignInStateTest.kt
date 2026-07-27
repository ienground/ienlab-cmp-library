package zone.ien.utils.firebase.auth

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SignInStateTest {
    @Test
    fun `진행 중인 인증은 중복 실행하지 않는다`() = runBlocking {
        val release = CompletableDeferred<Unit>()
        var launchCount = 0
        val state = LaunchingSignInState(this + Dispatchers.Unconfined) {
            launchCount += 1
            release.await()
        }

        state.launch()
        state.launch()

        assertTrue(state.isInProgress)
        assertEquals(1, launchCount)

        release.complete(Unit)
        yield()

        assertFalse(state.isInProgress)
    }
}
