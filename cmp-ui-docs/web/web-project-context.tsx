import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { useLocation } from "react-router";
import catalog from "./generated-project-presets.json";
import {
  defaultProjectState,
  parseProject,
  parseProjectState,
  projectCss,
  projectStorageKey,
  resolveProject,
  type ProjectEdits,
  type ProjectMode,
  type ProjectPreset,
  type ProjectVariables,
  type StoredProjects,
} from "./project-presets";

export const projectDefaults: ProjectVariables = catalog.defaults;
const publishedProjects = catalog.projects.map(parseProject);
const reservedProjectNames = new Set(catalog.names);

interface ProjectContextValue {
  project: ProjectPreset;
  base: ProjectPreset;
  projects: ProjectPreset[];
  published: boolean;
  dirty: boolean;
  storageError: string;
  select: (name: string) => void;
  edit: (patch: ProjectEdits) => void;
  setVariable: (mode: ProjectMode, name: string, value?: string) => void;
  reset: () => void;
  add: (project: ProjectPreset) => void;
}
const ProjectContext = createContext<ProjectContextValue | null>(null);
function loadProjects() {
  try {
    const stored =
      typeof window === "undefined"
        ? null
        : localStorage.getItem(projectStorageKey);
    return {
      state: stored
        ? parseProjectState(
            JSON.parse(stored),
            publishedProjects,
            reservedProjectNames,
          )
        : defaultProjectState(publishedProjects),
      error: "",
    };
  } catch {
    return {
      state: defaultProjectState(publishedProjects),
      error:
        "저장된 프로젝트를 읽지 못했습니다. 기본 프로젝트를 표시합니다. 브라우저 저장소를 확인하세요.",
    };
  }
}
export function WebProjectProvider({ children }: { children: ReactNode }) {
  const [initial] = useState(loadProjects);
  const [state, setState] = useState<StoredProjects>(initial.state);
  const [storageError, setStorageError] = useState(initial.error);
  const pathname = useLocation().pathname;
  const enabled = pathname.startsWith("/web/") || pathname === "/projects";
  const projects = useMemo(
    () => [...publishedProjects, ...state.local],
    [state.local],
  );
  const base =
    projects.find((entry) => entry.name === state.selected) ?? projects[0];
  const edits = state.edits[base.name];
  const project = useMemo(() => resolveProject(base, edits), [base, edits]);
  const css = useMemo(() => projectCss(project, projectDefaults), [project]);
  useEffect(() => {
    if (!enabled) return;
    const style = document.createElement("style");
    style.dataset.ienProject = project.name;
    style.textContent = css;
    document.head.appendChild(style);
    return () => style.remove();
  }, [enabled, project.name, css]);

  function save(next: StoredProjects) {
    setState(next);
    try {
      localStorage.setItem(projectStorageKey, JSON.stringify(next));
      setStorageError("");
    } catch {
      setStorageError(
        "브라우저에 저장하지 못했습니다. 현재 값은 이 화면에서 유지됩니다. JSON을 다운로드해 보관하세요.",
      );
    }
  }
  function edit(patch: ProjectEdits) {
    save({
      ...state,
      edits: { ...state.edits, [base.name]: { ...edits, ...patch } },
    });
  }
  function setVariable(mode: ProjectMode, name: string, value?: string) {
    const values = { ...edits?.cssVars?.[mode] };
    if (value === undefined || value === base.cssVars[mode][name])
      delete values[name];
    else values[name] = value;
    const cssVars = { ...edits?.cssVars, [mode]: values };
    edit({ cssVars });
  }
  function add(next: ProjectPreset) {
    if (
      reservedProjectNames.has(next.name) ||
      projects.some((entry) => entry.name === next.name)
    )
      throw new Error("이미 사용 중인 프로젝트·레지스트리 이름입니다.");
    save({ ...state, selected: next.name, local: [...state.local, next] });
  }
  const dirty = Boolean(
    edits &&
      ((edits.title !== undefined && edits.title !== base.title) ||
        (edits.description !== undefined &&
          edits.description !== base.description) ||
        Object.values(edits.cssVars ?? {}).some(
          (values) => Object.keys(values).length,
        )),
  );
  return (
    <ProjectContext.Provider
      value={{
        project,
        base,
        projects: projects.map((entry) =>
          resolveProject(entry, state.edits[entry.name]),
        ),
        published: publishedProjects.some((entry) => entry.name === base.name),
        dirty,
        storageError,
        select: (name) => save({ ...state, selected: name }),
        edit,
        setVariable,
        add,
        reset: () => {
          const next = { ...state.edits };
          delete next[base.name];
          save({ ...state, edits: next });
        },
      }}
    >
      {children}
    </ProjectContext.Provider>
  );
}
export function useWebProject() {
  const context = useContext(ProjectContext);
  if (!context)
    throw new Error(
      "프로젝트 기능은 WebProjectProvider 안에서 사용해야 합니다.",
    );
  return context;
}
