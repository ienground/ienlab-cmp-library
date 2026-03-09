package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun dpToPx(size: Dp): Float = with (LocalDensity.current) { size.toPx() }
@Composable
fun pxToDp(size: Int): Dp = with (LocalDensity.current) { size.toDp() }
@Composable
fun pxToDp(size: Float): Dp = with (LocalDensity.current) { size.toDp() }
@Composable
fun pxToSp(size: Int): TextUnit = with (LocalDensity.current) { size.toSp() }
@Composable
fun pxToSp(size: Float): TextUnit = with (LocalDensity.current) { size.toSp() }