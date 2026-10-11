package zone.ien.utils.example.ui.screens.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.screen.IenTopAppBarScaffold
import zone.ien.utils.ui.section.IenSection
import zone.ien.utils.ui.section.button

@Composable
fun HapticFeedbackTestScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val hapticFeedbackTypes = remember { HapticFeedbackType.values() }
    val scrollState = rememberScrollState()

    IenTheme {
        IenTopAppBarScaffold(
            modifier = modifier,
            title = { Text("진동 테스트") },
            subtitle = { Text("타입별 햅틱 반응을 확인하세요") },
            navigationIcon = { IenBackButton(onClick = navigateBack) },
            actions = emptyList(),
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                IenSection(title = { Text("HapticFeedbackType") }) {
                    hapticFeedbackTypes.forEach { type ->
                        button(
                            onClick = { hapticFeedback.performHapticFeedback(type) },
                            label = { Text(type.toString()) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
