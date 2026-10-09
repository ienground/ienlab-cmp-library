package zone.ien.utils.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.Font
import zone.ien.utils.icon.IconStyle
import zone.ien.utils.icon.LocalIconStyle
import zone.ien.utils.docs.generated.resources.Pretendard_Regular
import zone.ien.utils.docs.generated.resources.Res
import zone.ien.utils.example.ui.screens.designsystem.DesignSystemPlayground
import zone.ien.utils.ui.feedback.IenSnackbarHost
import zone.ien.utils.ui.feedback.IenToastProvider
import zone.ien.utils.ui.feedback.rememberIenToastState
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.defaultIenTokens
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.utils.getIenTypography
import androidx.compose.material3.SnackbarHostState

@Composable
fun DocsApp(
    componentId: String,
    darkTheme: Boolean = false,
    colors: IenColorScheme? = null,
    onContentHeight: (Int) -> Unit = {},
    showPreviewViewportControls: Boolean = false,
    initialPreviewViewport: String = "pc",
    onPreviewViewportChange: (String) -> Unit = {},
    iconStyle: IconStyle = IconStyle.Material.Filled,
) {
    val defaultTokens = defaultIenTokens()
    val docsTokens = defaultTokens.copy(
        lightColors = if (!darkTheme) colors ?: defaultTokens.lightColors else defaultTokens.lightColors,
        darkColors = if (darkTheme) colors ?: defaultTokens.darkColors else defaultTokens.darkColors,
        typography = getIenTypography(FontFamily(Font(Res.font.Pretendard_Regular))),
    )

    CompositionLocalProvider(LocalIconStyle provides iconStyle) {
        IenTheme(tokens = docsTokens, darkTheme = darkTheme) {
            val density = LocalDensity.current
            val snackbarHostState = remember { SnackbarHostState() }
            val toastState = rememberIenToastState()
            val coroutineScope = rememberCoroutineScope()

            IenToastProvider(state = toastState) {
                IenProvideTextStyle(
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textPrimary,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(IenTheme.colors.background),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp)
                                .onSizeChanged { size ->
                                    onContentHeight(with(density) { size.height.toDp().value.roundToInt() })
                                },
                        ) {
                            DesignSystemPlayground(
                                componentId = componentId,
                                snackbarHostState = snackbarHostState,
                                toastState = toastState,
                                coroutineScope = coroutineScope,
                                showPreviewViewportControls = showPreviewViewportControls,
                                initialPreviewViewport = initialPreviewViewport,
                                onPreviewViewportChange = onPreviewViewportChange,
                            )
                        }
                        IenSnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
