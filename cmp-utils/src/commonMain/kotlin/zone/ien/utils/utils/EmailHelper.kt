package zone.ien.utils.utils

/**
 * 플랫폼의 네이티브 이메일 애플리케이션을 사용하여 이메일을 전송합니다.
 *
 * 이 함수는 사용자의 기본 이메일 클라이언트나 애플리케이션을 사용하여 이메일을 전송하는
 * 교차 플랫폼 방식을 제공합니다. 이메일 애플리케이션을 열고 수신자, 제목, 본문 필드를 미리 채워줍니다.
 *
 * @param address 수신자 이메일 주소
 * @param subject 이메일 제목 (선택사항)
 * @param body 이메일 본문 내용 (선택사항)
 */
expect fun sendEmail(address: String, subject: String = "", body: String = "")