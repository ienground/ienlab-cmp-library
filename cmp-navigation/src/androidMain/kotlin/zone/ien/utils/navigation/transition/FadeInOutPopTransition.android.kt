package zone.ien.utils.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

/**
 * Android 플랫폼에서 fade in/out pop transition을 구현하는 실제 함수
 * @return 500ms 동안 fade in/out 효과를 적용하는 ContentTransform
 */
actual fun <T : Any> fadeInOutPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = {edge ->
    ContentTransform(
        fadeIn(tween(500)),
        fadeOut(tween(500))
    )
}