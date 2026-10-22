"use client";

import * as React from "react";
import { ChevronRight } from "lucide-react";
import { cn } from "@/lib/ien-utils";

export type IenListRowBorder = "indented" | "none";
export type IenListRowDisabledStyle = "type1" | "type2";
export type IenListRowPadding =
  "none" | "extraSmall" | "small" | "medium" | "large" | "xLarge";
export type IenListRowAlignment = "top" | "center";
export type IenListRowLoaderType = "square" | "circle" | "bar";
export type IenListRowAssetShape =
  "original" | "squircle" | "card" | "square" | "circle";
export type IenListRowAssetSize = "xSmall" | "small" | "medium";
export type IenListRowTextsType =
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

const paddingValue: Record<IenListRowPadding, string> = {
  none: "0",
  extraSmall: "4px",
  small: "8px",
  medium: "12px",
  large: "16px",
  xLarge: "24px",
};
const assetSize: Record<IenListRowAssetSize, string> = {
  xSmall: "32px",
  small: "40px",
  medium: "52px",
};

export interface IenListRowProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "content" | "onClick"
> {
  title?: string;
  subtitle?: string;
  contents?: React.ReactNode;
  left?: React.ReactNode;
  right?: React.ReactNode;
  leading?: React.ReactNode;
  trailing?: React.ReactNode;
  enabled?: boolean;
  disabled?: boolean;
  disabledStyle?: IenListRowDisabledStyle;
  selected?: boolean;
  border?: IenListRowBorder;
  verticalPadding?: IenListRowPadding;
  horizontalPadding?: IenListRowPadding;
  padding?: IenListRowPadding;
  leftAlignment?: IenListRowAlignment;
  rightAlignment?: IenListRowAlignment;
  withArrow?: boolean;
  withTouchEffect?: boolean;
  touchEffectColor?: string;
  onClick?: () => void;
}

export function IenListRow({
  title,
  subtitle,
  contents,
  left,
  right,
  leading,
  trailing,
  enabled = true,
  disabled = false,
  disabledStyle = "type1",
  selected = false,
  border = "indented",
  verticalPadding,
  horizontalPadding,
  padding,
  leftAlignment = "center",
  rightAlignment = "center",
  withArrow = false,
  withTouchEffect = false,
  touchEffectColor = "var(--ien-surface-variant)",
  onClick,
  onKeyDown,
  onPointerDown,
  onPointerUp,
  onPointerLeave,
  onPointerCancel,
  onBlur,
  className,
  style,
  ...props
}: IenListRowProps) {
  const [pressed, setPressed] = React.useState(false);
  const isDisabled = disabled || !enabled;
  const effectiveVerticalPadding = verticalPadding ?? padding ?? "large";
  const effectiveHorizontalPadding = horizontalPadding ?? padding ?? "large";
  const content =
    contents ??
    (title != null ? (
      <IenListRowTexts
        type={subtitle == null ? "OneRowTypeA" : "TwoRowTypeA"}
        top={title}
        bottom={subtitle}
        topColor={selected ? "var(--ien-brand)" : undefined}
      />
    ) : null);
  const clickable = onClick != null || withTouchEffect;
  return (
    <div
      role={clickable ? "button" : undefined}
      tabIndex={clickable && !isDisabled ? 0 : undefined}
      aria-pressed={onClick ? selected : undefined}
      aria-disabled={isDisabled || undefined}
      className={cn(
        "group/ien-list-row relative w-full",
        disabledStyle === "type2" && isDisabled && "bg-ien-surface-weak",
        clickable &&
          !isDisabled &&
          "cursor-pointer hover:bg-ien-surface-weak active:scale-[var(--ien-button-pressed-scale)]",
        isDisabled && "cursor-default",
        className,
      )}
      style={{
        backgroundColor:
          pressed && clickable && !isDisabled ? touchEffectColor : undefined,
        ...style,
      }}
      onPointerDown={(event) => {
        setPressed(true);
        onPointerDown?.(event);
      }}
      onPointerUp={(event) => {
        setPressed(false);
        onPointerUp?.(event);
      }}
      onPointerLeave={(event) => {
        setPressed(false);
        onPointerLeave?.(event);
      }}
      onPointerCancel={(event) => {
        setPressed(false);
        onPointerCancel?.(event);
      }}
      onBlur={(event) => {
        setPressed(false);
        onBlur?.(event);
      }}
      onClick={isDisabled ? undefined : onClick}
      onKeyDown={(event) => {
        onKeyDown?.(event);
        if (
          !event.defaultPrevented &&
          clickable &&
          !isDisabled &&
          (event.key === "Enter" || event.key === " ")
        ) {
          event.preventDefault();
          onClick?.();
        }
      }}
      {...props}
    >
      {border === "indented" && (
        <div
          aria-hidden
          className="h-[var(--ien-stroke-hairline)] bg-ien-border"
          style={{
            marginLeft: `calc(${paddingValue[effectiveHorizontalPadding]} + var(--ien-spacing-lg))`,
          }}
        />
      )}
      <div
        className={cn(
          "flex min-h-[var(--ien-state-minimum-touch-target)] w-full items-center gap-ien-sm",
          isDisabled && "opacity-[.45]",
        )}
        style={{
          padding: `${paddingValue[effectiveVerticalPadding]} ${paddingValue[effectiveHorizontalPadding]}`,
        }}
      >
        {left ?? leading}
        <div
          className={cn(
            "min-w-0 flex-1",
            leftAlignment === "top" && "self-start",
          )}
        >
          {content}
        </div>
        {(right ?? trailing) != null || withArrow ? (
          <div
            className={cn(
              "flex shrink-0 items-center gap-ien-xs text-ien-text-tertiary",
              rightAlignment === "top" && "self-start",
            )}
          >
            {right ?? trailing}
            {withArrow && <ChevronRight aria-hidden className="size-4" />}
          </div>
        ) : null}
      </div>
    </div>
  );
}

export interface IenListRowTextsProps extends React.ComponentPropsWithRef<"div"> {
  top: React.ReactNode;
  type?: IenListRowTextsType;
  middle?: React.ReactNode;
  bottom?: React.ReactNode;
  topColor?: string;
  middleColor?: string;
  bottomColor?: string;
  maxLines?: number;
}

export function IenListRowTexts({
  top,
  type = "OneRowTypeA",
  middle,
  bottom,
  topColor,
  middleColor,
  bottomColor,
  maxLines = 1,
  className,
  ...props
}: IenListRowTextsProps) {
  const right = type.startsWith("Right");
  const three = type.startsWith("Three");
  const rows =
    type.startsWith("Two") || type.startsWith("RightTwo") ? 2 : three ? 3 : 1;
  const emphasis = [
    "OneRowTypeB",
    "OneRowTypeC",
    "TwoRowTypeD",
    "TwoRowTypeE",
    "ThreeRowTypeC",
    "ThreeRowTypeD",
  ].includes(type);
  const strong = type === "TwoRowTypeG";
  const primaryBottom = [
    "TwoRowTypeC",
    "TwoRowTypeF",
    "RightTwoRowTypeD",
    "RightTwoRowTypeE",
  ].includes(type);
  const topStyle: React.CSSProperties =
    maxLines > 0
      ? {
          display: "-webkit-box",
          WebkitLineClamp: maxLines,
          WebkitBoxOrient: "vertical",
          overflow: "hidden",
        }
      : {};
  const middleStyle: React.CSSProperties = topStyle;
  const bottomStyle: React.CSSProperties = {
    display: "-webkit-box",
    WebkitLineClamp: 2,
    WebkitBoxOrient: "vertical",
    overflow: "hidden",
  };
  return (
    <div
      className={cn(
        "flex min-w-0 flex-col gap-ien-xxxs",
        right && "items-end text-right",
        className,
      )}
      {...props}
    >
      <div
        className={cn(
          "text-ien-body1",
          emphasis && "text-ien-label1",
          strong && "text-ien-title2 font-bold",
          !topColor &&
            (right || type === "OneRowTypeC") &&
            "text-ien-text-secondary",
          !topColor &&
            !right &&
            type !== "OneRowTypeC" &&
            "text-ien-text-primary",
        )}
        style={{ ...topStyle, color: topColor }}
      >
        {top}
      </div>
      {rows >= 3 && middle != null && (
        <div
          className="text-ien-body2 text-ien-text-secondary"
          style={{ ...middleStyle, color: middleColor }}
        >
          {middle}
        </div>
      )}
      {rows >= 2 && bottom != null && (
        <div
          className={cn(
            "text-ien-caption",
            ["TwoRowTypeF", "ThreeRowTypeF"].includes(type) &&
              "text-ien-label2",
            !bottomColor && primaryBottom && "text-ien-text-primary",
            !bottomColor && !primaryBottom && "text-ien-text-secondary",
          )}
          style={{ ...bottomStyle, color: bottomColor }}
        >
          {bottom}
        </div>
      )}
    </div>
  );
}

export interface IenListRowAssetTextProps extends React.ComponentPropsWithRef<"span"> {
  text: string;
  shape?: IenListRowAssetShape;
  size?: IenListRowAssetSize;
  backgroundColor?: string;
  contentColor?: string;
}

function assetShapeClass(shape: IenListRowAssetShape): string {
  return shape === "circle"
    ? "rounded-full"
    : shape === "square"
      ? "rounded-ien-sm"
      : shape === "card"
        ? "rounded-ien-default"
        : shape === "original"
          ? "rounded-none"
          : "rounded-ien-lg";
}

export function IenListRowAssetText({
  text,
  shape = "squircle",
  size = "medium",
  backgroundColor = "var(--ien-surface-weak)",
  contentColor = "var(--ien-text-primary)",
  className,
  ...props
}: IenListRowAssetTextProps) {
  return (
    <span
      className={cn(
        "grid shrink-0 place-items-center text-ien-label2 font-bold",
        assetShapeClass(shape),
        className,
      )}
      style={{
        width: assetSize[size],
        height: assetSize[size],
        backgroundColor,
        color: contentColor,
      }}
      {...props}
    >
      {text}
    </span>
  );
}

export interface IenListRowAssetFrameProps extends React.ComponentPropsWithRef<"span"> {
  shape?: IenListRowAssetShape;
  size?: IenListRowAssetSize;
  backgroundColor?: string;
}

export function IenListRowAssetFrame({
  shape = "squircle",
  size = "medium",
  backgroundColor = "var(--ien-surface-weak)",
  className,
  ...props
}: IenListRowAssetFrameProps) {
  return (
    <span
      className={cn(
        "grid shrink-0 place-items-center overflow-hidden",
        assetShapeClass(shape),
        className,
      )}
      style={{
        width: assetSize[size],
        height: assetSize[size],
        backgroundColor: shape === "original" ? "transparent" : backgroundColor,
      }}
      {...props}
    />
  );
}

export interface IenListRowLoaderProps extends React.ComponentPropsWithRef<"div"> {
  type?: IenListRowLoaderType;
  verticalPadding?: IenListRowPadding;
}

export function IenListRowLoader({
  type = "square",
  verticalPadding = "medium",
  className,
  ...props
}: IenListRowLoaderProps) {
  return (
    <div
      role="status"
      aria-label="불러오는 중"
      className={cn(
        "flex min-h-12 w-full items-center gap-ien-sm px-ien-md",
        className,
      )}
      style={{
        paddingTop: paddingValue[verticalPadding],
        paddingBottom: paddingValue[verticalPadding],
      }}
      {...props}
    >
      {type !== "bar" && (
        <span
          className={cn(
            "size-11 animate-pulse bg-ien-surface-variant",
            type === "circle" ? "rounded-full" : "rounded-ien-lg",
          )}
        />
      )}
      <span className="flex flex-1 flex-col gap-ien-xs">
        <span className="h-4 w-[70%] animate-pulse rounded-full bg-ien-surface-variant" />
        <span className="h-3.5 w-[45%] animate-pulse rounded-full bg-ien-surface-variant" />
      </span>
    </div>
  );
}

export {
  IenListRow as ListRow,
  IenListRowTexts as ListRowTexts,
  IenListRowAssetText as ListRowAssetText,
  IenListRowAssetFrame as ListRowAssetFrame,
  IenListRowLoader as ListRowLoader,
};
