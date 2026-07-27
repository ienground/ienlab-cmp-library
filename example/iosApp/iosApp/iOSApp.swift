import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseAuth

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        KoinInitializerKt.doInitKoin(additionalModules: [], appDeclaration: {_ in })
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    _ = Auth.auth().canHandle(url)
                }
        }
    }
}
