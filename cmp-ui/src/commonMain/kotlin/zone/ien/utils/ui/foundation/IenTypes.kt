package zone.ien.utils.ui.foundation

/**
 * UI 요소의 의미적 톤(Semantic Tone)을 정의하는 열거형 클래스입니다.
 */
enum class IenSemanticTone {
    /** 기본/중립 톤 */
    Neutral,
    /** 브랜드 고유 톤 */
    Brand,
    /** 성공 상태를 나타내는 톤 */
    Success,
    /** 경고 상태를 나타내는 톤 */
    Warning,
    /** 위험/에러 상태를 나타내는 톤 */
    Danger,
    /** 정보 제공 상태를 나타내는 톤 */
    Info,
}

/**
 * Cupertino 스위치의 선택 상태 색상 정책입니다.
 */
enum class IenSwitchColorPolicy {
    /** 플랫폼 기본 색상을 사용합니다. Cupertino에서는 초록색을 유지합니다. */
    Platform,

    /** [IenTheme]의 브랜드 색상을 사용합니다. */
    Brand,
}

/**
 * UI 콘텐츠의 강조도(Emphasis) 수준을 정의하는 열거형 클래스입니다.
 */
enum class IenContentEmphasis {
    /** 높은 강조 수준 (주요 텍스트, 활성 상태 등) */
    High,
    /** 중간 강조 수준 (보조 텍스트, 중간 강조 등) */
    Medium,
    /** 낮은 강조 수준 (비활성 텍스트, 힌트 등) */
    Low,
}
