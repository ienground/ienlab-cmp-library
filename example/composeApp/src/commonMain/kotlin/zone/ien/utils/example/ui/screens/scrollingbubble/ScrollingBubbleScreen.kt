package zone.ien.utils.example.ui.screens.scrollingbubble

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.list.ScrollingBubble
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.screen.IenTopBar

internal val scrollingBubbleSampleItems = listOf(
    "Ackee",
    "Acerola",
    "Apple",
    "Apricot",
    "Asian Pear",
    "Atemoya",
    "Avocado",
    "Banana",
    "Blackcurrant",
    "Blackberry",
    "Blueberry",
    "Boysenberry",
    "Breadfruit",
    "Buddha's Hand",
    "Cantaloupe",
    "Cherry",
    "Clementine",
    "Coconut",
    "Crabapple",
    "Cranberry",
    "Currant",
    "Damson",
    "Dangleberry",
    "Date",
    "Dewberry",
    "Desert Lime",
    "Dragon Fruit",
    "Durian",
    "Elderberry",
    "Eggfruit",
    "Emblic",
    "Feijoa",
    "Fig",
    "Finger Lime",
    "Genip",
    "Goldenberry",
    "Grape",
    "Grapefruit",
    "Gooseberry",
    "Governor's Plum",
    "Guava",
    "Hackberry",
    "Hawthorn Fruit",
    "Honeydew",
    "Huckleberry",
    "Jabuticaba",
    "Jackfruit",
    "Jujube",
    "Juneberry",
    "Kiwi",
    "Kiwano",
    "Korlan",
    "Kumquat",
    "Lemon",
    "Lime",
    "Longan",
    "Lychee",
    "Mango",
    "Mangosteen",
    "Melon",
    "Mulberry",
    "Nance",
    "Naranjilla",
    "Nectarine",
    "Nutmeg Fruit",
    "Orange",
    "Peach",
    "Pear",
    "Pineapple",
    "Raspberry",
    "Strawberry",
    "Watermelon",
)

@Composable
fun ScrollingBubbleScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    IenTheme {
        val lazyListState = rememberLazyListState()

        IenScaffold(
            modifier = modifier,
            contentEdge = IenScaffoldContentEdge(
                lazyListState = lazyListState,
            ),
            topBar = {
                IenTopBar(
                    title = "Scrolling Bubble",
                    subtitle = "스크롤 위치를 따라 움직이는 버블",
                    navigationIcon = {
                        IenTextButton(onClick = navigateBack) {
                            IenText("닫기")
                        }
                    },
                )
            },
        ) { contentPadding ->
            ScrollingBubble(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                bubbleContent = { index ->
                    IenText(
                        text = scrollingBubbleSampleItems[index].first().uppercase(),
                        style = IenTheme.typography.title1,
                    )
                },
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(scrollingBubbleSampleItems) { item ->
                        IenText(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = IenTheme.spacing.md,
                                    vertical = IenTheme.spacing.sm,
                                ),
                            style = IenTheme.typography.body2,
                        )
                    }
                }
            }
        }
    }
}
