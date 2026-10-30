"use client";

import * as React from "react";
import { ArrowRight, ChevronDown, ChevronRight, X } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type IenTopBarTitleAlignment = "start" | "center";
export type IenTopRightVerticalAlign = "center" | "end";
export type IenTopTitleSize = "default" | "large";
export type IenTopSubtitleSize = "small" | "medium" | "large";
export type IenTopSelectorType = "arrow" | "clear";
export type IenTextButtonVariant = "clear" | "arrow" | "underline";
export type IenTextButtonSize =
  "xSmall" | "small" | "medium" | "large" | "xLarge" | "xxLarge";
export interface IenButtonState {
  enabled?: boolean;
  loading?: boolean;
}

export interface IenTopSubtitleBadge {
  text: string;
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  variant?: "fill" | "weak" | "line";
}

export interface IenTopBarProps extends Omit<
  React.ComponentPropsWithRef<"header">,
  "title"
> {
  title: React.ReactNode;
  subtitle?: React.ReactNode;
  navigationIcon?: React.ReactNode;
  actions?: React.ReactNode;
  titleAlignment?: IenTopBarTitleAlignment;
  showDivider?: boolean;
  contentHeight?: number | string;
  containerColor?: string;
  isScrollTint?: boolean;
  isCenterAligned?: boolean;
  mode?: "static" | "expanded";
  contentPadding?: string;
  windowInsets?: "none" | "safe";
  floatingSlots?: boolean;
}

export function IenTopBar({
  title,
  subtitle,
  navigationIcon,
  actions,
  titleAlignment = "start",
  showDivider = false,
  contentHeight = 64,
  containerColor = "transparent",
  isScrollTint = false,
  isCenterAligned,
  mode = "static",
  contentPadding = "var(--ien-spacing-xxs) var(--ien-spacing-md)",
  windowInsets = "safe",
  floatingSlots = true,
  className,
  style,
  ...props
}: IenTopBarProps) {
  const centered = isCenterAligned ?? titleAlignment === "center";
  return (
    <header
      data-floating-slots={floatingSlots}
      className={cn(
        "relative flex w-full items-center gap-ien-sm text-ien-text-primary",
        showDivider && "border-b border-ien-border",
        isScrollTint && "bg-ien-surface/90 backdrop-blur",
        mode === "expanded" && "min-h-16",
        className,
      )}
      style={{
        minHeight: contentHeight,
        padding: contentPadding,
        paddingTop:
          windowInsets === "safe"
            ? "max(var(--ien-spacing-xxs), env(safe-area-inset-top))"
            : undefined,
        backgroundColor: containerColor,
        ...style,
      }}
      {...props}
    >
      {navigationIcon != null && (
        <div className="z-[1] flex shrink-0 items-center">{navigationIcon}</div>
      )}
      <div
        className={cn(
          "min-w-0",
          centered
            ? "absolute left-1/2 -translate-x-1/2 text-center"
            : "flex-1",
        )}
      >
        <div className="truncate text-ien-title3 font-semibold">{title}</div>
        {subtitle != null && (
          <div className="truncate text-ien-caption text-ien-text-secondary">
            {subtitle}
          </div>
        )}
      </div>
      {actions != null && (
        <div className="z-[1] ml-auto flex shrink-0 items-center gap-ien-xxs">
          {actions}
        </div>
      )}
    </header>
  );
}

export interface IenTopAppBarProps extends IenTopBarProps {
  navigation?: React.ReactNode;
  isCenterAligned?: boolean;
}

export function IenTopAppBar({
  navigation,
  navigationIcon,
  isCenterAligned = false,
  ...props
}: IenTopAppBarProps) {
  return (
    <IenTopBar
      navigationIcon={navigation ?? navigationIcon}
      isCenterAligned={isCenterAligned}
      {...props}
    />
  );
}

export type TopBarMode = "static" | "expanded";

export interface IenTopProps extends Omit<
  React.ComponentPropsWithRef<"header">,
  "title"
> {
  title: React.ReactNode;
  subtitle?: React.ReactNode;
  navigation?: React.ReactNode;
  actions?: React.ReactNode;
  upperGap?: number | string;
  lowerGap?: number | string;
  upper?: React.ReactNode;
  lower?: React.ReactNode;
  subtitleTop?: React.ReactNode;
  subtitleBottom?: React.ReactNode;
  right?: React.ReactNode;
  rightVerticalAlign?: IenTopRightVerticalAlign;
  contentPadding?: string;
}

export function IenTop({
  title,
  subtitle,
  navigation,
  actions,
  upperGap = "var(--ien-spacing-sm)",
  lowerGap = "var(--ien-spacing-sm)",
  upper,
  lower,
  subtitleTop,
  subtitleBottom,
  right,
  rightVerticalAlign = "center",
  contentPadding = "0 var(--ien-spacing-md)",
  className,
  style,
  ...props
}: IenTopProps) {
  return (
    <header
      className={cn("w-full text-ien-text-primary", className)}
      style={{
        padding: contentPadding,
        paddingTop: upperGap,
        paddingBottom: lowerGap,
        ...style,
      }}
      {...props}
    >
      {upper ?? navigation}
      <div
        className={cn(
          "flex w-full items-center gap-ien-md",
          upper != null || navigation != null ? "mt-ien-md" : "",
        )}
      >
        <div className="min-w-0 flex-1">
          {subtitleTop}
          <h1 className="text-ien-title1 font-bold">{title}</h1>
          {subtitleBottom ??
            (subtitle != null && (
              <p className="mt-ien-xxs text-ien-caption text-ien-text-secondary">
                {subtitle}
              </p>
            ))}
        </div>
        {right ??
          (actions != null && (
            <div
              className={cn(
                "flex shrink-0 items-center gap-ien-xs",
                rightVerticalAlign === "end" && "self-end",
              )}
            >
              {actions}
            </div>
          ))}
      </div>
      {lower != null && <div className="mt-ien-md">{lower}</div>}
    </header>
  );
}

export interface IenTopTitleParagraphProps extends React.ComponentPropsWithRef<"p"> {
  text: string;
  size?: IenTopTitleSize;
  color?: string;
  fontWeight?: React.CSSProperties["fontWeight"];
  maxLines?: number;
}

export function IenTopTitleParagraph({
  text,
  size = "default",
  color,
  fontWeight = "bold",
  maxLines,
  className,
  style,
  ...props
}: IenTopTitleParagraphProps) {
  return (
    <p
      className={cn(
        "text-ien-text-primary",
        size === "large" ? "text-ien-title1" : "text-ien-title2",
        className,
      )}
      style={{
        color,
        fontWeight,
        ...(maxLines
          ? {
              display: "-webkit-box",
              WebkitLineClamp: maxLines,
              WebkitBoxOrient: "vertical",
              overflow: "hidden",
            }
          : {}),
        ...style,
      }}
      {...props}
    >
      {text}
    </p>
  );
}

export interface IenTopSubtitleParagraphProps extends React.ComponentPropsWithRef<"p"> {
  text: string;
  size?: IenTopSubtitleSize;
  color?: string;
  fontWeight?: React.CSSProperties["fontWeight"];
  maxLines?: number;
}

export function IenTopSubtitleParagraph({
  text,
  size = "large",
  color = "var(--ien-text-secondary)",
  fontWeight,
  maxLines,
  className,
  style,
  ...props
}: IenTopSubtitleParagraphProps) {
  const classSize =
    size === "large"
      ? "text-ien-body1"
      : size === "medium"
        ? "text-ien-body2"
        : "text-ien-caption";
  return (
    <p
      className={cn(classSize, className)}
      style={{
        color,
        fontWeight,
        ...(maxLines
          ? {
              display: "-webkit-box",
              WebkitLineClamp: maxLines,
              WebkitBoxOrient: "vertical",
              overflow: "hidden",
            }
          : {}),
        ...style,
      }}
      {...props}
    >
      {text}
    </p>
  );
}

export interface IenTopSelectorProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children" | "type"
> {
  text: string;
  selectorType?: IenTopSelectorType;
  type?: IenTopSelectorType;
  color?: string;
  fontWeight?: React.CSSProperties["fontWeight"];
}

export function IenTopTitleSelector({
  text,
  selectorType,
  type,
  color = "var(--ien-text-primary)",
  fontWeight = "bold",
  className,
  style,
  ...props
}: IenTopSelectorProps) {
  const Icon = (selectorType ?? type ?? "arrow") === "clear" ? X : ChevronDown;
  return (
    <button
      type="button"
      className={cn(
        "inline-flex items-center gap-1 text-ien-title2 hover:text-ien-brand focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        className,
      )}
      style={{ color, fontWeight, ...style }}
      {...props}
    >
      {text}
      <Icon aria-hidden className="size-4" />
    </button>
  );
}

export interface IenTopSubtitleSelectorProps extends IenTopSelectorProps {
  size?: IenTopSubtitleSize;
}

export function IenTopSubtitleSelector({
  text,
  selectorType,
  type,
  size = "large",
  color = "var(--ien-text-secondary)",
  fontWeight = "medium",
  className,
  style,
  ...props
}: IenTopSubtitleSelectorProps) {
  const Icon = (selectorType ?? type ?? "arrow") === "clear" ? X : ChevronDown;
  const textSize =
    size === "large"
      ? "text-ien-body1"
      : size === "medium"
        ? "text-ien-body2"
        : "text-ien-caption";
  return (
    <button
      type="button"
      className={cn(
        "inline-flex items-center gap-1 hover:text-ien-brand focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        textSize,
        className,
      )}
      style={{ color, fontWeight, ...style }}
      {...props}
    >
      {text}
      <Icon aria-hidden className="size-3.5" />
    </button>
  );
}

export interface IenTopTextButtonProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children"
> {
  text: string;
  colorTone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  variant?: IenTextButtonVariant;
  size?: IenTextButtonSize;
  state?: IenButtonState;
}

const textButtonSize: Record<
  IenTextButtonSize,
  "xs" | "sm" | "default" | "lg"
> = {
  xSmall: "xs",
  small: "sm",
  medium: "default",
  large: "lg",
  xLarge: "lg",
  xxLarge: "lg",
};

export function IenTopTitleTextButton({
  text,
  colorTone = "neutral",
  variant = "clear",
  size = "xLarge",
  state,
  className,
  ...props
}: IenTopTextButtonProps) {
  return (
    <Button
      variant={variant === "underline" ? "line" : "ghost"}
      tone={colorTone}
      size={textButtonSize[size]}
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      className={cn(
        "text-ien-title2",
        variant === "underline" && "underline underline-offset-4",
        className,
      )}
      {...props}
    >
      {text}
    </Button>
  );
}

export function IenTopSubtitleTextButton({
  text,
  colorTone = "neutral",
  variant = "arrow",
  size = "medium",
  state,
  className,
  ...props
}: IenTopTextButtonProps) {
  return (
    <Button
      variant={variant === "underline" ? "line" : "ghost"}
      tone={colorTone}
      size={textButtonSize[size]}
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      className={cn(
        "text-ien-body2",
        variant === "underline" && "underline underline-offset-4",
        className,
      )}
      {...props}
    >
      {text}
      {variant === "arrow" && <ChevronRight aria-hidden className="size-3" />}
    </Button>
  );
}

export function IenTopSubtitleBadges({
  badges,
  className,
}: {
  badges: readonly IenTopSubtitleBadge[];
  className?: string;
}) {
  return (
    <div className={cn("flex flex-wrap items-center gap-1", className)}>
      {badges.map((badge, index) => (
        <Badge
          key={`${badge.text}-${index}`}
          variant={badge.variant ?? "weak"}
          tone={badge.tone ?? "brand"}
        >
          {badge.text}
        </Badge>
      ))}
    </div>
  );
}

export interface IenTopLowerButtonProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children"
> {
  text: string;
  size?: "xs" | "sm" | "default" | "lg";
  variant?: "fill" | "weak" | "line" | "ghost";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  state?: IenButtonState;
}

export function IenTopLowerButton({
  text,
  size = "sm",
  variant = "weak",
  tone = "brand",
  state,
  className,
  ...props
}: IenTopLowerButtonProps) {
  return (
    <Button
      size={size}
      variant={variant}
      tone={tone}
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      className={cn("min-h-11", className)}
      {...props}
    >
      {text}
    </Button>
  );
}

export interface IenTopLowerCTAProps extends React.ComponentPropsWithRef<"div"> {
  leftButton?: React.ReactNode;
  rightButton?: React.ReactNode;
}

export function IenTopLowerCTA({
  leftButton,
  rightButton,
  children,
  className,
  ...props
}: IenTopLowerCTAProps) {
  return (
    <div
      className={cn("flex w-full items-center gap-ien-xs", className)}
      {...props}
    >
      {leftButton}
      {rightButton}
      {children}
    </div>
  );
}

export interface IenTopLowerCTAButtonProps extends Omit<
  React.ComponentPropsWithRef<typeof Button>,
  "children"
> {
  text: string;
  state?: IenButtonState;
}
export function IenTopLowerCTAButton({
  text,
  state,
  ...props
}: IenTopLowerCTAButtonProps) {
  return (
    <Button
      size="lg"
      className="flex-1"
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      {...props}
    >
      {text}
    </Button>
  );
}

export interface IenTopRightButtonProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children"
> {
  text: string;
  size?: "xs" | "sm" | "default" | "lg";
  variant?: "fill" | "weak" | "line" | "ghost";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  state?: IenButtonState;
}
export function IenTopRightButton({
  text,
  size = "default",
  variant = "weak",
  tone = "brand",
  state,
  ...props
}: IenTopRightButtonProps) {
  return (
    <Button
      size={size}
      variant={variant}
      tone={tone}
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      {...props}
    >
      {text}
    </Button>
  );
}

export function IenTopRightAssetContent({
  children,
  className,
  ...props
}: React.ComponentPropsWithRef<"div">) {
  return (
    <div
      className={cn(
        "flex shrink-0 items-center gap-ien-xs self-center",
        className,
      )}
      {...props}
    >
      {children}
    </div>
  );
}

export function IenTopUpperAssetContent({
  children,
  className,
  ...props
}: React.ComponentPropsWithRef<"div">) {
  return (
    <div
      className={cn("mb-ien-md flex items-center gap-ien-xs", className)}
      {...props}
    >
      {children}
    </div>
  );
}

export interface IenBackButtonProps extends Omit<
  React.ComponentPropsWithRef<"button">,
  "children"
> {
  icon?: React.ReactNode;
  label?: string;
}

export function IenBackButton({
  icon,
  label = "뒤로 가기",
  className,
  ...props
}: IenBackButtonProps) {
  return (
    <Button
      aria-label={label}
      title={label}
      size="icon"
      variant="ghost"
      tone="neutral"
      className={cn(
        "rounded-full bg-ien-surface/90 shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.12)]",
        className,
      )}
      {...props}
    >
      {icon ?? <ArrowRight aria-hidden className="size-5 rotate-180" />}
    </Button>
  );
}

export function IenCloseButton({
  icon,
  label = "닫기",
  className,
  ...props
}: IenBackButtonProps) {
  return (
    <Button
      aria-label={label}
      title={label}
      size="icon"
      variant="ghost"
      tone="neutral"
      className={className}
      {...props}
    >
      {icon ?? <X aria-hidden className="size-5" />}
    </Button>
  );
}

export const IenTopTitleParagraphDefaults = {
  size: "default" as IenTopTitleSize,
};
export const IenTopSubtitleParagraphDefaults = {
  size: "small" as IenTopSubtitleSize,
};
export const IenTopSelectorDefaults = { type: "arrow" as IenTopSelectorType };
export const IenBackButtonDefaults = { label: "뒤로 가기" };
export const IenCloseButtonDefaults = { label: "닫기" };

export {
  IenTop as Top,
  IenTopBar as TopBar,
  IenTopAppBar as TopAppBar,
  IenTopTitleParagraph as TopTitleParagraph,
  IenTopTitleTextButton as TopTitleTextButton,
  IenTopTitleSelector as TopTitleSelector,
  IenTopSubtitleParagraph as TopSubtitleParagraph,
  IenTopSubtitleTextButton as TopSubtitleTextButton,
  IenTopSubtitleSelector as TopSubtitleSelector,
  IenTopSubtitleBadges as TopSubtitleBadges,
  IenTopLowerButton as TopLowerButton,
  IenTopLowerCTA as TopLowerCTA,
  IenTopLowerCTAButton as TopLowerCTAButton,
  IenTopRightButton as TopRightButton,
  IenTopRightAssetContent as TopRightAssetContent,
  IenTopUpperAssetContent as TopUpperAssetContent,
  IenBackButton as BackButton,
  IenCloseButton as CloseButton,
};
