import { Typography } from "@/components/ui/typography";
import { useEffect, useRef, useState } from "react";
import { generatePath } from "@lisse/core/path";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { CodeExample } from "./RegistryCodeExample";
import { WebComponentPreview } from "./WebRegistryDocs";
import { webCatalog } from "./web-registry";
import {
  isProjectCssValue,
  parseProject,
  projectNamePattern,
  projectRegistryItem,
  typographyRoles,
  type ProjectMode,
} from "./project-presets";
import { projectDefaults, useWebProject } from "./web-project-context";

export function WebProjects({
  theme,
  onThemeChange,
}: {
  theme: "light" | "dark";
  onThemeChange: (theme: "light" | "dark") => void;
}) {
  const {
    project,
    projects,
    published,
    dirty,
    storageError,
    select,
    edit,
    setVariable,
    reset,
    add,
  } = useWebProject();
  const [newName, setNewName] = useState("");
  const [message, setMessage] = useState("");
  const [mode, setMode] = useState<"light" | "dark">("light");
  const [previewId, setPreviewId] = useState("button");
  const [viewport, setViewport] = useState("desktop");
  const [previewRevision, setPreviewRevision] = useState(0);
  const [drafts, setDrafts] = useState<Record<string, string>>({});
  const fileInput = useRef<HTMLInputElement>(null);
  const radiusSamples = useRef(new Map<string, SVGSVGElement>());
  const json = JSON.stringify(projectRegistryItem(project), null, 2);
  useEffect(() => {
    let frame = 0;
    function drawCorners() {
      for (const svg of radiusSamples.current.values()) {
        const style = getComputedStyle(svg);
        const size = svg.getBoundingClientRect().width;
        if (!size) continue;
        const radius = style.borderTopLeftRadius;
        const normalizedRadius = radius.endsWith("%")
          ? (Number.parseFloat(radius) * 48) / 100
          : (Number.parseFloat(radius) * 48) / size;
        const smoothing = Number.parseFloat(
          style.getPropertyValue("--ien-control-corner-smoothing"),
        );
        svg.querySelector("path")?.setAttribute(
          "d",
          generatePath(47, 47, {
            radius: Math.max(0, normalizedRadius - 0.5),
            smoothing: Number.isFinite(smoothing)
              ? Math.min(1, Math.max(0, smoothing))
              : 1,
            preserveSmoothing: true,
          }),
        );
      }
    }
    function schedule() {
      cancelAnimationFrame(frame);
      frame = requestAnimationFrame(drawCorners);
    }
    const observer = new ResizeObserver(schedule);
    for (const svg of radiusSamples.current.values()) observer.observe(svg);
    schedule();
    return () => {
      cancelAnimationFrame(frame);
      observer.disconnect();
    };
  }, [project.cssVars]);
  function create() {
    if (!projectNamePattern.test(newName)) {
      setMessage(
        "프로젝트 ID는 영문 소문자로 시작하고 영문 소문자·숫자·하이픈만 사용할 수 있습니다.",
      );
      return;
    }
    try {
      add(
        parseProject({
          ...project,
          name: newName,
          title: newName,
          description: "",
          meta: {},
        }),
      );
      setNewName("");
      setDrafts({});
      setMessage("현재 설정을 복사한 프로젝트를 브라우저에 만들었습니다.");
    } catch (error) {
      setMessage(
        error instanceof Error
          ? error.message
          : "프로젝트를 만들지 못했습니다.",
      );
    }
  }
  function download() {
    const url = URL.createObjectURL(
      new Blob([json + "\n"], { type: "application/json;charset=utf-8" }),
    );
    const link = document.createElement("a");
    link.href = url;
    link.download = `${project.name}.json`;
    link.click();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
    setMessage(`${project.name}.json을 다운로드했습니다.`);
  }
  function token(mode: ProjectMode, name: string) {
    return (
      project.cssVars[mode][name] ??
      (mode === "dark"
        ? (projectDefaults.dark[name] ?? project.cssVars.light[name])
        : undefined) ??
      project.cssVars.theme[name] ??
      projectDefaults[mode][name] ??
      projectDefaults.light[name] ??
      projectDefaults.theme[name] ??
      (name === "spacing" ? "0.25rem" : "")
    );
  }
  function variable(
    mode: ProjectMode,
    name: string,
    label: string,
    property: string,
    placeholder?: string,
  ) {
    const key = `${project.name}.${mode}.${name}`;
    const applied = name.startsWith("ien-project-")
      ? (project.cssVars[mode][name] ?? "")
      : token(mode, name);
    return (
      <label className="project-token" key={`${mode}.${name}`}>
        <span>{label}</span>
        <Input
          value={drafts[key] ?? applied}
          placeholder={placeholder}
          aria-label={label}
          onChange={(event) => {
            const value = event.target.value.trim();
            setDrafts((previous) => ({
              ...previous,
              [key]: event.target.value,
            }));
            if (
              !value ||
              (isProjectCssValue(value) && CSS.supports(property, value))
            ) {
              setVariable(mode, name, value || undefined);
              setMessage("");
            }
          }}
          onBlur={(event) => {
            const value = event.target.value.trim();
            if (
              value &&
              (!isProjectCssValue(value) || !CSS.supports(property, value))
            )
              setMessage(
                `${label}: 유효한 CSS ${property} 값을 입력하세요. 마지막 적용값을 유지합니다.`,
              );
            setDrafts((previous) => {
              const next = { ...previous };
              delete next[key];
              return next;
            });
          }}
        />
        <code>--{name}</code>
      </label>
    );
  }
  return (
    <div className="project-workspace">
      <section
        className="content-panel project-presets"
        aria-label="프로젝트 설정"
      >
        <div className="panel-heading">
          <Typography variant="title1" asChild><h2>
            {project.title}
            {dirty ? " · 편집됨" : ""}
          </h2></Typography>
        </div>
        <div className="project-editor">
          <div className="playground-heading">
            <label className="playground-target">
              프로젝트
              <select
                className="playground-select"
                aria-label="프로젝트"
                value={project.name}
                onChange={(event) => {
                  select(event.target.value);
                  setDrafts({});
                  setMessage("");
                }}
              >
                {projects.map((entry) => (
                  <option key={entry.name} value={entry.name}>
                    {entry.title} · {entry.name}
                  </option>
                ))}
              </select>
            </label>
            <Button
              variant="line"
              size="sm"
              disabled={!dirty && !Object.keys(drafts).length}
              onClick={() => {
                setDrafts({});
                reset();
              }}
            >
              프로젝트 편집 초기화
            </Button>
          </div>
          <p>
            {published ? "레지스트리 프로젝트" : "브라우저에 저장된 프로젝트"} ·
            편집값은 프로젝트별로 이 브라우저에 저장됩니다. 프리뷰와
            Playground에 함께 적용됩니다.
          </p>
          <p>
            유효한 토큰 값은 입력 즉시 미리보기에 적용됩니다. 폰트 파일은 소비
            프로젝트에서 별도로 불러오세요.
          </p>
          {storageError && <p role="alert">{storageError}</p>}
          <div className="playground-controls">
            <label className="project-token">
              프로젝트 제목
              <Input
                value={project.title}
                onChange={(event) => edit({ title: event.target.value })}
                onBlur={() => {
                  if (!project.title.trim()) edit({ title: project.name });
                }}
              />
            </label>
            <label className="project-token">
              프로젝트 설명
              <Input
                value={project.description}
                onChange={(event) => edit({ description: event.target.value })}
              />
            </label>
          </div>
          <div className="project-actions">
            <Input
              aria-label="새 프로젝트 ID"
              placeholder="새 프로젝트 ID"
              value={newName}
              onChange={(event) => setNewName(event.target.value)}
            />
            <Button variant="line" size="sm" onClick={create}>
              현재 설정으로 프로젝트 추가
            </Button>
            <Button
              variant="line"
              size="sm"
              onClick={() => fileInput.current?.click()}
            >
              프로젝트 JSON 가져오기
            </Button>
            <input
              ref={fileInput}
              type="file"
              accept="application/json,.json"
              hidden
              onChange={async (event) => {
                const file = event.target.files?.[0];
                event.target.value = "";
                if (!file) return;
                try {
                  if (file.size > 100_000)
                    throw new Error("프로젝트 JSON은 100KB 이하여야 합니다.");
                  add(parseProject(JSON.parse(await file.text())));
                  setDrafts({});
                  setMessage("프로젝트를 브라우저로 가져왔습니다.");
                } catch (error) {
                  setMessage(
                    error instanceof Error
                      ? error.message
                      : "프로젝트를 가져오지 못했습니다.",
                  );
                }
              }}
            />
          </div>
          <section aria-label="프로젝트 반경">
            <Typography variant="title2" asChild><h3>반경</h3></Typography>
            <p>기존 radius 값에 연속 곡률(continuous)을 적용합니다.</p>
            <div className="playground-controls">
              {Object.keys(projectDefaults.light)
                .filter((name) => name.startsWith("ien-radius-"))
                .map((name) =>
                  variable(
                    "light",
                    name,
                    name.replace("ien-radius-", "radius · "),
                    "border-radius",
                  ),
                )}
            </div>
          </section>
          <section aria-label="프로젝트 간격과 폰트">
            <Typography variant="title2" asChild><h3>간격·폰트</h3></Typography>
            <div className="playground-controls">
              {variable("theme", "spacing", "레이아웃 간격 단위", "width")}
              {variable(
                "light",
                "ien-control-spacing-unit",
                "컨트롤 간격 단위",
                "width",
              )}
              {Object.keys(projectDefaults.light)
                .filter((name) => name.startsWith("ien-spacing-"))
                .map((name) =>
                  variable(
                    "light",
                    name,
                    name.replace("ien-spacing-", "spacing · "),
                    "width",
                  ),
                )}
              {variable("light", "ien-font-sans", "본문 폰트", "font-family")}
              {variable("light", "ien-font-mono", "코드 폰트", "font-family")}
            </div>
          </section>
          <section aria-label="프로젝트 타이포그래피">
            <Typography variant="title2" asChild><h3>타이포그래피</h3></Typography>
            <p>
              기존 컴포넌트의 글자 크기를 유지합니다. 값을 편집한 역할에만
              크기·행간·굵기를 적용합니다.
            </p>
            <div className="project-typography" key={project.name}>
              {typographyRoles.map((role) => (
                <fieldset key={role}>
                  <legend>{role}</legend>
                  <div className="playground-controls">
                    {(
                      [
                        ["font-size", "크기", "font-size"],
                        ["line-height", "행간", "line-height"],
                        ["font-weight", "굵기", "font-weight"],
                      ] as const
                    ).map(([suffix, label, property]) => {
                      const name = `ien-project-${suffix}-${role}`;
                      return variable(
                        "light",
                        name,
                        `${role} ${label}`,
                        property,
                        `기존 값 유지 (${token("light", `ien-${suffix}-${role}`)})`,
                      );
                    })}
                  </div>
                </fieldset>
              ))}
            </div>
          </section>
          <section aria-label="프로젝트 색상">
            <div className="playground-heading">
              <Typography variant="title2" asChild><h3>색상</h3></Typography>
              <label>
                편집할 색상 모드{" "}
                <select
                  className="playground-select"
                  value={mode}
                  onChange={(event) => {
                    const next =
                      event.target.value === "dark" ? "dark" : "light";
                    setMode(next);
                    onThemeChange(next);
                  }}
                >
                  <option value="light">라이트</option>
                  <option value="dark">다크</option>
                </select>
              </label>
            </div>
            <div className="playground-controls" key={project.name + mode}>
              {Object.keys(projectDefaults.dark)
                .filter(
                  (name) =>
                    name.startsWith("ien-") &&
                    !name.startsWith("ien-syntax-") &&
                    name !== "ien-neutral" &&
                    name !== "ien-on-neutral" &&
                    name !== "ien-neutral-weak",
                )
                .map((name) =>
                  variable(
                    mode,
                    name,
                    `${mode === "light" ? "라이트" : "다크"} · ${name.replace("ien-", "")}`,
                    "color",
                  ),
                )}
            </div>
          </section>
          <p role="status">{message}</p>
          <div className="project-actions">
            <Button size="sm" onClick={download}>
              프로젝트 JSON 다운로드
            </Button>
          </div>
          <p>
            공유하려면 JSON을{" "}
            <code>cmp-ui-web-registry/projects/{project.name}.json</code>에
            추가하고 레지스트리를 빌드·배포하세요. 브라우저 편집값은 서버에 자동
            반영되지 않습니다.
          </p>
          {published && !dirty ? (
            <>
              <CodeExample
                label="기존 소비 프로젝트의 레지스트리 등록"
                code={
                  'npx shadcn@latest registry add @ienlab="https://cmp.ien.zone/r/{name}.json"'
                }
              />
              <CodeExample
                label="새 소비 프로젝트 초기화"
                code={`npx shadcn@latest init https://cmp.ien.zone/r/${project.name}.json --base radix`}
              />
              <CodeExample
                label="기존 소비 프로젝트에 적용"
                code={`npx shadcn@latest add @ienlab/${project.name}`}
              />
            </>
          ) : (
            <p>
              현재 편집값은 JSON을 배포한 뒤 <code>@ienlab/{project.name}</code>
              으로 설치할 수 있습니다.
            </p>
          )}
          <details className="web-docs-disclosure">
            <summary>프로젝트 JSON</summary>
            <CodeExample label={`${project.name}.json`} code={json} />
          </details>
        </div>
      </section>
      <aside className="project-preview-panel" aria-label="프로젝트 미리보기">
        <div className="playground-heading">
          <div>
            <Typography variant="title1" asChild><h2>실시간 미리보기</h2></Typography>
            <p>
              {project.title}
              {dirty ? " · 편집 중" : ""}
            </p>
          </div>
          <Button
            variant="line"
            size="sm"
            onClick={() => setPreviewRevision((value) => value + 1)}
          >
            데모 초기화
          </Button>
        </div>
        <div className="project-preview-toolbar">
          <label>
            컴포넌트
            <select
              className="playground-select"
              aria-label="미리보기 컴포넌트"
              value={previewId}
              onChange={(event) => setPreviewId(event.target.value)}
            >
              {webCatalog.map((item) => (
                <option key={item.id} value={item.id}>
                  {item.name}
                </option>
              ))}
            </select>
          </label>
          <label>
            테마
            <select
              className="playground-select"
              aria-label="미리보기 테마"
              value={theme}
              onChange={(event) =>
                onThemeChange(event.target.value === "dark" ? "dark" : "light")
              }
            >
              <option value="light">라이트</option>
              <option value="dark">다크</option>
            </select>
          </label>
          <label>
            너비
            <select
              className="playground-select"
              aria-label="미리보기 너비"
              value={viewport}
              onChange={(event) => setViewport(event.target.value)}
            >
              <option value="desktop">데스크톱</option>
              <option value="tablet">태블릿</option>
              <option value="mobile">모바일</option>
            </select>
          </label>
        </div>
        <section
          className="project-preview-components"
          aria-label="컴포넌트 미리보기"
        >
          <div
            style={{
              width:
                viewport === "mobile"
                  ? "360px"
                  : viewport === "tablet"
                    ? "768px"
                    : "100%",
              minWidth:
                viewport === "desktop"
                  ? undefined
                  : viewport === "mobile"
                    ? "360px"
                    : "768px",
            }}
          >
            <WebComponentPreview
              key={`${project.name}.${previewRevision}`}
              componentId={previewId}
              resetKey={project.cssVars}
            />
          </div>
        </section>
        <section
          className="project-preview-tokens"
          aria-label="타이포그래피 미리보기"
        >
          <Typography variant="title2" asChild><h3>타이포그래피</h3></Typography>
          {typographyRoles.map((role) => (
            <div className="project-type-sample" key={role}>
              <code>
                {role} · {token(theme, `ien-font-size-${role}`)}
              </code>
              <span
                data-ien-typography={role}
                style={{
                  fontFamily:
                    role === "display"
                      ? "var(--ien-font-display)"
                      : "var(--ien-font-sans)",
                  fontSize: `var(--ien-font-size-${role})`,
                  lineHeight: `var(--ien-line-height-${role})`,
                  fontWeight: `var(--ien-font-weight-${role})`,
                }}
              >
                {role === "display"
                  ? project.title
                  : "프로젝트 디자인 · Aa 0123"}
              </span>
            </div>
          ))}
        </section>
        <section
          className="project-preview-tokens"
          aria-label="반경과 간격 미리보기"
        >
          <Typography variant="title2" asChild><h3>반경·간격</h3></Typography>
          <div className="project-radius-samples">
            {Object.keys(projectDefaults.light)
              .filter((name) => name.startsWith("ien-radius-"))
              .map((name) => (
                <div key={name}>
                  <svg
                    ref={(element) => {
                      if (element) radiusSamples.current.set(name, element);
                      else radiusSamples.current.delete(name);
                    }}
                    className="project-radius-sample"
                    viewBox="0 0 48 48"
                    aria-label={`${name.replace("ien-radius-", "")} continuous 코너`}
                    role="img"
                    style={{ borderRadius: `var(--${name})` }}
                  >
                    <path transform="translate(0.5 0.5)" />
                  </svg>
                  <code>{name.replace("ien-radius-", "")}</code>
                </div>
              ))}
          </div>
          {Object.keys(projectDefaults.light)
            .filter((name) => name.startsWith("ien-spacing-"))
            .map((name) => (
              <div className="project-spacing-sample" key={name}>
                <code>{name.replace("ien-spacing-", "")}</code>
                <span style={{ width: `var(--${name})` }} />
              </div>
            ))}
        </section>
        <section
          className="project-preview-tokens"
          aria-label="의미적 색상 미리보기"
        >
          <Typography variant="title2" asChild><h3>의미적 색상</h3></Typography>
          <div className="project-tone-samples">
            {["neutral", "brand", "success", "warning", "danger", "info"].map(
              (tone) => (
                <div
                  key={tone}
                  style={{
                    background: `var(--ien-${tone})`,
                    color: `var(--ien-on-${tone})`,
                  }}
                >
                  {tone}
                </div>
              ),
            )}
          </div>
        </section>
      </aside>
    </div>
  );
}
