/*
 * Copyright 2026 IENLAB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zone.ien.utils.ui.feedback

import androidx.compose.runtime.Composable

/**
 * iOS의 Compose Multiplatform 기본 햅틱에서 일부 타입이 동작하지 않는 현상을 우회합니다.
 *
 * Compose Multiplatform 1.12.1에서 보고된 현상을 위한 선택형 임시 구현이며,
 * 업스트림 결함의 원인이 확정되었다는 의미는 아닙니다. iOS에서만 UIKit 구현을
 * `LocalHapticFeedback`으로 제공하고 Android와 다른 플랫폼은 기존 구현을 유지합니다.
 * 기존 컴포넌트는 표준 Compose 햅틱 API를 그대로 사용할 수 있습니다.
 *
 * `LocalHapticFeedback`을 재정의하는 테마(예: HIG의 `CupertinoTheme`)를 사용하면
 * 해당 테마의 content 안에서 이 Provider로 화면을 감싸야 합니다.
 * Provider 안쪽에서 다시 햅틱을 제공하면 더 가까운 구현이 우선 적용됩니다.
 *
 * 업스트림 수정 후 기본 구현이 정상 동작하는지 확인하고 호출부의 래퍼와 구현을 제거할 수 있습니다.
 *
 * @param content 햅틱 구현을 제공받는 하위 컴포저블입니다.
 */
// TODO: 업스트림 iOS 햅틱 문제가 해결되고 검증되면 임시 Provider를 제거합니다.
@Composable
expect fun IenHapticFeedbackProvider(content: @Composable () -> Unit)
