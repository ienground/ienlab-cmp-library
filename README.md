[![official project](http://jb.gg/badges/official.svg)](https://github.com/JetBrains#jetbrains-on-github)

# Multiplatform library template

## What is it? 

This repository contains a simple library project, intended to demonstrate a [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) library that is deployable to [Maven Central](https://central.sonatype.com/).

The library has only one function: generate the [Fibonacci sequence](https://en.wikipedia.org/wiki/Fibonacci_sequence) starting from platform-provided numbers. Also, it has a test for each platform just to be sure that tests run.

Note that no other actions or tools usually required for the library development are set up, such as [tracking of backwards compatibility](https://kotlinlang.org/docs/jvm-api-guidelines-backward-compatibility.html#tools-designed-to-enforce-backward-compatibility), explicit API mode, licensing, contribution guideline, code of conduct and others. You can find a guide for best practices for designing Kotlin libraries [here](https://kotlinlang.org/docs/api-guidelines-introduction.html).

## Guide

Please find the detailed guide [here](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html).

# Other resources
* [Publishing via the Central Portal](https://central.sonatype.org/publish-ea/publish-ea-guide/)
* [Gradle Maven Publish Plugin \- Publishing to Maven Central](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)


---

## 🌿 Git 브랜치 전략 & 커밋 메시지 규칙

### 브랜치 토폴로지 (Branch Topology)
```text
main ← dev ← feature/*
hotfix/* → main → dev
```

### 브랜치 역할
- **main**: 항상 배포 가능한 상태를 유지합니다. 직접 `push`가 금지되며 PR을 통해서만 변경사항을 병합합니다.
- **dev**: 개발 통합 브랜치입니다. 모든 `feature/*` 브랜치는 최신 `dev`에서 분기합니다.
- **feature/***: 새로운 기능이나 일반 개발 작업에 사용합니다. 최신 `dev`에서 생성하고 작업 완료 후 `dev`로 PR을 생성합니다.
- **hotfix/***: 배포된 버전의 긴급 수정에 사용합니다. `main`에서 분기하며 수정 완료 후 `main`으로 병합합니다. `main` 병합 직후 변경 사항을 반드시 `dev`로 역동기화합니다.

### 머지 방식 (Merge Strategy)
| 방향 | GitHub 머지 방식 |
| --- | --- |
| `feature/* → dev` | Squash and merge |
| `dev → main` | Create a merge commit |
| `hotfix/* → main` | Create a merge commit |
| `main → dev` | Create a merge commit |

### main → dev 역동기화 규칙
`main`에 릴리스 또는 핫픽스 변경이 병합되면 즉시 `main → dev` 역동기화를 진행해야 합니다.

---

### 커밋 메시지 규칙

커밋 메시지는 다음 형식만 허용됩니다.

```text
<이모지> <타입>: <설명>
```

#### 허용 정규식
```regex
^(✨ feat|🐛 fix|📝 docs|💄 style|♻️ refactor|✅ test|🔧 chore|⚡ perf|👷 ci): .+$
```

#### 허용 타입 및 이모지
| 이모지 | 타입 | 용도 |
| :---: | :--- | :--- |
| ✨ | `feat` | 새로운 기능 |
| 🐛 | `fix` | 버그 수정 |
| 📝 | `docs` | 문서 변경 |
| 💄 | `style` | 코드 동작 변화 없는 포맷 또는 UI 스타일 변경 |
| ♻️ | `refactor` | 동작 변화 없는 코드 구조 개선 |
| ✅ | `test` | 테스트 추가 및 수정 |
| 🔧 | `chore` | 의존성, 설정, 유지보수 작업 |
| ⚡ | `perf` | 성능 개선 |
| 👷 | `ci` | CI/CD 설정 변경 |

#### 예시
- **유효한 예:**
  - `✨ feat: 음성 재료 추가 기능 구현`
  - `🐛 fix: 타이머가 초기화되지 않는 문제 수정`
- **유효하지 않은 예:**
  - `feat: 기능 추가` (이모지 누락)
  - `✨ 기능: 기능 추가` (타입 오류)
  - `fix 버그 수정` (`:` 구분자 누락)
  - `🐛 fix:` (설명 누락)

### 커밋 메시지 검증 (Git Hooks)
프로젝트 내 `.githooks/commit-msg` Hook이 구성되어 있습니다. 아래 명령을 실행하여 커밋 메시지 검증 Hook을 활성화할 수 있습니다.

```bash
git config core.hooksPath .githooks
# 또는 Gradle Task 실행
./gradlew installGitHooks
```
