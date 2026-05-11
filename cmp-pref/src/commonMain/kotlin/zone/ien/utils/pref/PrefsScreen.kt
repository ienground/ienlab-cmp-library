package zone.ien.utils.pref

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle

/**
 * DataStore 지원을 포함한 전체 설정 화면을 생성하는 Composable 함수입니다.
 *
 * 이 함수는 적절한 DataStore 컨텍스트, 스크롤 상태 및 스타일을 갖춘 설정 화면을 구성합니다.
 * DataStore를 사용하여 사용자 설정을 유지하는 설정 화면을 구축하기 위한 기초를 제공합니다.
 *
 * @param dataStore 설정을 저장하는 데 사용되는 DataStore 인스턴스
 * @param title 화면 제목을 표시하는 Composable
 * @param modifier 레이아웃에 적용할 Modifier
 * @param fullHeight 화면이 전체 높이를 차지해야 하는지 여부
 * @param scrollState 스크롤에 사용될 스크롤 상태
 * @param shape 화면 컨테이너의 모양
 * @param backdrop 화면의 백드롭 구성
 * @param content 설정 항목을 포함하는 Composable 컨텐츠 블록
 */
@Composable
fun PrefsScreen(
    dataStore: DataStore<Preferences>,
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = rememberScrollState(),
    shape: Shape = RectangleShape,
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    content: @Composable (ColumnScope.() -> Unit)
) {
    CompositionLocalProvider(LocalPrefsDataStore provides dataStore) {
        AdaptiveProvideSectionStyle(
            style = SectionStyle.InsetGrouped,
            modifier = modifier,
            fullHeight = fullHeight,
            scrollState = scrollState,
            shape = shape,
            backdrop = backdrop,
            title = title,
            content = {
                content()
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
    }
}

/**
 * 설정 컴포넌트에 DataStore를 제공하기 위한 CompositionLocal입니다.
 * 이를 통해 DataStore 인스턴스를 직접 전달하지 않고도 접근할 수 있습니다.
 */
val LocalPrefsDataStore = staticCompositionLocalOf<DataStore<Preferences>> {
    error("No DataStore provided. Ensure content is wrapped in PrefsScreen.")
}