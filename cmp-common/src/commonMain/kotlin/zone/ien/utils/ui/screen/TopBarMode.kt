package zone.ien.utils.ui.screen

/**
 * 상단 바의 표시 방식을 정의하는 열거형 클래스입니다.
 */
enum class TopBarMode {
    /**
     * 작은 상단 바가 스크롤 여부와 관계없이 고정된 위치에 표시됩니다.
     */
    Static,

    /**
     * 큰 네비게이션 타이틀을 콘텐츠 상단에 표시하고, 스크롤 시 작은 상단 바로 전환됩니다.
     */
    Expanded,
}
