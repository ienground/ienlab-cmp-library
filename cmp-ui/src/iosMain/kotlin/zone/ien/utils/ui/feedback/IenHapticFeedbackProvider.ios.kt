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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import platform.Foundation.NSThread
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UISelectionFeedbackGenerator
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun IenHapticFeedbackProvider(content: @Composable () -> Unit) {
    val hapticFeedback = remember { IenIosHapticFeedback() }
    CompositionLocalProvider(LocalHapticFeedback provides hapticFeedback) {
        content()
    }
}

/** 공식 Compose 매핑과 일치함을 보장하지 않는 임시 UIKit 햅틱 구현입니다. */
internal class IenIosHapticFeedback : HapticFeedback {
    private val heavyImpactGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
    }
    private val mediumImpactGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
    }
    private val lightImpactGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    }
    private val softImpactGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft)
    }
    private val selectionGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UISelectionFeedbackGenerator()
    }
    private val notificationGenerator by lazy(LazyThreadSafetyMode.NONE) {
        UINotificationFeedbackGenerator()
    }

    override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
        if (NSThread.isMainThread) {
            performOnMainThread(hapticFeedbackType)
        } else {
            dispatch_async(dispatch_get_main_queue()) {
                performOnMainThread(hapticFeedbackType)
            }
        }
    }

    private fun performOnMainThread(type: HapticFeedbackType) {
        when (type) {
            HapticFeedbackType.LongPress -> heavyImpactGenerator.impactOccurred()
            HapticFeedbackType.TextHandleMove -> {
                selectionGenerator.selectionChanged()
                // 드래그 중 이어지는 선택 이동의 지연을 줄입니다.
                selectionGenerator.prepare()
            }
            HapticFeedbackType.SegmentFrequentTick -> {
                softImpactGenerator.impactOccurred()
                // 연속 선택 중 다음 틱을 준비합니다.
                softImpactGenerator.prepare()
            }
            HapticFeedbackType.ToggleOff -> softImpactGenerator.impactOccurred()
            HapticFeedbackType.Confirm -> notificationGenerator.notificationOccurred(
                UINotificationFeedbackType.UINotificationFeedbackTypeSuccess,
            )
            HapticFeedbackType.Reject -> notificationGenerator.notificationOccurred(
                UINotificationFeedbackType.UINotificationFeedbackTypeError,
            )
            HapticFeedbackType.ContextClick,
            HapticFeedbackType.GestureThresholdActivate,
            HapticFeedbackType.ToggleOn -> mediumImpactGenerator.impactOccurred()
            HapticFeedbackType.SegmentTick,
            HapticFeedbackType.GestureEnd,
            HapticFeedbackType.KeyboardTap,
            HapticFeedbackType.VirtualKey -> lightImpactGenerator.impactOccurred()
            // 향후 추가되는 타입도 무시하지 않고 가벼운 충격으로 처리합니다.
            else -> lightImpactGenerator.impactOccurred()
        }
    }
}
