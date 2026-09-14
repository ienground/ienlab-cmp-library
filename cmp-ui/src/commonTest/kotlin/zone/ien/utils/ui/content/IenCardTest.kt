package zone.ien.utils.ui.content

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.defaultLightIenColorScheme

class IenCardTest {
    private val colors = defaultLightIenColorScheme()

    @Test
    fun `Filled Solid 브랜드 카드는 원래 톤 색상과 대비 콘텐츠 색상을 사용한다`() {
        assertEquals(
            IenCardColors(
                container = colors.brand,
                content = colors.onBrand,
                border = colors.brand,
            ),
            resolveIenCardColors(
                variant = IenCardVariant.Filled,
                toneVariant = IenCardToneVariant.Solid,
                tone = IenSemanticTone.Brand,
                colors = colors,
            ),
        )
    }

    @Test
    fun `Filled Weak 성공 카드는 약한 톤 배경과 원래 톤 콘텐츠 색상을 사용한다`() {
        assertEquals(
            IenCardColors(
                container = colors.successWeak,
                content = colors.success,
                border = colors.successWeak,
            ),
            resolveIenCardColors(
                variant = IenCardVariant.Filled,
                toneVariant = IenCardToneVariant.Weak,
                tone = IenSemanticTone.Success,
                colors = colors,
            ),
        )
    }

    @Test
    fun `Outlined 브랜드 카드는 표면과 톤 테두리를 사용한다`() {
        assertEquals(
            IenCardColors(
                container = colors.surface,
                content = colors.textPrimary,
                border = colors.brand,
            ),
            resolveIenCardColors(
                variant = IenCardVariant.Outlined,
                toneVariant = IenCardToneVariant.Solid,
                tone = IenSemanticTone.Brand,
                colors = colors,
            ),
        )
    }

    @Test
    fun `Outlined 중립 카드는 기본 테두리 색상을 사용한다`() {
        assertEquals(
            IenCardColors(
                container = colors.surface,
                content = colors.textPrimary,
                border = colors.border,
            ),
            resolveIenCardColors(
                variant = IenCardVariant.Outlined,
                toneVariant = IenCardToneVariant.Weak,
                tone = IenSemanticTone.Neutral,
                colors = colors,
            ),
        )
    }
}
