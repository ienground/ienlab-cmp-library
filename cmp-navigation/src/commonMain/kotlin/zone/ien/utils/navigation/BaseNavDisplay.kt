package zone.ien.utils.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent
import zone.ien.utils.navigation.transition.fadeInOutPopTransitionSpec

/**
 * 네비게이션 디스플레이를 생성하는 기본 Composable 함수
 * @param backStack 네비게이션 백스택
 * @param modifier Modifier
 * @param contentAlignment 콘텐츠 정렬
 * @param onBack 뒤로가기 처리 함수
 * @param entryDecorators NavEntry 데코레이터 리스트
 * @param sceneStrategies Scene 전략 리스트
 * @param sceneDecoratorStrategies Scene 데코레이터 전략 리스트
 * @param sharedTransitionScope 공유 전환 범위
 * @param sizeTransform 크기 변환
 * @param transitionSpec 전환 스펙
 * @param popTransitionSpec pop 전환 스펙
 * @param predictivePopTransitionSpec 예측 pop 전환 스펙
 * @param entryProvider NavEntry 제공자
 */
@Composable
fun <T : NavKey> BaseNavDisplay(
    backStack: NavBackStack<T>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = { if (backStack.size > 1) backStack.navigateBack() },
    entryDecorators: List<NavEntryDecorator<T>> =
        listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator() // 뷰모델 꺼짐 보장
        ),
    sceneStrategies: List<SceneStrategy<T>> = listOf(SinglePaneSceneStrategy()),
    sceneDecoratorStrategies: List<SceneDecoratorStrategy<T>> = emptyList(),
    sharedTransitionScope: SharedTransitionScope? = null,
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = defaultTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = defaultPopTransitionSpec(),
    predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = fadeInOutPopTransitionSpec(),
    entryProvider: (key: T) -> NavEntry<T>,
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        contentAlignment = contentAlignment,
        onBack = onBack,
        entryDecorators = entryDecorators,
        sceneStrategies = sceneStrategies,
        sceneDecoratorStrategies = sceneDecoratorStrategies,
        sharedTransitionScope = sharedTransitionScope,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider
    )
}