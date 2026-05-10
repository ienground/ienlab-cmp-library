package zone.ien.utils.utils

/**
 * 한국어 문자열의 마지막 공백이 아닌 문자가 자음인지 확인합니다.
 *
 * 이 함수는 유니코드 코드 포인트를 조사하여 한국어 문자열의 마지막 문자가 자음인지 확인합니다.
 * 
 * @return 마지막 공백이 아닌 문자가 자음이면 true, 그렇지 않으면 false
 */
fun String.hasLastConsonant(): Boolean {
    val last = findLast { !it.isWhitespace() } ?: return false
    val code = last.code
    if (code !in 0xAC00..0xD7A3) return false // 한글 음절이 아니면 false
    return (code - 0xAC00) % 28 != 0
}

/**
 * 문자열의 마지막 글자가 자음인지에 따라 적절한 한국어 조사( participle)을 추가합니다.
 *
 * 이 함수는 단어의 마지막 글자의 음성 품질에 따라 적절한 조사를 추가하여 한국어 문법 규칙에 따라
 * 올바르게 조사어를 붙이기 위해 사용됩니다.
 *
 * @param ifConsonant 마지막 글자가 자음일 때 붙일 조사
 * @param notConsonant 마지막 글자가 자음이 아닐 때 붙일 조사
 * @return 적절한 조사가 추가된 문자열
 */
fun String.withLetterParticle(ifConsonant: String, notConsonant: String) = this + (if (hasLastConsonant()) ifConsonant else notConsonant)
