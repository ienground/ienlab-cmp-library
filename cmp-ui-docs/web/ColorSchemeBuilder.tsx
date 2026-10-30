import { Typography } from "@/components/ui/typography";
import { useCallback, useEffect, useState } from "react";
import { Link } from "react-router";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/reui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  colorDescriptions,
  colorSchemesFromVariables,
  colorSchemeVariables,
  colorRoles,
  contrastRatio,
  defaultColorSchemes,
  defaultColorSeeds,
  generateColorSchemes,
  generateKotlinTheme,
  iconStyles,
  isHexColor,
  semanticGroups,
  toKotlinIdentifier,
} from "./color-scheme";
import type { ColorMode, ColorSchemes, IconStyle } from "./color-scheme";
import { copyTextToClipboard } from "./clipboard";
import { projectDefaults, useWebProject } from "./web-project-context";
import { projectRegistryItem } from "./project-presets";
import { CodeBlock } from "../../cmp-ui-web-registry/reui/code-block/code-block";

export const colorSchemeSections = [
  { id: "color-seeds", label: "기준 색상" },
  { id: "color-roles", label: "색상 역할" },
  { id: "color-contrast", label: "상태 색상과 대비" },
  { id: "color-code", label: "테마 코드" },
];

const kotlinKeywords = new Set([
  "as",
  "class",
  "const",
  "data",
  "else",
  "enum",
  "false",
  "fun",
  "if",
  "import",
  "internal",
  "interface",
  "isSystemInDarkTheme",
  "object",
  "private",
  "public",
  "return",
  "suspend",
  "this",
  "true",
  "val",
  "var",
  "when",
  "where",
]);

export function highlightKotlin(source: string) {
  let offset = 0;
  return source
    .split(/(\/\/[^\n]*|\/\*[\s\S]*?\*\/|"""[\s\S]*?"""|"(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'|@[A-Za-z_]\w*|0x[\da-fA-F]+|[A-Za-z_]\w*|\d+(?:\.\d+)?[fFL]?|\s+)/g)
    .filter(Boolean)
    .map((token, index) => {
      const tokenOffset = offset;
      offset += token.length;
      if (/^\s+$/.test(token)) return token;

      let tokenClass = "";
      if (token.startsWith("//") || token.startsWith("/*")) tokenClass = "kotlin-comment";
      else if (token.startsWith("@")) tokenClass = "kotlin-annotation";
      else if (token.startsWith('"') || token.startsWith("'")) tokenClass = "kotlin-string";
      else if (/^(?:0x[\da-fA-F]+|\d+(?:\.\d+)?[fFL]?)$/.test(token)) {
        tokenClass = "kotlin-number";
      } else if (kotlinKeywords.has(token)) tokenClass = "kotlin-keyword";
      else if (/^[A-Z]/.test(token)) tokenClass = "kotlin-type";
      else if (/^\s*\(/.test(source.slice(tokenOffset + token.length))) {
        tokenClass = "kotlin-function";
      } else if (/^\s*[:=]/.test(source.slice(tokenOffset + token.length))) {
        tokenClass = "kotlin-property";
      }

      return tokenClass ? (
        <span className={tokenClass} key={`${index}-${tokenOffset}`}>
          {token}
        </span>
      ) : (
        token
      );
    });
}

function ColorInput({
  id,
  label,
  value,
  allowAlpha = true,
  onChange,
  onValidityChange,
}: {
  id: string;
  label: string;
  value: string;
  allowAlpha?: boolean;
  onChange: (value: string) => void;
  onValidityChange: (id: string, valid: boolean) => void;
}) {
  const [draft, setDraft] = useState(value);
  useEffect(() => {
    setDraft(value);
    onValidityChange(id, true);
  }, [value, id, onValidityChange]);
  const valid = isHexColor(draft, allowAlpha);
  function update(next: string) {
    setDraft(next);
    const nextValid = isHexColor(next, allowAlpha);
    onValidityChange(id, nextValid);
    if (nextValid) onChange(next.toUpperCase());
  }
  return (
    <div className="color-field">
      <label htmlFor={id}>{label}</label>
      <div className="color-inputs">
        <Input
          aria-label={`${label} 컬러피커`}
          className="color-picker"
          type="color"
          value={value.slice(0, 7)}
          onChange={(event) => update(event.target.value + value.slice(7))}
        />
        <Input
          id={id}
          aria-invalid={!valid}
          aria-describedby={!valid ? `${id}-error` : undefined}
          className="color-hex"
          value={draft}
          spellCheck={false}
          onChange={(event) => update(event.target.value)}
        />
      </div>
      {!valid && (
        <span className="color-error" id={`${id}-error`}>
          {allowAlpha ? "#RRGGBB 또는 #RRGGBBAA" : "#RRGGBB"} 형식으로
          입력하세요.
        </span>
      )}
    </div>
  );
}

export function ColorSchemeBuilder({
  themeMode,
  iconStyle,
  onIconStyleChange,
  onApply,
  onApplyWeb,
}: {
  themeMode: ColorMode;
  iconStyle: IconStyle;
  onIconStyleChange: (style: IconStyle) => void;
  onApply: (schemes: ColorSchemes, mode: ColorMode) => void;
  onApplyWeb: (mode: ColorMode) => void;
}) {
  const { project, projects, select, edit } = useWebProject();
  const [seeds, setSeeds] = useState(defaultColorSeeds);
  const [schemes, setSchemes] = useState(defaultColorSchemes);
  const [appName, setAppName] = useState("Lovehero");
  const [mode, setMode] = useState<ColorMode>(themeMode);
  const [invalidFields, setInvalidFields] = useState<Set<string>>(
    () => new Set(),
  );
  const [message, setMessage] = useState("");
  const [resetRevision, setResetRevision] = useState(0);
  const validate = useCallback((id: string, valid: boolean) => {
    setInvalidFields((previous) => {
      if (previous.has(id) === !valid) return previous;
      const next = new Set(previous);
      if (valid) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);
  const code = generateKotlinTheme(schemes, appName, iconStyle);
  const variables = colorSchemeVariables(schemes);
  const projectWithColors = {
    ...project,
    config: { ...project.config, iconLibrary: "tabler" },
    cssVars: {
      ...project.cssVars,
      light: { ...project.cssVars.light, ...variables.light },
      dark: { ...project.cssVars.dark, ...variables.dark },
    },
  };
  const webCode = JSON.stringify(projectRegistryItem(projectWithColors), null, 2);
  function loadProjectColors() {
    if (project.config.iconLibrary === "tabler") onIconStyleChange("tabler");
    const light = { ...projectDefaults.theme, ...projectDefaults.light, ...project.cssVars.theme, ...project.cssVars.light };
    const dark = { ...light, ...projectDefaults.dark, ...project.cssVars.dark };
    const next = colorSchemesFromVariables({ light, dark });
    setSchemes(next);
    const nextSeeds = defaultColorSeeds();
    for (const { base } of semanticGroups) nextSeeds[base] = next.light[base];
    setSeeds(nextSeeds);
    setInvalidFields(new Set());
    setResetRevision((previous) => previous + 1);
    setMessage(`${project.title}의 색상을 불러왔습니다.`);
  }
  const appIdentifier = toKotlinIdentifier(appName);
  const scheme = schemes[mode];
  const invalid = invalidFields.size > 0;
  async function copyCode() {
    const copied = await copyTextToClipboard(code);
    setMessage(
      copied
        ? "Kotlin 코드를 복사했습니다."
        : "복사하지 못했습니다. 코드를 선택해 복사하거나 파일을 다운로드하세요.",
    );
  }
  function downloadCode() {
    const url = URL.createObjectURL(
      new Blob([code], { type: "text/plain;charset=utf-8" }),
    );
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = `${appIdentifier}Theme.kt`;
    anchor.click();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
    setMessage(`${appIdentifier}Theme.kt를 다운로드했습니다.`);
  }
  return (
    <div className="color-builder">
      <div className="panel-heading">
        <div>
          <p className="panel-kicker">THEME BUILDER</p>
          <Typography variant="title1" asChild><h2>ColorScheme 생성기</h2></Typography>
        </div>
        <Badge variant="line">IenColorScheme · 32 colors</Badge>
      </div>
      <p className="panel-description">
        기준색을 선택하면 라이트·다크 상태 색상을 자동 생성합니다. 세부 색상을
        조정한 뒤 같은 팔레트를 CMP 미리보기 또는 선택한 웹 프로젝트에 적용하세요.
      </p>
      <Card className="color-builder-card">
        <Typography variant="title2" asChild><h3>웹 프로젝트</h3></Typography>
        <p className="color-hint">웹 적용 대상입니다. 기존 프로젝트 색상으로 편집을 시작하려면 불러오기를 선택하세요.</p>
        <label className="playground-target">
          프로젝트
          <select className="playground-select" aria-label="컬러 스킴 프로젝트" value={project.name} onChange={(event) => select(event.target.value)}>
            {projects.map((entry) => <option key={entry.name} value={entry.name}>{entry.title} · {entry.name}</option>)}
          </select>
        </label>
        <div className="color-actions">
          <Button variant="line" onClick={loadProjectColors}>프로젝트 색상 불러오기</Button>
          <Button asChild variant="line"><Link to="/projects">프로젝트 설정</Link></Button>
        </div>
      </Card>
      <Card className="color-builder-card app-name-card">
        <div>
          <label htmlFor="color-scheme-app-name">앱 이름</label>
          <p className="color-hint">
            입력한 이름으로 색상 토큰과 테마 함수 이름을 만듭니다.
          </p>
        </div>
        <Input
          id="color-scheme-app-name"
          aria-label="앱 이름"
          value={appName}
          onChange={(event) => {
            setAppName(event.target.value);
            setMessage("");
          }}
        />
      </Card>
      <Card className="color-builder-card">
        <Typography variant="title2" asChild><h3>아이콘 스타일</h3></Typography>
        <p className="color-hint">
          Tabler는 CMP와 웹 프로젝트에 적용됩니다. Material 스타일은 CMP 미리보기와 Kotlin 테마에 적용되며, 웹 컴포넌트는 Tabler를 사용합니다.
        </p>
        <Tabs
          value={iconStyle}
          onValueChange={(value) => {
            if (Object.hasOwn(iconStyles, value)) {
              onIconStyleChange(value as IconStyle);
              setMessage("");
            }
          }}
        >
          <TabsList aria-label="아이콘 스타일" className="flex h-auto flex-wrap">
            {(Object.keys(iconStyles) as IconStyle[]).map((style) => (
              <TabsTrigger key={style} value={style}>
                {iconStyles[style].label}
              </TabsTrigger>
            ))}
          </TabsList>
        </Tabs>
      </Card>
      <Card id={colorSchemeSections[0].id} className="color-builder-card">
        <Typography variant="title2" asChild><h3>기준 색상</h3></Typography>
        <div className="color-seeds">
          {semanticGroups.map((group) => (
            <ColorInput
              key={`${resetRevision}-${group.base}`}
              id={`seed-${group.base}`}
              label={group.label}
              value={seeds[group.base]}
              allowAlpha={false}
              onValidityChange={validate}
              onChange={(value) => {
                const next = { ...seeds, [group.base]: value };
                setSeeds(next);
                setSchemes(generateColorSchemes(next));
                setMessage("");
              }}
            />
          ))}
        </div>
        <p className="color-hint">
          기준색을 바꾸면 양쪽 모드의 상태 색상과 개별 수정값을 다시 생성합니다.
          중립색은 라이브러리 기본값을 사용합니다.
        </p>
        <Button
          variant="line"
          onClick={() => {
            setSeeds(defaultColorSeeds());
            setSchemes(defaultColorSchemes());
            setInvalidFields(new Set());
            setResetRevision((previous) => previous + 1);
            setMessage("라이브러리 기본 색상으로 복원했습니다.");
          }}
        >
          기본 색상으로 초기화
        </Button>
      </Card>
      <Tabs
        id={colorSchemeSections[1].id}
        value={mode}
        onValueChange={(value) => {
          if (value === "light" || value === "dark") setMode(value);
        }}
      >
        <TabsList aria-label="팔레트 모드">
          <TabsTrigger value="light">라이트</TabsTrigger>
          <TabsTrigger value="dark">다크</TabsTrigger>
        </TabsList>
        {(["light", "dark"] as const).map((colorMode) => (
          <TabsContent
            key={colorMode}
            value={colorMode}
            forceMount
            className="content-panel color-palette"
          >
            <Card className="color-builder-card">
              <Typography variant="title2" asChild><h3>색상 역할 편집</h3></Typography>
              <p className="color-hint">
                HEX 8자리의 마지막 두 자리는 투명도입니다. 예: #00000099
              </p>
              <div className="color-roles">
                {colorRoles.map((role) => (
                  <div key={role}>
                    <ColorInput
                      key={`${resetRevision}-${colorMode}-${role}`}
                      id={`${colorMode}-${role}`}
                      label={role}
                      value={schemes[colorMode][role]}
                      onValidityChange={validate}
                      onChange={(value) =>
                        setSchemes((previous) => ({
                          ...previous,
                          [colorMode]: {
                            ...previous[colorMode],
                            [role]: value,
                          },
                        }))
                      }
                    />
                    <p className="color-role-description">
                      {colorDescriptions[role]}
                    </p>
                  </div>
                ))}
              </div>
            </Card>
          </TabsContent>
        ))}
      </Tabs>
      <Card id={colorSchemeSections[2].id} className="color-builder-card">
        <Typography variant="title2" asChild><h3>상태 색상과 대비</h3></Typography>
        <div
          className="color-swatches"
          style={{
            backgroundColor: scheme.background,
            color: scheme.textPrimary,
          }}
        >
          {semanticGroups.map((group) => {
            const opaqueBackgrounds = [
              scheme.background,
              scheme[group.base],
              scheme[group.weak],
            ].every((color) => color.length === 7 || color.endsWith("FF"));
            const strong = contrastRatio(scheme[group.on], scheme[group.base]);
            const weak = contrastRatio(
              scheme[group.onWeak],
              scheme[group.weak],
            );
            return (
              <div key={group.base}>
                <div
                  className="color-swatch"
                  style={{
                    backgroundColor: scheme[group.base],
                    color: scheme[group.on],
                  }}
                >
                  {group.label}
                </div>
                <div
                  className="color-swatch"
                  style={{
                    backgroundColor: scheme[group.weak],
                    color: scheme[group.onWeak],
                  }}
                >
                  {group.label} Weak
                </div>
                <p>
                  {opaqueBackgrounds
                    ? `${strong.toFixed(2)} / ${weak.toFixed(2)} : 1`
                    : "투명색 대비는 바탕색에 따라 달라집니다."}
                </p>
                {opaqueBackgrounds && Math.min(strong, weak) < 4.5 && (
                  <span className="color-error">텍스트 대비 4.5:1 미만</span>
                )}
              </div>
            );
          })}
        </div>
        <div className="color-actions">
          <Button disabled={invalid} onClick={() => onApply(schemes, mode)}>CMP 미리보기에 적용</Button>
          <Button disabled={invalid} onClick={() => {
            edit({ iconLibrary: projectWithColors.config.iconLibrary, cssVars: projectWithColors.cssVars });
            onApplyWeb(mode);
          }}>웹 프로젝트에 적용</Button>
        </div>
      </Card>
      <Card id={colorSchemeSections[3].id} className="color-builder-card">
        <div className="color-code-heading">
          <Typography variant="title2" asChild><h3>Kotlin 테마 코드</h3></Typography>
          <div className="color-actions">
            <Button variant="line" disabled={invalid} onClick={copyCode}>
              코드 복사
            </Button>
            <Button disabled={invalid} onClick={downloadCode}>
              {appIdentifier}Theme.kt 다운로드
            </Button>
          </div>
        </div>
        {invalid && (
          <p className="color-error">
            잘못된 HEX 입력을 수정하면 코드를 내보내거나 미리보기에 적용할 수
            있습니다.
          </p>
        )}
        <p role="status" aria-live="polite">
          {message}
        </p>
        <CodeBlock
          className="color-code"
          code={code}
          language="kotlin"
          label={`${appIdentifier}Theme.kt`}
          variant="ghost"
          defaultWrap
        />
      </Card>
      <Card className="color-builder-card">
        <div className="color-code-heading">
          <Typography variant="title2" asChild><h3>웹 프로젝트 JSON</h3></Typography>
          <Button variant="line" disabled={invalid} onClick={async () => {
            const copied = await copyTextToClipboard(webCode);
            setMessage(copied ? "웹 프로젝트 JSON을 복사했습니다." : "복사하지 못했습니다. 코드를 선택해 복사하세요.");
          }}>JSON 복사</Button>
        </div>
        <p className="color-hint">위 팔레트를 CSS 변수로 변환합니다. 기존 프로젝트의 버튼 모양·간격·글꼴 설정은 유지합니다.</p>
        <CodeBlock className="color-code" code={webCode} language="json" label={`${project.name}.json`} variant="ghost" defaultWrap />
      </Card>
    </div>
  );
}
