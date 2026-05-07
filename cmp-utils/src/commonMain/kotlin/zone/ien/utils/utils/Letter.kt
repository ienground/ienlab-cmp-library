package zone.ien.utils.utils

fun String.hasLastConsonant(): Boolean {
    val last = findLast { !it.isWhitespace() } ?: return false
    val code = last.code
    if (code !in 0xAC00..0xD7A3) return false // 한글 음절이 아니면 false
    return (code - 0xAC00) % 28 != 0
}


fun String.withLetterParticle(ifConsonant: String, notConsonant: String) = this + (if (hasLastConsonant()) ifConsonant else notConsonant)