import { useCallback, useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  colorDescriptions,
  colorRoles,
  contrastRatio,
  defaultColorSchemes,
  defaultColorSeeds,
  generateColorSchemes,
  generateKotlinTheme,
  isHexColor,
  semanticGroups,
} from "./color-scheme";
import type { ColorMode, ColorSchemes } from "./color-scheme";

export const colorSchemeSections = [
  { id: "color-seeds", label: "기준 색상" },
  { id: "color-roles", label: "색상 역할" },
  { id: "color-contrast", label: "상태 색상과 대비" },
  { id: "color-code", label: "Kotlin 테마 코드" },
];

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
  onApply,
}: {
  themeMode: ColorMode;
  onApply: (schemes: ColorSchemes, mode: ColorMode) => void;
}) {
  const [seeds, setSeeds] = useState(defaultColorSeeds);
  const [schemes, setSchemes] = useState(defaultColorSchemes);
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
  const code = generateKotlinTheme(schemes);
  const scheme = schemes[mode];
  const invalid = invalidFields.size > 0;
  async function copyCode() {
    try {
      await navigator.clipboard.writeText(code);
      setMessage("Kotlin 코드를 복사했습니다.");
    } catch {
      setMessage(
        "복사하지 못했습니다. 코드를 선택해 복사하거나 파일을 다운로드하세요.",
      );
    }
  }
  function downloadCode() {
    const url = URL.createObjectURL(
      new Blob([code], { type: "text/plain;charset=utf-8" }),
    );
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = "AppTheme.kt";
    anchor.click();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
    setMessage("AppTheme.kt를 다운로드했습니다.");
  }
  return (
    <div className="color-builder">
      <div className="panel-heading">
        <div>
          <p className="panel-kicker">THEME BUILDER</p>
          <h2>ColorScheme 생성기</h2>
        </div>
        <Badge variant="outline">IenColorScheme · 32 colors</Badge>
      </div>
      <p className="panel-description">
        기준색을 선택하면 라이트·다크 상태 색상을 자동 생성합니다. 세부 색상을
        조정한 뒤 AppTheme.kt를 프로젝트에 추가하고 AppTheme으로 화면을
        감싸세요.
      </p>
      <Card id={colorSchemeSections[0].id} className="color-builder-card">
        <h3>기준 색상</h3>
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
          variant="outline"
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
              <h3>색상 역할 편집</h3>
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
        <h3>상태 색상과 대비</h3>
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
        <Button disabled={invalid} onClick={() => onApply(schemes, mode)}>
          Compose 미리보기에 적용
        </Button>
      </Card>
      <Card id={colorSchemeSections[3].id} className="color-builder-card">
        <div className="color-code-heading">
          <h3>Kotlin 테마 코드</h3>
          <div className="color-actions">
            <Button variant="outline" disabled={invalid} onClick={copyCode}>
              코드 복사
            </Button>
            <Button disabled={invalid} onClick={downloadCode}>
              AppTheme.kt 다운로드
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
        <pre className="api-signature color-code" tabIndex={0}>
          <code>{code}</code>
        </pre>
      </Card>
    </div>
  );
}
