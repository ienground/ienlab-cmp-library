package zone.ien.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer

@Composable
inline fun <reified T: NavKey> rememberNavBackStack(
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