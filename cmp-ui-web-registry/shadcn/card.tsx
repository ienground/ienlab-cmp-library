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

type CardTone = "neutral" | "brand" | "success" | "warning" | "danger" | "info";
type CardProps = React.ComponentProps<"div"> & {
  size?: "default" | "sm";
  variant?: "filled" | "outlined";
  tone?: CardTone;
  toneVariant?: "solid" | "weak";
  useGradient?: boolean;
  contentPadding?: "none" | "small" | "default" | "large";
  onCardClick?: () => void;
};

const toneClasses: Record<
  CardTone,
  {
    solid: string;
    weak: string;
    outlinedBorder: string;
    outlinedWeakBorder: string;
    solidGradient: string;
    weakGradient: string;
  }
> = {
  neutral: {
    solid: "bg-ien-surface-raised text-ien-text-primary",
    weak: "bg-ien-surface-weak text-ien-text-primary",
    outlinedBorder: "border-ien-border",
    outlinedWeakBorder: "border-ien-border",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-surface-raised),var(--ien-text-primary)_2%),var(--ien-surface-raised),color-mix(in_oklab,var(--ien-surface-raised),var(--ien-text-primary)_4%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-surface-weak),var(--ien-text-primary)_1%),var(--ien-surface-weak),color-mix(in_oklab,var(--ien-surface-weak),var(--ien-text-primary)_2%))]",
  },
  brand: {
    solid: "bg-ien-brand text-ien-on-brand",
    weak: "bg-ien-brand-weak text-ien-brand",
    outlinedBorder: "border-ien-brand",
    outlinedWeakBorder: "border-ien-brand-weak",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_18%),var(--ien-brand),color-mix(in_oklab,var(--ien-brand),var(--ien-on-brand)_36%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-brand-weak),var(--ien-brand)_6%),var(--ien-brand-weak),color-mix(in_oklab,var(--ien-brand-weak),var(--ien-brand)_10%))]",
  },
  success: {
    solid: "bg-ien-success text-ien-on-success",
    weak: "bg-ien-success-weak text-ien-success",
    outlinedBorder: "border-ien-success",
    outlinedWeakBorder: "border-ien-success-weak",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-success),var(--ien-on-success)_18%),var(--ien-success),color-mix(in_oklab,var(--ien-success),var(--ien-on-success)_36%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-success-weak),var(--ien-success)_6%),var(--ien-success-weak),color-mix(in_oklab,var(--ien-success-weak),var(--ien-success)_10%))]",
  },
  warning: {
    solid: "bg-ien-warning text-ien-on-warning",
    weak: "bg-ien-warning-weak text-ien-warning",
    outlinedBorder: "border-ien-warning",
    outlinedWeakBorder: "border-ien-warning-weak",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-warning),var(--ien-on-warning)_18%),var(--ien-warning),color-mix(in_oklab,var(--ien-warning),var(--ien-on-warning)_36%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-warning-weak),var(--ien-warning)_6%),var(--ien-warning-weak),color-mix(in_oklab,var(--ien-warning-weak),var(--ien-warning)_10%))]",
  },
  danger: {
    solid: "bg-ien-danger text-ien-on-danger",
    weak: "bg-ien-danger-weak text-ien-danger",
    outlinedBorder: "border-ien-danger",
    outlinedWeakBorder: "border-ien-danger-weak",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-danger),var(--ien-on-danger)_18%),var(--ien-danger),color-mix(in_oklab,var(--ien-danger),var(--ien-on-danger)_36%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-danger-weak),var(--ien-danger)_6%),var(--ien-danger-weak),color-mix(in_oklab,var(--ien-danger-weak),var(--ien-danger)_10%))]",
  },
  info: {
    solid: "bg-ien-info text-ien-on-info",
    weak: "bg-ien-info-weak text-ien-info",
    outlinedBorder: "border-ien-info",
    outlinedWeakBorder: "border-ien-info-weak",
    solidGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-info),var(--ien-on-info)_18%),var(--ien-info),color-mix(in_oklab,var(--ien-info),var(--ien-on-info)_36%))]",
    weakGradient:
      "bg-[linear-gradient(135deg,color-mix(in_oklab,var(--ien-info-weak),var(--ien-info)_6%),var(--ien-info-weak),color-mix(in_oklab,var(--ien-info-weak),var(--ien-info)_10%))]",
  },
};

function Card({
  className,
  size = "default",
  variant = "filled",
  tone = "neutral",
  toneVariant = "solid",
  useGradient = false,
  contentPadding = "default",
  onCardClick,
  onClick,
  onKeyDown,
  style,
  ...props
}: CardProps) {
  const clickable = Boolean(onCardClick || onClick);
  const colors = toneClasses[tone];
  const isOutlined = variant === "outlined";
  const background = isOutlined
    ? "bg-ien-surface text-ien-text-primary"
    : toneVariant === "solid"
      ? colors.solid
      : colors.weak;
  const gradient =
    !isOutlined && useGradient
      ? toneVariant === "solid"
        ? colors.solidGradient
        : colors.weakGradient
      : "";
  return (
    <div
      data-slot="card"
      data-size={size}
      data-variant={variant}
      data-tone={tone}
      data-tone-variant={toneVariant}
      role={clickable ? "button" : props.role}
      tabIndex={clickable ? 0 : props.tabIndex}
      onKeyDown={
        clickable
          ? (event) => {
              onKeyDown?.(event);
              if (
                !event.defaultPrevented &&
                (event.key === "Enter" || event.key === " ")
              ) {
                event.preventDefault();
                if (onCardClick) onCardClick();
                else event.currentTarget.click();
              }
            }
          : onKeyDown
      }
      onClick={
        clickable
          ? (event) => {
              onClick?.(event);
              if (!event.defaultPrevented) onCardClick?.();
            }
          : undefined
      }
      className={cn(
        "group/card flex flex-col gap-ien-md overflow-hidden rounded-ien-default border text-ien-body2",
        isOutlined
          ? toneVariant === "weak"
            ? colors.outlinedWeakBorder
            : colors.outlinedBorder
          : "border-transparent",
        background,
        gradient,
        size === "sm" ? "gap-ien-sm" : "",
        contentPadding === "none"
          ? "p-ien-none"
          : contentPadding === "small"
            ? "p-ien-sm"
            : contentPadding === "large"
              ? "p-ien-xl"
              : "p-ien-md",
        clickable &&
          "cursor-pointer transition-colors hover:brightness-[0.98] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        className,
      )}
      style={style}
      {...props}
    />
  );
}

function CardHeader({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-header"
      className={cn("grid auto-rows-min items-start gap-ien-xxs", className)}
      {...props}
    />
  );
}
function CardTitle({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-title"
      className={cn(
        "text-ien-body1 font-[var(--ien-font-weight-label1)]",
        className,
      )}
      {...props}
    />
  );
}
function CardDescription({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-description"
      className={cn("text-ien-body2 text-ien-text-secondary", className)}
      {...props}
    />
  );
}
function CardAction({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-action"
      className={cn(
        "col-start-2 row-span-2 row-start-1 self-start justify-self-end",
        className,
      )}
      {...props}
    />
  );
}
function CardContent({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-content"
      className={cn("min-w-0", className)}
      {...props}
    />
  );
}
function CardFooter({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-footer"
      className={cn(
        "flex items-center gap-ien-sm border-t border-ien-border pt-ien-md",
        className,
      )}
      {...props}
    />
  );
}

type IenCardProps = Omit<
  CardProps,
  | "variant"
  | "tone"
  | "toneVariant"
  | "useGradient"
  | "onClick"
  | "size"
  | "contentPadding"
> & {
  variant?: "Filled" | "Outlined" | "filled" | "outlined";
  tone?:
    "Neutral" | "Brand" | "Success" | "Warning" | "Danger" | "Info" | CardTone;
  toneVariant?: "Solid" | "Weak" | "solid" | "weak";
  useGradient?: boolean;
  shape?: React.CSSProperties["borderRadius"];
  contentPadding?:
    React.CSSProperties["padding"] | "none" | "small" | "default" | "large";
  tonalElevation?: "none" | "raised";
  colors?: {
    container?: string;
    content?: string;
    border?: string;
    containerBrush?: string;
  };
  onClick?: () => void;
};

function IenCard({
  variant = "Filled",
  tone = "Neutral",
  toneVariant = "Solid",
  useGradient = true,
  shape,
  contentPadding = "default",
  tonalElevation,
  colors,
  onClick,
  style,
  className,
  ...props
}: IenCardProps) {
  const normalizedVariant = variant.toLowerCase() as CardProps["variant"];
  const normalizedTone = tone.toLowerCase() as CardTone;
  const normalizedToneVariant =
    toneVariant.toLowerCase() as CardProps["toneVariant"];
  const resolvedElevation =
    tonalElevation ?? (normalizedVariant === "filled" ? "raised" : "none");
  const cardStyle: React.CSSProperties = {
    ...style,
    borderRadius: shape ?? style?.borderRadius,
    padding:
      typeof contentPadding === "string" &&
      ["none", "small", "default", "large"].includes(contentPadding)
        ? style?.padding
        : contentPadding,
    backgroundColor: colors?.container ?? style?.backgroundColor,
    backgroundImage: colors?.containerBrush ?? style?.backgroundImage,
    color: colors?.content ?? style?.color,
    borderColor: colors?.border ?? style?.borderColor,
  };
  return (
    <Card
      {...props}
      variant={normalizedVariant}
      tone={normalizedTone}
      toneVariant={normalizedToneVariant}
      useGradient={useGradient && !colors?.containerBrush}
      contentPadding={
        typeof contentPadding === "string" &&
        ["none", "small", "default", "large"].includes(contentPadding)
          ? (contentPadding as CardProps["contentPadding"])
          : "none"
      }
      data-elevation={resolvedElevation}
      className={cn(resolvedElevation === "raised" && "shadow-sm", className)}
      onClick={onClick ? () => onClick() : undefined}
      style={cardStyle}
    />
  );
}

export type { CardProps, IenCardProps, CardTone };
export {
  Card,
  CardHeader,
  CardFooter,
  CardTitle,
  CardAction,
  CardDescription,
  CardContent,
  IenCard,
};
