package zone.ien.utils.adaptive.wrapper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveScaffold
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.icons.CupertinoIcons
import zone.ien.hig.icons.outlined.ChevronDown
import zone.ien.hig.icons.outlined.ChevronUp
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.next
import zone.ien.utils.cmp_ui.generated.resources.previous
import zone.ien.utils.isIos
import zone.ien.utils.ui.screen.LocalEnableImePadding
import zone.ien.utils.ui.screen.LocalSetEnableImePadding
import zone.ien.utils.ui.utils.advancedImePadding
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.ui.utils.dragToKeyboardClose
import zone.ien.utils.ui.utils.keyboardAsState

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun RootWrapper(
    modifier: Modifier = Modifier,
    showKeyboardDirection: Boolean = false,
    enableImePadding: Boolean = true,
    notification: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val backdrop = rememberDefaultBackdrop()
    var localEnableImePadding by remember { mutableStateOf(enableImePadding) }

    CompositionLocalProvider(
        LocalEnableImePadding provides localEnableImePadding,
        LocalSetEnableImePadding provides { localEnableImePadding = it }
    ) {

        AdaptiveScaffold(
            contentWindowInsets = WindowInsets(0.dp),
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .conditional(localEnableImePadding) { advancedImePadding(isIos) }
                    .dragToKeyboardClose(isKeyboardVisible)
                    .padding(it)
            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    content(
                        Modifier
                            .layerBackdrop(backdrop)
                    )
                    AnimatedVisibility(
                        visible = isKeyboardVisible && isIos,
                        enter = fadeIn(tween(150)) + expandVertically(tween(150)),
                        exit = fadeOut(tween(150)) + shrinkVertically(tween(150))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            if (showKeyboardDirection) {
                                CupertinoLiquidButton(
                                    backdrop = backdrop,
                                    onClick = {},
                                    isInteractive = false,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier.clickable(
                                            onClick = { focusManager.moveFocus(FocusDirection.Previous) },
                                            indication = null,
                                            interactionSource = null
                                        )
                                    ) {
                                        CupertinoIcon(
                                            painter = AdaptiveIcons.painter(
                                                material = { CupertinoIcons.Default.ChevronUp },
                                                cupertino = { "chevron_up" }
                                            ),
                                            contentDescription = stringResource(Res.string.previous)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier.clickable(
                                            onClick = { focusManager.moveFocus(FocusDirection.Next) },
                                            indication = null,
                                            interactionSource = null
                                        )
                                    ) {
                                        CupertinoIcon(
                                            painter = AdaptiveIcons.painter(
                                                material = { CupertinoIcons.Default.ChevronDown },
                                                cupertino = { "chevron_down" }
                                            ),
                                            contentDescription = stringResource(Res.string.next)
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier.clickable(
                                            onClick = { keyboardManager?.hide() },
                                            indication = null,
                                            interactionSource = null
                                        )
                                    ) {
                                        Text(text = stringResource(Res.string.close))
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                                CupertinoLiquidButton(
                                    backdrop = backdrop,
                                    colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors(),
                                    onClick = { keyboardManager?.hide() }
                                ) {
                                    Text(
                                        text = stringResource(Res.string.close),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                AdaptiveWidget(
                    material = {},
                    cupertino = {
                        notification()
                    }
                )
            }
        }
    }
}