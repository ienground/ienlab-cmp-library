"use client";

import { cn } from "cn";
import { IconChevronLeft, IconChevronRight } from "@tabler/icons-react";
import { ScrollArea as ScrollAreaPrimitive } from "radix-ui";
import * as React from "react";

import { Button } from "@/components/ui/button";
import { ScrollBar } from "@/components/ui/scroll-area";

// deltaMode 가 줄 단위(1)일 때 한 줄을 몇 px 로 볼지 — Firefox 휠 기본값
const WHEEL_LINE_HEIGHT = 16;

type XScrollAreaContextValue = {
  viewportId: string;
  setViewport: (node: HTMLDivElement | null) => void;
  canScrollPrev: boolean;
  canScrollNext: boolean;
  scrollPrev: () => void;
  scrollNext: () => void;
};

const XScrollAreaContext = React.createContext<
  XScrollAreaContextValue | undefined
>(undefined);

function useXScrollArea() {
  const context = React.useContext(XScrollAreaContext);
  if (!context) {
    throw new Error("useXScrollArea는 <XScrollArea /> 안에서 사용해야 합니다.");
  }
  return context;
}

// 페이드 폭은 뷰포트 scroll-padding 에 걸어 둔 --x-scroll-fade 의 계산값으로 읽는다
function readFadeSize(viewport: HTMLElement) {
  return Number.parseFloat(getComputedStyle(viewport).scrollPaddingLeft) || 0;
}

function XScrollArea({
  className,
  wheel = true,
  fade = true,
  children,
  ...props
}: React.ComponentProps<"div"> & {
  /** 포인터가 스크롤 영역 위에 있을 때 세로 휠을 가로 스크롤로 바꾼다 */
  wheel?: boolean;
  /** 더 스크롤할 내용이 있는 쪽 끝을 흐리게 한다 */
  fade?: boolean;
}) {
  const viewportId = React.useId();
  const [viewport, setViewport] = React.useState<HTMLDivElement | null>(null);
  const [canScrollPrev, setCanScrollPrev] = React.useState(false);
  const [canScrollNext, setCanScrollNext] = React.useState(false);

  // 스크롤 위치·크기가 바뀔 때마다 내비 버튼 상태와 양끝까지 남은 거리를 맞춘다
  React.useEffect(() => {
    if (!viewport) return;
    let last = "";

    const update = () => {
      const max = viewport.scrollWidth - viewport.clientWidth;
      const left = viewport.scrollLeft;
      setCanScrollPrev(left > 1);
      setCanScrollNext(left < max - 1);
      // 페이드는 끝까지 남은 거리만큼만 — 끝에 닿을수록 줄어 툭 끊기지 않는다. 페이드 폭과의 min 은 CSS 가 해서
      // 폭을 클래스로 바꿔도 바로 반영된다. 절반 폭에서 잘라 두면 가운데를 지나는 동안 값이 그대로라
      // 하위 요소 스타일을 매 프레임 다시 계산하지 않는다
      const cap = viewport.clientWidth / 2;
      const start = Math.min(Math.max(left, 0), cap);
      const end = Math.min(Math.max(max - left, 0), cap);
      const next = `${start} ${end}`;
      if (next === last) return;
      last = next;
      viewport.style.setProperty("--x-scroll-start", `${start}px`);
      viewport.style.setProperty("--x-scroll-end", `${end}px`);
    };

    update();
    viewport.addEventListener("scroll", update, { passive: true });
    const observer = new ResizeObserver(update);
    observer.observe(viewport);
    // 뷰포트 첫 자식은 Radix 가 감싼 내용 상자 — 항목이 늘거나 이미지가 로드되면 크기가 바뀐다
    if (viewport.firstElementChild)
      observer.observe(viewport.firstElementChild);
    return () => {
      viewport.removeEventListener("scroll", update);
      observer.disconnect();
    };
  }, [viewport]);

  // 세로 휠을 가로 스크롤로 바꾼다. 끝에 닿으면 가로채지 않아 페이지 세로 스크롤이 이어진다
  React.useEffect(() => {
    if (!viewport || !wheel) return;
    // 위에 겹친 내비 버튼에서도 휠이 먹도록 루트에 건다
    const root =
      viewport.closest<HTMLElement>('[data-slot="x-scroll-area"]') ?? viewport;

    const onWheel = (event: WheelEvent) => {
      // 핀치 줌(ctrl)·Shift+휠·트랙패드 가로 스와이프는 브라우저 기본 동작에 맡긴다
      if (event.ctrlKey || event.shiftKey) return;
      if (Math.abs(event.deltaX) >= Math.abs(event.deltaY)) return;
      // 머리글처럼 루트 안이지만 스크롤 영역 밖인 곳에서는 바꾸지 않는다
      const rect = viewport.getBoundingClientRect();
      if (
        event.clientX < rect.left ||
        event.clientX > rect.right ||
        event.clientY < rect.top ||
        event.clientY > rect.bottom
      ) {
        return;
      }

      const unit =
        event.deltaMode === 1
          ? WHEEL_LINE_HEIGHT
          : event.deltaMode === 2
            ? viewport.clientWidth
            : 1;
      const delta = event.deltaY * unit;
      const max = viewport.scrollWidth - viewport.clientWidth;
      if (
        (delta < 0 && viewport.scrollLeft <= 0) ||
        (delta > 0 && viewport.scrollLeft >= max - 1)
      ) {
        return;
      }

      event.preventDefault();
      viewport.scrollLeft += delta;
    };

    // React onWheel 은 passive 라 preventDefault 가 안 먹는다 — 네이티브 리스너로 건다
    root.addEventListener("wheel", onWheel, { passive: false });
    return () => root.removeEventListener("wheel", onWheel);
  }, [viewport, wheel]);

  const scrollByPage = React.useCallback(
    (direction: -1 | 1) => {
      if (!viewport) return;
      // 한쪽 페이드에 걸쳐 있던 내용이 반대쪽 페이드 바로 안쪽으로 오게 넘긴다 — 건너뛰는 내용이 없다
      const fadeSize = readFadeSize(viewport);
      const page = Math.max(
        viewport.clientWidth - fadeSize * 2,
        viewport.clientWidth / 2,
      );
      const reduceMotion = window.matchMedia(
        "(prefers-reduced-motion: reduce)",
      ).matches;
      viewport.scrollBy({
        left: direction * page,
        behavior: reduceMotion ? "auto" : "smooth",
      });
    },
    [viewport],
  );

  const contextValue = React.useMemo<XScrollAreaContextValue>(
    () => ({
      viewportId,
      setViewport,
      canScrollPrev,
      canScrollNext,
      scrollPrev: () => scrollByPage(-1),
      scrollNext: () => scrollByPage(1),
    }),
    [viewportId, canScrollPrev, canScrollNext, scrollByPage],
  );

  return (
    <XScrollAreaContext.Provider value={contextValue}>
      <div
        data-slot="x-scroll-area"
        data-can-scroll-prev={canScrollPrev || undefined}
        data-can-scroll-next={canScrollNext || undefined}
        // min-w-0 — 그리드·flex 자식일 때 내용 폭에 밀려 부모를 넓히지 않고 줄어든다
        className={cn(
          "relative min-w-0",
          fade && "[--x-scroll-fade:calc(var(--spacing)*10)]",
          className,
        )}
        {...props}
      >
        {children}
      </div>
    </XScrollAreaContext.Provider>
  );
}

function XScrollAreaContent({
  className,
  ...props
}: React.ComponentProps<"div">) {
  const { viewportId, setViewport } = useXScrollArea();

  return (
    <ScrollAreaPrimitive.Root
      data-slot="x-scroll-area-content"
      className="relative rounded-[inherit] transition-[color,box-shadow] has-[>[data-slot=x-scroll-area-viewport]:focus-visible]:ring-[3px] has-[>[data-slot=x-scroll-area-viewport]:focus-visible]:ring-ring/50 motion-reduce:transition-none"
    >
      {/* 페이드는 mask 라 배경색과 무관하다. 포커스 링은 mask 에 잘리므로 Root 에 그린다 */}
      <ScrollAreaPrimitive.Viewport
        ref={setViewport}
        id={viewportId}
        data-slot="x-scroll-area-viewport"
        className="size-full scroll-px-(--x-scroll-fade) rounded-[inherit] mask-r-from-[calc(100%-min(var(--x-scroll-fade,0px),var(--x-scroll-end,0px)))] mask-l-from-[calc(100%-min(var(--x-scroll-fade,0px),var(--x-scroll-start,0px)))] outline-none"
      >
        <div className={cn("flex w-max gap-4 pb-3", className)} {...props} />
      </ScrollAreaPrimitive.Viewport>
      <ScrollBar orientation="horizontal" />
    </ScrollAreaPrimitive.Root>
  );
}

function XScrollAreaPrevious({
  className,
  variant = "ghost",
  size = "icon-sm",
  disabled,
  onClick,
  children,
  ...props
}: React.ComponentProps<typeof Button>) {
  const { viewportId, canScrollPrev, scrollPrev } = useXScrollArea();
  // 투명한 variant(ghost·line)만 겹친 내용 위에서 읽히도록 표면색을 깐다 — fill·weak 는 자기 배경을 쓴다
  const transparent = variant === "ghost" || variant === "line";

  return (
    <Button
      type="button"
      data-slot="x-scroll-area-previous"
      variant={variant}
      size={size}
      aria-controls={viewportId}
      // 끝에 닿아도 네이티브 disabled 는 쓰지 않는다 — 누르던 버튼에서 포커스가 body 로 빠지지 않게
      disabled={disabled}
      aria-disabled={!canScrollPrev || undefined}
      data-disabled={disabled || !canScrollPrev || undefined}
      className={cn(
        "absolute inset-y-0 left-2 z-10 my-auto rounded-full shadow-md ring-1 ring-foreground/5 data-disabled:pointer-events-none data-disabled:opacity-0 data-disabled:focus-visible:opacity-100",
        transparent && "bg-card",
        className,
      )}
      onClick={(event) => {
        onClick?.(event);
        if (!event.defaultPrevented && canScrollPrev) scrollPrev();
      }}
      {...props}
    >
      {children ?? (
        <>
          <IconChevronLeft />
          <span className="sr-only">이전</span>
        </>
      )}
    </Button>
  );
}

function XScrollAreaNext({
  className,
  variant = "ghost",
  size = "icon-sm",
  disabled,
  onClick,
  children,
  ...props
}: React.ComponentProps<typeof Button>) {
  const { viewportId, canScrollNext, scrollNext } = useXScrollArea();
  // 투명한 variant(ghost·line)만 겹친 내용 위에서 읽히도록 표면색을 깐다 — fill·weak 는 자기 배경을 쓴다
  const transparent = variant === "ghost" || variant === "line";

  return (
    <Button
      type="button"
      data-slot="x-scroll-area-next"
      variant={variant}
      size={size}
      aria-controls={viewportId}
      // 끝에 닿아도 네이티브 disabled 는 쓰지 않는다 — 누르던 버튼에서 포커스가 body 로 빠지지 않게
      disabled={disabled}
      aria-disabled={!canScrollNext || undefined}
      data-disabled={disabled || !canScrollNext || undefined}
      className={cn(
        "absolute inset-y-0 right-2 z-10 my-auto rounded-full shadow-md ring-1 ring-foreground/5 data-disabled:pointer-events-none data-disabled:opacity-0 data-disabled:focus-visible:opacity-100",
        transparent && "bg-card",
        className,
      )}
      onClick={(event) => {
        onClick?.(event);
        if (!event.defaultPrevented && canScrollNext) scrollNext();
      }}
      {...props}
    >
      {children ?? (
        <>
          <IconChevronRight />
          <span className="sr-only">다음</span>
        </>
      )}
    </Button>
  );
}

export {
  useXScrollArea,
  XScrollArea,
  XScrollAreaContent,
  XScrollAreaNext,
  XScrollAreaPrevious,
};
