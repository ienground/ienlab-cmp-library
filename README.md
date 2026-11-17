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
import Shared
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
../gradlew :example:shared:embedAndSignAppleFrameworkForXcode
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

이 라이브러리는 `com.codingfeline.buildkonfig` 플러그인을 사용하여 컴파일 타임 상수를 주입합니다. 예제 `shared/build.gradle.kts`의 설정:

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
| `cmp-ui-kit` | 공통 UI 모듈 선택형 묶음 (Android/iOS) | cmp-common, cmp-icon, cmp-ui, cmp-adaptive |
| `cmp-common` | 공통 유틸리티, HIG 기본 | compose, hig-core |
| `cmp-ui` | 공통 UI 컴포넌트 (IenButton 등) | compose, cmp-common, cmp-utils |
| `cmp-adaptive` | Adaptive (Material3 + Cupertino) UI | compose-material3, hig-adaptive |
| `cmp-navigation` | Navigation3 기반 네비게이션 | navigation3, serialization |
| `cmp-date` | 날짜/시간 컴포넌트 | kdatetime, compose |
| `cmp-icon` | 아이콘 시스템 | compose, hig-adaptive |
| `cmp-utils` | 플랫폼 유틸리티 (Dlog, DecimalFormat 등) | compose, kotlinx-io |
| `cmp-pref` | 설정 저장소 (DataStore) | compose, datastore |
| `cmp-filekit` | 파일 관리 (FileKit) | filekit-dialogs |
| `cmp-coil` | 이미지 로딩 (Coil3) | coil-compose |
| `cmp-firebase` | Firebase Auth + Firestore (KMPAuth 대체) | zone.ien.firebase SDK |
| `cmp-ui-docs` | UI 문서 | - |

### 선택형 UI 묶음 사용

Android/iOS 앱은 `cmp-ui-kit` 하나로 `cmp-common`, `cmp-icon`, `cmp-ui`, `cmp-adaptive`를 함께 사용할 수 있습니다. 기존 Kotlin 패키지와 컴포넌트 호출은 그대로 유지됩니다.

```kotlin
commonMain.dependencies {
    implementation("zone.ien.utils:cmp-ui-kit:<version>")
}
```

`cmp-ui-kit`은 `cmp-adaptive`의 현재 타깃에 맞춰 Android/iOS를 제공합니다. Wasm 앱은 기존처럼 필요한 모듈을 각각 의존성에 추가해 주세요.

숫자 포맷에는 `cmp-utils`의 `zone.ien.utils.utils.DecimalFormat`을 사용합니다.
Android와 iOS에서 `DecimalFormat("0.##").format(12.3456)`은 `"12.35"`를 반환합니다.
인자 없는 생성자는 천 단위 구분과 소수점 없는 표시를 유지합니다. 구분 기호와 통화 기호는 현재 로케일을 따릅니다.

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

## React · Next.js 레지스트리

[REUI](https://reui.io)의 `radix-nova` 기본 컴포넌트 27개와 필요한 shadcn/ui 컴포넌트·훅을 가져와 제공합니다. 전체 58개 설치 항목 중 UI 컴포넌트 52개에 공식 예제를 연결했습니다.

```bash
npx shadcn@latest registry add @ienlab="https://cmp.ien.zone/r/{name}.json"
npx shadcn@latest add @ienlab/button @ienlab/alert @ienlab/data-grid
```

```tsx
import { Alert, AlertTitle } from "@/components/reui/alert";

export default function Example() {
  return <Alert><AlertTitle>알림</AlertTitle></Alert>;
}
```

REUI 원본 디자인과 API를 사용합니다. 필요한 소스·패키지·의미색 변수는 CLI가 함께 설치합니다. React 19와 Tailwind CSS 4, shadcn/ui 초기화가 필요합니다.

TypeScript의 `compilerOptions.types`를 지정한 프로젝트는 `node`를 포함하세요. REUI의 개발 환경 검사에 필요한 `@types/node`는 해당 항목과 함께 설치됩니다.

컴포넌트 목록과 설치 정의는 [레지스트리](cmp-ui-web-registry/registry.json)에 있습니다. `/web/{name}/preview`와 `/web/{name}/registry`에서 공식 예제, 공개 API, 설치 코드를 확인합니다.

REUI 소스는 `067aa0dc7f8bbfec1cd76fc5ad31f842be5f569a` 커밋에 고정했습니다. 다음 명령으로 동일한 소스를 다시 가져올 수 있습니다.

```bash
npm --prefix cmp-ui-docs run import:reui
```

### 문서와 레지스트리 빌드

```bash
npm --prefix cmp-ui-docs ci
npm --prefix cmp-ui-docs test
npm --prefix cmp-ui-docs run test:preview
npm --prefix cmp-ui-docs run build:compose
npm --prefix cmp-ui-docs run build:site
```

`generate:api`는 CMP 원본 토큰과 레지스트리 소스에서 docs 목록·API·사용 코드·타입 경로를 생성합니다. 생성물은 Git에 포함하지 않습니다. `test:preview`는 52개 공식 예제의 서버 렌더링을 검사하며, 실제 브라우저 상호작용 검증은 별도로 수행해야 합니다.

---

## 예제 앱 (example/)

`example/` 디렉토리에는 Android와 iOS에서 라이브러리를 사용하는 방법을 보여주는 예제 앱이 포함되어 있습니다.

```
example/
├── androidApp/         # Android Host 앱 (Application, AndroidManifest)
├── shared/             # 공통 Compose 코드 (commonMain/androidMain/iosMain)
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

<a id="native-navigation-integration"></a>

### 플랫폼 내비게이션 바 (iOS 네이티브 내비게이션)

`cmp-navigation`의 `BaseNavDisplay`를 사용하는 앱은 아래 wrapper로 기존 Navigation 3 그래프를 재사용합니다. iOS는 UIKit 탭·스택을 사용하고, 다른 플랫폼은 기존 Compose 화면과 Nav3 백스택을 사용합니다.

이 절은 **ienlab-cmp 소비 앱의 통합 가이드**입니다. `BaseNavigationFrame`, `BaseNavigationTabs`, `BaseNavigationGuard`는 이 저장소의 API입니다. 기반 라이브러리의 `NativeStack`, `NativeTabs`, 상태 복원 등은 [compose-swift-navigation3의 한국어 README](https://github.com/ienground/compose-swift-navigation3/blob/main/README_ko.md#migration)를 참고하세요.

#### 1. iOS 호스트와 공통 프레임

기본 iOS 진입점은 다음과 같습니다. 별도의 Swift 내비게이션 계층은 필요하지 않습니다.

```kotlin
fun MainViewController(): UIViewController {
    initializeIenIos()
    return NativeNavigationViewController { App() }
}
```

공통 `App()`에서 DI 공급자와 화면 UI를 분리합니다. `AppTheme`, `AppNavigation`, DI 공급자는 소비 앱의 기존 구현을 사용합니다.

```kotlin
BaseNavigationFrame(
    providers = { content ->
        IenTheme(tokens = appTokens()) { ProvideAppDependencies { content() } }
    },
    frame = { content ->
        AppTheme { AppRootUI { content() } }
    },
) {
    AppNavigation()
}
```

`providers`에는 `IenTheme`의 토큰·DI처럼 UI에 의존하지 않는 공급자만 둡니다. `appTokens()`는 앱의 기존 `IenTokens` 생성 함수입니다. `BaseNavigationFrame`은 공급된 `IenTheme.colors.brand`를 UIKit 내비게이션 바·툴바·뒤로가기·주요 액션의 강조색으로 전달하며, 테마 토큰 변경도 반영합니다. 네이티브 탭 구성과 개별 화면이 같은 테마를 사용하며, `BaseNavigationTabs`는 `IenTheme.colors.brand`를 선택 탭의 아이콘·제목 색으로 자동 전달합니다. 라이트·다크 테마 변경도 반영하며 비선택 탭은 시스템 기본색을 유지합니다. `frame`에는 `IenAdaptiveTheme`·`CupertinoTheme`를 포함한 화면 테마와 루트 UI를 둡니다. 화면 테마는 `LocalHapticFeedback` 등 UI 환경에 의존하므로 UI 없는 호스트의 `providers`에서 실행하면 안 됩니다. `BaseNavigationFrame`이 바 여백을 연결하므로 앱에서 `LocalCupertinoNativeBarsPadding`을 다시 제공하지 않습니다. 스플래시·업데이트 검사 화면도 `BaseNavDisplay`의 엔트리로 선언합니다.

#### 2. 기존 Nav3 그래프에 탭 wrapper 적용

다음 코드는 소비 앱의 기존 `mainBackStack`, `tabBackStack`, `tabRoutes`, `entries`, 화면 함수를 연결하는 예입니다. `entries`의 첫 엔트리는 앱의 탭 루트이며, 그 뒤의 엔트리는 선택한 탭 위에 열리는 공유 상세 화면입니다. `TabContent()`는 기존 탭 그래프를 그립니다.

```kotlin
BaseNavigationGuard(
    backStack = mainBackStack,
    isAllowed = { route -> canOpen(route) },
    onDenied = { route -> showAccessMessage(route) },
) {
    BaseNavigationTabs(
        tabBackStack = tabBackStack,
        tabs = tabRoutes,
        title = { route -> stringResource(route.label) },
        icon = { route -> route.icon },
        selectedIcon = { route -> route.selectedIcon },
        tabContent = { TabContent() },
        overlay = { AppFeedback() },
    ) {
        BaseNavDisplay(
            backStack = mainBackStack,
            entryProvider = entries,
        )
    }
}
```

- `BaseNavigationTabs`는 기존 `BaseNavDisplay`를 감쌉니다. `tabBackStack`은 앱과 UIKit이 공유하는 탭 선택 상태입니다. 현재 wrapper는 각 탭을 루트 엔트리 하나로 선언하며, 상세 화면은 `mainBackStack`에 쌓습니다. 탭마다 독립된 상세 백스택을 직접 구성하려면 `BaseTabNavDisplay`를 사용합니다.
- `title`은 탭 제목이고, `icon`·`selectedIcon`은 기존 `IconData.Vector` 또는 `IconData.Paint`입니다. 변환은 라이브러리가 처리하며 UIKit의 선택·비활성 tint를 사용합니다. 기존 `systemImage` overload도 유지합니다. 탭은 기본적으로 큰 제목을 사용하며, `largeTitle = false`로 인라인 제목을 선택할 수 있습니다.
- `tabContent`에는 해당 탭의 기존 화면 구성을 선언합니다. 화면 구성이 탭마다 다르면 인자로 받은 route로 구분합니다.
- 로그인 안내·오류 다이얼로그·토스트처럼 현재 화면 위에 표시할 UI는 `overlay`에 둡니다. 네이티브 모드의 그래프 선언 본문에 직접 UI를 만들지 않습니다.
- `BaseNavigationGuard`는 선택 사항입니다. 앱은 접근 허용 규칙과 거부 안내만 제공하고, guard가 차단된 화면의 렌더링·진입 효과를 막고 루트를 남긴 채 최상위 차단 엔트리를 제거합니다.
- 앱에서 `NativeTab` 생성, 서로 다른 route를 `NavKey`로 재포장, 공유 스택의 `drop(1)`·pop 분배, 네이티브용 decorator, `otherPlatforms` 분기를 다시 구현하지 않습니다.

직접 탭 목록을 구성할 때도 `ienlab-cmp`의 `NativeTab` 팩토리를 사용하면 `Painter` 변환을 앱에서 하지 않아도 됩니다. 기본·선택 이미지는 UIKit 탭 아이콘으로 전달되며, 선택 아이콘을 생략하면 UIKit이 기본 이미지를 사용합니다.

```kotlin
import zone.ien.utils.navigation.NativeTab

NativeTab(
    title = tabTitle,
    icon = route.icon,
    selectedIcon = route.selectedIcon,
    backStack = tabBackStack,
)
```

바깥 루트 그래프가 로그인 화면과 메인 탭 그래프를 함께 관리한다면, 메인 그래프 엔트리에 메타데이터를 붙입니다. 나머지 엔트리의 typed route와 이동 콜백은 유지합니다.

```kotlin
entry<RootRoute.Main>(metadata = BaseNavDisplay.navigationGraph()) {
    MainNavigationGraph()
}
```

탭 화면에서는 공유 선택 상태의 `lastOrNull()` 대신 `currentNavigationRoute(tabBackStack)`을 사용합니다. 숨겨진 탭도 자신의 route를 유지하며, 내부 `BaseNavDisplay`는 wrapper가 지정한 탭 엔트리를 표시합니다. `directRoute` 인자나 `entries(route).Content()` 우회 경로를 앱에 추가할 필요가 없습니다.

```kotlin
val route = currentNavigationRoute(tabBackStack)
val isActive = isNavigationEntryActive

BaseNavigationTabBar {
    AppBottomNavigation()
}
```

`isNavigationEntryActive`는 네이티브 화면의 최상위 여부를 전달합니다. 음성 인식·튜토리얼 등 화면 전용 부수 효과의 실행 조건에 사용합니다. `BaseNavigationTabBar`는 네이티브 탭 화면에서 앱 탭 바를 숨기고, 다른 환경에서는 기존 탭 바를 표시합니다.

#### 3. 화면 제목·액션·스크롤·검색 연결

`AdaptiveTopAppBarScaffold`의 문자열 `title` overload를 사용하면 제목과 지원되는 액션을 네이티브 바에 전달합니다. `ActionMenuItem.icon`도 같은 `IconData`를 전달하므로 기존 액션 아이콘을 재사용합니다. 추가 선행 버튼과 툴바 버튼은 `zone.ien.utils.ui.screen.NativeBarButton(title, icon = ..., onClick = ...)` 팩토리로 선언하고 `nativeLeading` 등에 전달합니다. 고정 route 메타데이터는 `nativeScreen(title = ..., largeTitle = ...)`로 지정할 수도 있습니다.

액션의 `AlwaysShown`은 상단 바에 유지하고, `ShownIfRoom`은 화면 너비와 선행·주요 액션을 고려한 표시 공간(최대 3개)에 따라 더보기 메뉴로 이동합니다. `NeverShown`은 항상 더보기 메뉴에 표시하며 숨긴 항목은 공간을 차지하지 않습니다. 주요 액션은 더보기 버튼 뒤에 고정하고 일반 액션 그룹과 간격을 둡니다. 네이티브 바에서는 UIKit 강조 스타일로 표시합니다. 아이콘·활성 상태·클릭 동작을 메뉴 안에서도 유지합니다. 배지는 0이면 숨김, 양수이면 숫자, 음수이면 표시점이며, 상단 바 버튼에는 원본 색상을 보존한 배지 이미지로, 더보기 메뉴 안에서는 UIKit 부제목으로 표시합니다.

주 세로 스크롤에는 `ienVerticalScroll` 또는 `IenLazyColumn`을 사용합니다. 상태 연결과 Compose 제스처 전환을 공통 컴포넌트가 처리합니다. 기존 저수준 `rememberNativeScreenScroll` API도 유지합니다.

```kotlin
val scrollState = rememberScrollState()

AdaptiveTopAppBarScaffold(title = screenTitle) { padding, title ->
    Column(
        modifier = Modifier
            .ienVerticalScroll(scrollState)
            .padding(padding),
    ) {
        title()
        ScreenContent()
    }
}
```

목록은 `IenLazyColumn(state = listState, ...) { ... }`으로 선언합니다. 일반 `LazyColumn`의 배치·패딩·fling·사용자 스크롤 옵션을 유지합니다. 이 연결이 있어야 UIKit이 큰 제목 접힘과 스크롤 경계 효과를 추적할 수 있습니다. `IenProvideSectionStyle`·`AdaptiveProvideSectionStyle`이 소유하는 스크롤은 컴포넌트에서 연결하므로 화면에서 중복 연결하지 않습니다. 중첩 미리보기·작은 보조 목록을 주 스크롤로 등록하지 않습니다.

위 스캐폴드의 `padding`에는 네이티브 바 여백이 이미 포함되므로 바 여백을 추가로 더하지 않습니다.

```kotlin
val usesNativeSearch = NativeScreenSearch(
    query = query,
    onQueryChange = onQueryChange,
    placeholder = searchPlaceholder,
)
if (!usesNativeSearch) {
    AppSearchField()
}
```

네이티브 검색창이 연결된 경우 기존 검색 필드를 숨깁니다. 입력 제한·검증과 실제 검색 동작은 소비 앱이 기존 규칙을 유지합니다. 시트는 자체 상단 바와 스크롤을 사용하며, 부모 화면의 네이티브 스크롤을 대신 등록하지 않습니다.

#### 바 여백과 기존 컴포넌트의 동작

`BaseNavigationFrame`을 사용하지 않고 기반 라이브러리를 직접 연결하는 경우에만 다음처럼 여백을 수동 제공합니다.



iOS UIKit `UINavigationController`처럼 플랫폼이 내비게이션 바를 그리는 화면(예: compose-swift-navigation3의 네이티브 화면)에서는, 그 바가 차지하는 여백을 compose-hig의 `LocalCupertinoNativeBarsPadding`으로 넘깁니다. Cupertino와 IEN 두 디자인이 같은 값을 따르므로 앱은 한 곳에서만 제공합니다.

```kotlin
NativeNavigationFrame(frame = { content ->
    CompositionLocalProvider(
        LocalCupertinoNativeBarsPadding provides if (LocalNativeScreen.current) nativeContentPadding() else null,
    ) {
        AppTheme(content)
    }
}) { /* NativeStack / NativeTabs */ }
```

값이 있으면 다음처럼 동작합니다. 기본값 `null`이면 지금과 같습니다.

- `AdaptiveTopAppBarScaffold`, `AdaptiveScaffold`, `IenScaffold`는 상단 바를 그리지 않습니다. 콘텐츠 여백은 `contentWindowInsets`가 여백을 두는 변만 플랫폼 바 여백으로 바꾸고, 0인 변은 그대로 0입니다(compose-hig `withCupertinoNativeBars` 규칙). 그래서 `RootWrapper`처럼 `WindowInsets(0)`으로 쓰는 틀은 여백을 더하지 않고, 그 안의 화면 스캐폴드만 여백을 받습니다. 부모가 이미 소비한 인셋은 제외합니다. 하단 바가 있으면 아래 여백은 그 높이와 남은 플랫폼 아래 여백 중 큰 값입니다. 콘텐츠의 `imePadding()`은 0이 됩니다(여백에 키보드 영역이 들어 있음).
- `IenTopAppBar`, `AdaptiveTopAppBar`, 큰 제목(`TopBarMode.Expanded`의 제목)은 그리지 않습니다. 제목과 버튼은 플랫폼 바에 선언합니다.
- `IenScaffold`의 상단 블러와 Duo 사이드 바 배치는 쓰지 않습니다. 하단 블러는 하단 바가 있을 때만 그립니다.
- `AdaptiveWebViewScaffold`는 iOS 상단 블러를 그리지 않습니다.
- 바텀시트(`IenBottomSheet`) 안에서는 적용하지 않습니다. 시트는 자기 상단 바를 그립니다.
- Cupertino 디자인의 동작은 compose-hig 문서의 "플랫폼 내비게이션 바"를 따릅니다. compose-hig `27.2.7-alpha01-dev03` 이상이 필요합니다.

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
