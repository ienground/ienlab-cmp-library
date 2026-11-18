package zone.ien.utils.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
import zone.ien.utils.ui.screen.LocalIenTopBarTransition

/**
 * 네비게이션 디스플레이를 생성하는 기본 Composable 함수
 *
 * [BaseNavigationFrame]이 iOS 네이티브 모드를 활성화하면 NativeStack을 사용합니다.
 * [BaseNavDisplay.navigationGraph]로 표시한 루트 엔트리는 UI 없는 하위 탭 그래프로
 * 실행하며, 상위 공유 화면과 그래프의 상태·ViewModel 수명을 공통으로 관리합니다.
 *
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
 * @param animateTopBar Cupertino 상단 버튼의 위치·크기·내용을 화면 전환과 함께 전환합니다.
 * 기본값은 false이며, Material에서는 적용하지 않습니다.
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
    animateTopBar: Boolean = false,
    entryProvider: (key: T) -> NavEntry<T>,
) {
    BaseNavDisplay(
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
        animateTopBar = animateTopBar,
        entryMetadata = { emptyMap() },
        entryProvider = entryProvider,
    )
}

/**
 * route별 기본 진입 메타데이터를 공통으로 제공하는 [BaseNavDisplay]입니다.
 * 개별 entry의 메타데이터가 우선하며, 진입 후 바뀌는 제목·액션은 `NativeScreenBar`로 전달합니다.
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
    animateTopBar: Boolean = false,
    entryMetadata: @Composable (T) -> Map<String, Any>,
    entryProvider: (key: T) -> NavEntry<T>,
) {
    val guards = LocalNavigationEntryGuards.current.filter { it.backStack === backStack }
    val visibleBackStack = if (guards.isEmpty()) backStack else {
        backStack.filter { route -> guards.all { it.isAllowed(route) } }.ifEmpty { backStack.take(1) }
    }
    val metadata = (visibleBackStack + listOfNotNull(currentNavigationRoute(backStack))).distinct()
        .associateWith { entryMetadata(it) }
    val provider: (T) -> NavEntry<T> = { route ->
        val entry = entryProvider(route)
        NavEntry(route, contentKey = entry.contentKey, metadata = metadata[route].orEmpty() + entry.metadata) {
            if (guards.all { it.isAllowed(route) }) entry.Content()
        }
    }
    if (NavigationTabEntry(backStack, provider)) return
    if (NativeBaseNavDisplay(backStack, onBack, entryDecorators, provider, visibleBackStack)) return

    @Composable
    fun Display(scope: SharedTransitionScope?) {
        val topBarTransition = remember(scope, animateTopBar) {
            if (animateTopBar && scope != null) NavigationTopBarTransition(scope) else null
        }
        CompositionLocalProvider(LocalIenTopBarTransition provides topBarTransition) {
            NavDisplay(
                backStack = visibleBackStack,
                modifier = modifier,
                contentAlignment = contentAlignment,
                onBack = onBack,
                entryDecorators = entryDecorators,
                sceneStrategies = sceneStrategies,
                sceneDecoratorStrategies = sceneDecoratorStrategies,
                sharedTransitionScope = scope,
                sizeTransform = sizeTransform,
                transitionSpec = transitionSpec,
                popTransitionSpec = popTransitionSpec,
                predictivePopTransitionSpec = predictivePopTransitionSpec,
                entryProvider = provider
            )
        }
    }

    if (animateTopBar && sharedTransitionScope == null) {
        SharedTransitionLayout { Display(this) }
    } else {
        Display(sharedTransitionScope)
    }
}
