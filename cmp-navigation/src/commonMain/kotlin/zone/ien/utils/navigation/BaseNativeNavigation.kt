package zone.ien.utils.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import zone.ien.navigation.NativeNavigationTint
import zone.ien.navigation.NativeNavigationFrame
import zone.ien.navigation.NativeStack
import zone.ien.navigation.NativeTab as SwiftNativeTab
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.asPainter
import zone.ien.navigation.nativeScreen
import zone.ien.navigation.NativeTabBarTint
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.navigation.NativeTabs
import zone.ien.utils.ui.screen.ProvideNativeScreenPadding

private val LocalNativeNavigationEnabled = staticCompositionLocalOf { false }
private val LocalNativeNavigationTopEntry = staticCompositionLocalOf { true }
private val LocalNativeParentEntries = staticCompositionLocalOf<NativeParentEntries?> { null }
private val LocalNativeTabConfiguration = staticCompositionLocalOf<NativeTabConfiguration?> { null }
private class NavigationRouteOverride(val backStack: NavBackStack<*>, val route: NavKey)
private val LocalNavigationRouteOverride = staticCompositionLocalOf<NavigationRouteOverride?> { null }
internal class NavigationEntryGuard(
    val backStack: NavBackStack<*>,
    val isAllowed: (NavKey) -> Boolean,
)
internal val LocalNavigationEntryGuards = staticCompositionLocalOf<List<NavigationEntryGuard>> { emptyList() }
private const val NavigationGraphMetadata = "zone.ien.utils.navigation.graph"

internal expect val usesNativeNavigationPlatform: Boolean

private class NativeTabConfiguration(
    val tabs: List<SwiftNativeTab<NavKey>>,
    val selectedTab: Int,
    val onTabSelected: (Int) -> Unit,
    val entries: Map<NavKey, NavEntry<NavKey>>,
    val overlay: @Composable () -> Unit,
)

/**
 * 기존 [BaseNavDisplay]를 감싸 첫 엔트리를 네이티브 탭으로 표시합니다.
 * [tabBackStack]은 앱과 UIKit이 함께 사용하는 탭 선택 상태이며, 상세 화면의 push/pop은
 * 기존 BaseNavDisplay의 백스택을 그대로 사용합니다. 다른 플랫폼에서는 [content]를 실행합니다.
 * [overlay]는 현재 보이는 화면 위에서 한 번 실행됩니다.
 */
@Composable
fun <T : NavKey> BaseNavigationTabs(
    tabBackStack: NavBackStack<T>,
    tabs: List<T>,
    title: @Composable (T) -> String,
    systemImage: (T) -> String,
    tabContent: @Composable (T) -> Unit,
    largeTitle: Boolean = true,
    overlay: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    NavigationTabs(
        tabBackStack, tabs, title,
        createTab = { route, label -> SwiftNativeTab(label, systemImage(route), listOf(route)) },
        tabContent, largeTitle, overlay, content,
    )
}

/** IconData의 기본·선택 아이콘을 실제 UIKit 탭으로 전달합니다. */
@Composable
fun <T : NavKey> NativeTab(
    title: String,
    icon: IconData,
    backStack: List<T>,
    selectedIcon: IconData? = null,
    onBack: () -> Unit = { (backStack as? MutableList<T>)?.removeLastOrNull() },
): SwiftNativeTab<T> = SwiftNativeTab(
    title = title,
    backStack = backStack,
    image = icon.asPainter(),
    selectedImage = selectedIcon?.asPainter(),
    onBack = onBack,
)

/** 기존 탭의 IconData와 선택 아이콘을 사용하여 BaseNavDisplay를 감쌉니다. */
@Composable
fun <T : NavKey> BaseNavigationTabs(
    tabBackStack: NavBackStack<T>,
    tabs: List<T>,
    title: @Composable (T) -> String,
    icon: @Composable (T) -> IconData,
    tabContent: @Composable (T) -> Unit,
    selectedIcon: @Composable (T) -> IconData? = { null },
    largeTitle: Boolean = true,
    overlay: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    NavigationTabs(
        tabBackStack, tabs, title,
        createTab = { route, label -> NativeTab<NavKey>(label, icon(route), listOf(route), selectedIcon(route)) },
        tabContent, largeTitle, overlay, content,
    )
}

@Composable
private fun <T : NavKey> NavigationTabs(
    tabBackStack: NavBackStack<T>,
    tabs: List<T>,
    title: @Composable (T) -> String,
    createTab: @Composable (T, String) -> SwiftNativeTab<NavKey>,
    tabContent: @Composable (T) -> Unit,
    largeTitle: Boolean,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!isNativeNavigation) {
        content()
        overlay()
        return
    }
    require(tabs.isNotEmpty() && tabs.distinct().size == tabs.size) { "탭은 비어 있거나 중복될 수 없습니다." }
    val titles = tabs.associateWith { title(it) }
    val configuration = NativeTabConfiguration(
        tabs = tabs.map { createTab(it, titles.getValue(it)) },
        selectedTab = tabs.indexOf(tabBackStack.lastOrNull()).coerceAtLeast(0),
        onTabSelected = { index -> tabBackStack.replaceTop(tabs[index]) },
        entries = tabs.associate { route ->
            route to NavEntry<NavKey>(
                key = route,
                metadata = nativeScreen(title = titles.getValue(route), largeTitle = largeTitle),
            ) {
                CompositionLocalProvider(
                    LocalNavigationRouteOverride provides NavigationRouteOverride(tabBackStack, route),
                ) { tabContent(route) }
            }
        },
        overlay = overlay,
    )
    NativeTabBarTint(IenTheme.colors.brand) {
        CompositionLocalProvider(LocalNativeTabConfiguration provides configuration, content = content)
    }
}

/** 현재 화면을 렌더링하는 route입니다. 탭별 화면에서는 공유 선택 상태와 독립된 해당 탭의 route를 반환합니다. */
@Composable
@Suppress("UNCHECKED_CAST")
fun <T : NavKey> currentNavigationRoute(backStack: NavBackStack<T>): T? {
    val override = LocalNavigationRouteOverride.current
    // 동일한 typed 백스택으로 등록한 route만 해당 백스택의 타입으로 되돌립니다.
    return if (override != null && override.backStack === backStack) override.route as T else backStack.lastOrNull()
}

/** 현재 화면이 사용자 입력과 화면 전용 부수 효과를 실행할 수 있는 최상위 화면인지 반환합니다. */
val isNavigationEntryActive: Boolean
    @Composable get() = !isNativeNavigation || isNativeNavigationTopEntry

/** 해당 백스택의 현재 route가 [route]이며 화면이 활성 상태인지 확인합니다. */
@Composable
fun <T : NavKey> isNavigationRouteActive(backStack: NavBackStack<T>, route: T): Boolean =
    isNavigationEntryActive && currentNavigationRoute(backStack) == route

/** UIKit 탭 바를 사용하는 화면에서는 앱의 탭 바를 중복 표시하지 않습니다. */
@Composable
fun BaseNavigationTabBar(content: @Composable () -> Unit) {
    if (LocalNavigationRouteOverride.current == null) content()
}

/**
 * 허용하지 않은 엔트리의 내용과 진입 효과를 차단합니다.
 * 차단된 최상위 엔트리는 루트를 남기고 제거한 뒤 [onDenied]에 전달합니다.
 */
@Composable
@Suppress("UNCHECKED_CAST")
fun <T : NavKey> BaseNavigationGuard(
    backStack: NavBackStack<T>,
    isAllowed: (T) -> Boolean,
    onDenied: (T) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val latestOnDenied by rememberUpdatedState(onDenied)
    val denied = backStack.lastOrNull()?.takeUnless(isAllowed)
    LaunchedEffect(denied) {
        if (denied != null) {
            if (backStack.size > 1 && backStack.lastOrNull() == denied) backStack.removeLastOrNull()
            latestOnDenied(denied)
        }
    }
    // BaseNavDisplay는 동일한 typed 백스택의 엔트리에만 이 정책을 적용합니다.
    val guards = LocalNavigationEntryGuards.current + NavigationEntryGuard(backStack) { isAllowed(it as T) }
    CompositionLocalProvider(LocalNavigationEntryGuards provides guards, content = content)
}

@Composable
internal fun <T : NavKey> NavigationTabEntry(
    backStack: NavBackStack<T>,
    entryProvider: (T) -> NavEntry<T>,
): Boolean {
    if (LocalNavigationRouteOverride.current?.backStack !== backStack) return false
    currentNavigationRoute(backStack)?.let { entryProvider(it).Content() }
    return true
}

/** 현재 호스트가 네이티브 내비게이션을 사용하는지 반환합니다. */
val isNativeNavigation: Boolean
    @Composable get() = LocalNativeNavigationEnabled.current

/** 현재 엔트리가 선택된 탭과 상위 공유 화면을 합친 스택의 맨 위인지 반환합니다. */
val isNativeNavigationTopEntry: Boolean
    @Composable get() = LocalNativeNavigationTopEntry.current

/**
 * 기존 테마와 DI를 유지하며 네이티브 내비게이션을 활성화합니다.
 * iOS에서는 NativeNavigationViewController 안에서 호출해야 합니다.
 * [providers]는 UI에 의존하지 않는 IenTheme 토큰·DI 공급자입니다.
 * IenAdaptiveTheme·CupertinoTheme 등 화면 테마와 루트 UI는 [frame]에 둡니다.
 */
@Composable
fun BaseNavigationFrame(
    providers: @Composable (content: @Composable () -> Unit) -> Unit = { it() },
    frame: @Composable (content: @Composable () -> Unit) -> Unit = { it() },
    content: @Composable () -> Unit,
) {
    NativeNavigationFrame(
        providers = { body ->
            CompositionLocalProvider(LocalNativeNavigationEnabled provides usesNativeNavigationPlatform) {
                providers {
                    NativeNavigationTint(IenTheme.colors.brand, content = body)
                }
            }
        },
        frame = { body ->
            ProvideNativeScreenPadding { frame(body) }
        },
        content = content,
    )
}

/** BaseNavDisplay의 루트 엔트리를 UI 없는 하위 탭 그래프로 선언하는 메타데이터입니다. */
object BaseNavDisplay {
    /** 하위 그래프의 저장 상태와 ViewModel 수명은 부모 디스플레이가 관리합니다. */
    fun navigationGraph(): Map<String, Any> = mapOf(NavigationGraphMetadata to true)
}

internal fun isNavigationGraph(entry: NavEntry<*>): Boolean = entry.metadata[NavigationGraphMetadata] == true

internal class NativeParentEntries(
    val keyedEntries: List<Pair<NavKey, NavEntry<NavKey>>>,
    val hasEntries: () -> Boolean,
    val onBack: () -> Unit,
) {
    val entries: List<NavEntry<NavKey>> get() = keyedEntries.map { it.second }
}

internal fun nativeParentEntries(
    localEntries: List<Pair<NavKey, NavEntry<NavKey>>>,
    localOnBack: () -> Unit,
    parent: NativeParentEntries?,
    localHasEntries: () -> Boolean = { localEntries.isNotEmpty() },
): NativeParentEntries {
    val localKeys = localEntries.map { it.first }
    require(localKeys.distinct().size == localKeys.size) {
        "탭과 공유 스택의 라우트 키는 중복될 수 없습니다."
    }
    val parentEntries = parent?.keyedEntries.orEmpty()
    require(localEntries.map { it.first }.toSet().intersect(parentEntries.map { it.first }.toSet()).isEmpty()) {
        "부모와 하위 그래프의 라우트 키는 서로 달라야 합니다."
    }
    return NativeParentEntries(
        keyedEntries = localEntries + parentEntries,
        hasEntries = { localHasEntries() || parent?.hasEntries?.invoke() == true },
        onBack = { if (parent?.hasEntries?.invoke() == true) parent.onBack() else localOnBack() },
    )
}

@Composable
private fun <T : NavKey> currentEntryProvider(keys: List<T>, provider: (T) -> NavEntry<T>): (T) -> NavEntry<T> {
    val latestEntries by rememberUpdatedState(keys.associateWith(provider))
    return remember {
        { key ->
            val entry = latestEntries.getValue(key)
            NavEntry(key, contentKey = entry.contentKey, metadata = entry.metadata) {
                (latestEntries[key] ?: entry).Content()
            }
        }
    }
}

private fun <T : NavKey> keyedEntry(key: T, entry: NavEntry<T>): Pair<NavKey, NavEntry<NavKey>> =
    key to NavEntry<NavKey>(key, contentKey = entry.contentKey, metadata = entry.metadata) { entry.Content() }

@Composable
private fun <T : NavKey> topEntryDecorator(topContentKey: Any?): NavEntryDecorator<T> {
    val latestTopKey by rememberUpdatedState(topContentKey)
    return remember {
        NavEntryDecorator { entry ->
            CompositionLocalProvider(LocalNativeNavigationTopEntry provides (entry.contentKey == latestTopKey)) {
                entry.Content()
            }
        }
    }
}

/** 네이티브 호스트에서는 UI 없는 그래프를 먼저 실행하고, 화면은 UIKit에 맡깁니다. */
@Composable
internal fun <T : NavKey> NativeBaseNavDisplay(
    backStack: NavBackStack<T>,
    onBack: () -> Unit,
    entryDecorators: List<NavEntryDecorator<T>>,
    entryProvider: (T) -> NavEntry<T>,
    visibleBackStack: List<T> = backStack,
): Boolean {
    if (!isNativeNavigation) return false
    val provider = currentEntryProvider(visibleBackStack, entryProvider)
    val decorated = rememberDecoratedNavEntries(visibleBackStack, entryDecorators, provider)
    val currentDecorated by rememberUpdatedState(visibleBackStack.zip(decorated).toMap())
    val firstKey = visibleBackStack.firstOrNull()
    val tabConfiguration = LocalNativeTabConfiguration.current
    if (tabConfiguration != null) {
        val sharedEntries = visibleBackStack.drop(1).zip(decorated.drop(1))
            .associate { keyedEntry(it.first, it.second) }
        val entries = tabConfiguration.entries + sharedEntries
        CompositionLocalProvider(LocalNativeTabConfiguration provides null) {
            NativeTabNavDisplay<NavKey>(
                tabs = tabConfiguration.tabs,
                selectedTab = tabConfiguration.selectedTab,
                onTabSelected = tabConfiguration.onTabSelected,
                sharedBackStack = visibleBackStack.drop(1),
                onSharedBack = onBack,
                sharedEntryDecorators = emptyList(),
                entryProvider = { route ->
                    val entry = entries.getValue(route)
                    NavEntry(route, contentKey = entry.contentKey, metadata = entry.metadata) {
                        entry.Content()
                        if (isNativeNavigationTopEntry) tabConfiguration.overlay()
                    }
                },
                otherPlatforms = {},
            )
        }
    } else if (firstKey != null && isNavigationGraph(provider(firstKey))) {
        val parent = nativeParentEntries(
            localEntries = visibleBackStack.drop(1).zip(decorated.drop(1)).map { keyedEntry(it.first, it.second) },
            localOnBack = onBack,
            parent = LocalNativeParentEntries.current,
            localHasEntries = { backStack.size > 1 },
        )
        CompositionLocalProvider(LocalNativeParentEntries provides parent) { decorated.first().Content() }
    } else {
        check(LocalNativeParentEntries.current?.entries.isNullOrEmpty()) {
            "상위 공유 화면이 있는 하위 그래프는 BaseTabNavDisplay로 선언해야 합니다."
        }
        NativeStack(
            backStack = visibleBackStack,
            onBack = onBack,
            entryDecorators = listOf(topEntryDecorator<T>(visibleBackStack.lastOrNull()?.let { provider(it).contentKey })),
            entryProvider = { route ->
                val entry = currentDecorated.getValue(route)
                NavEntry(route, contentKey = entry.contentKey, metadata = entry.metadata) {
                    (currentDecorated[route] ?: entry).Content()
                }
            },
        ) {}
    }
    return true
}

/**
 * iOS에서는 탭과 공유 화면을 네이티브로 표시하고 다른 플랫폼에서는 [otherPlatforms]를 실행합니다.
 * 상위 BaseNavDisplay에서 전달된 공유 화면은 이 스택 위에 자동으로 연결됩니다.
 * 혼합 라우트가 들어올 수 있으므로 [entryDecorators]는 NavKey 엔트리를 받습니다.
 * 모든 탭·공유·부모 스택의 라우트 키는 달라야 하며, [onSharedBack]은 공유 엔트리를 하나 제거해야 합니다.
 */
@Composable
fun <T : NavKey> BaseTabNavDisplay(
    tabs: List<SwiftNativeTab<T>>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    sharedBackStack: List<T> = emptyList(),
    onSharedBack: () -> Unit,
    entryDecorators: List<NavEntryDecorator<NavKey>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(),
    ),
    entryProvider: (T) -> NavEntry<T>,
    otherPlatforms: @Composable () -> Unit,
) {
    NativeTabNavDisplay(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = onTabSelected,
        sharedBackStack = sharedBackStack,
        onSharedBack = onSharedBack,
        entryDecorators = entryDecorators,
        sharedEntryDecorators = entryDecorators,
        entryProvider = entryProvider,
        otherPlatforms = otherPlatforms,
    )
}

@Composable
private fun <T : NavKey> NativeTabNavDisplay(
    tabs: List<SwiftNativeTab<T>>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    sharedBackStack: List<T> = emptyList(),
    onSharedBack: () -> Unit,
    entryDecorators: List<NavEntryDecorator<NavKey>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(),
    ),
    sharedEntryDecorators: List<NavEntryDecorator<NavKey>> = entryDecorators,
    entryProvider: (T) -> NavEntry<T>,
    otherPlatforms: @Composable () -> Unit,
) {
    if (!isNativeNavigation) {
        otherPlatforms()
        return
    }
    val provider = entryProvider
    val parent = LocalNativeParentEntries.current
    val rawEntries = (tabs.flatMap { it.backStack } + sharedBackStack)
        .associate { route -> keyedEntry(route, provider(route)) }
    val latestRawEntries by rememberUpdatedState(rawEntries)
    val promotedProvider: (NavKey) -> NavEntry<NavKey> = { route ->
        val entry = latestRawEntries.getValue(route)
        NavEntry(route, contentKey = entry.contentKey, metadata = entry.metadata) {
            (latestRawEntries[route] ?: entry).Content()
        }
    }
    val tabEntries = tabs.mapIndexed { index, tab ->
        key(index) { rememberDecoratedNavEntries<NavKey>(tab.backStack, entryDecorators, promotedProvider) }
    }
    val sharedEntries = rememberDecoratedNavEntries<NavKey>(sharedBackStack, sharedEntryDecorators, promotedProvider)
    val localEntries = (tabs.flatMapIndexed { index, tab ->
        tab.backStack.zip(tabEntries[index]).map { it.first to it.second }
    } + sharedBackStack.zip(sharedEntries).map { it.first to it.second })
    val merged = nativeParentEntries(localEntries, onSharedBack, parent)
    val currentMerged by rememberUpdatedState(merged)
    val combinedEntries by rememberUpdatedState(merged.keyedEntries.toMap())
    val parentKeys = parent?.keyedEntries.orEmpty().map { it.first }
    val selectedEntries = tabs.getOrNull(selectedTab)?.backStack.orEmpty()
    val topKey = parentKeys.lastOrNull() ?: sharedBackStack.lastOrNull() ?: selectedEntries.lastOrNull()
    NativeTabs<NavKey>(
        tabs = tabs.map { SwiftNativeTab<NavKey>(it.title, it.systemImage, it.backStack, it.image, it.selectedImage, it.onBack) },
        selectedTab = selectedTab,
        onTabSelected = onTabSelected,
        sharedBackStack = sharedBackStack + parentKeys,
        onSharedBack = { currentMerged.onBack() },
        entryDecorators = listOf(topEntryDecorator<NavKey>(combinedEntries[topKey]?.contentKey)),
        entryProvider = { key ->
            val entry = combinedEntries.getValue(key)
            NavEntry(key, contentKey = entry.contentKey, metadata = entry.metadata) { (combinedEntries[key] ?: entry).Content() }
        },
    ) {}
}
