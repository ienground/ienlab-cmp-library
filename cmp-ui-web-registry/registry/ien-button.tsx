"use client";

import * as React from "react"
import { cva, type VariantProps } from "class-variance-authority"
import { cn } from "@/lib/ien-utils"
import { Slot } from "radix-ui"

const buttonVariants = cva(
  "group/button inline-flex shrink-0 items-center justify-center rounded-[var(--ien-button-radius)] border-[length:var(--ien-stroke-thin)] border-transparent bg-clip-padding whitespace-nowrap transition-[scale,border-radius,background-color,border-color,color] duration-[var(--ien-button-press-duration)] ease-[var(--ien-button-press-easing)] motion-reduce:transition-none motion-reduce:active:scale-100 outline-none select-none focus-visible:border-ien-brand focus-visible:ring-[length:var(--ien-button-focus-ring-width)] focus-visible:ring-[color:color-mix(in_oklab,var(--ien-brand)_var(--ien-button-focus-ring-alpha),transparent)] active:scale-[var(--ien-button-pressed-scale)] disabled:cursor-not-allowed data-[enabled=false]:bg-none data-[enabled=false]:bg-ien-surface-variant data-[enabled=false]:text-ien-text-disabled data-[enabled=false]:border-transparent data-[enabled=false]:active:scale-100 aria-invalid:border-destructive aria-invalid:ring-3 aria-invalid:ring-destructive/20 dark:aria-invalid:border-destructive/50 dark:aria-invalid:ring-destructive/40 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-default)]",
  {
    variants: {
      variant: {
        fill:
          "border-[var(--ien-button-tone)] bg-[var(--ien-button-tone)] text-[var(--ien-button-on-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-fill)]",
        weak:
          "border-[var(--ien-button-tone-weak)] bg-[var(--ien-button-tone-weak)] text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-fill)]",
        line:
          "border-[var(--ien-button-tone)] bg-transparent text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-clear)]",
        ghost:
          "bg-transparent text-[var(--ien-button-tone)] active:inset-shadow-[0_0_0_100vmax_var(--ien-button-pressed-overlay-clear)]",
      },
      tone: {
        neutral:
          "[--ien-button-tone:var(--ien-neutral)] [--ien-button-on-tone:var(--ien-on-neutral)] [--ien-button-tone-weak:var(--ien-neutral-weak)]",
        brand:
          "[--ien-button-tone:var(--ien-brand)] [--ien-button-on-tone:var(--ien-on-brand)] [--ien-button-tone-weak:var(--ien-brand-weak)]",
        success:
          "[--ien-button-tone:var(--ien-success)] [--ien-button-on-tone:var(--ien-on-success)] [--ien-button-tone-weak:var(--ien-success-weak)]",
        warning:
          "[--ien-button-tone:var(--ien-warning)] [--ien-button-on-tone:var(--ien-on-warning)] [--ien-button-tone-weak:var(--ien-warning-weak)]",
        danger:
          "[--ien-button-tone:var(--ien-danger)] [--ien-button-on-tone:var(--ien-on-danger)] [--ien-button-tone-weak:var(--ien-danger-weak)]",
        info:
          "[--ien-button-tone:var(--ien-info)] [--ien-button-on-tone:var(--ien-on-info)] [--ien-button-tone-weak:var(--ien-info-weak)]",
      },
      useGradient: {
        true: "",
        false: "",
      },
      size: {
        small: "h-9 gap-ien-xxs px-ien-sm text-ien-label2 [&_svg:not([class*='size-'])]:size-4",
        medium: "h-11 gap-ien-xs px-ien-md text-ien-label1 [&_svg:not([class*='size-'])]:size-5",
        large: "h-13 gap-ien-xs px-ien-lg text-ien-body1 [&_svg:not([class*='size-'])]:size-6",
        default:
          "text-ien-body1 h-[var(--ien-button-height-default)] gap-[var(--ien-button-gap-default)] px-[var(--ien-button-padding-default)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-default)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-default)]",
        xs:
          "h-[var(--ien-button-height-xs)] gap-[var(--ien-button-gap-xs)] px-[var(--ien-button-padding-xs)] text-[length:var(--ien-button-font-size-xs)] leading-[var(--ien-button-line-height-xs)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-xs)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-xs)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-xs)]",
        sm:
          "h-[var(--ien-button-height-sm)] gap-[var(--ien-button-gap-sm)] px-[var(--ien-button-padding-sm)] text-[length:var(--ien-button-font-size-sm)] leading-[var(--ien-button-line-height-sm)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-sm)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-sm)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-sm)]",
        lg:
          "h-[var(--ien-button-height-lg)] gap-[var(--ien-button-gap-lg)] px-[var(--ien-button-padding-lg)] text-[length:var(--ien-button-font-size-lg)] leading-[var(--ien-button-line-height-lg)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-lg)] has-data-[icon=inline-end]:pr-[var(--ien-button-icon-padding-lg)] has-data-[icon=inline-start]:pl-[var(--ien-button-icon-padding-lg)]",
        "icon":
          "size-[var(--ien-button-height-default)]",
        "icon-xs":
          "size-[var(--ien-button-height-xs)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-xs)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)]",
        "icon-sm":
          "size-[var(--ien-button-height-sm)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-sm)] in-data-[slot=button-group]:rounded-[var(--ien-button-radius-group)]",
        "icon-lg":
          "size-[var(--ien-button-height-lg)] [&_svg:not([class*='size-'])]:size-[var(--ien-button-icon-lg)]",
      },
    },
    compoundVariants: [
      {
        variant: "fill",
        useGradient: true,
        className:
          "bg-[image:linear-gradient(to_bottom_right_in_srgb,color-mix(in_oklab,var(--ien-button-tone),var(--ien-button-on-tone)_var(--ien-button-gradient-fill-start)),var(--ien-button-tone),color-mix(in_oklab,var(--ien-button-tone),var(--ien-button-on-tone)_var(--ien-button-gradient-fill-end)))]",
      },
      {
        variant: "weak",
        useGradient: true,
        className:
          "bg-[image:linear-gradient(to_bottom_right_in_srgb,color-mix(in_oklab,var(--ien-button-tone-weak),var(--ien-button-tone)_var(--ien-button-gradient-weak-start)),var(--ien-button-tone-weak),var(--ien-button-tone-weak))]",
      },
    ],
    defaultVariants: {
      variant: "fill",
      tone: "brand",
      size: "large",
      useGradient: true,
    },
  }
)

export type IenButtonVariant = "fill" | "weak" | "line" | "ghost";
export type IenSemanticTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
export type IenButtonSize = NonNullable<VariantProps<typeof buttonVariants>["size"]>;
export type IenButtonDisplay = "inline" | "block" | "full";
export type IenButtonState = { enabled?: boolean; loading?: boolean };
export type IenContentPadding = number | string | Pick<React.CSSProperties, "padding" | "paddingInline" | "paddingBlock" | "paddingTop" | "paddingRight" | "paddingBottom" | "paddingLeft">;
export type IenButtonColors = {
  variant?: IenButtonVariant;
  container?: string;
  content?: string;
  border?: string;
  containerBrush?: string | null;
  disabledContainer?: string;
  disabledContent?: string;
  disabledBorder?: string;
};
export const IenButtonDefault = {
  colors({ variant = "fill", tone = "brand", useGradient = true, ...overrides }: IenButtonColors & { tone?: IenSemanticTone; useGradient?: boolean } = {}): IenButtonColors {
    const primary = `var(--ien-${tone})`;
    const weak = `var(--ien-${tone}-weak)`;
    const onTone = `var(--ien-on-${tone})`;
    const container = overrides.container ?? (variant === "fill" ? primary : variant === "weak" ? weak : "transparent");
    const content = overrides.content ?? (variant === "fill" ? onTone : primary);
    const border = overrides.border ?? (variant === "fill" || variant === "line" ? primary : variant === "weak" ? weak : "transparent");
    const containerBrush = overrides.containerBrush !== undefined ? overrides.containerBrush : !useGradient ? null : variant === "fill"
      ? `linear-gradient(to bottom right in srgb, color-mix(in oklab, ${container}, ${content} 18%), ${container}, color-mix(in oklab, ${container}, ${content} 36%))`
      : variant === "weak" ? `linear-gradient(to bottom right in srgb, color-mix(in oklab, ${container}, ${content} 6%), ${container}, ${container})` : null;
    return {
      variant, container, content, border, containerBrush,
      disabledContainer: variant === "fill" || variant === "weak" ? "var(--ien-surface-variant)" : "transparent",
      disabledContent: "var(--ien-text-disabled)",
      disabledBorder: variant === "line" ? "color-mix(in srgb, var(--ien-border) 48%, transparent)" : "transparent",
      ...overrides,
    };
  },
  textColors({ tone = "brand", ...overrides }: IenButtonColors & { tone?: IenSemanticTone } = {}): IenButtonColors {
    return IenButtonDefault.colors({ variant: "ghost", tone, useGradient: false, ...overrides });
  },
};

export type ButtonProps = Omit<React.ComponentProps<"button">, "content"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean;
    enabled?: boolean;
    state?: IenButtonState;
    loading?: boolean;
    loadingLabel?: string;
    shape?: React.CSSProperties["borderRadius"];
    contentPadding?: IenContentPadding;
    colors?: IenButtonColors;
    display?: IenButtonDisplay;
    content?: React.ReactNode;
    shakeOnDisabledClick?: boolean;
  };
export type IenButtonProps = ButtonProps;

function Button({
  className,
  variant = "fill",
  tone = "brand",
  size = "large",
  useGradient = true,
  asChild = false,
  enabled = true,
  disabled = false,
  state,
  loading: loadingProp = false,
  loadingLabel = "불러오는 중",
  shape,
  contentPadding,
  colors,
  display = "inline",
  content,
  children,
  style,
  onClick,
  onKeyDown,
  onPointerDown,
  tabIndex,
  type = "button",
  shakeOnDisabledClick = true,
  ...props
}: ButtonProps) {
  const Comp = asChild ? Slot.Root : "button";
  const isEnabled = enabled && !disabled && state?.enabled !== false;
  const loading = loadingProp || state?.loading === true;
  const interactive = isEnabled && !loading;
  const resolvedVariant = colors?.variant ?? variant ?? "fill";
  const resolved = colors ? IenButtonDefault.colors({ variant: resolvedVariant, tone: tone ?? "brand", useGradient: useGradient ?? true, ...colors }) : undefined;
  const paddingStyle = typeof contentPadding === "object" ? contentPadding : contentPadding !== undefined ? { padding: contentPadding } : undefined;
  const colorStyle = !isEnabled ? {
    backgroundColor: resolved?.disabledContainer ?? (resolvedVariant === "fill" || resolvedVariant === "weak" ? "var(--ien-surface-variant)" : "transparent"),
    backgroundImage: "none",
    color: resolved?.disabledContent ?? "var(--ien-text-disabled)",
    borderColor: resolved?.disabledBorder ?? (resolvedVariant === "line" ? "color-mix(in srgb, var(--ien-border) 48%, transparent)" : "transparent"),
  } : resolved ? {
    backgroundColor: resolved.container,
    backgroundImage: resolved.containerBrush ?? "none",
    color: resolved.content,
    borderColor: resolvedVariant === "line" ? resolved.border : "transparent",
  } : undefined;
  const body = content ?? children;
  const loadingContent = (node: React.ReactNode) => (
    <span className="inline-grid min-w-0 place-items-center gap-[inherit]">
      <span className="col-start-1 row-start-1 inline-flex items-center justify-center gap-[inherit] transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none" style={{ opacity: loading ? 0 : 1, scale: loading ? 0.8 : 1 }} aria-hidden={loading || undefined} inert={loading || undefined}>{node}</span>
      <span role="status" aria-label={loading ? loadingLabel : undefined} aria-hidden={!loading} className="pointer-events-none col-start-1 row-start-1 inline-flex transition-[opacity,scale] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none" style={{ opacity: loading ? 1 : 0, scale: loading ? 1 : 0.8 }}>
        <span className="size-[var(--ien-button-loading-size,var(--ien-icon-md))] animate-spin rounded-full border-2 border-current border-r-transparent motion-reduce:animate-none" />
      </span>
    </span>
  );
  const child = asChild && React.isValidElement<{ children?: React.ReactNode }>(body)
    ? React.cloneElement(body, {}, loadingContent(body.props.children))
    : loadingContent(body);
  return (
    <Comp
      data-slot="button"
      data-variant={resolvedVariant}
      data-tone={tone}
      data-size={size}
      data-gradient={useGradient && isEnabled}
      data-enabled={isEnabled}
      data-loading={loading}
      data-display={display}
      disabled={asChild ? undefined : !interactive}
      aria-disabled={!interactive || undefined}
      aria-busy={loading || undefined}
      tabIndex={!interactive ? -1 : tabIndex}
      type={asChild ? undefined : type}
      className={cn(buttonVariants({ variant: resolvedVariant, tone, size, useGradient }), display !== "inline" && "w-full", display === "full" && "rounded-none", className)}
      style={{ ...colorStyle, borderRadius: display === "full" ? 0 : shape, ...paddingStyle, ...style }}
      onClick={(event) => {
        if (!interactive) { event.preventDefault(); return; }
        onClick?.(event);
      }}
      onKeyDown={(event) => {
        if (!interactive && (event.key === "Enter" || event.key === " ")) { event.preventDefault(); return; }
        onKeyDown?.(event);
      }}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (!interactive && shakeOnDisabledClick && !event.defaultPrevented && !window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
          event.currentTarget.animate([{ translate: "0" }, { translate: "-8px" }, { translate: "8px" }, { translate: "-6px" }, { translate: "6px" }, { translate: "-3px" }, { translate: "3px" }, { translate: "0" }], { duration: 280, easing: "linear" });
        }
      }}
      {...props}
    >{child}</Comp>
  );
}

export { Button, Button as IenButton, buttonVariants };
