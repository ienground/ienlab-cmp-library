package zone.ien.utils.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigationevent.NavigationEvent

/**
 * iOS 플랫폼에서 fade in/out pop transition을 구현하는 실제 함수
 * @return 기본 예측 pop transition spec 사용
 */
actual fun <T : Any> fadeInOutPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = defaultPredictivePopTransitionSpec()