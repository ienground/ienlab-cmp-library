package zone.ien.utils.adaptive.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.hig.theme.Typography

@Composable
fun getHigTypography(fontFamily: FontFamily? = null): Typography =
    CupertinoTheme.typography.let {
        it.copy(
            largeTitle = fontFamily?.let { f -> it.largeTitle.copy(fontFamily = f) } ?: it.largeTitle,
            title1 = fontFamily?.let { f -> it.title1.copy(fontFamily = f) } ?: it.title1,
            title2 = fontFamily?.let { f -> it.title2.copy(fontFamily = f) } ?: it.title2,
            title3 = fontFamily?.let { f -> it.title3.copy(fontFamily = f) } ?: it.title3,
            headline = fontFamily?.let { f -> it.headline.copy(fontFamily = f) } ?: it.headline,
            body = (fontFamily?.let { f -> it.body.copy(fontFamily = f) } ?: it.body),
            callout = fontFamily?.let { f -> it.callout.copy(fontFamily = f) } ?: it.callout,
            subhead = fontFamily?.let { f -> it.subhead.copy(fontFamily = f) } ?: it.subhead,
            footnote = fontFamily?.let { f -> it.footnote.copy(fontFamily = f) } ?: it.footnote,
            caption1 = fontFamily?.let { f -> it.caption1.copy(fontFamily = f) } ?: it.caption1,
            caption2 = fontFamily?.let { f -> it.caption2.copy(fontFamily = f) } ?: it.caption2
        )
    }