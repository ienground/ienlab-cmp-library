package zone.ien.utils.navigation

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
import zone.ien.navigation.NativeNavigationFrame
import zone.ien.navigation.NativeStack
import zone.ien.navigation.NativeTab
import zone.ien.navigation.NativeTabs
import zone.ien.utils.ui.screen.ProvideNativeScreenPadding

private val LocalNativeNavigationEnabled = staticCompositionLocalOf { false }
private val LocalNativeNavigationTopEntry = staticCompositionLocalOf { true }
private val LocalNativeParentEntries = staticCompositionLocalOf<NativeParentEntries?> { null }
private const val NavigationGraphMetadata = "zone.ien.utils.navigation.graph"

internal expect val usesNativeNavigationPlatform: Boolean

/** 현재 호스트가 네이티브 내비게이션을 사용하는지 반환합니다. */
val isNativeNavigation: Boolean
    @Composable get() = LocalNativeNavigationEnabled.current

/** 현재 엔트리가 선택된 탭과 상위 공유 화면을 합친 스택의 맨 위인지 반환합니다. */
val isNativeNavigationTopEntry: Boolean
    @Composable get() = LocalNativeNavigationTopEntry.current

/**
 * 기존 테마와 DI를 유지하며 네이티브 내비게이션을 활성화합니다.
 * iOS에서는 NativeNavigationViewController 안에서 호출해야 합니다.
 * [providers]는 UI 없는 DI 공급자, [frame]은 각 화면의 테마와 루트 UI를 담당합니다.
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
                providers(body)
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
private fun <T : NavKey> currentEntryProvider(provider: (T) -> NavEntry<T>): (T) -> NavEntry<T> {
    val latestProvider by rememberUpdatedState(provider)
    return remember {
        { key ->
            val entry = latestProvider(key)
            NavEntry(key, contentKey = entry.contentKey, metadata = entry.metadata) { latestProvider(key).Content() }
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
): Boolean {
    if (!isNativeNavigation) return false
    val provider = currentEntryProvider(entryProvider)
    val decorated = rememberDecoratedNavEntries(backStack, entryDecorators, provider)
    val currentDecorated by rememberUpdatedState(backStack.zip(decorated).toMap())
    val firstKey = backStack.firstOrNull()
    if (firstKey != null && isNavigationGraph(provider(firstKey))) {
        val parent = nativeParentEntries(
            localEntries = backStack.drop(1).zip(decorated.drop(1)).map { keyedEntry(it.first, it.second) },
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
            backStack = backStack,
            onBack = onBack,
            entryDecorators = listOf(topEntryDecorator<T>(backStack.lastOrNull()?.let { provider(it).contentKey })),
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
    tabs: List<NativeTab<T>>,
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
    if (!isNativeNavigation) {
        otherPlatforms()
        return
    }
    val provider = currentEntryProvider(entryProvider)
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
    val sharedEntries = rememberDecoratedNavEntries<NavKey>(sharedBackStack, entryDecorators, promotedProvider)
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
        tabs = tabs.map { NativeTab<NavKey>(it.title, it.systemImage, it.backStack, it.onBack) },
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
