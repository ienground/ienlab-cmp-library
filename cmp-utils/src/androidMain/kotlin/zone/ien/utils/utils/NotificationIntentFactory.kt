package zone.ien.utils.utils

import android.content.Context
import android.content.Intent

/**
 * 알림 클릭 시 실행될 [Intent]를 생성하는 팩토리 타입입니다.
 *
 * @param context 안드로이드 컨텍스트
 * @param data 인텐트에 전달할 추가 데이터 (예: 페이로드 또는 딥링크)
 */
typealias NotificationIntentFactory = (context: Context, data: String?) -> Intent