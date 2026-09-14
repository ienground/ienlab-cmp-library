package zone.ien.utils.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface

/** 카드 컨테이너의 표현 방식을 정의합니다. */
enum class IenCardVariant {
    /** 배경색과 고도로 구분하는 카드입니다. */
    Filled,

    /** 표면과 테두리로 구분하는 카드입니다. */
    Outlined,
}

/** 카드의 기본 색상 강도를 정의합니다. */
enum class IenCardToneVariant {
    /** 의미적 톤의 원래 색상을 사용합니다. */
    Solid,

    /** 의미적 톤의 약한 배경색을 사용합니다. */
    Weak,
}

/** 카드 컨테이너, 콘텐츠, 테두리에 사용할 색상 묶음입니다. */
@Immutable
data class IenCardColors(
    /** 카드 배경색입니다. */
    val container: Color,
    /** 카드 내부 콘텐츠의 기본 전경색입니다. */
    val content: Color,
    /** 카드 테두리 색상입니다. 투명하면 테두리를 그리지 않습니다. */
    val border: Color,
)

/** [IenCard]에서 사용하는 기본 색상과 스타일을 제공합니다. */
object IenCardDefaults {
    /** 현재 테마와 변형에 맞는 카드 색상 묶음을 생성합니다. */
    @Composable
    fun colors(
        variant: IenCardVariant = IenCardVariant.Filled,
        tone: IenSemanticTone = IenSemanticTone.Neutral,
        toneVariant: IenCardToneVariant = IenCardToneVariant.Solid,
        container: Color = resolveIenCardColors(variant, toneVariant, tone, IenTheme.colors).container,
        content: Color = resolveIenCardColors(variant, toneVariant, tone, IenTheme.colors).content,
        border: Color = resolveIenCardColors(variant, toneVariant, tone, IenTheme.colors).border,
    ): IenCardColors = IenCardColors(
        container = container,
        content = content,
        border = border,
    )
}

/**
 * 테마 색상과 콘텐츠를 담는 재사용 가능한 카드 컨테이너입니다.
 *
 * [variant]와 [toneVariant]로 Filled/Outlined 및 Solid/Weak 조합을 선택할 수
 * 있으며, [onClick]을 지정하면 동일한 컨테이너를 클릭 가능한 카드로 사용할 수
 * 있습니다.
 */
@Composable
fun IenCard(
    modifier: Modifier = Modifier,
    variant: IenCardVariant = IenCardVariant.Filled,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    toneVariant: IenCardToneVariant = IenCardToneVariant.Solid,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.lg),
    contentPadding: PaddingValues = PaddingValues(IenTheme.spacing.md),
    colors: IenCardColors = IenCardDefaults.colors(
        variant = variant,
        tone = tone,
        toneVariant = toneVariant,
    ),
    tonalElevation: Dp = if (variant == IenCardVariant.Filled) {
        IenTheme.elevation.raised
    } else {
        IenTheme.elevation.none
    },
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val cardModifier = if (onClick == null) {
        modifier
    } else {
        modifier.clickable(
            role = Role.Button,
            onClick = { onClick() },
        )
    }

    IenSurface(
        modifier = cardModifier,
        color = colors.container,
        contentColor = colors.content,
        shape = shape,
        border = colors.border
            .takeUnless { it == Color.Transparent }
            ?.let { BorderStroke(IenTheme.stroke.thin, it) },
        tonalElevation = tonalElevation,
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

internal fun resolveIenCardColors(
    variant: IenCardVariant,
    toneVariant: IenCardToneVariant,
    tone: IenSemanticTone,
    colors: IenColorScheme,
): IenCardColors {
    val solidContainer = when (tone) {
        IenSemanticTone.Neutral -> colors.surfaceRaised
        IenSemanticTone.Brand -> colors.brand
        IenSemanticTone.Success -> colors.success
        IenSemanticTone.Warning -> colors.warning
        IenSemanticTone.Danger -> colors.danger
        IenSemanticTone.Info -> colors.info
    }
    val weakContainer = when (tone) {
        IenSemanticTone.Neutral -> colors.surfaceWeak
        IenSemanticTone.Brand -> colors.brandWeak
        IenSemanticTone.Success -> colors.successWeak
        IenSemanticTone.Warning -> colors.warningWeak
        IenSemanticTone.Danger -> colors.dangerWeak
        IenSemanticTone.Info -> colors.infoWeak
    }
    val solidContent = when (tone) {
        IenSemanticTone.Neutral -> colors.textPrimary
        IenSemanticTone.Brand -> colors.onBrand
        IenSemanticTone.Success -> colors.onSuccess
        IenSemanticTone.Warning -> colors.onWarning
        IenSemanticTone.Danger -> colors.onDanger
        IenSemanticTone.Info -> colors.onInfo
    }
    val toneContent = when (tone) {
        IenSemanticTone.Neutral -> colors.textPrimary
        IenSemanticTone.Brand -> colors.brand
        IenSemanticTone.Success -> colors.success
        IenSemanticTone.Warning -> colors.warning
        IenSemanticTone.Danger -> colors.danger
        IenSemanticTone.Info -> colors.info
    }
    val container = if (toneVariant == IenCardToneVariant.Solid) solidContainer else weakContainer

    return when (variant) {
        IenCardVariant.Filled -> IenCardColors(
            container = container,
            content = if (toneVariant == IenCardToneVariant.Solid) solidContent else toneContent,
            border = container,
        )

        IenCardVariant.Outlined -> IenCardColors(
            container = colors.surface,
            content = colors.textPrimary,
            border = when {
                tone == IenSemanticTone.Neutral -> colors.border
                toneVariant == IenCardToneVariant.Weak -> weakContainer
                else -> solidContainer
            },
        )
    }
}
