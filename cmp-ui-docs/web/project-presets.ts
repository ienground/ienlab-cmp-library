export type ProjectMode = "theme" | "light" | "dark";
export type ProjectVariables = Record<ProjectMode, Record<string, string>>;
export interface ProjectPreset {
  $schema: string;
  name: string;
  type: "registry:base";
  title: string;
  description: string;
  config: Record<string, unknown>;
  registryDependencies: string[];
  cssVars: ProjectVariables;
  meta: Record<string, unknown>;
}
export interface ProjectEdits {
  title?: string;
  description?: string;
  cssVars?: Partial<ProjectVariables>;
}
export const typographyRoles = [
  "display",
  "title1",
  "title2",
  "title3",
  "body1",
  "body2",
  "label1",
  "label2",
  "caption",
] as const;
export const projectNamePattern = /^[a-z][a-z0-9]*(?:-[a-z0-9]+)*$/;
export const projectStorageKey = "ienlab:projects:v1";
export function isProjectCssValue(value: string) {
  return (
    Boolean(value.trim()) &&
    value.length <= 500 &&
    !/[;{}<>\r\n\\]|url\s*\(|@import/i.test(value)
  );
}

function record(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === "object" && !Array.isArray(value);
}
function variables(value: unknown): Record<string, string> {
  if (!record(value))
    throw new Error("cssVars에는 CSS 변수 객체가 필요합니다.");
  const result: Record<string, string> = {};
  for (const [name, entry] of Object.entries(value)) {
    if (
      !/^[a-zA-Z][a-zA-Z0-9-]*$/.test(name) ||
      typeof entry !== "string" ||
      !isProjectCssValue(entry)
    )
      throw new Error(`CSS 변수 값이 올바르지 않습니다: ${name}`);
    result[name] = entry;
  }
  return result;
}
export function parseProject(value: unknown): ProjectPreset {
  if (
    !record(value) ||
    value.type !== "registry:base" ||
    typeof value.name !== "string" ||
    !projectNamePattern.test(value.name) ||
    typeof value.title !== "string" ||
    !value.title.trim() ||
    !record(value.cssVars) ||
    !record(value.config)
  )
    throw new Error(
      "프로젝트 이름·제목·config·cssVars가 포함된 registry:base JSON이 필요합니다.",
    );
  if (
    !Array.isArray(value.registryDependencies) ||
    !value.registryDependencies.every(
      (entry): entry is string => typeof entry === "string",
    ) ||
    !value.registryDependencies.includes("@ienlab/ien-theme")
  )
    throw new Error("@ienlab/ien-theme 의존성이 필요합니다.");
  return {
    $schema: "https://ui.shadcn.com/schema/registry-item.json",
    name: value.name,
    type: "registry:base",
    title: value.title.trim(),
    description: typeof value.description === "string" ? value.description : "",
    config: value.config,
    registryDependencies: value.registryDependencies,
    cssVars: {
      theme: variables(value.cssVars.theme ?? {}),
      light: variables(value.cssVars.light ?? {}),
      dark: variables(value.cssVars.dark ?? {}),
    },
    meta: record(value.meta) ? value.meta : {},
  };
}

export function resolveProject(
  base: ProjectPreset,
  edits?: ProjectEdits,
): ProjectPreset {
  const project: ProjectPreset = {
    ...base,
    title: edits?.title ?? base.title,
    description: edits?.description ?? base.description,
    cssVars: {
      theme: { ...base.cssVars.theme, ...edits?.cssVars?.theme },
      light: { ...base.cssVars.light, ...edits?.cssVars?.light },
      dark: { ...base.cssVars.dark, ...edits?.cssVars?.dark },
    },
  };
  for (const values of Object.values(project.cssVars)) {
    for (const [name, value] of Object.entries(values)) {
      if (/^ien-project-(font-size|line-height|font-weight)-/.test(name))
        values[name.replace("ien-project-", "ien-")] = value;
    }
  }
  return project;
}

// 글자 크기를 명시적으로 편집한 역할만 덮어 기존 컴포넌트의 기본 크기를 유지합니다.
export function projectTypographyCss(
  project: ProjectPreset,
): Record<string, Record<string, string>> {
  const rules: Record<string, Record<string, string>> = {};
  for (const role of typographyRoles)
    for (const mode of ["light", "dark"] as const) {
      const properties: Record<string, string> = {};
      for (const [suffix, property] of [
        ["font-size", "font-size"],
        ["line-height", "line-height"],
        ["font-weight", "font-weight"],
      ]) {
        const name = `ien-project-${suffix}-${role}`;
        if (project.cssVars[mode][name])
          properties[property] = `var(--${name})`;
        else if (mode === "dark" && project.cssVars.light[name])
          properties[property] = `var(--${name})`;
      }
      if (Object.keys(properties).length) {
        const targets = [
          `[data-ien-typography="${role}"]`,
          ...(role === "body2"
            ? [
                "input",
                "textarea",
                "number-field-input",
                "combobox-chip-input",
              ].map(
                (slot) => `[data-slot="${slot}"]:not([data-ien-typography])`,
              )
            : []),
        ];
        const scopes =
          mode === "light"
            ? [":root:not(.dark)", ".light"]
            : [":root.dark", ".dark"];
        const selector = scopes
          .flatMap((scope) => targets.map((target) => `${scope} ${target}`))
          .join(", ");
        rules[selector] = properties;
      }
    }
  return rules;
}
export function projectRegistryItem(project: ProjectPreset) {
  return { ...resolveProject(project), css: projectTypographyCss(project) };
}
export function projectCss(
  project: ProjectPreset,
  projectDefaults: ProjectVariables,
): string {
  const rule = (selector: string, values: Record<string, string>) =>
    `${selector} {\n${Object.entries(values)
      .map(([name, value]) => `  --${name}: ${value};`)
      .join("\n")}\n}`;
  return [
    rule(":root:not(.dark), :root .light", {
      ...projectDefaults.theme,
      ...projectDefaults.light,
      ...project.cssVars.theme,
      ...project.cssVars.light,
    }),
    rule(":root.dark, :root .dark", {
      ...projectDefaults.theme,
      ...projectDefaults.light,
      ...project.cssVars.theme,
      ...project.cssVars.light,
      ...projectDefaults.dark,
      ...project.cssVars.dark,
    }),
    ...Object.entries(projectTypographyCss(project)).map(
      ([selector, properties]) =>
        `${selector} {\n${Object.entries(properties)
          .map(([name, value]) => `  ${name}: ${value};`)
          .join("\n")}\n}`,
    ),
  ].join("\n\n");
}

export interface StoredProjects {
  version: 1;
  selected: string;
  local: ProjectPreset[];
  edits: Record<string, ProjectEdits>;
}
export function defaultProjectState(
  publishedProjects: ProjectPreset[],
): StoredProjects {
  return {
    version: 1,
    selected: publishedProjects[0].name,
    local: [],
    edits: {},
  };
}
export function parseProjectState(
  value: unknown,
  publishedProjects: ProjectPreset[],
  reservedProjectNames: ReadonlySet<string>,
): StoredProjects {
  if (
    !record(value) ||
    value.version !== 1 ||
    typeof value.selected !== "string" ||
    !Array.isArray(value.local) ||
    !record(value.edits)
  )
    throw new Error("저장된 프로젝트 형식이 올바르지 않습니다.");
  const publishedNames = new Set(
    publishedProjects.map((project) => project.name),
  );
  const local = value.local
    .map(parseProject)
    .filter((project) => !publishedNames.has(project.name));
  const names = new Set(publishedNames);
  for (const project of local) {
    if (reservedProjectNames.has(project.name) || names.has(project.name))
      throw new Error(`프로젝트 이름이 중복됩니다: ${project.name}`);
    names.add(project.name);
  }
  const edits: Record<string, ProjectEdits> = {};
  for (const [name, entry] of Object.entries(value.edits)) {
    if (!names.has(name)) continue;
    if (!record(entry)) throw new Error("프로젝트 편집값이 올바르지 않습니다.");
    const patch: ProjectEdits = {};
    if (typeof entry.title === "string" && entry.title.trim())
      patch.title = entry.title;
    if (typeof entry.description === "string")
      patch.description = entry.description;
    if (entry.cssVars !== undefined) {
      if (!record(entry.cssVars))
        throw new Error("프로젝트 편집값 cssVars가 올바르지 않습니다.");
      patch.cssVars = Object.fromEntries(
        Object.entries(entry.cssVars).map(([mode, values]) => {
          if (!["theme", "light", "dark"].includes(mode))
            throw new Error("지원하지 않는 색상 모드입니다.");
          return [mode, variables(values)];
        }),
      );
    }
    edits[name] = patch;
  }
  return {
    version: 1,
    selected: names.has(value.selected)
      ? value.selected
      : publishedProjects[0].name,
    local,
    edits,
  };
}
