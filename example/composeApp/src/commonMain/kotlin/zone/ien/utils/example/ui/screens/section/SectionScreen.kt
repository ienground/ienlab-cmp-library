package zone.ien.utils.example.ui.screens.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.section.AdaptiveLinkIcon
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle
import zone.ien.utils.adaptive.section.AdaptiveSection
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionLink
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.adaptive.section.AdaptiveSectionTextField
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.example.Android
import zone.ien.utils.example.isIos
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.navigation.result.ResultStore
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.section.IenProvideSectionStyle

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    resultStore: ResultStore
) {
    val backdrop = rememberDefaultBackdrop()

    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    var checked by remember { mutableStateOf(false) }

    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = { IenText(text = "Title") },
            adaptation = {
                cupertino {
                    this.backdrop = backdrop
                    this.showNavTitle = true
                }
            },
            actions = listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Test",
                    icon = IconData.Paint(
                        AdaptiveIcons.painter(
                            material = { Android },
                            cupertino = { "chevron.right" }
                        )
                    ),
                    onClick = {}
                )
            ),
            modifier = modifier
        ) { pv, title ->
            AdaptiveProvideSectionStyle(
                style = SectionStyle.InsetGrouped,
                fullHeight = true,
                backdrop = backdrop,
                modifier = Modifier
                    .padding(pv)
            ) {
                title()
                AdaptiveSection(
                    title = { IenText(text = "title") }
                ) {
                    AdaptiveSectionItem(
                        leadingContent = {
                            AdaptiveLinkIcon(
                                icon = IconData.Paint(
                                    AdaptiveIcons.painter(
                                        material = { Android },
                                        cupertino = { "apple.logo" }
                                    )
                                )
                            )
                        },
                        adaptation = {
                            cupertino {
                                this.showLeadingContent = true
                            }
                        }
                    ) {

                        IenText(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        IenText(text = "Section2")
                    }
                    var text by remember { mutableStateOf("") }
                    AdaptiveSectionTextField(
                        value = text,
                        onValueChange = { text = it },
                        trailingIcon = {
                            IenIconButton(
                                onClick = { resultStore.setResult("text", text) },
                                size = IenButtonSize.Small,
                                variant = IenButtonVariant.Ghost,
                                tone = IenSemanticTone.Neutral,
                            ) {
                                IenIcon(
                                    imageVector = M3SystemIcons.Save,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    AdaptiveSectionLink(
                        onClick = {}
                    ) {
                        IenText(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        IenText(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { IenText(text = "Check") },
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                AdaptiveSection(
//                    title = { Text(text = "Title") }
                ) {
                    AdaptiveSectionItem {
                        IenText(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { IenText(text = "Check") },
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                AdaptiveSection(
                    title = { IenText(text = "Title") }
                ) {
                    AdaptiveSectionItem {
                        IenText(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { IenText(text = "Check") },
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                /*
                AdaptiveSection(
                    title = { Text(text = "Section Title") },
                    caption = { Text(text = "Section Caption") }
                ){
                    AdaptiveSectionItem {
                        Text(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section2")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { Text(text = "Check") },
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
                AdaptiveSection(
                    title = { Text(text = "Section Title") },
                    caption = { Text(text = "Section Caption") }
                ){
                    AdaptiveSectionItem {
                        Text(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section2")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { Text(text = "Check") },
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }

                 */
            }
            /*
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .verticalScroll(rememberScrollState())
                    .padding(pv)
            ) {
                Text(
                    text = "Hello World",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Red)
                )
//                IenProvideSectionStyle(
                AdaptiveProvideSectionStyle(
                    style = SectionStyle.InsetGrouped,
                    scrollState = null,
                    fullHeight = true,
    //                scrollState = scrollState,
                        modifier = Modifier
                ) {
                        title()
                        AdaptiveSection {
                            AdaptiveSectionItem {
                                Text(text = "Section1")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section2")
                            }
                            AdaptiveSectionLink(
                                onClick = {}
                            ) {
                                Text(text = "Section3")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section4")
                            }
                            AdaptiveSectionSwitchItem(
                                title = { Text(text = "Check") },
                                checked = isMaterialTheme,
                                onCheckedChange = { isMaterialTheme = it }
                            )
                        }
                        /*
                        AdaptiveSection(
                            title = { Text(text = "Section Title") },
                            caption = { Text(text = "Section Caption") }
                        ){
                            AdaptiveSectionItem {
                                Text(text = "Section1")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section2")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section3")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section4")
                            }
                            AdaptiveSectionSwitchItem(
                                title = { Text(text = "Check") },
                                checked = checked,
                                onCheckedChange = { checked = it }
                            )
                        }
                        AdaptiveSection(
                            title = { Text(text = "Section Title") },
                            caption = { Text(text = "Section Caption") }
                        ){
                            AdaptiveSectionItem {
                                Text(text = "Section1")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section2")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section3")
                            }
                            AdaptiveSectionItem {
                                Text(text = "Section4")
                            }
                            AdaptiveSectionSwitchItem(
                                title = { Text(text = "Check") },
                                checked = checked,
                                onCheckedChange = { checked = it }
                            )
                        }

                         */
                    }
                Text(
                    text = "Hello World",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Red)
                )
            }

             */
        }
    }
}
