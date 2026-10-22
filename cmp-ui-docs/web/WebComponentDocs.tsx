import { useState } from "react";
import { ArrowUpRight } from "lucide-react";
import { Button } from "./components/ui/button";
import { Badge } from "./components/ui/badge";
import { Card } from "./components/ui/card";
import {
  Button as RegistryButton,
  type IenButtonSize,
  type IenButtonVariant,
  type IenSemanticTone,
} from "../../cmp-ui-web-registry/registry/ien-button";
import registry from "../../cmp-ui-web-registry/registry.json";
import designTokens from "./generated-design-tokens.json";
import buttonSource from "../../cmp-ui-web-registry/registry/ien-button.tsx?raw";
import { CodeExample } from "./RegistryCodeExample";

const buttonItem = registry.items.find((item) => item.name === "button");
const tokensItem = registry.items.find((item) => item.name === "tokens");
const dimensionGroups = ["spacing", "radius", "stroke", "elevation", "icon"] as const;
const variants = ["fill", "weak", "line", "ghost"] as const;
const tones = ["neutral", "brand", "success", "warning", "danger", "info"] as const;
const sizes = ["small", "medium", "large"] as const;

const setupCommand = 'npx shadcn@latest registry add @ienlab="https://cmp.ien.zone/r/{name}.json"';
const installCommand = "npx shadcn@latest add @ienlab/button";


export function WebComponentDocs({
  tab,
}: {
  tab: "preview" | "registry";
}) {
  const [variant, setVariant] = useState<IenButtonVariant>("fill");
  const [tone, setTone] = useState<IenSemanticTone>("brand");
  const [size, setSize] = useState<IenButtonSize>("large");
  const [disabled, setDisabled] = useState(false);
  const [useGradient, setUseGradient] = useState(true);
  const [clickCount, setClickCount] = useState(0);

  return (
    <section
      className="content-panel web-component-docs"
      aria-label={tab === "preview" ? "웹 버튼 프리뷰" : "웹 버튼 레지스트리"}
    >
      <div className="panel-heading">
        <div>
          <p className="panel-kicker">{tab === "preview" ? "LIVE WEB COMPONENT" : "SHADCN REGISTRY"}</p>
          <h2>{tab === "preview" ? "웹 버튼 프리뷰" : "@ienlab/button 설치"}</h2>
        </div>
        <Badge className="runtime-badge" variant="outline">React · Next.js</Badge>
      </div>
      <p className="panel-description">
        CMP의 크기, 상태, 모서리, variant와 tone을 웹 버튼에 적용합니다. 글자 크기는 웹 기준을 사용합니다.
      </p>
      <p className="panel-description">
        누르면 중심 기준으로 {designTokens.web.button["pressed-scale"]}배 축소되며 검정 오버레이가 적용됩니다.
        fill · weak는 CMP와 같은 색상 혼합 비율의 그라데이션을 기본으로 사용합니다.
      </p>

      {tab === "preview" ? (
        <div className="space-y-6">
          <div className="web-button-controls">
            <fieldset>
              <legend>variant</legend>
              <div className="flex flex-wrap gap-2">
                {variants.map((value) => (
                  <Button
                    key={value}
                    size="sm"
                    variant={variant === value ? "secondary" : "outline"}
                    aria-pressed={variant === value}
                    onClick={() => setVariant(value)}
                  >
                    {value}
                  </Button>
                ))}
              </div>
            </fieldset>
            <fieldset>
              <legend>tone</legend>
              <div className="flex flex-wrap gap-2">
                {tones.map((value) => (
                  <Button
                    key={value}
                    size="sm"
                    variant={tone === value ? "secondary" : "outline"}
                    aria-pressed={tone === value}
                    onClick={() => setTone(value)}
                  >
                    {value}
                  </Button>
                ))}
              </div>
            </fieldset>
            <fieldset>
              <legend>size</legend>
              <div className="flex flex-wrap gap-2">
                {sizes.map((value) => (
                  <Button
                    key={value}
                    size="sm"
                    variant={size === value ? "secondary" : "outline"}
                    aria-pressed={size === value}
                    onClick={() => setSize(value)}
                  >
                    {value}
                  </Button>
                ))}
              </div>
            </fieldset>
            <Button
              className="w-fit"
              variant="outline"
              size="sm"
              aria-pressed={useGradient}
              onClick={() => setUseGradient(!useGradient)}
            >
              그라데이션 {useGradient ? "켜짐" : "꺼짐"}
            </Button>
            <Button
              className="w-fit"
              variant="outline"
              size="sm"
              aria-pressed={disabled}
              onClick={() => setDisabled(!disabled)}
            >
              비활성 {disabled ? "켜짐" : "꺼짐"}
            </Button>
          </div>
          <Card className="web-button-stage">
            <RegistryButton
              variant={variant}
              tone={tone}
              size={size}
              useGradient={useGradient}
              disabled={disabled}
              onClick={() => setClickCount((count) => count + 1)}
            >
              버튼 <ArrowUpRight aria-hidden="true" data-icon="inline-end" />
            </RegistryButton>
            <p role="status" className="text-xs text-muted-foreground">클릭 {clickCount}회</p>
          </Card>
          <CodeExample label="사용 예시" code={`import { Button } from "./components/ui/button"\n\n<Button variant="${variant}" tone="${tone}" size="${size}"${disabled ? " disabled" : ""}${useGradient ? "" : " useGradient={false}"}>\n  버튼\n</Button>`} />
          <div className="space-y-3">
            <h3 className="text-sm font-medium">전체 tone · variant</h3>
            <div className="grid gap-3 sm:grid-cols-2">
              {tones.map((value) => (
                <Card key={value} className="gap-3 p-4">
                  <h4 className="text-sm font-medium">{value}</h4>
                  <div className="flex flex-wrap gap-2">
                    {variants.map((buttonVariant) => (
                      <RegistryButton key={buttonVariant} tone={value} variant={buttonVariant} useGradient={useGradient}>
                        {buttonVariant}
                      </RegistryButton>
                    ))}
                  </div>
                </Card>
              ))}
            </div>
          </div>
          <div className="space-y-3">
            <h3 className="text-sm font-medium">shadcn/ui 기본 API</h3>
            <div className="flex flex-wrap items-center gap-3">
              <RegistryButton asChild variant="line"><a href="https://ui.shadcn.com/docs/components/radix/button" target="_blank" rel="noreferrer">asChild 링크 <ArrowUpRight aria-hidden="true" data-icon="inline-end" /></a></RegistryButton>
              <RegistryButton size="icon" aria-label="아이콘 버튼" onClick={() => setClickCount((count) => count + 1)}><ArrowUpRight aria-hidden="true" /></RegistryButton>
              <RegistryButton disabled>비활성 버튼</RegistryButton>
            </div>
          </div>
        </div>
      ) : (
        <div className="space-y-6">
          <CodeExample label="1. 프로젝트에 네임스페이스 등록 · 최초 한 번" code={setupCommand} />
          <CodeExample label="2. 버튼 설치" code={installCommand} />
          <CodeExample label="3. React / Next.js에서 사용" code={'import { Button } from "./components/ui/button"\n\n<Button variant="fill" tone="brand">저장</Button>\n<Button variant="weak" tone="danger" useGradient={false}>삭제</Button>'} />
          <p className="text-sm text-muted-foreground">공통 디자인 토큰은 버튼의 의존성으로 함께 설치됩니다.</p>
          <CodeExample label="토큰만 설치" code="npx shadcn@latest add @ienlab/tokens" />
          <p className="text-sm text-muted-foreground">색상·간격·모서리는 CMP IenTheme을 따릅니다. 글자 크기는 shadcn/ui에 맞춘 웹 크기이며 단계 이름은 CMP와 같습니다.</p>
          <div className="grid gap-ien-md sm:grid-cols-2">
            {dimensionGroups.map((group) => (
              <Card key={group} className="gap-ien-sm p-ien-md">
                <h3 className="font-medium">{group}</h3>
                <dl className="grid grid-cols-2 gap-ien-xs text-xs">
                  {Object.entries(designTokens[group]).map(([name, value]) => (
                    <div key={name} className="min-w-0">
                      <dt className="text-muted-foreground">{name}</dt>
                      <dd><code className="break-all">--ien-{group}-{name}</code><br />{value}px</dd>
                    </div>
                  ))}
                </dl>
              </Card>
            ))}
            <Card className="gap-ien-sm p-ien-md">
              <h3 className="font-medium">typography</h3>
              <dl className="space-y-ien-xs text-xs">
                {Object.entries(designTokens.web.typography).map(([name, style]) => (
                  <div key={name}>
                    <dt>{name}</dt>
                    <dd className="text-muted-foreground">크기 {style.fontSize}px · 행간 {style.lineHeight}px · 굵기 {style.fontWeight}</dd>
                  </div>
                ))}
              </dl>
            </Card>
            {(["motion", "state"] as const).map((group) => (
              <Card key={group} className="gap-ien-sm p-ien-md">
                <h3 className="font-medium">{group}</h3>
                <dl className="space-y-ien-xs text-xs">
                  {Object.entries(designTokens[group]).map(([name, value]) => (
                    <div key={name}>
                      <dt>{name}</dt>
                      <dd className="text-muted-foreground">{Array.isArray(value) ? value.join(", ") : value}{name.endsWith("Millis") ? "ms" : name === "minimumTouchTarget" ? "px" : ""}</dd>
                    </div>
                  ))}
                </dl>
              </Card>
            ))}
          </div>
          <CodeExample label="토큰 사용 예시" code={'<div className="gap-ien-md rounded-ien-default p-ien-md text-ien-body1">콘텐츠</div>\n\n/* CSS */\n.container { padding: var(--ien-spacing-md); }'} />
          <p className="text-sm text-muted-foreground">shadcn/ui를 초기화한 Tailwind CSS 프로젝트에서 설치합니다. Next.js에서 이벤트와 상태를 사용하면 해당 사용 파일에 <code>"use client"</code>를 선언하세요.</p>
          <div className="flex flex-wrap gap-2">
            <Button asChild variant="outline"><a href="/r/button.json" target="_blank" rel="noreferrer">버튼 레지스트리 JSON <ArrowUpRight aria-hidden="true" /></a></Button>
            <Button asChild variant="outline"><a href="/r/tokens.json" target="_blank" rel="noreferrer">공통 토큰 JSON <ArrowUpRight aria-hidden="true" /></a></Button>
            <Button asChild variant="outline"><a href="/r/registry.json" target="_blank" rel="noreferrer">레지스트리 목록 <ArrowUpRight aria-hidden="true" /></a></Button>
          </div>
          <details>
            <summary className="cursor-pointer text-sm font-medium">레지스트리 설정 보기</summary>
            <CodeExample label="registry.json · button" code={JSON.stringify(buttonItem, null, 2)} />
          </details>
          <details>
            <summary className="cursor-pointer text-sm font-medium">공통 토큰 설정 보기</summary>
            <CodeExample label="registry.json · tokens" code={JSON.stringify(tokensItem, null, 2)} />
          </details>
          <details>
            <summary className="cursor-pointer text-sm font-medium">배포되는 버튼 소스 보기</summary>
            <CodeExample label="button.tsx" code={buttonSource} />
          </details>
        </div>
      )}
    </section>
  );
}
