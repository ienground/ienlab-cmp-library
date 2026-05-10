package zone.ien.utils.utils

/**
 * 맵에서 키에 해당하는 값을 가져오고 기본값을 제공합니다.
 *
 * 이 확장 함수는 지정된 키를 사용하여 맵에서 값을 가져옵니다.
 * 키가 맵에 존재하지 않으면 지정된 기본값이나 맵의 값 중 첫 번째 값을 반환합니다.
 *
 * @param key 맵에서 찾을 키
 * @param defaultValue 키가 존재하지 않을 때 반환할 기본값 (선택사항)
 * @return 키에 해당하는 값 또는 키가 없을 때 기본값
 */
fun <K, V> Map<K, V>.getWithDefault(key: K, defaultValue: V = this.values.first()): V = this[key] ?: defaultValue
