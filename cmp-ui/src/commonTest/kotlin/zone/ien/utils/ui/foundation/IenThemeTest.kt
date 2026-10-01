package zone.ien.utils.ui.foundation

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class IenThemeTest {
    @Test
    fun `기본 스위치 색상 정책은 플랫폼 기본값이다`() {
        assertEquals(IenSwitchColorPolicy.Platform, defaultIenTokens().switchColorPolicy)
    }

    @Test
    fun `스위치 색상 정책은 토큰에서 브랜드 색상으로 변경할 수 있다`() {
        assertEquals(
            IenSwitchColorPolicy.Brand,
            defaultIenTokens().copy(switchColorPolicy = IenSwitchColorPolicy.Brand).switchColorPolicy,
        )
    }

    @Test
    fun `테마 색상은 배경색이 아니라 다크 테마 플래그로 선택한다`() {
        val lightColor = Color(0xFFF2F4F6)
        val darkColor = Color(0xFF20252B)

        assertEquals(
            lightColor,
            resolveThemeColor(isDarkTheme = false, lightColor = lightColor, darkColor = darkColor),
        )
        assertEquals(
            darkColor,
            resolveThemeColor(isDarkTheme = true, lightColor = lightColor, darkColor = darkColor),
        )
    }
}
