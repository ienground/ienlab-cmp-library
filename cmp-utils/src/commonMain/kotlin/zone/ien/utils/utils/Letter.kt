package zone.ien.utils.utils

private fun String.hasLastConsonant(): Boolean {
    val last = this.trimEnd().lastOrNull() ?: return false
    if (last.code !in 0xAC00..0xD7A3) return false // 한글 음절이 아니면 false
    return (last.code - 0xAC00) % 28 != 0
}


fun String.withLetterParticle(ifConsonant: String, notConsonant: String) = this + (if (hasLastConsonant()) ifConsonant else notConsonant)