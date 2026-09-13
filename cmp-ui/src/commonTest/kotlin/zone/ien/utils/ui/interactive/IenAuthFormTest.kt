package zone.ien.utils.ui.interactive

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IenAuthFormTest {
    @Test
    fun `회원가입 모드에는 확인 비밀번호 상태와 보안 조건을 전달할 수 있다`() {
        val state = IenAuthFormState(
            confirmPassword = IenTextFieldState(
                status = IenFieldStatus.Success("일치합니다"),
            ),
        )
        val rules = listOf(
            IenPasswordRule("8자 이상", satisfied = true),
            IenPasswordRule("숫자 포함", satisfied = false),
        )

        assertEquals(IenFieldStatus.Success("일치합니다"), state.confirmPassword.status)
        assertEquals(2, rules.size)
        assertTrue(rules.first().satisfied)
    }

    @Test
    fun `게스트 액션은 선택적으로 라벨과 클릭을 소유한다`() {
        var clicked = false
        val guestAction = IenAuthGuestAction("게스트로 계속하기") { clicked = true }

        assertEquals("게스트로 계속하기", guestAction.label)
        guestAction.onClick()
        assertTrue(clicked)
    }

    @Test
    fun `provider 모델은 호출자가 공급한 식별자와 이름을 보존한다`() {
        val provider = IenAuthProvider(
            id = "google",
            label = "Google",
            icon = {},
        )

        assertEquals("google", provider.id)
        assertEquals("Google", provider.label)
    }
}
