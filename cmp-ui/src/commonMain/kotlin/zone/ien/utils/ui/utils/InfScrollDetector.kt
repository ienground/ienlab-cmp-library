package zone.ien.utils.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

/**
 * InfScrollDetector은 무한 스크롤 감지를 위한 함수입니다.
 *
 * @param listState 빈 리스트 상태
 * @param isLoading 로딩 중 여부
 * @param hasMore 더 있음 여부
 * @param onLoadNext 다음 데이터 로드 시 호출되는 콜백 함수
 */
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

/**
 * InfScrollDetector은 무한 스크롤 감지를 위한 함수입니다.
 *
 * @param listState 그리드 리스트 상태
 * @param isLoading 로딩 중 여부
 * @param hasMore 더 있음 여부
 * @param onLoadNext 다음 데이터 로드 시 호출되는 콜백 함수
 */
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