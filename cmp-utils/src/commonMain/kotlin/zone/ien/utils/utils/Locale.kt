package zone.ien.utils.utils

import androidx.compose.ui.text.intl.Locale

val Locale.Companion.KOREA: Locale get() = Locale("ko-KR")
fun Pair<String?, String?>.locale() = (if (Locale.current == Locale.KOREA) first else second).orEmpty()