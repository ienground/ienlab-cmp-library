package zone.ien.utils.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent.SwipeEdge

expect fun <T: Any> fadeInOutPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@SwipeEdge Int) -> ContentTransform