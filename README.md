# IENLab CMP Library

IENGROUND의 Kotlin & Compose Multiplatform 헬퍼 라이브러리입니다.

멀티모듈 KMP 구성으로, Android와 iOS에서 공통으로 사용할 수 있는 UI 컴포넌트, Firebase 인증, 파일 관리, 네비게이션, 아이콘, 날짜/시간, 설정 저장소 등을 제공합니다.

---

## 수동 설정 가이드

이 라이브러리를 사용하려면 아래 설정들을 프로젝트에서 직접 구성해야 합니다. 자동으로 처리되지 않는 항목들이므로 빠짐없이 따라야 합니다.

---

### 1. Firebase Console — 공통 (Android / iOS 모두)

Firebase Console(https://console.firebase.google.com)에서 프로젝트를 생성하고 다음을 등록합니다.

| 항목 | 설명 |
|---|---|
| **Android 앱** | 패키지명 `zone.ien.utils.example` (또는 사용 중인 패키지명) 등록. 디버그/릴리스 SHA-1 지문 등록 |
| **iOS 앱** | 번들 ID `zone.ien.utils.example.Example` (또는 사용 중인 번들 ID) 등록 |
| **Google Sign-In** | Firebase Console → Authentication → Sign-in method → Google 사용 설정. **Web Client ID** 복사 |
| **Apple Sign-In** | Firebase Console → Authentication → Sign-in method → Apple 사용 설정. **Services ID** 등록 필요 (Android Web OAuth용) |

Firebase Console 등록 후, 각 플랫폼에 맞는 설정 파일과 Client ID를 다운로드/복사해야 합니다.

---

### 2. Android 전용 설정

#### 2.1 google-services.json

Firebase Console → Project settings → 일반 → 내 앱 (Android) → `google-services.json` 다운로드.

```
your-android-app/
  └── google-services.json    <-- 여기에 배치
```

`.gitignore`에 포함하여 커밋되지 않도록 합니다.

#### 2.2 AndroidManifest.xml — Application 등록

`AndroidManifest.xml`에서 `android:name`에 `Application` 서브클래스를 지정합니다.

```xml
<application
    android:name=".MyApplication"
    ...>
```

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication.applicationContext)
        }
    }
}
```

#### 2.3 build.gradle.kts — google-services 플러그인

Android 앱 모듈의 `build.gradle.kts`에 `google-services` 플러그인이 선언되어 있어야 합니다.

```kotlin
plugins {
    alias(libs.plugins.google.services)
}
```

루트 `build.gradle.kts`에도 `apply false`로 선언되어 있어야 합니다.

```kotlin
plugins {
    alias(libs.plugins.google.services) apply false
}
```

#### 2.4 build.gradle.kts — Firebase Android 의존성

`cmp-firebase` 모듈을 구현하고 있다면, Android 앱 모듈의 의존성에 Firebase Android 라이브러리가 포함되어야 합니다.

```kotlin
dependencies {
    implementation(libs.firebase.common.android)
    implementation(projects.cmpFirebase)
}
```

`cmp-firebase` 모듈이 내부적으로 `firebase-auth-android`, `firebase-firestore-android`, `firebase-storage-android`, `androidx-credentials`, `credentials-play-services-auth`, `googleid` 등을 사용하므로, Android 앱 모듈에서 선언할 필요 없이 모듈 자체에서 이미 처리합니다.

#### 2.5 local.properties — GCP_WEB_CLIENT_ID

Google Sign-In에 필요한 Web Client ID를 `local.properties`에 설정합니다.

```properties
GCP_WEB_CLIENT_ID=123456789-xxxxxxxxxxxxxxxxx.apps.googleusercontent.com
```

BuildKonfig가 이 값을 읽어 `BuildKonfig.GCP_WEB_CLIENT_ID`로 컴파일 타임에 주입합니다.

값을 주입하는 우선순위는 다음과 같습니다.
1. `local.properties`의 `GCP_WEB_CLIENT_ID`
2. `gradle.properties`의 `GCP_WEB_CLIENT_ID`
3. 환경 변수 `GCP_WEB_CLIENT_ID`
4. 모두 없으면 빈 문자열

> **주의**: `local.properties`는 `.gitignore`에 포함되어 있으므로, 팀 공유 시 다른 방식을 사용해야 합니다.

#### 2.6 Firebase Console — SHA-1 지문 등록

Android Google Sign-In이 정상 작동하려면 Firebase Console에 앱의 SHA-1 지문이 등록되어 있어야 합니다.

```bash
./gradlew signingReport
```

출력된 `SHA1` 값을 Firebase Console → Android 앱 설정 → SHA 인증서 지문에 추가합니다.

---

### 3. iOS 전용 설정

#### 3.1 GoogleService-Info.plist

Firebase Console → Project settings → 일반 → 내 앱 (iOS) → `GoogleService-Info.plist` 다운로드.

```
your-ios-app/
  └── GoogleService-Info.plist    <-- Xcode 프로젝트의 iosApp 그룹 아래에 배치

`.gitignore`에 포함하여 커밋되지 않도록 합니다.

#### 3.2 iOSApp.swift — Firebase 초기화 및 URL 콜백

SwiftUI `@main` 진입점에서 `FirebaseApp.configure()`와 URL 핸들러를 설정합니다.

```swift
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
                    // Firebase Auth URL 콜백 (Google/Apple Web OAuth)
                    _ = Auth.auth().canHandle(url)
                }
        }
    }
}
```

#### 3.3 Info.plist — URL Scheme 등록

Firebase Auth에서 웹 기반 OAuth 흐름(Google Sign-In, Apple Web OAuth 등)이 완료된 후 앱으로 돌아오기 위해 URL Scheme 등록이 필요합니다.

`GoogleService-Info.plist`에서 다음 두 값을 URL Scheme으로 등록합니다.

| 값 | 출처 | 용도 |
|---|---|---|
| `REVERSED_CLIENT_ID` | `GoogleService-Info.plist`의 `REVERSED_CLIENT_ID` 키 값 | Google Sign-In 리버스 클라이언트 ID |
| Firebase 커스텀 Scheme | `GOOGLE_APP_ID`에서 `:`를 `-`로 치환 (`app-` + `{GOOGLE_APP_ID}`) | Firebase 일반 OAuth 콜백 (Apple 등) |

예를 들어 `GOOGLE_APP_ID`가 `1:628868686373:ios:2cf17c08fddc74b31095cc`라면 Firebase 커스텀 Scheme은 `app-1-628868686373-ios-2cf17c08fddc74b31095cc`입니다.

`Info.plist`에 다음을 추가합니다.

```xml
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleTypeRole</key>
        <string>Editor</string>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>com.googleusercontent.apps.123456789-xxxxxxxxxxxx</string>
            <string>app-1-628868686373-ios-xxxxxxxxxxxxxxxx</string>
        </array>
    </dict>
</array>
```

> Google Sign-In만 사용한다면 `REVERSED_CLIENT_ID`만 있어도 동작할 수 있습니다. Apple Sign-In이나 Web OAuth 폴백이 필요하면 Firebase 커스텀 Scheme도 함께 등록해야 합니다.

#### 3.4 Xcode — Sign in with Apple Capability

Apple Sign-In (iOS 네이티브)을 사용하려면 Xcode에서 Capability를 추가해야 합니다.

1. Xcode → 프로젝트 → Target → **Signing & Capabilities** 탭
2. **+ Capability** → **Sign in with Apple** 추가
3. 자동 생성된 `.entitlements` 파일에 다음이 포함되었는지 확인

```xml
<key>com.apple.developer.applesignin</key>
<array>
    <string>Default</string>
</array>
```

#### 3.5 SPM (Swift Package Manager) — Firebase iOS SDK

iOS에서 Firebase 인증/파이어스토어가 동작하려면 Swift Package 종속성을 연결해야 합니다.

##### KotlinMultiplatformLinkedPackage (권장)

`example/iosApp/KotlinMultiplatformLinkedPackage/` 디렉토리 구조를 참고하여, Firebase iOS SDK의 패키지들을 subpackages로 구성합니다.

각 subpackage의 `Package.swift`는 다음과 같습니다.

```swift
// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "zone_ien_firebase_firebase_auth_1_0_0_beta08",
  platforms: [.iOS("15.0")],
  products: [
    .library(name: "zone_ien_firebase_firebase_auth_1_0_0_beta08", type: .none, targets: ["zone_ien_firebase_firebase_auth_1_0_0_beta08"])
  ],
  dependencies: [
    .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "12.14.0")
  ],
  targets: [
    .target(name: "zone_ien_firebase_firebase_auth_1_0_0_beta08", dependencies: [
      .product(name: "FirebaseAuth", package: "firebase-ios-sdk")
    ])
  ]
)
```

필요한 subpackages:
- `zone_ien_firebase_firebase_auth_1_0_0_beta08` → `FirebaseAuth` (v12.14.0+)
- `zone_ien_firebase_firebase_common_1_0_0_beta08` → `FirebaseCore`
- `zone_ien_firebase_firebase_firestore_1_0_0_beta08` → `FirebaseFirestore`
- `zone_ien_firebase_firebase_storage_1_0_0_beta08` → `FirebaseStorage`

최상위 `Package.swift`에서 이 subpackage들을 모두 의존성으로 참조합니다.

```swift
dependencies: [
    .package(path: "subpackages/zone_ien_firebase_firebase_auth_1_0_0_beta08"),
    .package(path: "subpackages/zone_ien_firebase_firebase_firestore_1_0_0_beta08"),
    // ...
]
```

> **Xcode 연동**: Xcode → Target → **Package Dependencies**에 `KotlinMultiplatformLinkedPackage`를 추가하거나, Xcode 프로젝트 파일 시스템 동기화로 `iosApp/` 폴더가 자동 인식되도록 합니다. 예제의 `iosApp.xcodeproj`는 File System Synchronized Groups를 사용합니다.

#### 3.6 Configuration — xcconfig

`Configuration/Config.xcconfig`에서 기본 앱 설정 값을 관리합니다.

```ini
TEAM_ID=

PRODUCT_NAME=Example
PRODUCT_BUNDLE_IDENTIFIER=zone.ien.utils.example.Example$(TEAM_ID)

CURRENT_PROJECT_VERSION=1
MARKETING_VERSION=1.0
```

`TEAM_ID`에 Apple Developer Team ID를 설정해야 시뮬레이터/실기기에서 빌드할 수 있습니다. Team ID는 Apple Developer → Account → Membership에서 확인합니다.

#### 3.7 Xcode — Compile Kotlin Framework Build Phase

Xcode 프로젝트에 "Compile Kotlin Framework" 빌드 페이즈가 있어야 Kotlin Framework가 번들에 포함됩니다.

1. Xcode → Target → **Build Phases** 탭
2. **+** → **New Run Script Phase**
3. 이름을 "Compile Kotlin Framework"로 지정
4. 스크립트 내용 (예제 기준):

```sh
cd "$SRCROOT/.."
../gradlew :example:composeApp:embedAndSignAppleFrameworkForXcode
```

---

### 4. 공통 (Android / iOS 모두)

#### 4.1 Dlog 초기화 (isDebug)

앱의 공통 Composable 진입점에서 로깅 시스템을 초기화합니다.

```kotlin
// 최상위 App() Composable
fun App() {
    Dlog.init(isDebug = true)  // 릴리스 빌드에서는 false
    // ...
}
```

`Dlog.init(isDebug = true)`가 호출되어야만 `Dlog.d()`, `Dlog.e()` 등의 로그가 출력됩니다. 호출하지 않으면 모든 로그가 무시됩니다.

#### 4.2 GoogleAuthProvider.create()

앱이 시작될 때 `GoogleAuthProvider.create()`를 한 번 호출하여 Google Sign-In Credentials를 등록합니다.

```kotlin
// BuildKonfig 사용 (권장)
GoogleAuthProvider.create(
    credentials = GoogleAuthCredentials(serverId = BuildKonfig.GCP_WEB_CLIENT_ID)
)
```

직접 값을 하드코딩할 수도 있습니다.

```kotlin
GoogleAuthProvider.create(
    credentials = GoogleAuthCredentials(serverId = "123456789-xxxxxxxxx.apps.googleusercontent.com")
)
```

> 이 호출은 remember 블록 안에서 앱 전체 수명 동안 한 번만 실행되어야 합니다. Compose에서는 `remember { GoogleAuthProvider.create(...) }`로 감싸는 것을 권장합니다.

            additionalModules + listOf(commonModule)
        )
    }
}
```

Android는 `MyApplication`에서, iOS는 `iOSApp.swift`의 `init()`에서 호출합니다. 각 플랫폼에 맞는 컨텍스트(Android Context 등)가 필요한 경우 `appDeclaration` 파라미터를 통해 전달할 수 있습니다.

---

### 5. BuildKonfig 설정

이 라이브러리는 `com.codingfeline.buildkonfig` 플러그인을 사용하여 컴파일 타임 상수를 주입합니다. 예제 `composeApp/build.gradle.kts`의 설정:

```kotlin
buildkonfig {
    packageName = "zone.ien.utils.example"
    defaultConfigs {
        val clientId = localProps.getProperty("GCP_WEB_CLIENT_ID")
            ?: project.findProperty("GCP_WEB_CLIENT_ID") as? String
            ?: System.getenv("GCP_WEB_CLIENT_ID")
            ?: ""
        buildConfigField(STRING, "GCP_WEB_CLIENT_ID", clientId)
    }
}
```

값을 주입하는 우선순위:
1. `local.properties`의 `GCP_WEB_CLIENT_ID`
2. `gradle.properties`의 `GCP_WEB_CLIENT_ID`
3. 환경 변수 `GCP_WEB_CLIENT_ID`
4. 모두 없으면 빈 문자열 → 인증 실패

---

## 프로젝트 구성

### 모듈 목록

| 모듈 | 설명 | 주요 의존성 |
|---|---|---|
| `cmp-common` | 공통 유틸리티, HIG 기본 | compose, hig-core |
| `cmp-ui` | 공통 UI 컴포넌트 (IenButton, IenText 등) | compose, cmp-common, cmp-utils |
| `cmp-adaptive` | Adaptive (Material3 + Cupertino) UI | compose-material3, hig-adaptive |
| `cmp-navigation` | Navigation3 기반 네비게이션 | navigation3, serialization |
| `cmp-date` | 날짜/시간 컴포넌트 | kdatetime, compose |
| `cmp-icon` | 아이콘 시스템 | compose, hig-adaptive |
| `cmp-utils` | 플랫폼 유틸리티 (Dlog, etc.) | compose, kotlinx-io |
| `cmp-pref` | 설정 저장소 (DataStore) | compose, datastore |
| `cmp-filekit` | 파일 관리 (FileKit) | filekit-dialogs |
| `cmp-coil` | 이미지 로딩 (Coil3) | coil-compose |
| `cmp-firebase` | Firebase Auth + Firestore (KMPAuth 대체) | zone.ien.firebase SDK |
| `cmp-ui-docs` | UI 문서 | - |

### 기술 스택

- Kotlin 2.4.10
- Compose Multiplatform 1.11.1
- Android SDK 37 (compile), API 29 (min)
- iOS deployment target 15.0+
- Firebase Kotlin SDK (zone.ien.firebase, v1.0.0-beta08)
- Firebase iOS SDK 12.14.0+ (SPM)
- Koin 4.2.2 의존성 주입
- Kotlinx Serialization 1.11.0
- BuildKonfig 0.22.0 (컴파일 타임 상수)

---

## 예제 앱 (example/)

`example/` 디렉토리에는 Android와 iOS에서 라이브러리를 사용하는 방법을 보여주는 예제 앱이 포함되어 있습니다.

```
example/
├── androidApp/         # Android Host 앱 (Application, AndroidManifest)
├── composeApp/         # 공통 Compose 코드 (commonMain/androidMain/iosMain)
└── iosApp/             # iOS Host 앱 (SwiftUI, Xcode)
```

예제 앱을 실행하려면 위에서 설명한 모든 수동 설정(google-services.json, GoogleService-Info.plist, GCP_WEB_CLIENT_ID 등)이 완료되어 있어야 합니다.

### 예제 앱 실행

**Android:**
```bash
./gradlew :example:androidApp:installDebug
```

**iOS:**
Xcode에서 `example/iosApp/iosApp.xcodeproj` 열고 scheme을 `Example`로 선택 후 실행.

---

## 주요 API

### Firebase Auth (KMPAuth 대체)

```kotlin
// 1. 앱 시작 시 GoogleAuthProvider 초기화
remember {
    GoogleAuthProvider.create(
        GoogleAuthCredentials(serverId = BuildKonfig.GCP_WEB_CLIENT_ID)
    )
}

// 2. Google Sign-In State
val googleState = rememberFirebaseGoogleSignInState(
    onResult = { result ->
        // Result<FirebaseUser?>
    }
)
googleState.launch()

// 3. Apple Sign-In State
val appleState = rememberFirebaseAppleSignInState(
    onResult = { result ->
        // Result<FirebaseUser?>
    }
)
appleState.launch()
```

---

## 🌿 Git 브랜치 전략 & 커밋 메시지 규칙

### 브랜치 토폴로지

```
main ← dev ← feature/*
hotfix/* → main → dev
```

### 머지 방식

| 방향 | 머지 방식 |
|---|---|
| feature/* → dev | Squash and merge |
| dev → main | Create a merge commit |
| hotfix/* → main | Create a merge commit |
| main → dev | Create a merge commit |

### 커밋 메시지 형식

```
<이모지> <타입>: <설명>
```

| 이모지 | 타입 | 용도 |
|---|---|---|
| ✨ | feat | 새로운 기능 |
| 🐛 | fix | 버그 수정 |
| 📝 | docs | 문서 변경 |
| 💄 | style | 코드 스타일 변경 |
| ♻️ | refactor | 코드 구조 개선 |
| ✅ | test | 테스트 추가 및 수정 |
| 🔧 | chore | 의존성, 설정, 유지보수 |
| ⚡ | perf | 성능 개선 |
| 👷 | ci | CI/CD 설정 변경 |
