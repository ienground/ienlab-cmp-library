import { Typography } from "@/components/ui/typography";
import {
  Component,
  lazy,
  Suspense,
  type ComponentType,
  type ReactNode,
} from "react";
import { Link } from "react-router";
import { CodeExample } from "./RegistryCodeExample";
import { webCatalog } from "./web-registry";
import { WebPlayground } from "./web-playground";
import { useWebProject } from "./web-project-context";

const previewModules = import.meta.glob<{ default: ComponentType }>(
  "../../cmp-ui-web-registry/**/previews/*.tsx",
);
const loadSources = () => import("./generated-web-sources.json");
const previews = Object.fromEntries(
  Object.entries(previewModules).map(([path, load]) => [
    path.replace("../../", ""),
    lazy(load),
  ]),
);
const sourceCache = new Map<string, ReturnType<typeof lazy>>();
function sourceComponent(path: string) {
  if (!sourceCache.has(path)) {
    if (!path) return null;
    sourceCache.set(
      path,
      lazy(async () => {
        const sources: Record<string, string> = (await loadSources()).default;
        const source = sources[path] ?? "소스를 찾을 수 없습니다.";
        return {
          default: () => (
            <CodeExample
              label={
                path.includes("/previews/")
                  ? "실행 가능한 React · Next.js 사용 예시"
                  : "설치되는 원본 소스"
              }
              code={source}
            />
          ),
        };
      }),
    );
  }
  return sourceCache.get(path)!;
}
class PreviewBoundary extends Component<
  { children: ReactNode; resetKey?: unknown },
  { error: string }
> {
  state = { error: "" };
  static getDerivedStateFromError(error: Error) {
    return { error: error.message };
  }
  componentDidUpdate(previous: { resetKey?: unknown }) {
    if (this.state.error && previous.resetKey !== this.props.resetKey) this.setState({ error: "" });
  }
  render() {
    return this.state.error ? (
      <p role="alert">프리뷰를 불러오지 못했습니다: {this.state.error}</p>
    ) : (
      this.props.children
    );
  }
}
export function WebComponentPreview({ componentId, resetKey }: { componentId: string; resetKey?: unknown }) {
  const item = webCatalog.find((entry) => entry.id === componentId);
  const Preview = item && previews[item.preview];
  return (
    <PreviewBoundary key={componentId} resetKey={resetKey}>
      <Suspense fallback={<p role="status">프리뷰를 불러오는 중입니다.</p>}>
        {Preview ? <Preview /> : <p role="alert">연결된 프리뷰가 없습니다.</p>}
      </Suspense>
    </PreviewBoundary>
  );
}
export function WebRegistryDocs({
  componentId,
  tab,
}: {
  componentId: string;
  tab: "preview" | "playground" | "registry";
}) {
  const { project, published, dirty } = useWebProject();
  const item = webCatalog.find((entry) => entry.id === componentId);
  if (!item) return <p role="alert">컴포넌트를 찾을 수 없습니다.</p>;
  const Preview = previews[item.preview];
  const Source = sourceComponent(item.source);
  const PreviewSource = sourceComponent(item.preview);
  return (
    <section
      className="content-panel web-component-docs"
      aria-label={`${item.name} 웹 컴포넌트`}
    >
      <div className="panel-heading">
        <div>
          <p className="panel-kicker">
            {tab === "preview" ? "LIVE WEB COMPONENT" : tab === "playground" ? "COMPONENT PLAYGROUND" : "SHADCN REGISTRY"}
          </p>
          <Typography variant="title1" asChild><h2>{item.name}</h2></Typography>
          <p>{item.description}</p>
        </div>
      </div>
      {tab === "playground" && Preview ? (
        <WebPlayground definition={item.playground} source={item.playground.example} Preview={Preview} renderPreview={(preview, resetKey) => (
          <PreviewBoundary resetKey={resetKey}><Suspense fallback={<p role="status">프리뷰를 불러오는 중입니다.</p>}>{preview}</Suspense></PreviewBoundary>
        )} />
      ) : tab === "preview" ? (
        <>
          <div className="web-component-preview min-h-48 overflow-auto rounded-ien-lg border border-ien-border p-ien-xl">
            <WebComponentPreview componentId={item.id} />
          </div>
          <CodeExample label="사용 예시" code={item.example} />
          <details className="web-docs-disclosure">
            <summary>프리뷰 전체 코드 · React / Next.js</summary>
            <Suspense
              fallback={<p role="status">예시 코드를 불러오는 중입니다.</p>}
            >
              {PreviewSource && <PreviewSource />}
            </Suspense>
          </details>
          <p>
            {item.client
              ? "상태·이벤트를 사용하는 클라이언트 컴포넌트입니다. Next.js의 서버 페이지에서 가져와 사용할 수 있습니다."
              : "서버 렌더링 가능한 컴포넌트입니다. 이벤트 핸들러를 전달할 때는 호출부를 클라이언트 컴포넌트로 작성하세요."}
          </p>
        </>
      ) : (
        <>
          <CodeExample
            label="레지스트리 등록 (최초 한 번)"
            code={
              'npx shadcn@latest registry add @ienlab="https://cmp.ien.zone/r/{name}.json"'
            }
          />
          <CodeExample label="컴포넌트 설치" code={item.installCommand} />
          {published && !dirty && <CodeExample label={`${project.title} 프로젝트 적용`} code={`npx shadcn@latest add @ienlab/${project.name}`} />}
          <p>
            React · Next.js 프로젝트에 shadcn/ui를 초기화한 뒤 설치합니다.
            REUI 원본 컴포넌트와 아래 내부 의존성은 CLI가 함께 설치합니다.
          </p>
          <ul>
            {item.dependencies.map((name) => (
              <li key={name}>
                {webCatalog.some((entry) => entry.id === name.replace("@ienlab/", "")) ? (
                  <Link to={`/web/${name.replace("@ienlab/", "")}/registry`}>{name}</Link>
                ) : (
                  <code>{name}</code>
                )}
              </li>
            ))}
          </ul>
          {item.packages.length > 0 && (
            <p>패키지: {item.packages.join(", ")}</p>
          )}
          <Suspense fallback={<p role="status">소스를 불러오는 중입니다.</p>}>
            {Source && <Source />}
          </Suspense>
          <details className="web-docs-disclosure">
            <summary>레지스트리 JSON</summary>
            <CodeExample
              label="레지스트리 정의"
              code={JSON.stringify(item.manifest, null, 2)}
            />
          </details>
        </>
      )}
      <section aria-label="접근성">
        <Typography variant="title2" asChild><h3>접근성</h3></Typography>
        <p>
          입력에는 연결된 label을, 아이콘만 있는 버튼에는 aria-label을
          지정하세요. 오버레이의 제목과 설명을 유지하고 Tab·방향키·Escape로
          조작할 수 있도록 기본 이벤트와 ref를 전달하세요. 비활성 상태는
          disabled 속성으로 제어합니다.
        </p>
      </section>
      <section aria-label="공개 API">
        <Typography variant="title2" asChild><h3>공개 API</h3></Typography>
        <p className="break-words text-sm">{item.api.join(", ")}</p>
        {item.apiEntries.map((entry) => (
          <details className="web-docs-disclosure" key={entry.name}>
            <summary>{entry.name}</summary>
            <pre className="api-signature" tabIndex={0}>
              <code>{entry.signature}</code>
            </pre>
            {entry.props.length > 0 && (
              <table>
                <thead>
                  <tr>
                    <th>속성</th>
                    <th>타입</th>
                    <th>필수</th>
                  </tr>
                </thead>
                <tbody>
                  {entry.props.map((prop) => (
                    <tr key={prop.name}>
                      <td>{prop.name}</td>
                      <td>
                        <code>{prop.type}</code>
                      </td>
                      <td>{prop.optional ? "선택" : "필수"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </details>
        ))}
      </section>
      {item.cmpIds.length > 0 && (
        <section>
          <Typography variant="title2" asChild><h3>CMP 대응 컴포넌트</h3></Typography>
          <ul>
            {item.cmpIds.map((id) => (
              <li key={id}>
                <Link to={`/components/${id}/preview`}>
                  {id} 프리뷰·Kotlin API
                </Link>
              </li>
            ))}
          </ul>
          <p>
            같은 IEN 디자인 토큰을 사용하며, React 속성과 DOM 접근성 방식에 맞춰
            API를 제공합니다.
          </p>
        </section>
      )}
      {item.limitations.length > 0 && (
        <section>
          <Typography variant="title2" asChild><h3>플랫폼 차이</h3></Typography>
          <ul>
            {item.limitations.map((note) => (
              <li key={note}>{note}</li>
            ))}
          </ul>
        </section>
      )}
    </section>
  );
}
