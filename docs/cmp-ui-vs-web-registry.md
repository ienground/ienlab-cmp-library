# CMP UI와 웹 레지스트리

웹 컴포넌트는 REUI의 `radix-nova` 기본 구현을 가져오는 방식으로 새로 시작합니다. 기존 CMP 기반 React 재구현과 이전 비교 명세는 이 전환으로 대체했습니다.

- 원본: https://github.com/keenthemes/reui
- 기준 커밋: `067aa0dc7f8bbfec1cd76fc5ad31f842be5f569a`
- 범위: REUI 기본 컴포넌트 27개, 필수 의존성을 포함한 설치 항목 58개
- 프리뷰: UI 컴포넌트 52개의 REUI 공식 예제
- 정의: `cmp-ui-web-registry/registry.json`
- 다시 가져오기: `npm --prefix cmp-ui-docs run import:reui`

컴포넌트는 REUI 원본 디자인과 API를 사용합니다. REUI 컴포넌트는 `components/reui`, 기반 shadcn/ui 컴포넌트는 `components/ui`에 설치됩니다. 사이트 전용 아이콘 경로는 Lucide 아이콘을 제공하는 공통 모듈로 연결합니다.

CMP Kotlin 컴포넌트와 웹 컴포넌트의 일대일 매핑 및 IEN 디자인 적용은 현재 레지스트리에 포함하지 않습니다. `tokens.json`은 문서 사이트의 기존 스타일 설정에 사용하며 설치 항목으로 배포하지 않습니다.
