package zone.ien.utils.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent.SwipeEdge

/**
 * Fade in/out transition을 위한 pop transition spec을 생성하는 예상 함수
 * @return AnimatedContentTransitionScope에서 사용할 ContentTransform
 */
expect fun <T: Any> fadeInOutPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@SwipeEdge Int) -> ContentTransform