package zone.ien.utils.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

actual fun <T : Any> fadeInOutPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = {edge ->
    ContentTransform(
        fadeIn(tween(500)),
        fadeOut(tween(500))
    )
}