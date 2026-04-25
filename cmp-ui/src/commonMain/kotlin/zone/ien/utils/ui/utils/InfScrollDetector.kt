package zone.ien.utils.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun InfScrollDetector(
    listState: LazyListState,
    isLoading: Boolean,
    hasMore: Boolean,
    onLoadNext: () -> Unit
) {
    val shouldLoadMore by remember(isLoading, hasMore) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

            lastVisibleItem?.index == layoutInfo.totalItemsCount - 1 &&
                    layoutInfo.totalItemsCount > 0 &&
                    hasMore &&
                    !isLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadNext()
        }
    }
}

@Composable
fun InfScrollDetector(
    listState: LazyGridState,
    isLoading: Boolean,
    hasMore: Boolean,
    onLoadNext: () -> Unit
) {
    val shouldLoadMore by remember(isLoading, hasMore) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

            lastVisibleItem?.index == layoutInfo.totalItemsCount - 1 &&
                    layoutInfo.totalItemsCount > 0 &&
                    hasMore &&
                    !isLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadNext()
        }
    }
}