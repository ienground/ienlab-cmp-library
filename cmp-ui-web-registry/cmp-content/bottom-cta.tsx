"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";
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

function useBottomCTA({
  show = true,
  showAfterDelay,
  hideOnScroll = false,
  scrollDelta = 0,
  hideOnScrollDistanceThreshold = 1,
  fixed = false,
  takeSpace = fixed,
  ref: forwardedRef,
}: Pick<
  IenBottomCTAProps,
  | "show"
  | "showAfterDelay"
  | "hideOnScroll"
  | "scrollDelta"
  | "hideOnScrollDistanceThreshold"
  | "fixed"
  | "takeSpace"
  | "ref"
>) {
  const hasDelay = showAfterDelay != null;
  const delay = Math.max(0, showAfterDelay?.delayMillis ?? 1000);
  const [delayElapsed, setDelayElapsed] = React.useState(!hasDelay);
  const [height, setHeight] = React.useState(0);
  const elementRef = React.useRef<HTMLDivElement>(null);
  const setRef = React.useCallback(
    (element: HTMLDivElement | null) => {
      elementRef.current = element;
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );
  React.useEffect(() => {
    setDelayElapsed(!hasDelay);
    if (!hasDelay) return;
    const timer = window.setTimeout(() => setDelayElapsed(true), delay);
    return () => window.clearTimeout(timer);
  }, [hasDelay, delay]);
  React.useLayoutEffect(() => {
    const element = elementRef.current;
    if (!element || !fixed || !takeSpace) return;
    const measure = () => setHeight(element.offsetHeight);
    measure();
    const observer = new ResizeObserver(measure);
    observer.observe(element);
    return () => observer.disconnect();
  }, [fixed, takeSpace]);
  const animation = showAfterDelay?.animation ?? "slide";
  return {
    ref: setRef,
    height,
    visible:
      show &&
      delayElapsed &&
      !(hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold),
    hiddenTransform:
      animation === "scale"
        ? "scale(var(--ien-button-pressed-scale))"
        : animation === "slide"
          ? "translateY(100%)"
          : undefined,
  };
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
  ref,
  ...props
}: IenBottomCTAProps) {
  const motion = useBottomCTA({
    show,
    showAfterDelay,
    hideOnScroll,
    scrollDelta,
    hideOnScrollDistanceThreshold,
    fixed,
    takeSpace,
    ref,
  });
  const { visible, hiddenTransform } = motion;
  const positionClass = fixed
    ? fixedAboveKeyboard
      ? "fixed inset-x-0 bottom-[env(keyboard-inset-height,0px)] z-30"
      : "fixed inset-x-0 bottom-0 z-30"
    : "w-full";
  const isDisabled = disabled || state?.enabled === false;
  const isLoading = loading || state?.loading === true;
  const content = (
    <div
      ref={motion.ref}
      className={cn(
        positionClass,
        "transition-[opacity,transform] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
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
          disabled={isDisabled}
          loading={isLoading}
          className="w-full rounded-ien-default text-ien-body1"
          onClick={onClick}
        >
          {icon}
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
      {takeSpace && fixed && (
        <div aria-hidden style={{ height: motion.height }} />
      )}
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
  leftButtonWeight?: number;
  rightButtonWeight?: number;
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
  leftButtonWeight,
  rightButtonWeight,
  primaryButtonWeight = rightButtonWeight ?? 1,
  secondaryButtonWeight = leftButtonWeight ?? 1,
  className,
  style,
  ref,
  ...props
}: IenDoubleBottomCTAProps) {
  const motion = useBottomCTA({
    show,
    showAfterDelay,
    hideOnScroll,
    scrollDelta,
    hideOnScrollDistanceThreshold,
    fixed,
    takeSpace,
    ref,
  });
  const { visible, hiddenTransform } = motion;
  const buttonContent =
    leftButton != null || rightButton != null ? (
      <>
        {leftButton != null && <div className="min-w-0 [&>*]:w-full" style={{ flex: leftButtonWeight ?? 1 }}>{leftButton}</div>}
        {rightButton != null && <div className="min-w-0 [&>*]:w-full" style={{ flex: rightButtonWeight ?? 1 }}>{rightButton}</div>}
      </>
    ) : (
      <>
        {secondaryText != null && (
          <Button
            variant="weak"
            tone="neutral"
            disabled={
              secondaryDisabled ||
              secondaryState?.enabled === false
            }
            loading={secondaryState?.loading}
            className="min-w-0 text-ien-body1"
            style={{ flex: secondaryButtonWeight }}
            onClick={onSecondaryClick}
          >
            {secondaryIcon}
            {secondaryText}
          </Button>
        )}
        {primaryText != null && (
          <Button
            disabled={
              primaryDisabled ||
              primaryState?.enabled === false
            }
            loading={primaryState?.loading}
            className="min-w-0 text-ien-body1"
            style={{ flex: primaryButtonWeight }}
            onClick={onPrimaryClick}
          >
            {primaryIcon}
            {primaryText}
          </Button>
        )}
      </>
    );
  const content = (
    <div
      ref={motion.ref}
      className={cn(
        fixed && "fixed inset-x-0 z-30",
        fixed &&
          (fixedAboveKeyboard
            ? "bottom-[env(keyboard-inset-height,0px)]"
            : "bottom-0"),
        "transition-[opacity,transform] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
        !visible && "pointer-events-none opacity-0",
        "w-full",
        background === "default" && "bg-ien-surface/95",
        hasPaddingBottom && "pt-ien-sm",
        hasSafeAreaPadding
          ? "pb-[max(var(--ien-spacing-sm),env(safe-area-inset-bottom))]"
          : hasPaddingBottom && "pb-ien-sm",
        className,
      )}
      style={{ transform: visible ? undefined : hiddenTransform, ...style }}
      {...props}
      aria-hidden={!visible}
      inert={!visible}
    >
      {topAccessory}
      <div className="flex w-full gap-ien-xs px-ien-md">{buttonContent}</div>
      {bottomAccessory}
    </div>
  );
  return (
    <>
      {content}
      {takeSpace && fixed && (
        <div aria-hidden style={{ height: motion.height }} />
      )}
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
      state={state}
      {...props}
    >
      {icon}
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
