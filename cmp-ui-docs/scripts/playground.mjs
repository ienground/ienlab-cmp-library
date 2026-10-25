import { parse } from "@babel/parser";
import { registryModuleAliases } from "./registry.mjs";
import { existsSync, readFileSync } from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const nodeModules = fileURLToPath(new URL("../node_modules/", import.meta.url));
const kebab = (name) =>
  name.replace(/([a-z0-9])([A-Z])/g, "$1-$2").toLowerCase();
function declarationFile(source, from) {
  const base = source.startsWith(".")
    ? path.resolve(path.dirname(from), source)
    : path.join(nodeModules, source);
  return [
    base.replace(/\.js$/, ".d.ts"),
    `${base}.d.ts`,
    path.join(base, "index.d.ts"),
    path.join(base, "dist/index.d.ts"),
  ].find((file) => file.endsWith(".d.ts") && existsSync(file));
}

const parseSource = (source) =>
  parse(source, { sourceType: "module", plugins: ["typescript", "jsx"] });
const singleExamples = {
  skeleton: "import { Skeleton } from \"@/components/ui/skeleton\"\n\nexport function Pattern() {\n  return <Skeleton pattern=\"TopListWithIcon\" background=\"Grey\" play=\"Show\" repeatLastItemCount={{ type: \"Count\", value: 3 }} />\n}\nexport default Pattern\n",
  "progress-indicator": "import { ProgressIndicator } from \"@/components/ui/progress-indicator\"\n\nexport function Pattern() {\n  return <ProgressIndicator variant=\"circular\" shape=\"wavy\" size={48} strokeWidth={4} />\n}\nexport default Pattern\n",
  "loading-indicator": "import { LoadingIndicator } from \"@/components/ui/loading-indicator\"\n\nexport function Pattern() {\n  return <LoadingIndicator size={48} />\n}\nexport default Pattern\n",
  switch:
    'import { Switch } from "@/components/ui/switch"\nimport { CheckIcon, XIcon } from "lucide-react"\n\nexport function Pattern() {\n  return <Switch defaultChecked enabled onTrackContent={<CheckIcon />} offTrackContent={<XIcon />} aria-label="자동 적용" />\n}\nexport default Pattern\n',

  tabs:
    'import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs"\n\nexport function Pattern() {\n  return (\n    <Tabs defaultValue="summary" size="large" fluid={false} ariaLabel="주문 정보 탭">\n      <TabsList>\n        <TabsTrigger value="summary">요약</TabsTrigger>\n        <TabsTrigger value="detail" badge={-1}>상세</TabsTrigger>\n        <TabsTrigger value="history" badge={120}>내역</TabsTrigger>\n      </TabsList>\n      <TabsContent value="summary">주문 요약을 확인합니다.</TabsContent>\n      <TabsContent value="detail">상세 정보를 확인합니다.</TabsContent>\n      <TabsContent value="history">이전 내역을 확인합니다.</TabsContent>\n    </Tabs>\n  )\n}\nexport default Pattern\n',

  textarea:
    'import { Textarea } from "@/components/ui/textarea"\n\nexport function Pattern() {\n  return <Textarea label="내용" placeholder="내용을 입력하세요" lengthLimit={{ mode: "block", maxLength: 200 }} minLines={4} maxLines={8} />\n}\nexport default Pattern\n',

  button:
    'import { Button } from "@/components/ui/button"\n\nexport function Pattern() {\n  return <Button>버튼</Button>\n}\nexport default Pattern\n',
  badge:
    'import { Badge } from "@/components/reui/badge"\n\nexport function Pattern() {\n  return <Badge>배지</Badge>\n}\nexport default Pattern\n',
  alert:
    'import { Alert, AlertTitle, AlertDescription } from "@/components/reui/alert"\n\nexport function Pattern() {\n  return (\n    <Alert>\n      <AlertTitle>알림 제목</AlertTitle>\n      <AlertDescription>알림 내용을 확인하세요.</AlertDescription>\n    </Alert>\n  )\n}\nexport default Pattern\n',
};
function walk(node, visit) {
  if (!node || typeof node !== "object") return;
  visit(node);
  for (const [key, value] of Object.entries(node)) {
    if (key === "loc") continue;
    if (Array.isArray(value)) value.forEach((child) => walk(child, visit));
    else if (value && typeof value === "object") walk(value, visit);
  }
}
const nameOf = (node) =>
  node?.name ??
  (node?.right
    ? `${nameOf(node.left)}.${nameOf(node.right)}`
    : node?.property
      ? `${nameOf(node.object)}.${nameOf(node.property)}`
      : "");
function literal(node) {
  if (!node) return undefined;
  if (["StringLiteral", "NumericLiteral", "BooleanLiteral"].includes(node.type))
    return node.value;
  if (node.type === "NullLiteral") return null;
  if (node.type === "ObjectExpression") {
    const entries = [];
    for (const property of node.properties) {
      if (property.type !== "ObjectProperty" || property.computed) return undefined;
      const value = literal(property.value);
      if (value === undefined) return undefined;
      entries.push([property.key.name ?? property.key.value, value]);
    }
    return Object.fromEntries(entries);
  }
  if (
    node.type === "UnaryExpression" &&
    node.operator === "-" &&
    node.argument.type === "NumericLiteral"
  )
    return -node.argument.value;
  return undefined;
}
const common = () => [
  { name: "id", type: "string", kind: "text", optional: true },
  { name: "aria-describedby", type: "string", kind: "text", optional: true },
  { name: "className", type: "string", kind: "text", optional: true },
  { name: "aria-label", type: "string", kind: "text", optional: true },
  { name: "aria-invalid", type: "boolean", kind: "boolean", optional: true },
  { name: "style", type: "CSSProperties", kind: "json", optional: true },
];
const native = (tag) => {
  const props =
    /(?:Select|Combobox|Autocomplete|Popover|Tooltip|DropdownMenu|ContextMenu)Primitive\.Root(?:\.Props)?$/.test(
      tag,
    )
      ? []
      : common();
  const add = (name, kind, extra = {}) => {
    if (!props.some((prop) => prop.name === name))
      props.push({ name, type: kind, kind, optional: true, ...extra });
  };
  const input =
    /^(input|textarea)$|\.(Input|Textarea)(?:\.Props)?$|NumberFieldPrimitive\.Root/.test(
      tag,
    );
  const valueRoot =
    /(?:Select|Combobox|Autocomplete|RadioGroup|Tabs)Primitive\.Root(?:\.Props)?$/.test(
      tag,
    );
  const checkRoot = /(?:Checkbox|Switch)Primitive\.Root(?:\.Props)?$/.test(tag);
  const trigger = /Trigger(?:\.Props)?$/.test(tag);
  const number = /NumberFieldPrimitive/.test(tag);
  if (input || valueRoot || checkRoot || trigger || tag === "button")
    add("disabled", "boolean");
  if (input || valueRoot || checkRoot) {
    add("name", "text");
    add("required", "boolean");
  }
  if (input) {
    add("placeholder", "text");
    add("readOnly", "boolean");
  }
  if (/SelectPrimitive\.Value$/.test(tag)) add("placeholder", "text");
  if (input || valueRoot) {
    for (const name of ["value", "defaultValue"])
      add(name, number ? "number" : "text");
    add(
      input && !number && !/Primitive/.test(tag) ? "onChange" : "onValueChange",
      "callback",
    );
  }
  if (input) {
    for (const name of ["minLength", "maxLength"]) add(name, "number");
    if (!/textarea/i.test(tag))
      for (const name of ["min", "max", "step"]) add(name, "number");
  }
  if (tag === "textarea")
    for (const name of ["rows", "cols"]) add(name, "number");
  if (tag === "input")
    add("type", "select", {
      type: "HTML input type",
      options: [
        "text",
        "password",
        "email",
        "search",
        "number",
        "tel",
        "url",
        "date",
        "time",
      ],
    });
  if (checkRoot) {
    for (const name of ["checked", "defaultChecked"])
      add(
        name,
        /Checkbox/.test(tag) ? "select" : "boolean",
        /Checkbox/.test(tag)
          ? {
              type: 'boolean | "indeterminate"',
              options: [false, true, "indeterminate"],
            }
          : {},
      );
    add("onCheckedChange", "callback");
  }
  if (/ComboboxPrimitive\.Root/.test(tag)) add("multiple", "boolean");
  if (
    /(?:Popover|Tooltip|DropdownMenu|ContextMenu|Select|Combobox|Autocomplete)Primitive\.Root(?:\.Props)?$/.test(
      tag,
    )
  ) {
    for (const name of ["open", "defaultOpen"]) add(name, "boolean");
    add("onOpenChange", "callback");
  }
  return props;
};

export function createPlaygroundCatalog(items, previews) {
  const aliases = registryModuleAliases(items);
  const sources = new Map(
    items.flatMap((item) =>
      item.files.map((file) => [file.absolutePath, file.content]),
    ),
  );
  const modules = new Map();
  function module(file) {
    if (!file) return undefined;
    if (modules.has(file)) return modules.get(file);
    let source = sources.get(file);
    if (source === undefined && file.endsWith(".d.ts") && existsSync(file)) {
      source = readFileSync(file, "utf8");
      sources.set(file, source);
    }
    if (!source) return undefined;
    const ast = parseSource(source);
    const definitions = new Map();
    const imports = new Map();
    for (const statement of ast.program.body) {
      const node =
        statement.type === "ExportNamedDeclaration"
          ? statement.declaration
          : statement;
      if (node?.id?.name) definitions.set(node.id.name, node);
      if (node?.type === "VariableDeclaration")
        for (const entry of node.declarations)
          if (entry.init) definitions.set(entry.id.name, entry.init);
      if (statement.type === "ImportDeclaration")
        for (const specifier of statement.specifiers) {
          if (
            specifier.type === "ImportSpecifier" ||
            specifier.type === "ImportNamespaceSpecifier"
          )
            imports.set(specifier.local.name, {
              file:
                aliases[statement.source.value] ??
                declarationFile(statement.source.value, file),
              name: specifier.imported?.name ?? "*",
              source: statement.source.value,
            });
        }
    }
    for (const statement of ast.program.body)
      if (statement.type === "ExportNamedDeclaration") {
        for (const specifier of statement.specifiers) {
          const name = specifier.exported.name;
          if (statement.source)
            imports.set(name, {
              file: declarationFile(statement.source.value, file),
              name: specifier.local.name,
            });
          else if (definitions.has(specifier.local.name))
            definitions.set(name, definitions.get(specifier.local.name));
          else if (imports.has(specifier.local.name))
            imports.set(name, imports.get(specifier.local.name));
        }
      }
    const result = { source, definitions, imports };
    modules.set(file, result);
    return result;
  }
  function reference(file, name) {
    const own = module(file);
    if (own?.definitions.has(name))
      return { file, node: own.definitions.get(name) };
    if (name.includes(".")) {
      const [namespace, part] = name.split(".");
      const imported = own?.imports.get(namespace);
      if (imported?.source?.startsWith("@base-ui/react")) {
        const family = imported.name;
        const target = path.join(
          nodeModules,
          "@base-ui/react",
          kebab(family),
          kebab(part),
          `${family}${part}.d.ts`,
        );
        const node = module(target)?.definitions.get(`${family}${part}Props`);
        if (node) return { file: target, node };
      }
      if (imported?.source === "radix-ui") {
        const family = imported.name;
        const target = path.join(
          nodeModules,
          `@radix-ui/react-${kebab(family)}`,
          "dist/index.d.ts",
        );
        const propName =
          part === "Root"
            ? `${family}Props`
            : `${part.startsWith(family) ? part : family + part}Props`;
        const node = module(target)?.definitions.get(propName);
        if (node) return { file: target, node };
      }
      if (imported?.name === "*" && imported.file)
        return reference(imported.file, name.split(".").slice(1).join("."));
    }
    const imported = own?.imports.get(name);
    if (imported?.file) return reference(imported.file, imported.name);
    return undefined;
  }
  function describe(node, file, seen = new Set()) {
    if (!node || seen.size > 5) return { kind: "json", type: "unknown" };
    const type = module(file)?.source.slice(node.start, node.end) ?? "unknown";
    if (node.type === "TSParenthesizedType")
      return describe(node.typeAnnotation, file, seen);
    if (node.type === "TSBooleanKeyword") return { kind: "boolean", type };
    if (node.type === "TSStringKeyword") return { kind: "text", type };
    if (node.type === "TSNumberKeyword") return { kind: "number", type };
    if (node.type === "TSFunctionType") return { kind: "callback", type };
    if (node.type === "TSLiteralType")
      return { kind: "select", options: [literal(node.literal)], type };
    if (["TSInterfaceDeclaration", "TSTypeLiteral"].includes(node.type)) {
      const fields = properties(node, file, seen);
      if (fields.length && fields.every((field) => ["text", "number", "boolean", "select", "object"].includes(field.kind)))
        return { kind: "object", type, fields };
    }
    if (node.type === "TSUnionType") {
      const members = node.types
        .filter(
          (part) =>
            !["TSUndefinedKeyword", "TSNullKeyword"].includes(part.type),
        )
        .map((part) => describe(part, file, seen));
      if (members.length && members.every((part) => part.kind === "select"))
        return {
          kind: "select",
          options: [...new Set(members.flatMap((part) => part.options))],
          type,
        };
      if (
        members.length &&
        members.every(
          (part) => part.kind === "select" || part.kind === "boolean",
        )
      )
        return {
          kind: "select",
          options: [
            ...new Set(
              members.flatMap((part) =>
                part.kind === "boolean" ? [true, false] : part.options,
              ),
            ),
          ],
          type,
        };
      if (members.length && members.every((part) => part.kind === "object")) {
        const discriminator = members[0].fields.find((field) =>
          field.kind === "select" && members.every((member) =>
            member.fields.some((entry) => entry.name === field.name && entry.kind === "select")));
        const fields = new Map();
        for (const member of members) {
          for (const field of member.fields) {
            const previous = fields.get(field.name);
            fields.set(field.name, previous?.kind === "select" && field.kind === "select"
              ? { ...field, optional: previous.optional && field.optional, options: [...new Set([...previous.options, ...field.options])], type: [...new Set([...previous.options, ...field.options])].map(JSON.stringify).join(" | ") }
              : { ...field, optional: previous ? previous.optional && field.optional : field.optional });
          }
        }
        if (discriminator) {
          for (const field of fields.values()) {
            const present = members.filter((member) => member.fields.some((entry) => entry.name === field.name));
            if (present.length !== members.length)
              field.condition = {
                field: discriminator.name,
                options: present.flatMap((member) => member.fields.find((entry) => entry.name === discriminator.name).options),
              };
          }
        }
        return { kind: "object", type, fields: [...fields.values()] };
      }
      if (
        members.length &&
        members.every((part) => part.kind === members[0]?.kind)
      )
        return { ...members[0], type };
    }
    if (node.type === "TSTypeReference") {
      const name = nameOf(node.typeName);
      const key = `${file}#${name}`;
      if (!seen.has(key)) {
        const found = reference(file, name);
        if (found && ["TSTypeAliasDeclaration", "TSInterfaceDeclaration"].includes(found.node.type))
          return {
            ...describe(
              found.node.type === "TSInterfaceDeclaration" ? found.node : found.node.typeAnnotation,
              found.file,
              new Set([...seen, key]),
            ),
            type,
          };
      }
      if (/ReactNode|ReactElement|ElementType/.test(name))
        return { kind: "slot", type };
      if (/RefObject|Ref$/.test(name)) return { kind: "reference", type };
      if (name === "Date") return { kind: "date", type };
    }
    return { kind: "json", type };
  }
  function properties(node, file, seen = new Set()) {
    if (!node) return [];
    if (node.type === "TSParenthesizedType")
      return properties(node.typeAnnotation, file, seen);
    if (["TSIntersectionType", "TSUnionType"].includes(node.type))
      return node.types.flatMap((part) => properties(part, file, seen));
    if (["TSInterfaceDeclaration", "TSTypeLiteral"].includes(node.type)) {
      const own = (node.body?.body ?? node.members ?? [])
        .filter(
          (part) =>
            part.type === "TSPropertySignature" ||
            part.type === "TSMethodSignature",
        )
        .map((part) => ({
          name: part.key.name ?? part.key.value,
          optional: Boolean(part.optional),
          ...(part.type === "TSMethodSignature"
            ? {
                kind: "callback",
                type: module(file).source.slice(part.start, part.end),
              }
            : describe(part.typeAnnotation?.typeAnnotation, file, seen)),
        }));
      return [
        ...(node.extends ?? []).flatMap((base) =>
          properties(
            { ...base, type: "TSTypeReference", typeName: base.expression },
            file,
            seen,
          ),
        ),
        ...own,
      ];
    }
    if (node.type === "TSTypeAliasDeclaration")
      return properties(node.typeAnnotation, file, seen);
    if (node.type === "TSTypeReference") {
      const name = nameOf(node.typeName);
      const args = (node.typeParameters ?? node.typeArguments)?.params ?? [];
      if (name === "BaseUIComponentProps" && args[0]?.type === "TSLiteralType")
        return native(args[0].literal.value);
      if (["Omit", "Pick", "Partial", "Required"].includes(name)) {
        let props = properties(args[0], file, seen);
        if (name === "Partial")
          return props.map((prop) => ({ ...prop, optional: true }));
        if (name === "Required")
          return props.map((prop) => ({ ...prop, optional: false }));
        const keys = describe(args[1], file).options ?? [];
        return props.filter((prop) =>
          name === "Pick"
            ? keys.includes(prop.name)
            : !keys.includes(prop.name),
        );
      }
      if (name === "VariantProps" && args[0]?.type === "TSTypeQuery") {
        const found = reference(file, nameOf(args[0].exprName));
        const config = found?.node?.arguments?.[1];
        const variants = config?.properties?.find(
          (prop) => prop.key.name === "variants",
        )?.value;
        const defaults = config?.properties?.find(
          (prop) => prop.key.name === "defaultVariants",
        )?.value;
        return (variants?.properties ?? []).map((prop) => {
          const options = prop.value.properties.map(
            (option) => option.key.name ?? option.key.value,
          );
          const boolean = options.every(
            (value) => value === "true" || value === "false",
          );
          return {
            name: prop.key.name,
            optional: true,
            type: options.map(JSON.stringify).join(" | "),
            kind: boolean ? "boolean" : "select",
            ...(boolean ? {} : { options }),
            defaultValue: literal(
              defaults?.properties?.find(
                (entry) => entry.key.name === prop.key.name,
              )?.value,
            ),
          };
        });
      }
      if (/ComponentProps(?:WithRef|WithoutRef)?$/.test(name)) {
        if (args[0]?.type === "TSLiteralType")
          return native(args[0].literal.value);
        const component = nameOf(args[0]?.exprName);
        const intrinsic = component.split(".").at(-1);
        if (/^[a-z]+$/.test(intrinsic)) return native(intrinsic);
        const found = reference(file, component);
        if (found?.node.type === "FunctionDeclaration")
          return properties(
            found.node.params[0]?.typeAnnotation?.typeAnnotation,
            found.file,
            seen,
          );
        if (
          found &&
          ["TSInterfaceDeclaration", "TSTypeAliasDeclaration"].includes(
            found.node.type,
          )
        )
          return properties(
            found.node,
            found.file,
            new Set([...seen, `${found.file}#${component}`]),
          );
        return native(component);
      }
      if (/HTMLAttributes|HTMLProps/.test(name)) return native(name);
      const key = `${file}#${name}`;
      if (!seen.has(key)) {
        const found = reference(file, name);
        if (found)
          return properties(found.node, found.file, new Set([...seen, key]));
      }
      if (/\.Props$/.test(name)) return native(name);
    }
    return [];
  }
  const result = {};
  for (const item of items.filter((entry) => entry.type === "registry:ui")) {
    const source = singleExamples[item.name] ?? previews[item.name];
    if (!source) throw new Error(`${item.name}: Playground 예시가 없습니다.`);
    const ast = parseSource(source);
    const bindings = new Map();
    for (const statement of ast.program.body) {
      if (
        statement.type !== "ImportDeclaration" ||
        !statement.source.value.startsWith("@/components/")
      )
        continue;
      for (const specifier of statement.specifiers)
        if (
          specifier.type === "ImportSpecifier" &&
          specifier.importKind !== "type" &&
          statement.importKind !== "type"
        ) {
          bindings.set(specifier.local.name, {
            name: specifier.imported.name,
            module: statement.source.value,
            key: `${statement.source.value}#${specifier.imported.name}`,
          });
        }
    }
    const targets = new Map();
    const edits = [];
    walk(ast, (node) => {
      if (
        node.type !== "JSXElement" ||
        node.openingElement.name.type !== "JSXIdentifier"
      )
        return;
      const binding = bindings.get(node.openingElement.name.name);
      if (!binding) return;
      edits.push({
        key: binding.key,
        offset:
          node.openingElement.end - (node.openingElement.selfClosing ? 2 : 1),
        childrenStart: node.openingElement.end,
        childrenEnd: node.closingElement?.start,
        attributes: node.openingElement.attributes
          .filter((attribute) => attribute.type === "JSXAttribute")
          .map((attribute) => ({
            name: attribute.name.name,
            start: attribute.start,
            end: attribute.end,
          })),
      });
      if (targets.has(binding.key)) return;
      const file = aliases[binding.module];
      const found = reference(file, binding.name);
      if (!found || found.node.type !== "FunctionDeclaration") return;
      const parameter = found.node.params[0];
      const props = new Map();
      for (const prop of properties(
        parameter?.typeAnnotation?.typeAnnotation,
        found.file,
      )) {
        const previous = props.get(prop.name);
        if (previous?.kind === "select" && prop.kind === "select")
          props.set(prop.name, {
            ...prop,
            options: [...new Set([...previous.options, ...prop.options])],
            optional: previous.optional || prop.optional,
          });
        else props.set(prop.name, prop);
      }
      if (parameter?.type === "ObjectPattern")
        for (const prop of parameter.properties.filter(
          (entry) => entry.type === "ObjectProperty",
        )) {
          const name = prop.key.name ?? prop.key.value;
          const defaultValue = literal(
            prop.value.type === "AssignmentPattern"
              ? prop.value.right
              : undefined,
          );
          if (!props.has(name))
            props.set(name, {
              name,
              type: "unknown",
              kind:
                typeof defaultValue === "boolean"
                  ? "boolean"
                  : typeof defaultValue === "number"
                    ? "number"
                    : typeof defaultValue === "string"
                      ? "text"
                      : "json",
              optional: true,
            });
          if (defaultValue !== undefined)
            props.get(name).defaultValue = defaultValue;
        }
      for (const prop of props.values()) {
        if (binding.name === "Skeleton" && ["tone", "useGradient"].includes(prop.name)) {
          props.delete(prop.name);
          continue;
        }
        if (binding.name === "Skeleton" && prop.name === "listRowTextsType") {
          prop.kind = "select";
          const source = readFileSync(file, "utf8");
          const list = source.match(/skeletonListRowTextsTypes = \[([\s\S]*?)\] as const/);
          prop.options = [...(list?.[1].matchAll(/"([^"\n]+)"/g) ?? [])].map((entry) => entry[1]);
        }
        if (prop.name === "multiple" && prop.kind === "json")
          prop.kind = "boolean";
        if (prop.name === "useGradient" && prop.defaultValue === undefined)
          prop.defaultValue = true;
        const commonChoices = {
          orientation: ["horizontal", "vertical"],
          side: ["top", "right", "bottom", "left"],
          align: ["start", "center", "end"],
        }[prop.name];
        if (commonChoices && (prop.kind === "text" || prop.kind === "json")) {
          prop.kind = "select";
          prop.options = commonChoices;
        }
        if (
          prop.kind === "select" &&
          prop.options.includes(true) &&
          prop.options.includes(false) &&
          prop.options.every((option) => typeof option === "boolean")
        )
          prop.kind = "boolean";
        if (
          [
            "table",
            "calendar",
            "gantt",
            "tree",
            "container",
            "apiRef",
            "actionsRef",
          ].includes(prop.name)
        )
          prop.kind = "reference";
        if (/^on[A-Z]/.test(prop.name) && prop.kind !== "slot") prop.kind = "callback";
        if (["children", "render", "asChild"].includes(prop.name))
          prop.kind = "slot";
        if (prop.name === "ref") prop.kind = "reference";
        if (binding.name === "Skeleton" && ["height", "width", "radius"].includes(prop.name)) {
          prop.kind = "number";
          if (prop.name === "radius") prop.defaultValue = 8;
        }
        if (binding.name === "Skeleton" && prop.name === "custom") prop.kind = "slot";
        if (prop.name === "items" && /TabItem\[\]/.test(prop.type)) prop.kind = "slot";
        if (binding.name === "Switch" && ["trackContent", "onTrackContent", "offTrackContent", "thumbContent"].includes(prop.name)) prop.kind = "icon";
      }
      const textChildren =
        node.children.length > 0 &&
        node.children.every(
          (child) =>
            child.type === "JSXText" ||
            (child.type === "JSXExpressionContainer" &&
              ["StringLiteral", "NumericLiteral", "Identifier"].includes(
                child.expression.type,
              )),
        );
      if (textChildren)
        props.set("children", {
          name: "children",
          type: "string",
          kind: "text",
          optional: true,
        });
      for (const attribute of node.openingElement.attributes) {
        if (attribute.type !== "JSXAttribute") continue;
        const prop = props.get(attribute.name.name);
        const value =
          attribute.value === null
            ? true
            : literal(
                attribute.value.type === "JSXExpressionContainer"
                  ? attribute.value.expression
                  : attribute.value,
              );
        if (prop && value !== undefined) prop.defaultValue = value;
      }
      if (
        textChildren &&
        node.children.every((child) => child.type === "JSXText")
      )
        props.get("children").defaultValue = node.children
          .map((child) => child.value)
          .join("")
          .trim();
      for (const event of [
        ...(props.has("className") ? ["onClick"] : []),
        ...(/Input|Textarea/.test(binding.name) ? ["onChange"] : []),
      ])
        if (!props.has(event))
          props.set(event, {
            name: event,
            type: "이벤트 함수",
            kind: "callback",
            optional: true,
          });
      targets.set(binding.key, { ...binding, props: [...props.values()] });
    });
    const primary = item.name
      .split("-")
      .map((part) => part[0].toUpperCase() + part.slice(1))
      .join("");
    const list = [...targets.values()].sort(
      (a, b) => Number(b.name === primary) - Number(a.name === primary),
    );
    if (!list.length)
      throw new Error(`${item.name}: Playground 대상이 없습니다.`);
    result[item.name] = { targets: list, edits, example: source };
  }
  return result;
}

export function instrumentPlayground(source, runtimeModule, previewName) {
  const ast = parseSource(source);
  const jsxNames = new Set();
  walk(ast, (node) => {
    if (node.type === "JSXOpeningElement" && node.name.type === "JSXIdentifier")
      jsxNames.add(node.name.name);
  });
  const edits = [];
  const declarations = [];
  for (const statement of ast.program.body) {
    if (
      statement.type !== "ImportDeclaration" ||
      !statement.source.value.startsWith("@/components/")
    )
      continue;
    for (const specifier of statement.specifiers) {
      if (
        specifier.type !== "ImportSpecifier" ||
        specifier.importKind === "type" ||
        statement.importKind === "type" ||
        !jsxNames.has(specifier.local.name)
      )
        continue;
      const name = specifier.local.name;
      edits.push({
        start: specifier.start,
        end: specifier.end,
        text: `${specifier.imported.name} as __playground_${name}`,
      });
      declarations.push(
        `const ${name} = __playgroundComponent(__playground_${name}, ${JSON.stringify(`${statement.source.value}#${specifier.imported.name}`)});`,
      );
    }
  }
  if (!declarations.length) return source;
  const single = singleExamples[previewName];
  if (single) {
    const original = ast.program.body
      .map((node) => node.declaration ?? node)
      .find(
        (node) =>
          node.type === "FunctionDeclaration" && node.id.name === "Pattern",
      );
    const sample = parseSource(single).program.body.find(
      (node) => node.type === "ExportNamedDeclaration",
    ).declaration;
    edits.push({
      start: original.body.start + 1,
      end: original.body.start + 1,
      text: `\nif (__usePlaygroundActive()) ${single.slice(sample.body.start, sample.body.end)}\n`,
    });
  }
  const offset =
    ast.program.body.filter((node) => node.type === "ImportDeclaration").at(-1)
      ?.end ?? 0;
  edits.push({
    start: offset,
    end: offset,
    text: `\nimport { playgroundComponent as __playgroundComponent${single ? ", usePlaygroundActive as __usePlaygroundActive" : ""} } from ${JSON.stringify(runtimeModule)};\n${declarations.join("\n")}\n`,
  });
  return edits
    .sort((a, b) => b.start - a.start)
    .reduce(
      (text, edit) =>
        text.slice(0, edit.start) + edit.text + text.slice(edit.end),
      source,
    );
}
