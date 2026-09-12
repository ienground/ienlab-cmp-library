package zone.ien.utils.example.ui.screens.permission

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.icons.CupertinoIcons
import zone.ien.hig.icons.filled.Bell
import zone.ien.hig.icons.filled.Lock
import zone.ien.hig.icons.filled.Mic
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenBottomCTA
import zone.ien.utils.ui.screen.IenPermissionItem
import zone.ien.utils.ui.screen.IenPermissionScreen
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopBar

@Composable
fun PermissionScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    IenTheme {
        IenScaffold(
            modifier = modifier,
            topBar = {
                IenTopBar(
                    title = { IenText("Permission Screen") },
                    navigationIcon = {
                        IenTextButton(onClick = navigateBack) {
                            IenText("닫기")
                        }
                    },
                )
            },
            bottomBar = {
                IenBottomCTA(
                    text = "계속하기",
                    onClick = {},
                )
            },
        ) { contentPadding ->
            IenPermissionScreen(
                title = "서비스 이용을 위해 권한이 필요해요",
                description = "원활한 서비스 이용을 위해 다음 권한을 허용해주세요.",
                items = listOf(
                    IenPermissionItem(
                        icon = CupertinoIcons.Filled.Mic,
                        title = "녹음",
                        description = "확언과 책 내용을 목소리로 기록하기 위해 꼭 필요해요.",
                    ),
                    IenPermissionItem(
                        icon = CupertinoIcons.Filled.Bell,
                        title = "알림",
                        description = "나의 기록 현황과 소중한 피드백 알림을 받아보세요.",
                        isRequired = false,
                    ),
                ),
                headerIcon = CupertinoIcons.Filled.Lock,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )
        }
    }
}
