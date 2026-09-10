package zone.ien.utils.ui.state

import kotlin.test.Test
import kotlin.test.assertIs

class FailureTest {
    @Test
    fun featureFailureCanImplementFailureContract() {
        val failure: Failure = TestFailure.LoadFailed

        assertIs<TestFailure>(failure)
    }

    private sealed interface TestFailure : Failure {
        data object LoadFailed : TestFailure
    }
}
