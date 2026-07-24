package zone.ien.utils.ui.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * IenTheme에서 사용하는 색상 스키마를 정의하는 데이터 클래스입니다.
 *
 * @property background 화면의 기본 배경 색상.
 * @property surface 카드, 다이얼로그 등 컨텐츠가 올라가는 기본 표면 색상.
 * @property surfaceRaised 기본 표면보다 더 강조되거나 떠 있는 느낌을 주는 표면 색상.
 * @property surfaceWeak 기본 표면보다 약하게 강조되는 대비가 낮은 표면 색상.
 * @property textPrimary 가장 중요한 텍스트(예: 제목, 본문 핵심 내용)에 사용하는 색상.
 * @property textSecondary 보조적인 설명 텍스트나 덜 중요한 정보에 사용하는 색상.
 * @property textTertiary 비활성화되지는 않았으나 부가적인 텍스트나 플레이스홀더 등에 사용하는 색상.
 * @property textDisabled 비활성화된 상태의 텍스트에 사용하는 색상.
 * @property border 요소 간의 경계를 구분하는 기본 테두리 색상.
 * @property borderStrong 보다 명확하게 경계를 구분해야 할 때 사용하는 강한 테두리 색상.
 * @property overlay 다이얼로그나 바텀 시트 뒤의 화면을 어둡게 가리는 레이어에 사용하는 색상.
 * @property surfaceVariant 다양하게 활용할 수 있는 대체 표면 색상.
 * @property brand 브랜드 기본 색상.
 * @property onBrand 브랜드 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property brandWeak 브랜드 색상의 대비가 낮은(연한) 배경 색상.
 * @property onBrandWeak 대비가 낮은 브랜드 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property success 성공 상태를 나타내는 기본 색상.
 * @property onSuccess 성공 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property successWeak 성공 상태의 대비가 낮은(연한) 배경 색상.
 * @property onSuccessWeak 대비가 낮은 성공 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property warning 경고 상태를 나타내는 기본 색상.
 * @property onWarning 경고 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property warningWeak 경고 상태의 대비가 낮은(연한) 배경 색상.
 * @property onWarningWeak 대비가 낮은 경고 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property danger 위험/에러 상태를 나타내는 기본 색상.
 * @property onDanger 위험/에러 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property dangerWeak 위험/에러 상태의 대비가 낮은(연한) 배경 색상.
 * @property onDangerWeak 대비가 낮은 위험/에러 상태 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property info 정보를 제공할 때 사용하는 기본 색상.
 * @property onInfo 정보 색상 위에 올라가는 텍스트/아이콘 색상.
 * @property infoWeak 정보 색상의 대비가 낮은(연한) 배경 색상.
 * @property onInfoWeak 대비가 낮은 정보 색상 위에 올라가는 텍스트/아이콘 색상.
 */
@Immutable
data class IenColorScheme(
    val background: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val surfaceWeak: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val border: Color,
    val borderStrong: Color,
    val overlay: Color,
    val surfaceVariant: Color,

    // Brand
    val brand: Color,
    val onBrand: Color,
    val brandWeak: Color,
    val onBrandWeak: Color,

    // Success
    val success: Color,
    val onSuccess: Color,
    val successWeak: Color,
    val onSuccessWeak: Color,

    // Warning
    val warning: Color,
    val onWarning: Color,
    val warningWeak: Color,
    val onWarningWeak: Color,

    // Danger
    val danger: Color,
    val onDanger: Color,
    val dangerWeak: Color,
    val onDangerWeak: Color,

    // Info
    val info: Color,
    val onInfo: Color,
    val infoWeak: Color,
    val onInfoWeak: Color,
)

internal object IenLightColorTokens {
    val background = Color(0xFFFFFFFF)
    val surface = Color(0xFFFFFFFF)
    val surfaceRaised = Color(0xFFFFFFFF)
    val surfaceWeak = Color(0xFFF9FAFB)
    val textPrimary = Color(0xFF191F28)
    val textSecondary = Color(0xFF4E5968)
    val textTertiary = Color(0xFF8B95A1)
    val textDisabled = Color(0xFFB0B8C1)
    val border = Color(0xFFE5E8EB)
    val borderStrong = Color(0xFFD1D6DB)
    val overlay = Color(0x99000000)
    val surfaceVariant = Color(0xFFF2F4F6)
    val brand = Color(0xFF3182F6)
    val onBrand = Color(0xFFFFFFFF)
    val brandWeak = Color(0xFFE8F3FF)
    val onBrandWeak = Color(0xFF3182F6)
    val success = Color(0xFF03B26C)
    val onSuccess = Color(0xFFFFFFFF)
    val successWeak = Color(0xFFF0FAF6)
    val onSuccessWeak = Color(0xFF03B26C)
    val warning = Color(0xFFFE9800)
    val onWarning = Color(0xFFFFFFFF)
    val warningWeak = Color(0xFFFFF3E0)
    val onWarningWeak = Color(0xFFFE9800)
    val danger = Color(0xFFF04452)
    val onDanger = Color(0xFFFFFFFF)
    val dangerWeak = Color(0xFFFFEEEE)
    val onDangerWeak = Color(0xFFF04452)
    val info = Color(0xFF18A5A5)
    val onInfo = Color(0xFFFFFFFF)
    val infoWeak = Color(0xFFEDF8F8)
    val onInfoWeak = Color(0xFF18A5A5)
}

internal object IenDarkColorTokens {
    val background = Color(0xFF101318)
    val surface = Color(0xFF171B22)
    val surfaceRaised = Color(0xFF202631)
    val surfaceWeak = Color(0xFF11151B)
    val textPrimary = Color(0xFFF2F4F6)
    val textSecondary = Color(0xFFD1D6DB)
    val textTertiary = Color(0xFF8B95A1)
    val textDisabled = Color(0xFF6B7684)
    val border = Color(0xFF333D4B)
    val borderStrong = Color(0xFF4E5968)
    val overlay = Color(0xB3000000)
    val surfaceVariant = Color(0xFF202632)
    val brand = Color(0xFF64A8FF)
    val onBrand = Color(0xFFFFFFFF)
    val brandWeak = Color(0xFF17365D)
    val onBrandWeak = Color(0xFF64A8FF)
    val success = Color(0xFF3FD599)
    val onSuccess = Color(0xFFFFFFFF)
    val successWeak = Color(0xFF113B2B)
    val onSuccessWeak = Color(0xFF3FD599)
    val warning = Color(0xFFFFBD51)
    val onWarning = Color(0xFFFFFFFF)
    val warningWeak = Color(0xFF4A3211)
    val onWarningWeak = Color(0xFFFFBD51)
    val danger = Color(0xFFFB8890)
    val onDanger = Color(0xFFFFFFFF)
    val dangerWeak = Color(0xFF4A1D22)
    val onDangerWeak = Color(0xFFFB8890)
    val info = Color(0xFF58C7C7)
    val onInfo = Color(0xFFFFFFFF)
    val infoWeak = Color(0xFF123A3A)
    val onInfoWeak = Color(0xFF58C7C7)
}

internal fun defaultLightIenColorScheme() = IenColorScheme(
    background = IenLightColorTokens.background,
    surface = IenLightColorTokens.surface,
    surfaceRaised = IenLightColorTokens.surfaceRaised,
    surfaceWeak = IenLightColorTokens.surfaceWeak,
    textPrimary = IenLightColorTokens.textPrimary,
    textSecondary = IenLightColorTokens.textSecondary,
    textTertiary = IenLightColorTokens.textTertiary,
    textDisabled = IenLightColorTokens.textDisabled,
    border = IenLightColorTokens.border,
    borderStrong = IenLightColorTokens.borderStrong,
    overlay = IenLightColorTokens.overlay,
    surfaceVariant = IenLightColorTokens.surfaceVariant,
    brand = IenLightColorTokens.brand,
    onBrand = IenLightColorTokens.onBrand,
    brandWeak = IenLightColorTokens.brandWeak,
    onBrandWeak = IenLightColorTokens.onBrandWeak,
    success = IenLightColorTokens.success,
    onSuccess = IenLightColorTokens.onSuccess,
    successWeak = IenLightColorTokens.successWeak,
    onSuccessWeak = IenLightColorTokens.onSuccessWeak,
    warning = IenLightColorTokens.warning,
    onWarning = IenLightColorTokens.onWarning,
    warningWeak = IenLightColorTokens.warningWeak,
    onWarningWeak = IenLightColorTokens.onWarningWeak,
    danger = IenLightColorTokens.danger,
    onDanger = IenLightColorTokens.onDanger,
    dangerWeak = IenLightColorTokens.dangerWeak,
    onDangerWeak = IenLightColorTokens.onDangerWeak,
    info = IenLightColorTokens.info,
    onInfo = IenLightColorTokens.onInfo,
    infoWeak = IenLightColorTokens.infoWeak,
    onInfoWeak = IenLightColorTokens.onInfoWeak,
)

internal fun defaultDarkIenColorScheme() = IenColorScheme(
    background = IenDarkColorTokens.background,
    surface = IenDarkColorTokens.surface,
    surfaceRaised = IenDarkColorTokens.surfaceRaised,
    surfaceWeak = IenDarkColorTokens.surfaceWeak,
    textPrimary = IenDarkColorTokens.textPrimary,
    textSecondary = IenDarkColorTokens.textSecondary,
    textTertiary = IenDarkColorTokens.textTertiary,
    textDisabled = IenDarkColorTokens.textDisabled,
    border = IenDarkColorTokens.border,
    borderStrong = IenDarkColorTokens.borderStrong,
    overlay = IenDarkColorTokens.overlay,
    surfaceVariant = IenDarkColorTokens.surfaceVariant,
    brand = IenDarkColorTokens.brand,
    onBrand = IenDarkColorTokens.onBrand,
    brandWeak = IenDarkColorTokens.brandWeak,
    onBrandWeak = IenDarkColorTokens.onBrandWeak,
    success = IenDarkColorTokens.success,
    onSuccess = IenDarkColorTokens.onSuccess,
    successWeak = IenDarkColorTokens.successWeak,
    onSuccessWeak = IenDarkColorTokens.onSuccessWeak,
    warning = IenDarkColorTokens.warning,
    onWarning = IenDarkColorTokens.onWarning,
    warningWeak = IenDarkColorTokens.warningWeak,
    onWarningWeak = IenDarkColorTokens.onWarningWeak,
    danger = IenDarkColorTokens.danger,
    onDanger = IenDarkColorTokens.onDanger,
    dangerWeak = IenDarkColorTokens.dangerWeak,
    onDangerWeak = IenDarkColorTokens.onDangerWeak,
    info = IenDarkColorTokens.info,
    onInfo = IenDarkColorTokens.onInfo,
    infoWeak = IenDarkColorTokens.infoWeak,
    onInfoWeak = IenDarkColorTokens.onInfoWeak,
)
