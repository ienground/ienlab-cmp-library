"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import * as React from "react";
import { cn } from "cn";

type IenSkeletonTextPattern =
  | "OneRowTypeA"
  | "OneRowTypeB"
  | "OneRowTypeC"
  | "RightOneRowTypeA"
  | "RightOneRowTypeB"
  | "RightOneRowTypeC"
  | "RightOneRowTypeD"
  | "RightOneRowTypeE"
  | "TwoRowTypeA"
  | "TwoRowTypeB"
  | "TwoRowTypeC"
  | "TwoRowTypeD"
  | "TwoRowTypeE"
  | "TwoRowTypeF"
  | "TwoRowTypeG"
  | "RightTwoRowTypeA"
  | "RightTwoRowTypeB"
  | "RightTwoRowTypeC"
  | "RightTwoRowTypeD"
  | "RightTwoRowTypeE"
  | "ThreeRowTypeA"
  | "ThreeRowTypeB"
  | "ThreeRowTypeC"
  | "ThreeRowTypeD"
  | "ThreeRowTypeE"
  | "ThreeRowTypeF";
type IenSkeletonPattern =
  | "TopList"
  | "TopListWithIcon"
  | "AmountTopList"
  | "AmountTopListWithIcon"
  | "SubtitleList"
  | "SubtitleListWithIcon"
  | "ListOnly"
  | "ListWithIconOnly"
  | "CardOnly"
  | { type: "ListRowTexts"; variant: IenSkeletonTextPattern };
type IenSkeletonElement =
  | { type: "Title" | "Subtitle" | "List" | "ListWithIcon" | "Card" }
  | { type: "ListRowTexts"; variant: IenSkeletonTextPattern }
  | {
      type: "Block";
      width?: string | number;
      height?: string | number;
      radius?: number | string;
    }
  | { type: "Spacer"; width?: string | number; height: string | number }
  | {
      type: "Row" | "Column" | "Box";
      elements: IenSkeletonElement[];
      className?: string;
    };
type IenSkeletonRepeat = number | "Infinite" | { type: "Count"; value: number };
type SkeletonProps = Omit<React.ComponentProps<"div">, "pattern"> & {
  width?: string | number;
  height?: string | number;
  radius?: "none" | "small" | "default" | "large" | "full" | number;
  pattern?: "block" | "text" | "avatar" | "card" | IenSkeletonPattern;
  custom?: React.ReactNode | IenSkeletonElement[];
  repeatLastItemCount?: IenSkeletonRepeat;
  play?: boolean | "show" | "hide" | "Show" | "Hide";
  background?:
    "white" | "grey" | "greyOpacity100" | "White" | "Grey" | "GreyOpacity100";
};

const SkeletonMotionEnabled = React.createContext(true);
const SkeletonBackgroundClass = React.createContext("bg-ien-surface-variant");
const radiusClasses = {
  none: "rounded-ien-none",
  small: "rounded-ien-sm",
  default: "rounded-ien-default",
  large: "rounded-ien-lg",
  full: "rounded-ien-full",
} as const;
const backgroundClasses = {
  white: "bg-ien-surface-raised",
  grey: "bg-ien-surface-variant",
  greyOpacity100: "bg-ien-text-primary/5",
} as const;

function getRepeatCount(repeat: IenSkeletonRepeat = 3) {
  if (repeat === "Infinite") return 30;
  const value = typeof repeat === "number" ? repeat : repeat.value;
  return Math.max(1, Math.floor(value));
}

function SkeletonBlock({
  className,
  width,
  height,
  radius,
  index = 0,
  style,
  ...props
}: React.ComponentProps<"span"> & {
  width?: string | number;
  height?: string | number;
  radius?: number | string;
  index?: number;
}) {
  const motionEnabled = React.useContext(SkeletonMotionEnabled);
  const backgroundClass = React.useContext(SkeletonBackgroundClass);
  return (
    <span
      aria-hidden="true"
      className={cn(
        "block",
        motionEnabled &&
          "animate-[ien-skeleton-wave_1200ms_ease-in-out_infinite] motion-reduce:animate-none",
        backgroundClass,
        className,
      )}
      style={{
        width,
        height,
        borderRadius: radius,
        animationDelay: motionEnabled ? `${-index * 149}ms` : undefined,
        ...style,
      }}
      {...props}
    />
  );
}

function SkeletonTextRows({
  variant = "OneRowTypeA",
  index = 0,
}: {
  variant?: IenSkeletonTextPattern;
  index?: number;
}) {
  const rows = variant.startsWith("ThreeRow")
    ? 3
    : variant.startsWith("TwoRow")
      ? 2
      : 1;
  const isRightAligned = variant.startsWith("Right");
  const widths = ["w-[70%]", "w-[55%]", "w-[40%]"];
  return (
    <span
      className={cn(
        "grid w-full gap-ien-xs",
        isRightAligned && "justify-items-end",
      )}
    >
      {Array.from({ length: rows }, (_, row) => (
        <SkeletonBlock
          key={row}
          index={index + row}
          height="var(--ien-spacing-md)"
          className={widths[row]}
        />
      ))}
    </span>
  );
}

function renderElement(
  element: IenSkeletonElement,
  index: number,
): React.ReactNode {
  switch (element.type) {
    case "Title":
      return (
        <SkeletonBlock
          index={index}
          width="42%"
          height="var(--ien-spacing-xl)"
        />
      );
    case "Subtitle":
      return (
        <SkeletonBlock
          index={index}
          width="34%"
          height="var(--ien-spacing-md)"
        />
      );
    case "List":
      return <SkeletonBlock index={index} width="86%" height="3rem" />;
    case "ListWithIcon":
      return (
        <span className="flex h-14 w-full items-center gap-ien-md">
          <SkeletonBlock
            index={index}
            width="2.5rem"
            height="2.5rem"
            radius="9999px"
          />
          <span className="grid flex-1 gap-ien-xs">
            <SkeletonBlock
              index={index + 1}
              width="64%"
              height="var(--ien-spacing-md)"
            />
            <SkeletonBlock
              index={index + 2}
              width="38%"
              height="var(--ien-spacing-sm)"
            />
          </span>
        </span>
      );
    case "Card":
      return (
        <SkeletonBlock
          index={index}
          height="8.25rem"
          radius="var(--ien-radius-default)"
        />
      );
    case "ListRowTexts":
      return <SkeletonTextRows index={index} variant={element.variant} />;
    case "Block":
      return (
        <SkeletonBlock
          index={index}
          width={element.width}
          height={element.height ?? "var(--ien-spacing-md)"}
          radius={element.radius}
        />
      );
    case "Spacer":
      return (
        <span
          aria-hidden="true"
          style={{ width: element.width, height: element.height }}
        />
      );
    case "Row":
      return (
        <span
          className={cn(
            "flex w-full items-start gap-ien-sm",
            element.className,
          )}
        >
          {element.elements.map((child, childIndex) => (
            <React.Fragment key={childIndex}>
              {renderElement(child, index + childIndex + 1)}
            </React.Fragment>
          ))}
        </span>
      );
    case "Column":
      return (
        <span className={cn("grid w-full gap-ien-sm", element.className)}>
          {element.elements.map((child, childIndex) => (
            <React.Fragment key={childIndex}>
              {renderElement(child, index + childIndex + 1)}
            </React.Fragment>
          ))}
        </span>
      );
    case "Box":
      return (
        <span
          className={cn(
            "grid w-full [&>*]:col-start-1 [&>*]:row-start-1",
            element.className,
          )}
        >
          {element.elements.map((child, childIndex) => (
            <React.Fragment key={childIndex}>
              {renderElement(child, index + childIndex + 1)}
            </React.Fragment>
          ))}
        </span>
      );
  }
}

function isSkeletonElements(
  value: SkeletonProps["custom"],
): value is IenSkeletonElement[] {
  return (
    Array.isArray(value) &&
    value.every(
      (item) => typeof item === "object" && item !== null && "type" in item,
    )
  );
}

function Skeleton({
  className,
  width,
  height,
  radius = "small",
  pattern = "block",
  custom,
  repeatLastItemCount = 3,
  play = true,
  background = "grey",
  style,
  ...props
}: SkeletonProps) {
  const motionEnabled = React.useContext(SkeletonMotionEnabled);
  if (play === false || play === "hide" || play === "Hide") return null;
  const repeatCount = getRepeatCount(repeatLastItemCount);
  const patternType = typeof pattern === "string" ? pattern : "text";
  const textVariant = typeof pattern === "string" ? undefined : pattern.variant;
  const normalizedBackground = background.toLowerCase();
  const radiusClass =
    typeof radius === "number" ? undefined : radiusClasses[radius];
  const dimensions: React.CSSProperties = {
    width,
    height,
    borderRadius: typeof radius === "number" ? radius : undefined,
    ...style,
  };
  const backgroundClass =
    normalizedBackground === "greyopacity100"
      ? backgroundClasses.greyOpacity100
      : backgroundClasses[normalizedBackground as "white" | "grey"];
  const rootClass = cn("grid gap-ien-sm", className);
  const renderRows = (withIcon: boolean) =>
    Array.from({ length: repeatCount }, (_, index) =>
      withIcon ? (
        <React.Fragment key={index}>
          {renderElement({ type: "ListWithIcon" }, index * 3)}
        </React.Fragment>
      ) : (
        <SkeletonBlock
          key={index}
          index={index}
          width={index % 3 === 1 ? "78%" : index % 3 === 2 ? "86%" : "92%"}
          height="3rem"
        />
      ),
    );

  const customElements = isSkeletonElements(custom) ? custom : undefined;
  const repeatedCustomElements = customElements?.length
    ? [
        ...customElements.slice(0, -1),
        ...Array.from(
          { length: repeatCount },
          () => customElements[customElements.length - 1],
        ),
      ]
    : customElements;
  const content: React.ReactNode = customElements
    ? repeatedCustomElements?.map((element, index) => (
        <React.Fragment key={index}>
          {renderElement(element, index)}
        </React.Fragment>
      ))
    : ((custom as React.ReactNode) ?? null);
  if (custom)
    return (
      <SkeletonBackgroundClass.Provider value={backgroundClass}>
        <div
          data-slot="skeleton"
          aria-hidden="true"
          className={cn(rootClass, radiusClass)}
          style={dimensions}
          {...props}
        >
          {content}
        </div>
      </SkeletonBackgroundClass.Provider>
    );
  if (height != null || patternType === "block")
    return (
      <SkeletonBackgroundClass.Provider value={backgroundClass}>
        <div
          data-slot="skeleton"
          aria-hidden="true"
          className={cn(
            "bg-ien-surface-variant",
            motionEnabled &&
              "animate-[ien-skeleton-wave_1200ms_ease-in-out_infinite] motion-reduce:animate-none",
            radiusClass,
            backgroundClass,
            className,
          )}
          style={dimensions}
          {...props}
        />
      </SkeletonBackgroundClass.Provider>
    );

  let patternContent: React.ReactNode;
  if (patternType === "text")
    patternContent = Array.from({ length: repeatCount }, (_, index) => (
      <SkeletonTextRows key={index} index={index * 3} variant={textVariant} />
    ));
  else if (patternType === "avatar")
    patternContent = (
      <span className="flex items-center gap-ien-sm">
        <SkeletonBlock
          width="2.5rem"
          height="2.5rem"
          radius="9999px"
          index={0}
        />
        <span className="grid flex-1 gap-ien-xs">
          <SkeletonBlock width="66%" height="var(--ien-spacing-md)" index={1} />
          <SkeletonBlock width="50%" height="var(--ien-spacing-sm)" index={2} />
        </span>
      </span>
    );
  else if (patternType === "card" || patternType === "CardOnly")
    patternContent = (
      <span className="grid gap-ien-sm rounded-ien-default border border-ien-border p-ien-md">
        <SkeletonBlock height="8.25rem" index={0} />
        <SkeletonBlock width="50%" height="var(--ien-spacing-lg)" index={1} />
        <SkeletonBlock width="80%" height="var(--ien-spacing-md)" index={2} />
      </span>
    );
  else {
    const onlyList =
      patternType === "ListOnly" || patternType === "ListWithIconOnly";
    const subtitleOnly = patternType.startsWith("Subtitle");
    const amount = patternType.startsWith("Amount");
    const withIcon =
      patternType.endsWith("WithIcon") || patternType === "ListWithIconOnly";
    patternContent = (
      <>
        {!onlyList && !subtitleOnly && (
          <SkeletonBlock width="42%" height="var(--ien-spacing-xl)" index={0} />
        )}
        {(subtitleOnly || amount) && (
          <SkeletonBlock width="34%" height="var(--ien-spacing-md)" index={1} />
        )}
        {(amount || !onlyList) && (
          <span aria-hidden="true" className="h-ien-xs" />
        )}
        {renderRows(withIcon)}
      </>
    );
  }

  return (
    <SkeletonBackgroundClass.Provider value={backgroundClass}>
      <div
        data-slot="skeleton"
        aria-hidden="true"
        className={rootClass}
        style={dimensions}
        {...props}
      >
        {patternContent}
      </div>
    </SkeletonBackgroundClass.Provider>
  );
}

type IenSkeletonProps = Omit<SkeletonProps, "pattern"> & {
  pattern?: IenSkeletonPattern | "block" | "text" | "avatar" | "card";
};
function IenSkeleton({ pattern = "TopList", ...props }: IenSkeletonProps) {
  const shouldShow =
    props.play !== false && props.play !== "hide" && props.play !== "Hide";
  if (!shouldShow) return null;
  return (
    <div className="w-full" role="status" aria-label="로딩 중" aria-busy="true">
      <Skeleton pattern={pattern} {...props} />
    </div>
  );
}

function IenSkeletonMotionGroup({
  className,
  animationIndex = 0,
  children,
  style,
  ...props
}: React.ComponentProps<"div"> & { animationIndex?: number }) {
  return (
    <SkeletonMotionEnabled.Provider value={false}>
      <div
        className={cn(
          "animate-[ien-skeleton-wave_1200ms_ease-in-out_infinite] motion-reduce:animate-none",
          className,
        )}
        style={{ ...style, animationDelay: `${-animationIndex * 149}ms` }}
        {...props}
      >
        {children}
      </div>
    </SkeletonMotionEnabled.Provider>
  );
}

export type {
  SkeletonProps,
  IenSkeletonProps,
  IenSkeletonPattern,
  IenSkeletonTextPattern,
  IenSkeletonElement,
  IenSkeletonRepeat,
};
export { Skeleton, IenSkeleton, IenSkeletonMotionGroup };
