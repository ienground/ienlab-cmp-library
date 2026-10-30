"use client";

import * as React from "react";
import { cn } from "cn";
import { Button } from "@/components/ui/button";

export type IenBottomCTABackground = "default" | "none";
export type IenBottomCTAAnimation = "slide" | "fade" | "scale";
export interface IenBottomCTAShowAfterDelay {
  animation?: IenBottomCTAAnimation;
  delayMillis?: number;
}
export interface IenButtonState {
  enabled?: boolean;
  loading?: boolean;
}

export interface IenBottomCTAProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "onClick"
> {
  text: string;
  onClick: () => void;
  state?: IenButtonState;
  disabled?: boolean;
  loading?: boolean;
  icon?: React.ReactNode;
  variant?: "fill" | "weak" | "line" | "ghost";
  tone?: "neutral" | "brand" | "success" | "warning" | "danger" | "info";
  background?: IenBottomCTABackground;
  fixed?: boolean;
  fixedAboveKeyboard?: boolean;
  hasSafeAreaPadding?: boolean;
  hasPaddingBottom?: boolean;
  takeSpace?: boolean;
  show?: boolean;
  showAfterDelay?: IenBottomCTAShowAfterDelay;
  hideOnScroll?: boolean;
  hideOnScrollDistanceThreshold?: number;
  scrollDelta?: number;
  topAccessory?: React.ReactNode;
  bottomAccessory?: React.ReactNode;
}

export function IenBottomCTA({
  text,
  onClick,
  state,
  disabled = false,
  loading = false,
  icon,
  variant = "fill",
  tone = "brand",
  background = "default",
  fixed = false,
  fixedAboveKeyboard = false,
  hasSafeAreaPadding = true,
  hasPaddingBottom = true,
  takeSpace = fixed,
  show = true,
  showAfterDelay,
  hideOnScroll = false,
  hideOnScrollDistanceThreshold = 1,
  scrollDelta = 0,
  topAccessory,
  bottomAccessory,
  className,
  style,
  ...props
}: IenBottomCTAProps) {
  const [delayElapsed, setDelayElapsed] = React.useState(
    showAfterDelay == null,
  );
  React.useEffect(() => {
    if (showAfterDelay == null) {
      setDelayElapsed(true);
      return;
    }
    setDelayElapsed(false);
    const timer = window.setTimeout(
      () => setDelayElapsed(true),
      Math.max(0, showAfterDelay.delayMillis ?? 1000),
    );
    return () => window.clearTimeout(timer);
  }, [showAfterDelay?.animation, showAfterDelay?.delayMillis]);
  const visible =
    show &&
    delayElapsed &&
    !(hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold);
  const positionClass = fixed
    ? fixedAboveKeyboard
      ? "fixed inset-x-0 bottom-[env(keyboard-inset-height,0px)] z-30"
      : "fixed inset-x-0 bottom-0 z-30"
    : "w-full";
  const isDisabled = disabled || state?.enabled === false;
  const isLoading = loading || state?.loading === true;
  const animation = showAfterDelay?.animation ?? "slide";
  const hiddenTransform =
    animation === "scale"
      ? "scale(var(--ien-button-pressed-scale))"
      : animation === "slide"
        ? "translateY(100%)"
        : undefined;
  const content = (
    <div
      className={cn(
        positionClass,
        "transition-[opacity,transform] duration-[var(--ien-motion-normal)] motion-reduce:transition-none",
        !visible && "pointer-events-none opacity-0",
        className,
      )}
      style={{ transform: visible ? undefined : hiddenTransform, ...style }}
      {...props}
      aria-hidden={!visible}
      inert={!visible}
    >
      {topAccessory}
      <div
        className={cn(
          "w-full px-ien-md",
          background === "default" && "bg-ien-surface/95 pt-ien-sm",
          hasSafeAreaPadding
            ? "pb-[max(var(--ien-spacing-sm),env(safe-area-inset-bottom))]"
            : hasPaddingBottom && "pb-ien-sm",
        )}
      >
        <Button
          variant={variant}
          tone={tone}
          disabled={isDisabled || isLoading}
          aria-busy={isLoading}
          className="h-12 w-full rounded-ien-default text-ien-body1"
          onClick={onClick}
        >
          {isLoading ? (
            <span
              aria-label="요청 중"
              role="status"
              className="size-4 animate-spin rounded-full border-2 border-current border-r-transparent"
            />
          ) : (
            icon
          )}
          {text}
        </Button>
      </div>
      {bottomAccessory}
      {hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold && (
        <span className="sr-only">스크롤 중 숨김</span>
      )}
    </div>
  );
  return (
    <>
      {content}
      {takeSpace && fixed && <div aria-hidden className="h-16" />}
    </>
  );
}

export interface IenDoubleBottomCTAProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "onClick"
> {
  primaryText?: string;
  secondaryText?: string;
  onPrimaryClick?: () => void;
  onSecondaryClick?: () => void;
  primaryDisabled?: boolean;
  secondaryDisabled?: boolean;
  primaryState?: IenButtonState;
  secondaryState?: IenButtonState;
  primaryIcon?: React.ReactNode;
  secondaryIcon?: React.ReactNode;
  leftButton?: React.ReactNode;
  rightButton?: React.ReactNode;
  fixed?: boolean;
  fixedAboveKeyboard?: boolean;
  takeSpace?: boolean;
  show?: boolean;
  showAfterDelay?: IenBottomCTAShowAfterDelay;
  hideOnScroll?: boolean;
  hideOnScrollDistanceThreshold?: number;
  scrollDelta?: number;
  background?: IenBottomCTABackground;
  hasSafeAreaPadding?: boolean;
  hasPaddingBottom?: boolean;
  topAccessory?: React.ReactNode;
  bottomAccessory?: React.ReactNode;
  primaryButtonWeight?: number;
  secondaryButtonWeight?: number;
}

export function IenDoubleBottomCTA({
  primaryText,
  secondaryText,
  onPrimaryClick,
  onSecondaryClick,
  primaryDisabled,
  secondaryDisabled,
  primaryState,
  secondaryState,
  primaryIcon,
  secondaryIcon,
  leftButton,
  rightButton,
  fixed = false,
  fixedAboveKeyboard = false,
  takeSpace = fixed,
  show = true,
  showAfterDelay,
  hideOnScroll = false,
  hideOnScrollDistanceThreshold = 1,
  scrollDelta = 0,
  background = "default",
  hasSafeAreaPadding = true,
  hasPaddingBottom = true,
  topAccessory,
  bottomAccessory,
  primaryButtonWeight = 1,
  secondaryButtonWeight = 1,
  className,
  ...props
}: IenDoubleBottomCTAProps) {
  const [delayElapsed, setDelayElapsed] = React.useState(
    showAfterDelay == null,
  );
  React.useEffect(() => {
    if (showAfterDelay == null) {
      setDelayElapsed(true);
      return;
    }
    setDelayElapsed(false);
    const timer = window.setTimeout(
      () => setDelayElapsed(true),
      Math.max(0, showAfterDelay.delayMillis ?? 1000),
    );
    return () => window.clearTimeout(timer);
  }, [showAfterDelay?.animation, showAfterDelay?.delayMillis]);
  const visible =
    show &&
    delayElapsed &&
    !(hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold);
  const buttonContent =
    leftButton != null || rightButton != null ? (
      <>
        {leftButton}
        {rightButton}
      </>
    ) : (
      <>
        {secondaryText != null && (
          <Button
            variant="weak"
            tone="neutral"
            disabled={
              secondaryDisabled ||
              secondaryState?.enabled === false ||
              secondaryState?.loading
            }
            aria-busy={secondaryState?.loading}
            className="min-w-0 text-ien-body1"
            style={{ flex: secondaryButtonWeight }}
            onClick={onSecondaryClick}
          >
            {secondaryState?.loading ? (
              <span
                aria-label="요청 중"
                role="status"
                className="size-4 animate-spin rounded-full border-2 border-current border-r-transparent"
              />
            ) : (
              secondaryIcon
            )}
            {secondaryText}
          </Button>
        )}
        {primaryText != null && (
          <Button
            disabled={
              primaryDisabled ||
              primaryState?.enabled === false ||
              primaryState?.loading
            }
            aria-busy={primaryState?.loading}
            className="min-w-0 text-ien-body1"
            style={{ flex: primaryButtonWeight }}
            onClick={onPrimaryClick}
          >
            {primaryState?.loading ? (
              <span
                aria-label="요청 중"
                role="status"
                className="size-4 animate-spin rounded-full border-2 border-current border-r-transparent"
              />
            ) : (
              primaryIcon
            )}
            {primaryText}
          </Button>
        )}
      </>
    );
  const content = (
    <div
      className={cn(
        fixed && "fixed inset-x-0 z-30",
        fixed &&
          (fixedAboveKeyboard
            ? "bottom-[env(keyboard-inset-height,0px)]"
            : "bottom-0"),
        "transition-[opacity,transform] duration-[var(--ien-motion-normal)] motion-reduce:transition-none",
        !visible && "pointer-events-none translate-y-full opacity-0",
        "w-full",
        background === "default" && "bg-ien-surface/95",
        hasPaddingBottom && "pt-ien-sm",
        hasSafeAreaPadding
          ? "pb-[max(var(--ien-spacing-sm),env(safe-area-inset-bottom))]"
          : hasPaddingBottom && "pb-ien-sm",
        className,
      )}
      aria-hidden={!visible}
      inert={!visible}
      {...props}
    >
      {topAccessory}
      <div className="flex w-full gap-ien-xs px-ien-md">{buttonContent}</div>
      {bottomAccessory}
    </div>
  );
  return (
    <>
      {content}
      {takeSpace && fixed && <div aria-hidden className="h-16" />}
    </>
  );
}

export interface IenBottomCTAButtonProps extends Omit<
  React.ComponentPropsWithRef<typeof Button>,
  "children"
> {
  text: string;
  icon?: React.ReactNode;
  state?: IenButtonState;
}
export function IenBottomCTAButton({
  text,
  icon,
  state,
  ...props
}: IenBottomCTAButtonProps) {
  return (
    <Button
      className="flex-1 text-ien-body1"
      disabled={state?.enabled === false || state?.loading}
      aria-busy={state?.loading}
      {...props}
    >
      {state?.loading ? (
        <span
          aria-label="요청 중"
          role="status"
          className="size-4 animate-spin rounded-full border-2 border-current border-r-transparent"
        />
      ) : (
        icon
      )}
      {text}
    </Button>
  );
}

export interface IenFixedBottomCTAProps extends Omit<
  IenBottomCTAProps,
  "fixed"
> {
  contentPadding?: string;
}
export function IenFixedBottomCTA({
  contentPadding,
  ...props
}: IenFixedBottomCTAProps) {
  return (
    <IenBottomCTA {...props} fixed style={{ paddingInline: contentPadding }} />
  );
}

export interface IenFixedDoubleBottomCTAProps extends Omit<
  IenDoubleBottomCTAProps,
  "fixed"
> {}
export function IenFixedDoubleBottomCTA(props: IenFixedDoubleBottomCTAProps) {
  return <IenDoubleBottomCTA {...props} fixed />;
}

export {
  IenBottomCTA as BottomCTA,
  IenDoubleBottomCTA as DoubleBottomCTA,
  IenBottomCTAButton as BottomCTAButton,
  IenFixedBottomCTA as FixedBottomCTA,
  IenFixedDoubleBottomCTA as FixedDoubleBottomCTA,
};
