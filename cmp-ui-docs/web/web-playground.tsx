import { Typography } from "@/components/ui/typography";
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
import { Textarea } from "../../cmp-ui-web-registry/ui/textarea";
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

function conflictingProps(values: Record<string, unknown>) {
  return Object.entries(controlledPairs).flatMap(([controlled, initial]) =>
    Object.hasOwn(values, controlled)
      ? [initial]
      : Object.hasOwn(values, initial)
        ? [controlled]
        : [],
  );
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
  return value instanceof Date
    ? `new Date(${JSON.stringify(value.toISOString())})`
    : JSON.stringify(value);
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
  return edits
    .sort((a, b) => b.start - a.start)
    .reduce(
      (text, edit) =>
        text.slice(0, edit.start) + edit.text + text.slice(edit.end),
      source,
    );
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
  const [jsonDrafts, setJsonDrafts] = useState<Record<string, string>>({});
  const [jsonErrors, setJsonErrors] = useState<Record<string, string>>({});
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
    if (conflicts.length)
      setJsonDrafts((previous) => {
        const next = { ...previous };
        for (const conflict of conflicts)
          delete next[`${target.key}.${conflict}`];
        return next;
      });
  }
  function reset(name?: string) {
    if (!name) {
      setValues({});
      setJsonDrafts({});
      setJsonErrors({});
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
    const key = `${target.key}.${name}`;
    setJsonDrafts((previous) => {
      const next = { ...previous };
      delete next[key];
      return next;
    });
    setJsonErrors((previous) => {
      const next = { ...previous };
      delete next[key];
      return next;
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
    const key = `${target.key}.${path}`;
    if (prop.kind === "object") {
      const initial = value ?? prop.defaultValue;
      const object = initial && typeof initial === "object" && !Array.isArray(initial)
        ? initial as Record<string, unknown>
        : {};
      const fields = prop.fields ?? [];
      const visible = fields.filter((field) => !field.condition || field.condition.options.includes(
        object[field.condition.field] as string | number | boolean | null ??
        fields.find((entry) => entry.name === field.condition?.field)?.options?.[0] ?? null
      ));
      return (
        <fieldset id={id} className="flex min-w-0 flex-col gap-3 rounded-lg border border-border p-3" aria-label={path}>
          {visible.map((field) => {
            const fieldPath = `${path}.${field.name}`;
            const fieldId = `playground-${target.name}-${fieldPath}`;
            const updateField = (nextValue: unknown) => {
              const next = { ...object, [field.name]: nextValue };
              for (const entry of fields) {
                if (entry.condition && !entry.condition.options.includes(next[entry.condition.field] as string | number | boolean | null)) {
                  delete next[entry.name];
                } else if (!entry.optional && next[entry.name] === undefined) {
                  next[entry.name] = entry.defaultValue ?? (entry.kind === "select" ? entry.options?.[0] : entry.kind === "number" ? 0 : entry.kind === "boolean" ? false : "");
                }
              }
              change(next);
            };
            const nested = field.kind === "boolean" && field.optional
              ? { ...field, kind: "select" as const, options: [true, false] }
              : field;
            return (
              <div key={field.name} className="flex min-w-0 flex-col gap-1.5">
                <label htmlFor={fieldId}>{field.name}</label>
                {control(nested, object[field.name] ?? (field.optional ? undefined : field.defaultValue ??
                  (field.kind === "select" ? field.options?.[0] : field.kind === "number" ? 0 : "")), updateField, () => {
                  const next = { ...object };
                  delete next[field.name];
                  change(next);
                }, fieldPath)}
              </div>
            );
          })}
        </fieldset>
      );
    }
    if (prop.kind === "boolean")
      return (
        <Switch
          id={id}
          checked={Boolean(value ?? prop.defaultValue ?? false)}
          onCheckedChange={(checked) => change(checked)}
        />
      );
    if (prop.kind === "select")
      return (
        <select
          id={id}
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
    if (prop.kind === "json")
      return (
        <div className="playground-json">
          <Textarea
            id={id}
            spellCheck={false}
            rows={3}
            placeholder="JSON 값을 입력하세요"
            value={
              jsonDrafts[key] ??
              (value === undefined ? "" : JSON.stringify(value, null, 2))
            }
            aria-invalid={Boolean(jsonErrors[key])}
            aria-describedby={jsonErrors[key] ? `${id}-error` : undefined}
            onChange={(event) => {
              const draft = event.target.value;
              setJsonDrafts((previous) => ({ ...previous, [key]: draft }));
              try {
                const parsed: unknown = JSON.parse(draft);
                change(parsed);
                setJsonErrors((previous) => ({ ...previous, [key]: "" }));
              } catch {
                setJsonErrors((previous) => ({
                  ...previous,
                  [key]:
                    "유효한 JSON을 입력하세요. 마지막으로 적용한 값이 유지됩니다.",
                }));
              }
            }}
          />
          {jsonErrors[key] && (
            <p id={`${id}-error`} role="alert">
              {jsonErrors[key]}
            </p>
          )}
        </div>
      );
    return (
      <Input
        id={id}
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
                {Object.hasOwn(current, prop.name) ||
                Object.hasOwn(jsonDrafts, `${target.key}.${prop.name}`) ? (
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
