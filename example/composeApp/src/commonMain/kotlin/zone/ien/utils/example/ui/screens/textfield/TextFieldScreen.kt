package zone.ien.utils.example.ui.screens.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.adaptive.AdaptiveHorizontalDivider
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.adaptiveComponent
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.view.textfield.PlaceholderBasicTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class)
@Composable
fun TextFieldScreen(
    modifier: Modifier = Modifier
) {
    val backdrop = rememberDefaultBackdrop()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    val snackbarState = remember { SnackbarHostState() }
    var text by remember { mutableStateOf("") }

//    InfScrollDetector(
//        listState = lazyListState,
//        isLoading = answersInfoStateList.isLoading,
//        hasMore = answersInfoStateList.hasMore,
//        onLoadNext = {
//            viewModel.loadNextPage()
//        }
//    )

    Scaffold(
//        navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
        snackbarHost = { SnackbarHost(snackbarState) },
//        title = { Text(text = stringResource(Res.string.daily_mission)) },
        bottomBar = {
            BottomAppBar(
                containerColor = adaptiveComponent(
                    material = { BottomAppBarDefaults.containerColor },
                    cupertino = { Color.Transparent }
                )
            ) {
                Column {
                    AdaptiveWidget(
                        material = {},
                        cupertino = {
                            AdaptiveHorizontalDivider()
                        }
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        PlaceholderBasicTextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text(text = stringResource(Res.string.save)) },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            maxLines = 6,
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                                .padding(horizontal = 16.dp)
                                .weight(1f)

                        )
                        IconButton(
                            onClick = {
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = M3SystemIcons.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { pv, ->
        LazyColumn(
            modifier = Modifier
        ) {
            itemsIndexed(items = listOf("1", "2", "3")) { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(listOf(Color.Red, Color.Blue, Color.Green)[index % 3])
                )
            }
        }
//        ScreenBody(
//            uiState = viewModel.uiState,
//            onItemValueChanged = viewModel::updateUiState,
//            missionInfoState = missionInfoState,
//            answersInfoStateList = answersInfoStateList,
//            userInfoState = userInfoState,
//            lazyListState = lazyListState,
//            paddingValues = pv,
//            currentDate = currentDate,
//            onSelectDateChanged = viewModel::changeDate,
//            modifier = Modifier.layerBackdrop(backdrop)
//        )
    }
}