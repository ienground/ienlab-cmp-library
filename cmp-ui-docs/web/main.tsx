import {
  Fragment,
  StrictMode,
  Suspense,
  lazy,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import { createRoot } from "react-dom/client";
import { RiGithubFill } from "@remixicon/react";
import type { CatalogItem } from "./catalog.js";
import {
  ArrowUpRight,
  Command,
  Info,
  Menu,
  Moon,
  Search,
  Sun,
  X,
} from "lucide-react";
import {
  BrowserRouter,
  Link,
  Navigate,
  Route,
  Routes,
  useLocation,
  useNavigate,
  useParams,
} from "react-router";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetTitle,
} from "@/components/ui/sheet";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { catalog, categories } from "./catalog.js";
import apiDocs from "./generated-api-docs.json";
import ienlabLogo from "./assets/ienlab-logo.png";
import {
  ColorSchemeBuilder,
  colorSchemeSections,
  highlightKotlin,
} from "./ColorSchemeBuilder";
import { composeColorQuery } from "./color-scheme";
import type { ColorSchemes } from "./color-scheme";
import "./styles.css";

const SkeletonComposer = lazy(() =>
  import("./SkeletonComposer").then(({ SkeletonComposer: component }) => ({
    default: component,
  })),
);

const repositoryUrl =
  "https://github.com/ienground/ienlab-cmp-library/blob/main/";
const defaultComponentId = "button";
const libraryVersion = import.meta.env.VITE_LIBRARY_VERSION;

interface ApiParameterDocumentation {
  name: string;
  description: string;
}

interface ApiDocumentationEntry {
  name: string;
  signature: string;
  description: string;
  parameters: ApiParameterDocumentation[];
}

type PreviewTab = "preview" | "composer" | "api";
type ThemeMode = "light" | "dark";

const typedApiDocs = apiDocs as Record<string, ApiDocumentationEntry[]>;
const themeStorageKey = "ienlab-cmp-ui-docs-theme";
const minimumPreviewHeight = window.matchMedia("(max-width: 780px)").matches
  ? 460
  : 530;

function getInitialThemeMode(): ThemeMode {
  const savedTheme = window.localStorage.getItem(themeStorageKey);
  if (savedTheme === "light" || savedTheme === "dark") return savedTheme;
  return window.matchMedia("(prefers-color-scheme: dark)").matches
    ? "dark"
    : "light";
}

function App() {
  const [themeMode, setThemeMode] = useState<ThemeMode>(getInitialThemeMode);

  useEffect(() => {
    document.documentElement.classList.toggle("dark", themeMode === "dark");
    document.documentElement.style.colorScheme = themeMode;
    document
      .querySelector('meta[name="theme-color"]')
      ?.setAttribute("content", themeMode === "dark" ? "#0a0a0a" : "#ffffff");
    window.localStorage.setItem(themeStorageKey, themeMode);
  }, [themeMode]);

  return (
    <BrowserRouter>
      <Routes>
        <Route element={<InitialRedirect />} path="/" />
        <Route
          element={
            <ComponentPage
              onThemeModeChange={setThemeMode}
              themeMode={themeMode}
            />
          }
          path="/:section/:componentId?/:page?"
        />
        <Route
          element={
            <Navigate
              replace
              to={`/components/${defaultComponentId}/preview`}
            />
          }
          path="/components"
        />
        <Route
          element={
            <Navigate
              replace
              to={`/components/${defaultComponentId}/preview`}
            />
          }
          path="*"
        />
      </Routes>
    </BrowserRouter>
  );
}

function InitialRedirect() {
  const requestedId = new URLSearchParams(window.location.search).get(
    "component",
  );
  const componentId =
    catalog.find((item) => item.id === requestedId)?.id ?? defaultComponentId;
  const requestedPage = new URLSearchParams(window.location.search).get("page");
  const page =
    requestedPage === "api"
      ? "api"
      : requestedPage === "composer" && componentId === "skeleton"
        ? "composer"
        : "preview";

  return <Navigate replace to={`/components/${componentId}/${page}`} />;
}

function ComponentPage({
  onThemeModeChange,
  themeMode,
}: {
  onThemeModeChange: (themeMode: ThemeMode) => void;
  themeMode: ThemeMode;
}) {
  const { section, componentId: requestedComponentId, page } = useParams();
  const navigate = useNavigate();
  const isColorSchemePage = useLocation().pathname === "/color-scheme";
  const [headerScrolled, setHeaderScrolled] = useState(false);
  const [activeColorSection, setActiveColorSection] = useState(
    colorSchemeSections[0].id,
  );
  const [lastComponentId, setLastComponentId] = useState(defaultComponentId);
  const [query, setQuery] = useState("");
  const [mobileNavigationOpen, setMobileNavigationOpen] = useState(false);
  const [previewLoaded, setPreviewLoaded] = useState(false);
  const [previewHeight, setPreviewHeight] = useState(minimumPreviewHeight);
  const previewFrameRef = useRef<HTMLIFrameElement>(null);
  const [customColorSchemes, setCustomColorSchemes] =
    useState<ColorSchemes | null>(null);
  const activeComponent = useMemo(
    () =>
      catalog.find(
        (item) =>
          item.id ===
          (isColorSchemePage ? lastComponentId : requestedComponentId),
      ),
    [requestedComponentId, isColorSchemePage, lastComponentId],
  );
  useEffect(() => {
    const updateHeader = () => setHeaderScrolled(window.scrollY > 24);
    updateHeader();
    window.addEventListener("scroll", updateHeader, { passive: true });
    return () => window.removeEventListener("scroll", updateHeader);
  }, []);

  useEffect(() => {
    let pendingPreviewHeight: number | null = null;
    let heightUpdateTimeout: number | undefined;

    function updatePreviewHeight(event: MessageEvent<unknown>) {
      if (event.source !== previewFrameRef.current?.contentWindow) return;
      if (typeof event.data !== "object" || event.data === null) return;

      const message = event.data as { type?: unknown; height?: unknown };
      if (
        message.type !== "ien-compose-preview-height" ||
        typeof message.height !== "number" ||
        !Number.isFinite(message.height)
      ) {
        return;
      }

      pendingPreviewHeight = Math.ceil(message.height + 48);
      window.clearTimeout(heightUpdateTimeout);
      heightUpdateTimeout = window.setTimeout(() => {
        if (pendingPreviewHeight !== null) {
          setPreviewHeight(
            Math.max(minimumPreviewHeight, pendingPreviewHeight),
          );
          pendingPreviewHeight = null;
        }
        heightUpdateTimeout = undefined;
      }, 300);
    }

    window.addEventListener("message", updatePreviewHeight);
    return () => {
      window.removeEventListener("message", updatePreviewHeight);
      window.clearTimeout(heightUpdateTimeout);
    };
  }, []);

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: "instant" });
  }, [requestedComponentId, isColorSchemePage]);

  useEffect(() => {
    if (!isColorSchemePage) return;
    setActiveColorSection(colorSchemeSections[0].id);
    const observer = new IntersectionObserver(
      (entries) => {
        for (const entry of entries) {
          if (entry.isIntersecting) setActiveColorSection(entry.target.id);
        }
      },
      { rootMargin: "-20% 0px -60% 0px", threshold: 0 },
    );
    for (const section of colorSchemeSections) {
      const element = document.getElementById(section.id);
      if (element) observer.observe(element);
    }
    return () => observer.disconnect();
  }, [isColorSchemePage]);

  function selectColorSection(id: string) {
    setActiveColorSection(id);
    setMobileNavigationOpen(false);
    document.getElementById(id)?.scrollIntoView({
      behavior: window.matchMedia("(prefers-reduced-motion: reduce)").matches
        ? "instant"
        : "smooth",
      block: "start",
    });
  }

  const filteredCatalog = useMemo(() => {
    const normalizedQuery = query.trim().toLowerCase();
    if (!normalizedQuery) return catalog;

    return catalog.filter((item) => {
      const documentation = typedApiDocs[item.id] ?? [];
      const searchableText = [
        item.name,
        item.category,
        item.description,
        ...item.api,
        ...documentation.flatMap((api) => [
          api.name,
          api.signature,
          api.description,
          ...api.parameters.flatMap((parameter) => [
            parameter.name,
            parameter.description,
          ]),
        ]),
      ]
        .join(" ")
        .toLowerCase();
      return searchableText.includes(normalizedQuery);
    });
  }, [query]);
  useEffect(() => {
    function focusSearch(event: KeyboardEvent) {
      const target = event.target;
      if (
        event.key !== "/" ||
        target instanceof HTMLInputElement ||
        target instanceof HTMLTextAreaElement ||
        (target instanceof HTMLElement && target.isContentEditable)
      ) {
        return;
      }

      event.preventDefault();
      const visibleSearchInput = Array.from(
        document.querySelectorAll<HTMLInputElement>(".search-box input"),
      ).find((input) => input.getClientRects().length > 0);
      visibleSearchInput?.focus();
    }

    window.addEventListener("keydown", focusSearch);
    return () => window.removeEventListener("keydown", focusSearch);
  }, []);

  useEffect(() => {
    if (
      requestedComponentId &&
      catalog.some((item) => item.id === requestedComponentId)
    ) {
      setLastComponentId(requestedComponentId);
    }
  }, [requestedComponentId]);

  useEffect(() => {
    setPreviewLoaded(false);
    setPreviewHeight(minimumPreviewHeight);
  }, [activeComponent?.id, themeMode, customColorSchemes, isColorSchemePage]);

  if (page === "colors") return <Navigate replace to="/color-scheme" />;

  if (
    !activeComponent ||
    (!isColorSchemePage &&
      (section !== "components" ||
        (page !== "preview" &&
          page !== "api" &&
          !(page === "composer" && activeComponent.id === "skeleton"))))
  ) {
    return (
      <Navigate replace to={`/components/${defaultComponentId}/preview`} />
    );
  }

  const activeTab: PreviewTab =
    page === "api" ? "api" : page === "composer" ? "composer" : "preview";
  const composeUrl =
    import.meta.env.BASE_URL +
    "compose/index.html?component=" +
    encodeURIComponent(activeComponent.id) +
    "&theme=" +
    themeMode +
    (customColorSchemes
      ? "&" + composeColorQuery(customColorSchemes[themeMode])
      : "");

  function selectComponent(): void {
    setMobileNavigationOpen(false);
  }

  return (
    <>
      <header className={headerScrolled ? "topbar header-floating" : "topbar"}>
        <nav className="header-navigation" aria-label="데모 페이지">
          <Button
            asChild
            variant="ghost"
            aria-current={!isColorSchemePage ? "page" : undefined}
          >
            <Link
              to={`/components/${activeComponent.id}/preview`}
              onClick={selectComponent}
            >
              컴포넌트
            </Link>
          </Button>
          <Button
            asChild
            variant="ghost"
            aria-current={isColorSchemePage ? "page" : undefined}
          >
            <Link to="/color-scheme" onClick={selectComponent}>
              컬러 스킴
            </Link>
          </Button>
        </nav>
        <Link
          className="header-brand"
          to="/components"
          aria-label="IENLAB 데모 홈"
        >
          <img alt="" src={ienlabLogo} width={32} height={32} />
          <span className="header-brand-label">
            <span className="header-brand-name">cmp-library</span>
            <Badge className="header-version" variant="secondary">
              v{libraryVersion}
            </Badge>
          </span>
        </Link>
        <div className="topbar-actions">
          <Button
            aria-expanded={mobileNavigationOpen}
            aria-label="탐색 메뉴 열기"
            className="mobile-menu-button rounded-xl border"
            onClick={() => setMobileNavigationOpen(true)}
            size="icon-sm"
            variant="ghost"
          >
            <Menu aria-hidden="true" />
          </Button>

          <Button
            aria-label={
              themeMode === "dark" ? "라이트 모드로 전환" : "다크 모드로 전환"
            }
            onClick={() =>
              onThemeModeChange(themeMode === "dark" ? "light" : "dark")
            }
            size="icon-sm"
            title={themeMode === "dark" ? "라이트 모드" : "다크 모드"}
            variant="outline"
          >
            {themeMode === "dark" ? (
              <Sun aria-hidden="true" />
            ) : (
              <Moon aria-hidden="true" />
            )}
          </Button>
          <Button
            asChild
            className="github-link"
            size="icon-sm"
            variant="ghost"
          >
            <a
              href="https://github.com/ienground/ienlab-cmp-library"
              rel="noreferrer"
              target="_blank"
              aria-label="GitHub 저장소"
            >
              <RiGithubFill aria-hidden="true" />
            </a>
          </Button>
        </div>
      </header>
      <div
        className={
          isColorSchemePage ? "app-shell color-scheme-page" : "app-shell"
        }
      >
        <Sidebar
          isColorSchemePage={isColorSchemePage}
          activeColorSection={activeColorSection}
          onColorSectionSelect={selectColorSection}
          activePage={activeTab === "composer" ? "preview" : activeTab}
          activeComponentId={activeComponent.id}
          filteredCatalog={filteredCatalog}
          mobileNavigationOpen={mobileNavigationOpen}
          onClose={() => setMobileNavigationOpen(false)}
          onQueryChange={setQuery}
          onSelect={selectComponent}
          query={query}
        />

        <div className="main-column">
          <main className="main-content">
            <div className="page-heading">
              <div className="page-heading-copy">
                <p className="eyebrow">
                  {isColorSchemePage ? "THEME BUILDER" : "COMPONENT LIBRARY"}
                </p>
                <h1>
                  {isColorSchemePage ? "ColorScheme" : activeComponent.name}
                </h1>
                <p className="component-description">
                  {isColorSchemePage
                    ? "IENLAB 테마의 색상을 만들고 Kotlin 코드로 내보냅니다."
                    : activeComponent.description}
                </p>
              </div>
              {!isColorSchemePage && (
                <Badge className="component-count" variant="secondary">
                  {catalog.length} components
                </Badge>
              )}
            </div>

            {!isColorSchemePage && (
              <Tabs
                aria-label="콘텐츠 보기 방식"
                className="view-tabs-root"
                onValueChange={(value) => {
                  if (
                    value === "preview" ||
                    value === "api" ||
                    (value === "composer" && activeComponent.id === "skeleton")
                  ) {
                    navigate(`/components/${activeComponent.id}/${value}`);
                  }
                }}
                value={activeTab}
              >
                <TabsList className="view-tabs">
                  <TabsTrigger className="view-tab" value="preview">
                    Preview
                  </TabsTrigger>
                  {activeComponent.id === "skeleton" && (
                    <TabsTrigger className="view-tab" value="composer">
                      조합기
                    </TabsTrigger>
                  )}
                  <TabsTrigger className="view-tab" value="api">
                    API 참고
                  </TabsTrigger>
                </TabsList>

                <TabsContent
                  className="content-panel"
                  forceMount
                  value="preview"
                >
                  <div className="panel-heading">
                    <div>
                      <p className="panel-kicker">LIVE COMPONENT</p>
                      <h2>Compose 미리보기</h2>
                    </div>
                    <Badge className="runtime-badge" variant="outline">
                      <span className="status-dot" /> Kotlin · Wasm
                    </Badge>
                  </div>
                  <p className="panel-description">
                    실제 Compose Multiplatform 컴포넌트입니다. 미리보기 안에서
                    상태와 동작을 확인할 수 있습니다.
                  </p>
                  {customColorSchemes && (
                    <Button
                      className="preview-reset"
                      variant="outline"
                      onClick={() => setCustomColorSchemes(null)}
                    >
                      기본 색상 복원
                    </Button>
                  )}
                  <div className="preview-frame-wrap">
                    {!previewLoaded ? (
                      <div
                        aria-live="polite"
                        className="preview-loading"
                        role="status"
                      >
                        <Skeleton aria-hidden="true" className="loading-mark" />
                        <span>Compose 미리보기 불러오는 중</span>
                      </div>
                    ) : null}
                    <iframe
                      key={composeUrl}
                      ref={previewFrameRef}
                      aria-label={activeComponent.name + " Compose 미리보기"}
                      className={previewLoaded ? "compose-frame loaded" : "compose-frame"}
                      onLoad={() => setPreviewLoaded(true)}
                      src={composeUrl}
                      style={{ height: `${previewHeight}px` }}
                      title={activeComponent.name + " Compose 미리보기"}
                    />
                  </div>
                </TabsContent>

                {activeComponent.id === "skeleton" && (
                  <TabsContent
                    className="content-panel"
                    forceMount
                    value="composer"
                  >
                    <Suspense
                      fallback={
                        <p className="skeleton-composer-loading" role="status">
                          스켈레톤 조합기를 불러오는 중입니다.
                        </p>
                      }
                    >
                      <SkeletonComposer />
                    </Suspense>
                  </TabsContent>
                )}

                <TabsContent className="content-panel" forceMount value="api">
                  <ApiSidebar component={activeComponent} />
                  <ApiDocumentation component={activeComponent} />
                </TabsContent>
              </Tabs>
            )}
            <section
              hidden={!isColorSchemePage}
              className="content-panel"
              aria-label="컬러 스킴 생성기"
            >
              <ColorSchemeBuilder
                themeMode={themeMode}
                onApply={(schemes, mode) => {
                  setCustomColorSchemes({
                    light: { ...schemes.light },
                    dark: { ...schemes.dark },
                  });
                  onThemeModeChange(mode);
                  navigate(`/components/${activeComponent.id}/preview`);
                }}
              />
            </section>
          </main>
        </div>
      </div>
    </>
  );
}

function Sidebar({
  isColorSchemePage,
  activeColorSection,
  onColorSectionSelect,
  activePage,
  activeComponentId,
  filteredCatalog,
  mobileNavigationOpen,
  onClose,
  onQueryChange,
  onSelect,
  query,
}: {
  isColorSchemePage: boolean;
  activeColorSection: string;
  onColorSectionSelect: (id: string) => void;
  activePage: PreviewTab;
  activeComponentId: string;
  filteredCatalog: CatalogItem[];
  mobileNavigationOpen: boolean;
  onClose: () => void;
  onQueryChange: (query: string) => void;
  onSelect: () => void;
  query: string;
}) {
  const visibleCategories = categories
    .map((category) => ({
      category,
      items: filteredCatalog.filter((item) => item.category === category),
    }))
    .filter((group) => group.items.length > 0);

  return (
    <>
      <aside className="sidebar desktop-sidebar">
        <SidebarContents
          isColorSchemePage={isColorSchemePage}
          activeColorSection={activeColorSection}
          onColorSectionSelect={onColorSectionSelect}
          activePage={activePage}
          activeComponentId={activeComponentId}
          filteredCatalog={filteredCatalog}
          onQueryChange={onQueryChange}
          onSelect={onSelect}
          query={query}
          visibleCategories={visibleCategories}
        />
      </aside>
      <Sheet
        onOpenChange={(open) => {
          if (!open) onClose();
        }}
        open={mobileNavigationOpen}
      >
        <SheetContent
          className="mobile-sidebar-sheet"
          side="left"
          showCloseButton={false}
        >
          <div className="mobile-sidebar-header">
            <SheetTitle className="sr-only">
              {isColorSchemePage ? "컬러 스킴 목차" : "컴포넌트 탐색"}
            </SheetTitle>
            <SheetClose asChild>
              <Button
                aria-label="탐색 메뉴 닫기"
                size="icon-sm"
                variant="ghost"
              >
                <X aria-hidden="true" />
              </Button>
            </SheetClose>
          </div>
          <SidebarContents
            isColorSchemePage={isColorSchemePage}
            activeColorSection={activeColorSection}
            onColorSectionSelect={onColorSectionSelect}
            activePage={activePage}
            activeComponentId={activeComponentId}
            filteredCatalog={filteredCatalog}
            onQueryChange={onQueryChange}
            onSelect={onSelect}
            query={query}
            visibleCategories={visibleCategories}
          />
        </SheetContent>
      </Sheet>
    </>
  );
}

function SidebarContents({
  isColorSchemePage,
  activeColorSection,
  onColorSectionSelect,
  activePage,
  activeComponentId,
  filteredCatalog,
  onQueryChange,
  onSelect,
  query,
  visibleCategories,
}: {
  isColorSchemePage: boolean;
  activeColorSection: string;
  onColorSectionSelect: (id: string) => void;
  activePage: PreviewTab;
  activeComponentId: string;
  filteredCatalog: CatalogItem[];
  onQueryChange: (query: string) => void;
  onSelect: () => void;
  query: string;
  visibleCategories: { category: string; items: CatalogItem[] }[];
}) {
  return (
    <div className="sidebar-content">
      <div className="sidebar-heading">
        <p className="toc-title">
          (00) {isColorSchemePage ? "컬러 스킴" : "컴포넌트"}
        </p>
        {!isColorSchemePage && (
          <Badge variant="secondary">{filteredCatalog.length}</Badge>
        )}
      </div>
      {isColorSchemePage && (
        <nav className="color-toc" aria-label="컬러 스킴 목차">
          {colorSchemeSections.map((section, index) => (
            <Button
              key={section.id}
              className="toc-link"
              variant="ghost"
              aria-current={
                activeColorSection === section.id ? "location" : undefined
              }
              onClick={() => onColorSectionSelect(section.id)}
            >
              <span>({String(index + 1).padStart(2, "0")})</span>
              {section.label}
            </Button>
          ))}
        </nav>
      )}

      {!isColorSchemePage && (
        <>
          <div className="search-box">
            <Search aria-hidden="true" className="search-icon" />
            <Input
              aria-label="컴포넌트 또는 API 검색"
              className="search-input"
              onChange={(event) => onQueryChange(event.target.value)}
              placeholder="컴포넌트·API 검색"
              type="search"
              value={query}
            />
            <kbd>/</kbd>
          </div>

          <ScrollArea className="component-navigation">
            <nav
              aria-label="컴포넌트 탐색"
              className="component-navigation-content"
            >
              {visibleCategories.length === 0 ? (
                <p className="empty-search">검색 결과가 없습니다.</p>
              ) : (
                visibleCategories.map((group, index) => (
                  <div className="navigation-group" key={group.category}>
                    <h2>
                      ({String(index + 1).padStart(2, "0")}) {group.category}
                    </h2>
                    {group.items.map((item) => {
                      const isActive = activeComponentId === item.id;

                      return (
                        <Button
                          asChild
                          aria-current={isActive ? "page" : undefined}
                          className={
                            isActive
                              ? "component-link active"
                              : "component-link"
                          }
                          key={item.id}
                          size="sm"
                          variant={isActive ? "secondary" : "ghost"}
                        >
                          <Link
                            onClick={onSelect}
                            to={`/components/${item.id}/${activePage}`}
                          >
                            <span>{item.name}</span>
                            <code>{item.api[0]}</code>
                          </Link>
                        </Button>
                      );
                    })}
                  </div>
                ))
              )}
            </nav>
          </ScrollArea>
        </>
      )}

      <footer className="sidebar-footer">
        <span className="status-dot" />
        <span>Design system · v{libraryVersion}</span>
      </footer>
    </div>
  );
}

function ApiSidebar({ component }: { component: CatalogItem }) {
  const apiSource = repositoryUrl + component.source;
  const sampleSource = repositoryUrl + component.sample;
  const apiSourceLabel =
    component.module === "cmp-ui" ? "API 원문 보기" : "API 사용 예제 보기";

  return (
    <aside className="api-sidebar">
      <div className="api-sidebar-heading">
        <Command aria-hidden="true" className="api-heading-icon" />
        <h2>API 참고</h2>
      </div>
      <p className="api-sidebar-description">
        미리보기 컴포넌트와 연결된 공개 API입니다.
      </p>
      <div className="api-module">
        <span>MODULE</span>
        <code>{component.module}</code>
      </div>
      <div className="api-module">
        <span>PACKAGE</span>
        <code>{component.packageName}</code>
      </div>
      <div className="api-name-list">
        <p>API</p>
        {component.api.map((name) => (
          <Badge key={name} variant="outline">
            {name}
          </Badge>
        ))}
      </div>
      <Button asChild className="api-source-link" size="sm" variant="ghost">
        <a href={apiSource} rel="noreferrer" target="_blank">
          {apiSourceLabel} <ArrowUpRight aria-hidden="true" />
        </a>
      </Button>
      <Button asChild className="sample-source-link" size="sm" variant="ghost">
        <a href={sampleSource} rel="noreferrer" target="_blank">
          Compose 사용 예제 <ArrowUpRight aria-hidden="true" />
        </a>
      </Button>
      <div className="api-note">
        <Info aria-hidden="true" />
        <p>
          {component.module === "cmp-ui"
            ? "함수 시그니처와 KDoc은 원본 Kotlin API에서 확인할 수 있습니다."
            : "Compose API의 적용 예제를 열어 확인할 수 있습니다."}
        </p>
      </div>
    </aside>
  );
}

function ApiDocumentation({ component }: { component: CatalogItem }) {
  const apiSource = repositoryUrl + component.source;
  const sampleSource = repositoryUrl + component.sample;
  const apiSourceLabel =
    component.module === "cmp-ui" ? "Kotlin API 원문" : "Compose API 사용 예제";
  const documentation = typedApiDocs[component.id] ?? [];

  return (
    <div className="api-documentation">
      <p className="panel-kicker">API REFERENCE</p>
      <h2>{component.name} API</h2>
      <p className="panel-description">{component.description}</p>
      <Card className="api-detail-card">
        <span className="api-detail-label">PACKAGE</span>
        <code>{component.packageName}</code>
      </Card>
      <Card className="api-detail-card">
        <span className="api-detail-label">PUBLIC API</span>
        <div className="api-detail-names">
          {component.api.map((name) => (
            <Badge key={name} variant="outline">
              {name}
            </Badge>
          ))}
        </div>
      </Card>
      {documentation.map((api) => (
        <Card className="api-reference-card" key={api.name}>
          <h3>{api.name}</h3>
          {api.signature ? (
            <pre className="api-signature">
              <code>{highlightKotlin(api.signature)}</code>
            </pre>
          ) : null}
          {api.description ? <p>{api.description}</p> : null}
          {api.parameters.length > 0 ? (
            <dl className="api-parameters">
              {api.parameters.map((parameter) => (
                <Fragment key={parameter.name}>
                  <dt>
                    <code>{parameter.name}</code>
                  </dt>
                  <dd>{parameter.description}</dd>
                </Fragment>
              ))}
            </dl>
          ) : null}
        </Card>
      ))}
      <Card className="api-detail-card">
        <span className="api-detail-label">MODULE</span>
        <code>{component.module}</code>
      </Card>
      <div className="api-links">
        <Button asChild size="sm">
          <a href={apiSource} rel="noreferrer" target="_blank">
            {apiSourceLabel} <ArrowUpRight aria-hidden="true" />
          </a>
        </Button>
        <Button asChild size="sm" variant="secondary">
          <a href={sampleSource} rel="noreferrer" target="_blank">
            샘플 사용 코드 <ArrowUpRight aria-hidden="true" />
          </a>
        </Button>
      </div>
    </div>
  );
}

const rootElement = document.getElementById("root");
if (!rootElement) throw new Error("React root element was not found.");

createRoot(rootElement).render(
  <StrictMode>
    <App />
  </StrictMode>,
);
