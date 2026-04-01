package zone.ien.utils.utils

fun <K, V> Map<K, V>.getWithDefault(key: K, defaultValue: V = this.values.first()): V = this[key] ?: defaultValue
