import { Typography } from "@/components/ui/typography";
import {
  IconCheck as CheckIcon,
  IconX as XIcon,
  IconSun as SunIcon,
  IconMoon as MoonIcon,
} from "@tabler/icons-react";
import {
  createContext,
  createElement,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ComponentProps,
  type ComponentType,
  type ElementType,
  type ReactNode,
} from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Switch } from "../../cmp-ui-web-registry/ui/switch";
import { CodeExample } from "./RegistryCodeExample";
import type {
  PlaygroundDefinition,
  PlaygroundProp,
  PlaygroundValues,
} from "./web-playground-types";

interface PlaygroundContextValue {
  values: PlaygroundValues;
  events: Record<string, string[]>;
  onEvent: (target: string, name: string, args: unknown[]) => void;
}
const PlaygroundContext = createContext<PlaygroundContextValue | null>(null);
const controlledPairs = {
  value: "defaultValue",
  checked: "defaultChecked",
  open: "defaultOpen",
};
const remountProp = /^(?:default|value$|checked$|open$|multiple$)/;
const playgroundIcons = { CheckIcon, XIcon, SunIcon, MoonIcon };
const playgroundIconExports = {
  CheckIcon: "IconCheck",
  XIcon: "IconX",
  SunIcon: "IconSun",
  MoonIcon: "IconMoon",
};
const iconLabels = { CheckIcon: "체크", XIcon: "닫기", SunIcon: "해", MoonIcon: "달" };
function iconSelection(value: unknown): keyof typeof playgroundIcons | "none" | undefined {
  if (!value || typeof value !== "object" || !("playgroundIcon" in value)) return undefined;
  const name = value.playgroundIcon;
  return name === "none" || (typeof name === "string" && Object.hasOwn(playgroundIcons, name))
    ? name as keyof typeof playgroundIcons | "none"
    : undefined;
}

function conflictingProps(values: Record<string, unknown>) {
  const conflicts = Object.entries(controlledPairs).flatMap(([controlled, initial]) =>
    Object.hasOwn(values, controlled)
      ? [initial]
      : Object.hasOwn(values, initial)
        ? [controlled]
        : [],
  );
  if (Object.hasOwn(values, "trackContent"))
    conflicts.push(...["onTrackContent", "offTrackContent"].filter((name) => !Object.hasOwn(values, name)));
  return conflicts;
}
export function usePlaygroundActive() {
  return useContext(PlaygroundContext) !== null;
}

// 실제 예시에만 문서용 속성을 합성하며 설치되는 컴포넌트에는 의존성을 추가하지 않습니다.
export function playgroundComponent<C extends ElementType>(
  Component: C,
  target: string,
): C {
  function ControlledComponent(props: ComponentProps<C>) {
    const context = useContext(PlaygroundContext);
    if (!context) return createElement(Component, props);
    const overrides = context.values[target] ?? {};
    const merged: Record<string, unknown> = { ...props, ...overrides };
    for (const [name, value] of Object.entries(overrides)) {
      const icon = iconSelection(value);
      if (icon !== undefined) merged[name] = icon === "none" ? null : createElement(playgroundIcons[icon]);
    }
    for (const name of conflictingProps(overrides)) delete merged[name];
    if (typeof overrides.multiple === "boolean" && merged.value !== undefined) {
      if (overrides.multiple && !Array.isArray(merged.value))
        merged.value = merged.value === null ? [] : [merged.value];
      if (!overrides.multiple && Array.isArray(merged.value))
        merged.value = merged.value[0] ?? null;
    }
    for (const name of context.events[target] ?? []) {
      const original = merged[name];
      merged[name] = (...args: unknown[]) => {
        context.onEvent(target, name, args);
        if (typeof original === "function") return original(...args);
      };
    }
    return createElement(Component, merged);
  }
  ControlledComponent.displayName = `Playground(${target.split("#").at(-1)})`;
  return ControlledComponent as C;
}

function codeValue(value: unknown): string {
  const icon = iconSelection(value);
  if (icon !== undefined) return icon === "none" ? "null" : `<${icon} />`;
  if (value instanceof Date) return `new Date(${JSON.stringify(value.toISOString())})`;
  if (Array.isArray(value)) return `[${value.map(codeValue).join(", ")}]`;
  if (value && typeof value === "object")
    return `{ ${Object.entries(value).map(([name, entry]) => `${JSON.stringify(name)}: ${codeValue(entry)}`).join(", ")} }`;
  return JSON.stringify(value);
}
export function playgroundExample(
  source: string,
  definition: PlaygroundDefinition,
  values: PlaygroundValues,
): string {
  const edits: { start: number; end: number; text: string }[] = [];
  for (const edit of definition.edits) {
    const overrides = values[edit.key];
    if (!overrides) continue;
    const conflicts = conflictingProps(overrides);
    for (const attribute of edit.attributes)
      if (conflicts.includes(attribute.name))
        edits.push({ start: attribute.start, end: attribute.end, text: "" });
    const { children, ...props } = overrides;
    if (Object.keys(props).length) {
      const text = Object.entries(props)
        .map(([name, value]) => `${JSON.stringify(name)}: ${codeValue(value)}`)
        .join(", ");
      edits.push({
        start: edit.offset,
        end: edit.offset,
        text: ` {...{ ${text} }}`,
      });
    }
    if (children !== undefined && edit.childrenEnd !== undefined)
      edits.push({
        start: edit.childrenStart,
        end: edit.childrenEnd,
        text: `{${codeValue(children)}}`,
      });
  }
  const result = edits
    .sort((a, b) => b.start - a.start)
    .reduce(
      (text, edit) =>
        text.slice(0, edit.start) + edit.text + text.slice(edit.end),
      source,
    );
  const icons = [...new Set(Object.values(values).flatMap((props) => Object.values(props).map(iconSelection)))]
    .filter((name): name is keyof typeof playgroundIcons => name !== undefined && name !== "none");
  const imported = [...source.matchAll(/import\s*\{([^}]+)\}\s*from\s*["']@tabler\/icons-react["']/g)]
    .flatMap((match) => match[1].split(",").map((name) => name.trim().split(/\s+as\s+/).at(-1)));
  const missing = icons.filter((name) => !imported.includes(name));
  const names = missing.map((name) => `${playgroundIconExports[name]} as ${name}`);
  return names.length ? `import { ${names.join(", ")} } from "@tabler/icons-react"\n${result}` : result;
}
function eventValue(value: unknown): unknown {
  if (value === null || ["string", "number", "boolean"].includes(typeof value))
    return value;
  if (value instanceof Date) return value.toISOString();
  if (Array.isArray(value)) return value.slice(0, 5).map(eventValue);
  if (
    value &&
    typeof value === "object" &&
    "type" in value &&
    "target" in value
  ) {
    const target = value.target;
    return {
      type: value.type,
      ...(target && typeof target === "object" && "value" in target
        ? { value: target.value }
        : {}),
    };
  }
  if (typeof value === "object") return "객체";
  return String(value);
}

function initialValue(prop: PlaygroundProp): unknown {
  if (prop.defaultValue !== undefined) return prop.defaultValue;
  if (prop.kind === "select") return prop.options?.[0];
  if (prop.kind === "boolean") return false;
  if (prop.kind === "number") return 0;
  if (prop.kind === "date") return new Date(new Date().setHours(0, 0, 0, 0));
  if (prop.kind === "array") return [];
  if (prop.kind === "record") return {};
  if (prop.kind === "union" && prop.variants?.[0]) return initialValue(prop.variants[0]);
  if (prop.kind === "object") {
    const fields = (prop.fields ?? []).filter((field) => !field.optional && !["slot", "reference", "callback"].includes(field.kind));
    const object = Object.fromEntries(fields.map((field) => [field.name, initialValue(field)]));
    return prop.tuple ? (prop.fields ?? []).map((field) => object[field.name]) : object;
  }
  return "";
}
function matchesValue(prop: PlaygroundProp, value: unknown): boolean {
  if (prop.kind === "select") return prop.options?.includes(value as string | number | boolean | null) ?? false;
  if (prop.kind === "array") return Array.isArray(value);
  if (prop.kind === "date") return value instanceof Date;
  if (prop.kind === "object" || prop.kind === "record") return value !== null && typeof value === "object" && !Array.isArray(value) && !(value instanceof Date);
  return typeof value === (prop.kind === "text" ? "string" : prop.kind);
}
const valueKindLabels: Partial<Record<PlaygroundProp["kind"], string>> = {
  text: "문자 입력", number: "숫자 입력", boolean: "켜기·끄기", date: "날짜 선택",
  object: "세부 항목 설정", array: "목록 편집", record: "이름별 값 설정", select: "선택지에서 고르기",
};

export function WebPlayground({
  definition,
  source,
  Preview,
  renderPreview,
}: {
  definition: PlaygroundDefinition;
  source: string;
  Preview: ComponentType;
  renderPreview: (preview: ReactNode, resetKey: unknown) => ReactNode;
}) {
  const [values, setValues] = useState<PlaygroundValues>({});
  const [targetKey, setTargetKey] = useState(definition.targets[0].key);
  const [query, setQuery] = useState("");
  const [viewport, setViewport] = useState("desktop");
  const [revision, setRevision] = useState(0);
  const [eventLog, setEventLog] = useState<string[]>([]);
  const target =
    definition.targets.find((entry) => entry.key === targetKey) ??
    definition.targets[0];
  const current = values[target.key] ?? {};
  const events = useMemo(
    () =>
      Object.fromEntries(
        definition.targets.map((entry) => [
          entry.key,
          entry.props
            .filter(
              (prop) => prop.kind === "callback" && /^on[A-Z]/.test(prop.name),
            )
            .map((prop) => prop.name),
        ]),
      ),
    [definition],
  );
  const onEvent = useCallback((key: string, name: string, args: unknown[]) => {
    const event = `${key.split("#").at(-1)}.${name}(${args.map((arg) => JSON.stringify(eventValue(arg))).join(", ")})`;
    setEventLog((previous) => [event, ...previous].slice(0, 12));
  }, []);
  const context = useMemo(
    () => ({ values, events, onEvent }),
    [values, events, onEvent],
  );
  const preview = useMemo(
    () => <Preview key={revision} />,
    [Preview, revision],
  );
  const code = useMemo(
    () => playgroundExample(source, definition, values),
    [source, definition, values],
  );
  const editable = target.props.filter(
    (prop) =>
      !["callback", "slot", "reference"].includes(prop.kind) &&
      prop.name.toLowerCase().includes(query.toLowerCase()),
  );
  const codeOnly = target.props.filter(
    (prop) =>
      ["slot", "reference"].includes(prop.kind) ||
      (prop.kind === "callback" && !/^on[A-Z]/.test(prop.name)),
  );

  function update(name: string, value: unknown) {
    if (remountProp.test(name)) setRevision((previous) => previous + 1);
    const conflicts = conflictingProps({ [name]: value });
    setValues((previous) => {
      const next = { ...previous[target.key], [name]: value };
      for (const conflict of conflicts) delete next[conflict];
      return { ...previous, [target.key]: next };
    });
  }
  function reset(name?: string) {
    if (!name) {
      setValues({});
      setEventLog([]);
      setRevision((value) => value + 1);
      return;
    }
    if (remountProp.test(name)) setRevision((value) => value + 1);
    setValues((previous) => {
      const next = { ...previous[target.key] };
      delete next[name];
      return { ...previous, [target.key]: next };
    });
  }

  function control(
    prop: PlaygroundProp,
    value: unknown,
    change: (value: unknown) => void = (value) => update(prop.name, value),
    clear: () => void = () => reset(prop.name),
    path = prop.name,
  ): ReactNode {
    const id = `playground-${target.name}-${path}`;

    if (prop.kind === "icon")
      return (
        <select id={id} aria-label={path} className="playground-select"
          value={iconSelection(value) ?? "default"}
          onChange={(event) => event.target.value === "default" ? clear() : change({ playgroundIcon: event.target.value })}>
          <option value="default">예시 기본값</option>
          <option value="none">없음</option>
          {Object.entries(iconLabels).map(([name, label]) => <option key={name} value={name}>{label}</option>)}
        </select>
      );
    if (["reference", "slot", "callback"].includes(prop.kind))
      return <p className="playground-prop-type">이 항목은 예시 코드에서 구성합니다. <code>{prop.type}</code></p>;
    if (prop.kind === "union") {
      const variants = prop.variants ?? [];
      const selected = Math.max(0, variants.findIndex((variant) => matchesValue(variant, value ?? prop.defaultValue)));
      const variant = variants[selected];
      return <div className="flex min-w-0 flex-col gap-3">
        <select id={id} className="playground-select" aria-label={`${path} 입력 방식`}
          value={selected} onChange={(event) => change(initialValue(variants[Number(event.target.value)]))}>
          {variants.map((entry, index) => <option key={index} value={index}>{valueKindLabels[entry.kind] ?? entry.type}{entry.kind === "select" ? ` (${entry.options?.join(", ")})` : ""}</option>)}
        </select>
        {variant && control({ ...variant, name: prop.name, optional: prop.optional }, value ?? prop.defaultValue, change, clear, `${path}.value`)}
      </div>;
    }
    if ((prop.kind === "array" || prop.kind === "record") && prop.item && ["reference", "callback", "slot"].includes(prop.item.kind))
      return <p className="playground-prop-type">이 목록의 항목은 예시 코드에서 구성합니다. <code>{prop.item.type}</code></p>;
    if (prop.kind === "array" && prop.item) {
      const item = prop.item;
      const initial = value ?? prop.defaultValue;
      const list = Array.isArray(initial) ? initial : [];
      return <fieldset id={id} className="flex min-w-0 flex-col gap-3 rounded-lg border border-border p-3" aria-label={path}>
        <p className="playground-prop-type">항목을 추가한 뒤 각 값을 입력하세요. ({list.length}개)</p>
        {list.map((entry, index) => <div key={index} className="flex min-w-0 flex-col gap-1.5">
          <div className="playground-control-heading">
            <label htmlFor={`playground-${target.name}-${path}.${index}`}>{index + 1}번째 항목</label>
            <Button variant="ghost" size="xs" aria-label={`${path} ${index + 1}번째 항목 삭제`} onClick={() => change(list.filter((_, position) => position !== index))}>삭제</Button>
          </div>
          {control(item, entry, (next) => change(list.map((previous, position) => position === index ? next : previous)), () => change(list.map((previous, position) => position === index ? initialValue(item) : previous)), `${path}.${index}`)}
        </div>)}
        <Button variant="line" size="sm" onClick={() => change([...list, initialValue(item)])}>항목 추가</Button>
        {value === undefined && <Button variant="ghost" size="xs" onClick={() => change([])}>빈 목록 적용</Button>}
      </fieldset>;
    }
    if (prop.kind === "record" && prop.item) {
      const item = prop.item;
      const initial = value ?? prop.defaultValue;
      const object: Record<string, unknown> = initial && typeof initial === "object" && !Array.isArray(initial) ? Object.fromEntries(Object.entries(initial)) : {};
      return <fieldset id={id} className="flex min-w-0 flex-col gap-3 rounded-lg border border-border p-3" aria-label={path}>
        <p className="playground-prop-type">각 항목의 이름과 값을 설정하세요. 이름은 중복될 수 없습니다.</p>
        {Object.entries(object).map(([name, entry], index) => <div key={index} className="flex min-w-0 flex-col gap-1.5">
          <Input aria-label={`${path} ${index + 1}번째 이름`} value={name} onChange={(event) => {
            const next = event.target.value;
            if (next !== name && !Object.hasOwn(object, next)) change(Object.fromEntries(Object.entries(object).map(([key, previous]) => [key === name ? next : key, previous])));
          }} />
          {control(item, entry, (next) => change({ ...object, [name]: next }), () => change({ ...object, [name]: initialValue(item) }), `${path}.${index}.value`)}
          <Button variant="ghost" size="xs" aria-label={`${path} ${name} 삭제`} onClick={() => change(Object.fromEntries(Object.entries(object).filter(([key]) => key !== name)))}>삭제</Button>
        </div>)}
        <Button variant="line" size="sm" onClick={() => {
          let index = Object.keys(object).length + 1;
          while (Object.hasOwn(object, `item${index}`)) index++;
          change({ ...object, [`item${index}`]: initialValue(item) });
        }}>항목 추가</Button>
      </fieldset>;
    }
    if (prop.kind === "object") {
      const initial = value ?? prop.defaultValue;
      const object: Record<string, unknown> = initial && typeof initial === "object"
        ? Object.fromEntries(Object.entries(initial)) : {};
      const fields = prop.fields ?? [];
      const visible = fields.filter((field) => !field.condition || field.condition.options.includes(
        object[field.condition.field] as string | number | boolean | null ??
        fields.find((entry) => entry.name === field.condition?.field)?.options?.[0] ?? null
      ));
      const applyObject = (next: Record<string, unknown>) => change(prop.tuple ? fields.map((field) => next[field.name]) : next);
      const updateField = (field: PlaygroundProp, nextValue: unknown) => {
        const next = { ...object, [field.name]: nextValue };
        for (const entry of fields) {
          if (entry.condition && !entry.condition.options.includes(next[entry.condition.field] as string | number | boolean | null)) delete next[entry.name];
          else if (!entry.optional && next[entry.name] === undefined && !["callback", "slot", "reference"].includes(entry.kind)) next[entry.name] = initialValue(entry);
        }
        applyObject(next);
      };
      return <fieldset id={id} className="flex min-w-0 flex-col gap-3 rounded-lg border border-border p-3" aria-label={path}>
        <p className="playground-prop-type">각 항목을 설정하세요. 선택 항목은 사용 여부를 켜서 입력할 수 있습니다.</p>
        {visible.map((field) => {
          const fieldPath = `${path}.${field.name}`;
          const fieldId = `playground-${target.name}-${fieldPath}`;
          const supported = !["callback", "slot", "reference"].includes(field.kind);
          const enabled = object[field.name] !== undefined || !field.optional;
          const remove = () => { const next = { ...object }; delete next[field.name]; applyObject(next); };
          return <div key={field.name} className="flex min-w-0 flex-col gap-1.5">
            <div className="playground-control-heading">
              <label htmlFor={field.optional && supported ? `${fieldId}-enabled` : fieldId}>{field.label ?? field.name}{!field.optional && <span>필수</span>}</label>
              {field.optional && supported && <Switch id={`${fieldId}-enabled`} aria-label={`${fieldPath} 사용`} checked={enabled} onCheckedChange={(checked) => checked ? updateField(field, initialValue(field)) : remove()} />}
            </div>
            {field.description && <p className="playground-prop-type">{field.description}</p>}
            {(enabled || !supported) && control(field, object[field.name] ?? initialValue(field), (next) => updateField(field, next), remove, fieldPath)}
          </div>;
        })}
      </fieldset>;
    }
    if (prop.kind === "boolean")
      return (
        <Switch
          id={id}
          aria-label={path}
          checked={Boolean(value ?? prop.defaultValue ?? false)}
          onCheckedChange={(checked) => change(checked)}
        />
      );
    if (prop.kind === "select")
      return (
        <select
          id={id}
          aria-label={path}
          className="playground-select"
          value={
            value === undefined
              ? "default"
              : String(prop.options?.findIndex((option) => option === value))
          }
          onChange={(event) =>
            event.target.value === "default"
              ? clear()
              : change(prop.options?.[Number(event.target.value)])
          }
        >
          <option value="default" disabled={path.includes(".") && !prop.optional}>
            예시 기본값
            {prop.defaultValue !== undefined
              ? ` (${String(prop.defaultValue)})`
              : ""}
          </option>
          {prop.options?.map((option, index) => (
            <option key={index} value={index}>
              {String(option)}
            </option>
          ))}
        </select>
      );
    return (
      <Input
        id={id}
        aria-label={path}
        type={
          prop.kind === "number"
            ? "number"
            : prop.kind === "date"
              ? "date"
              : "text"
        }
        step={prop.kind === "number" ? "any" : undefined}
        value={
          value instanceof Date
            ? [
                value.getFullYear(),
                String(value.getMonth() + 1).padStart(2, "0"),
                String(value.getDate()).padStart(2, "0"),
              ].join("-")
            : value === undefined
              ? ""
              : String(value)
        }
        placeholder={
          prop.defaultValue !== undefined
            ? `예시 기본값 (${String(prop.defaultValue)})`
            : "예시 기본값"
        }
        onChange={(event) => {
          const text = event.target.value;
          if ((prop.kind === "number" || prop.kind === "date") && !text)
            clear();
          else
            change(
              prop.kind === "number"
                ? Number(text)
                : prop.kind === "date"
                  ? new Date(`${text}T00:00:00`)
                  : text,
            );
        }}
      />
    );
  }
  return (
    <div className="web-playground">
      <section className="playground-panel" aria-label="Playground 미리보기">
        <div className="playground-heading">
          <Typography variant="title2" asChild><h3>미리보기</h3></Typography>
          <label>
            미리보기 너비{" "}
            <select
              className="playground-select"
              value={viewport}
              onChange={(event) => setViewport(event.target.value)}
            >
              <option value="desktop">데스크톱</option>
              <option value="tablet">태블릿</option>
              <option value="mobile">모바일</option>
            </select>
          </label>
        </div>
        <div className="web-component-preview playground-preview">
          <div
            style={{
              width:
                viewport === "mobile"
                  ? "360px"
                  : viewport === "tablet"
                    ? "768px"
                    : "100%",
              maxWidth: "100%",
            }}
          >
            <PlaygroundContext.Provider value={context}>
              {renderPreview(preview, values)}
            </PlaygroundContext.Provider>
          </div>
        </div>
      </section>
      <section className="playground-panel" aria-label="속성 조정">
        <div className="playground-heading">
          <div>
            <Typography variant="title2" asChild><h3>속성 조정</h3></Typography>
            <p>값을 바꾸면 실제 컴포넌트에 바로 반영됩니다.</p>
          </div>
          <Button variant="line" size="sm" onClick={() => reset()}>
            전체 초기화
          </Button>
        </div>
        <label className="playground-target">
          구성 요소
          <select
            className="playground-select"
            value={target.key}
            onChange={(event) => {
              setTargetKey(event.target.value);
              setQuery("");
            }}
          >
            {definition.targets.map((entry) => (
              <option key={entry.key} value={entry.key}>
                {entry.name}
              </option>
            ))}
          </select>
        </label>
        <Input
          aria-label="속성 검색"
          value={query}
          onChange={(event) => setQuery(event.target.value)}
          placeholder="파라미터 이름으로 검색"
        />
        <div className="playground-controls">
          {editable.map((prop) => (
            <div
              className="playground-control"
              key={`${target.key}.${prop.name}`}
            >
              <div className="playground-control-heading">
                <label htmlFor={`playground-${target.name}-${prop.name}`}>
                  {prop.name}
                  {!prop.optional && <span>필수</span>}
                </label>
                {Object.hasOwn(current, prop.name) ? (
                  <Button
                    variant="ghost"
                    size="xs"
                    aria-label={`${prop.name} 초기화`}
                    onClick={() => reset(prop.name)}
                  >
                    초기화
                  </Button>
                ) : (
                  <span>예시 기본값</span>
                )}
              </div>
              <code className="playground-prop-type">{prop.type}</code>
              {prop.description && <p className="playground-prop-type">{prop.description}</p>}
              {control(prop, current[prop.name])}
            </div>
          ))}
        </div>
        {!editable.length && <p>검색 조건에 맞는 속성이 없습니다.</p>}
        {codeOnly.length > 0 && (
          <details className="web-docs-disclosure">
            <summary>코드에서 구성하는 슬롯·참조 ({codeOnly.length})</summary>
            <p>
              React 요소·렌더 함수·ref는 아래 예시 코드의 구성을 사용합니다.
            </p>
            <ul>
              {codeOnly.map((prop) => (
                <li key={prop.name}>
                  <code>
                    {prop.name}: {prop.type}
                  </code>
                </li>
              ))}
            </ul>
          </details>
        )}
      </section>
      <section className="playground-panel" aria-label="이벤트 기록">
        <div className="playground-heading">
          <Typography variant="title2" asChild><h3>이벤트 기록</h3></Typography>
          <Button variant="ghost" size="sm" onClick={() => setEventLog([])}>
            기록 지우기
          </Button>
        </div>
        <p>
          컴포넌트를 조작하면 기존 이벤트 핸들러를 실행하고 호출 내용을
          표시합니다.
        </p>
        <ol className="playground-events">
          {eventLog.length ? (
            eventLog.map((event, index) => (
              <li key={index}>
                <code>{event}</code>
              </li>
            ))
          ) : (
            <li>아직 발생한 이벤트가 없습니다.</li>
          )}
        </ol>
      </section>
      {Object.values(values).some((props) =>
        Object.hasOwn(props, "multiple"),
      ) && (
        <p>
          다중 선택 모드에서는 예시의 value와 이벤트 핸들러 타입도 해당 모드에
          맞춰 설정하세요.
        </p>
      )}
      <CodeExample label="현재 설정이 반영된 사용 예시" code={code} />
    </div>
  );
}
