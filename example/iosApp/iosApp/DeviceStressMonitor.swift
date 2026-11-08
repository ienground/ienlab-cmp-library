import Darwin
import Foundation
import os
import QuartzCore
import UIKit
import WebKit

@MainActor
final class DeviceStressMonitor: NSObject {
    static let shared = DeviceStressMonitor()

    private struct Phase: Codable {
        let name: String
        let startUptime: Double
        let endUptime: Double
        let entryIntervalMs: Double?
        let firstCallbackDelayMs: Double?
        let intervalsMs: [Double]
        let expectedIntervalsMs: [Double]
        let residentBytes: UInt64
        let thermalState: Int
        let applicationState: Int
        let webViewScrollUpdates: Int
        let webMaskSamples: Int
        let webMaskViewCount: Int
        let stableSizeMaskImageChanges: Int
        let nativePresentedControllers: Int?
        let nativeKeyboardAttempts: Int?
        let nativeKeyboardDismissals: Int?
        let nativeInputAttempts: Int?
        let nativeInputFailures: Int?
        let nativeTitleUpdateFailures: Int?
    }

    private struct Measurement: Codable {
        let phase: String
        let name: String
        let durationMillis: Double?
        let count: Int?
    }

    private struct Report: Codable {
        let runIdentifier: String
        let completed: Bool
        let activeCase: String
        let model: String
        let systemVersion: String
        let maximumFramesPerSecond: Int
        let currentThermalState: Int
        let phases: [Phase]
        let measurements: [Measurement]
        let clockWallTime: Double
        let clockUptime: Double
    }

    private let writer = DispatchQueue(label: "zone.ien.device-stress-report", qos: .utility)
    private let runIdentifier = UUID().uuidString
    private let signposter = OSSignposter(subsystem: "zone.ien.utils.example", category: "DeviceStress")
    private var interval: OSSignpostIntervalState?
    private var displayLink: CADisplayLink?
    private var observers: [NSObjectProtocol] = []
    private var phaseName = "warmup"
    private var phaseStart = ProcessInfo.processInfo.systemUptime
    private var lastTimestamp: CFTimeInterval?
    private var entryIntervalMs: Double?
    private var firstCallbackDelayMs: Double?
    private var intervalsMs: [Double] = []
    private var expectedIntervalsMs: [Double] = []
    private var phases: [Phase] = []
    private var measurements: [Measurement] = []
    private weak var webView: WKWebView?
    private var webViewScrollUpdates = 0
    private var webMaskSamples = 0
    private var stableSizeMaskImageChanges = 0
    private var nativeKeyboardAttempts = 0
    private var nativeKeyboardDismissals = 0
    private var nativeInputAttempts = 0
    private var nativeInputFailures = 0
    private var maskImages: [ObjectIdentifier: (CGRect, UIImage)] = [:]
    private var previousIdleTimerDisabled = false
    private var idleTimerObserver: NSObjectProtocol?
    private var thermalStateObserver: NSObjectProtocol?

    static func startIfRequested() {
        guard ProcessInfo.processInfo.arguments.contains("--ien-device-stress") else { return }
        shared.start()
    }

    private func start() {
        previousIdleTimerDisabled = UIApplication.shared.isIdleTimerDisabled
        UIApplication.shared.isIdleTimerDisabled = true
        idleTimerObserver = NotificationCenter.default.addObserver(
            forName: UIApplication.didEnterBackgroundNotification, object: nil, queue: .main
        ) { [weak self] _ in
            MainActor.assumeIsolated {
                guard let self else { return }
                UIApplication.shared.isIdleTimerDisabled = self.previousIdleTimerDisabled
                if let observer = self.idleTimerObserver { NotificationCenter.default.removeObserver(observer) }
                self.idleTimerObserver = nil
                if let observer = self.thermalStateObserver { NotificationCenter.default.removeObserver(observer) }
                self.thermalStateObserver = nil
            }
        }
        observers.append(NotificationCenter.default.addObserver(
            forName: Notification.Name("IENDeviceStressPhase"), object: nil, queue: .main
        ) { [weak self] notification in
            guard let name = notification.userInfo?["case"] as? String else { return }
            MainActor.assumeIsolated { self?.beginPhase(name) }
        })
        observers.append(NotificationCenter.default.addObserver(
            forName: Notification.Name("IENDeviceStressComplete"), object: nil, queue: .main
        ) { [weak self] _ in
            MainActor.assumeIsolated { self?.finish() }
        })
        observers.append(NotificationCenter.default.addObserver(
            forName: Notification.Name("IENDeviceStressMeasurement"), object: nil, queue: .main
        ) { [weak self] notification in
            guard let name = notification.userInfo?["name"] as? String else { return }
            let duration = notification.userInfo?["durationMillis"] as? Double
            let count = notification.userInfo?["count"] as? Int
            guard duration != nil || count != nil else { return }
            MainActor.assumeIsolated {
                guard let self else { return }
                self.measurements.append(Measurement(phase: self.phaseName, name: name, durationMillis: duration, count: count))
            }
        })
        let link = CADisplayLink(target: self, selector: #selector(frame(_:)))
        link.add(to: .main, forMode: .common)
        displayLink = link
        interval = signposter.beginInterval("ComponentStress", "warmup")
        save(completed: false)
    }

    private func beginPhase(_ name: String) {
        endPhase()
        phaseName = name
        phaseStart = ProcessInfo.processInfo.systemUptime
        entryIntervalMs = nil
        firstCallbackDelayMs = nil
        intervalsMs = []
        expectedIntervalsMs = []
        webViewScrollUpdates = 0
        webMaskSamples = 0
        stableSizeMaskImageChanges = 0
        nativeKeyboardAttempts = 0
        nativeKeyboardDismissals = 0
        nativeInputAttempts = 0
        nativeInputFailures = 0
        maskImages.removeAll()
        webView = nil
        interval = signposter.beginInterval("ComponentStress", "\(name, privacy: .public)")
        save(completed: false)
    }

    private func endPhase() {
        if let interval { signposter.endInterval("ComponentStress", interval) }
        let titleUpdateFailures: Int?
        if phaseName.hasPrefix("native/"), phaseName.hasSuffix("/open"),
           phaseName.contains("/Date/") || phaseName.contains("/Time/") {
            let expectedTitle = phaseName.contains("/Date/") ? "날짜 선택 반복 5" : "시간 선택 반복 5"
            let titles = nativeViewControllers().compactMap { controller in
                controller.viewIfLoaded?.subviews.compactMap { $0 as? UINavigationBar }.first?.topItem?.title
            }
            titleUpdateFailures = titles.contains(expectedTitle) ? 0 : 1
        } else {
            titleUpdateFailures = nil
        }
        phases.append(Phase(
            name: phaseName,
            startUptime: phaseStart,
            endUptime: ProcessInfo.processInfo.systemUptime,
            entryIntervalMs: entryIntervalMs,
            firstCallbackDelayMs: firstCallbackDelayMs,
            intervalsMs: intervalsMs,
            expectedIntervalsMs: expectedIntervalsMs,
            residentBytes: residentBytes(),
            thermalState: ProcessInfo.processInfo.thermalState.rawValue,
            applicationState: UIApplication.shared.applicationState.rawValue,
            webViewScrollUpdates: webViewScrollUpdates,
            webMaskSamples: webMaskSamples,
            webMaskViewCount: maskImages.count,
            stableSizeMaskImageChanges: stableSizeMaskImageChanges,
            nativePresentedControllers: phaseName.hasPrefix("native/") ? nativePresentedControllerCount() : nil,
            nativeKeyboardAttempts: phaseName.contains("/TextField/") ? nativeKeyboardAttempts : nil,
            nativeKeyboardDismissals: phaseName.contains("/TextField/") ? nativeKeyboardDismissals : nil,
            nativeInputAttempts: phaseName.contains("/TextField/") ? nativeInputAttempts : nil,
            nativeInputFailures: phaseName.contains("/TextField/") ? nativeInputFailures : nil,
            nativeTitleUpdateFailures: titleUpdateFailures
        ))
    }

    @objc private func frame(_ link: CADisplayLink) {
        if firstCallbackDelayMs == nil {
            firstCallbackDelayMs = (ProcessInfo.processInfo.systemUptime - phaseStart) * 1000
            entryIntervalMs = lastTimestamp.map { (link.timestamp - $0) * 1000 }
        } else if let previous = lastTimestamp {
            intervalsMs.append((link.timestamp - previous) * 1000)
            expectedIntervalsMs.append((link.targetTimestamp - link.timestamp) * 1000)
        }
        lastTimestamp = link.timestamp
        if phaseName.hasPrefix("native/"), phaseName.hasSuffix("/TextField/open"), intervalsMs.count % 12 == 0 {
            exerciseNativeTextField()
        }
        if phaseName.hasPrefix("webview/") {
            if webView == nil {
                webView = UIApplication.shared.connectedScenes
                    .compactMap { $0 as? UIWindowScene }
                    .flatMap(\.windows)
                    .compactMap { findWebView(in: $0) }
                    .first
            }
            if let webView {
                let scroll = webView.scrollView
                let minimumOffset = -scroll.adjustedContentInset.top
                let maximumOffset = max(minimumOffset, scroll.contentSize.height + scroll.adjustedContentInset.bottom - scroll.bounds.height)
                if maximumOffset > minimumOffset {
                    let elapsed = ProcessInfo.processInfo.systemUptime - phaseStart
                    let fraction = (1 - cos(elapsed * .pi)) / 2
                    scroll.setContentOffset(CGPoint(x: 0, y: minimumOffset + (maximumOffset - minimumOffset) * fraction), animated: false)
                    webViewScrollUpdates += 1
                    let maskContainers = (webView.superview?.subviews ?? []) + webView.subviews
                    for blur in maskContainers.compactMap({ $0 as? UIVisualEffectView }) {
                        guard let mask = blur.mask as? UIImageView, let image = mask.image else { continue }
                        let key = ObjectIdentifier(mask)
                        if let previous = maskImages[key], previous.0 == mask.bounds, previous.1 !== image {
                            stableSizeMaskImageChanges += 1
                        }
                        maskImages[key] = (mask.bounds, image)
                        webMaskSamples += 1
                    }
                }
            }
        }
    }

    private func nativeViewControllers() -> [UIViewController] {
        var pending = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap(\.windows)
            .compactMap(\.rootViewController)
        var visited = Set<ObjectIdentifier>()
        var controllers: [UIViewController] = []
        while let controller = pending.popLast() {
            guard visited.insert(ObjectIdentifier(controller)).inserted else { continue }
            controllers.append(controller)
            pending.append(contentsOf: controller.children)
            if let presented = controller.presentedViewController { pending.append(presented) }
        }
        return controllers
    }

    private func nativePresentedControllerCount() -> Int {
        Set(nativeViewControllers().compactMap { $0.presentedViewController }.map(ObjectIdentifier.init)).count
    }

    private func exerciseNativeTextField() {
        guard let field = nativeViewControllers()
            .compactMap({ $0 as? UIAlertController })
            .compactMap({ $0.textFields?.first }).first else { return }
        nativeInputAttempts += 1
        field.text = "ab"
        NotificationCenter.default.post(name: UITextField.textDidChangeNotification, object: field)
        if field.text != "AB" { nativeInputFailures += 1 }

        guard nativeKeyboardAttempts == 0, field.becomeFirstResponder() else { return }
        nativeKeyboardAttempts += 1
        guard let toolbar = field.inputAccessoryView?.subviews.compactMap({ $0 as? UIToolbar }).first,
              let done = toolbar.items?.last, let action = done.action, let target = done.target else { return }
        UIApplication.shared.sendAction(action, to: target, from: done, for: nil)
        if !field.isFirstResponder { nativeKeyboardDismissals += 1 }
    }

    private func findWebView(in view: UIView) -> WKWebView? {
        if let webView = view as? WKWebView { return webView }
        for child in view.subviews {
            if let webView = findWebView(in: child) { return webView }
        }
        return nil
    }

    private func finish() {
        endPhase()
        displayLink?.invalidate()
        displayLink = nil
        for observer in observers { NotificationCenter.default.removeObserver(observer) }
        observers.removeAll()
        thermalStateObserver = NotificationCenter.default.addObserver(
            forName: ProcessInfo.thermalStateDidChangeNotification, object: nil, queue: .main
        ) { [weak self] _ in
            MainActor.assumeIsolated { self?.save(completed: true) }
        }
        save(completed: true)
        print("IEN_DEVICE_STRESS_COMPLETE phases=\(phases.count)")
    }

    private func save(completed: Bool) {
        let maximumFramesPerSecond = UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }.first?.screen.maximumFramesPerSecond ?? 0
        let report = Report(
            runIdentifier: runIdentifier,
            completed: completed,
            activeCase: phaseName,
            model: UIDevice.current.model,
            systemVersion: UIDevice.current.systemVersion,
            maximumFramesPerSecond: maximumFramesPerSecond,
            currentThermalState: ProcessInfo.processInfo.thermalState.rawValue,
            phases: phases,
            measurements: measurements,
            clockWallTime: Date().timeIntervalSince1970,
            clockUptime: ProcessInfo.processInfo.systemUptime
        )
        let url = URL.documentsDirectory.appending(path: "device-stress-results.json")
        writer.async {
            do {
                let data = try JSONEncoder().encode(report)
                try data.write(to: url, options: .atomic)
            } catch {
                print("IEN_DEVICE_STRESS_REPORT_ERROR \(error)")
            }
        }
    }

    private func residentBytes() -> UInt64 {
        var info = mach_task_basic_info_data_t()
        var count = mach_msg_type_number_t(MemoryLayout<mach_task_basic_info_data_t>.size / MemoryLayout<integer_t>.size)
        let status = withUnsafeMutablePointer(to: &info) { pointer in
            pointer.withMemoryRebound(to: integer_t.self, capacity: Int(count)) { infoPointer in
                task_info(mach_task_self_, task_flavor_t(MACH_TASK_BASIC_INFO), infoPointer, &count)
            }
        }
        return status == KERN_SUCCESS ? UInt64(info.resident_size) : 0
    }
}
