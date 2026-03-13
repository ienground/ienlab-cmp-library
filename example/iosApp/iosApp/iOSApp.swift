import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinInitializerKt.doInitKoin(additionalModules: [], appDeclaration: {_ in })
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
