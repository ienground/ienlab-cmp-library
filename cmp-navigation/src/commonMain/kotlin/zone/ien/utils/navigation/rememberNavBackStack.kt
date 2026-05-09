package zone.ien.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.SavedStateConfiguration.Companion
import kotlinx.serialization.PolymorphicSerializer

/**
 * 네비게이션 백스택을 기억하는 Composable 함수
 * @param elements 초기 백스택 요소들
 * @return 기억된 NavBackStack
 */
@Composable
inline fun <reified T: NavKey> rememberNavBackStack(
//    configuration: SavedStateConfiguration,
    vararg elements: T,
): NavBackStack<T> {
    val configuration = getConfig<T>()
    require(configuration.serializersModule != SavedStateConfiguration.DEFAULT.serializersModule) {
        "You must pass a `SavedStateConfiguration.serializersModule` configured to handle " +
                "`NavKey` open polymorphism. Define it with: `polymorphic(NavKey::class) { ... }`"
    }
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer(PolymorphicSerializer(T::class)),
    ) {
        NavBackStack(*elements)
    }
}

//@Composable
//fun rememberNavBackStack(
//    configuration: SavedStateConfiguration,
//    vararg elements: NavKey,
//): NavBackStack<NavKey> {
//    require(configuration.serializersModule != SavedStateConfiguration.DEFAULT.serializersModule) {
//        "You must pass a `SavedStateConfiguration.serializersModule` configured to handle " +
//                "`NavKey` open polymorphism. Define it with: `polymorphic(NavKey::class) { ... }`"
//    }
//    return rememberSerializable(
//        configuration = configuration,
//        serializer = NavBackStackSerializer(PolymorphicSerializer(NavKey::class)),
//    ) {
//        NavBackStack(*elements)
//    }
//}