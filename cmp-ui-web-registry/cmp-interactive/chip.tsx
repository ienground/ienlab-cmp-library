"use client";

import * as React from "react";
import { X } from "lucide-react";
import { cn } from "@/lib/ien-utils";
import {
  buttonVariants,
  type IenContentPadding,
  type IenButtonVariant,
  type IenSemanticTone,
} from "@/components/ui/button";

export type ChipKind = "assist" | "filter" | "input" | "suggestion";
export type ChipState = {
  enabled?: boolean;
  loading?: boolean;
};
export type ChipColors = {
  container?: React.CSSProperties["backgroundColor"];
  content?: React.CSSProperties["color"];
  border?: React.CSSProperties["borderColor"];
  containerBrush?: React.CSSProperties["backgroundImage"] | null;
  selectedContainer?: React.CSSProperties["backgroundColor"];
  selectedContent?: React.CSSProperties["color"];
  selectedBorder?: React.CSSProperties["borderColor"];
  selectedContainerBrush?: React.CSSProperties["backgroundImage"] | null;
  disabledContainer?: React.CSSProperties["backgroundColor"];
  disabledContent?: React.CSSProperties["color"];
  disabledBorder?: React.CSSProperties["borderColor"];
  disabledSelectedContainer?: React.CSSProperties["backgroundColor"];
  disabledSelectedContent?: React.CSSProperties["color"];
  disabledSelectedBorder?: React.CSSProperties["borderColor"];
};

export type ChipProps = Omit<
  React.ComponentProps<"span">,
  "children" | "ref" | "className" | "onClick" | "disabled"
> & {
  ref?: React.Ref<HTMLSpanElement>;
  children: React.ReactNode;
  label?: React.ReactNode;
  kind?: ChipKind;
  selected?: boolean;
  defaultSelected?: boolean;
  onSelectedChange?: (selected: boolean) => void;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  onRemove?: () => void;
  enabled?: boolean;
  disabled?: boolean;
  loading?: boolean;
  state?: ChipState;
  tone?: IenSemanticTone;
  variant?: IenButtonVariant;
  useGradient?: boolean;
  colors?: Partial<ChipColors>;
  shape?: React.CSSProperties["borderRadius"];
  contentPadding?: IenContentPadding;
  leadingIcon?: React.ReactNode;
  trailingIcon?: React.ReactNode;
  avatar?: React.ReactNode;
  icon?: React.ReactNode;
  elevated?: boolean;
  removeLabel?: string;
  className?: string;
};

function shakeElement(element: HTMLElement | null) {
  element?.animate(
    [
      { translate: "0 0" },
      { translate: "-6px 0" },
      { translate: "6px 0" },
      { translate: "-3.6px 0" },
      { translate: "3.6px 0" },
      { translate: "0 0" },
    ],
    { duration: 200 },
  );
}

function Chip({
  children,
  label,
  kind = "assist",
  selected,
  defaultSelected = false,
  onSelectedChange,
  onClick,
  onRemove,
  enabled,
  disabled = false,
  loading,
  state,
  tone = "brand",
  variant,
  useGradient = true,
  colors,
  shape = "9999px",
  contentPadding,
  leadingIcon,
  trailingIcon,
  avatar,
  icon,
  elevated = false,
  removeLabel = "항목 제거",
  className,
  ref,
  ...rootProps
}: ChipProps) {
  const chipRef = React.useRef<HTMLSpanElement>(null);
  const [internalSelected, setInternalSelected] = React.useState(defaultSelected);
  const isControlled = selected !== undefined;
  const isSelected = selected ?? internalSelected;
  const isEnabled = (state?.enabled ?? enabled ?? true) && !disabled;
  const isLoading = state?.loading ?? loading ?? false;
  const canInteract = isEnabled && !isLoading;
  const isSelectable = kind === "filter" || kind === "input";
  const isAction = kind === "assist" || kind === "suggestion";
  const active = isSelectable && isSelected;
  const resolvedVariant = variant ?? (active ? "fill" : "weak");
  const currentContainer = active ? colors?.selectedContainer : colors?.container;
  const currentContent = active ? colors?.selectedContent : colors?.content;
  const currentBorder = active ? colors?.selectedBorder : colors?.border;
  const currentBrush = active
    ? colors?.selectedContainerBrush
    : colors?.containerBrush;
  const variantClasses = buttonVariants({
    variant: resolvedVariant,
    tone,
    useGradient,
  });
  const visualStyle: React.CSSProperties = {
    borderRadius: shape,
    fontSize: "var(--ien-font-size-label2)",
    lineHeight: "var(--ien-line-height-label2)",
    ...(contentPadding !== undefined
      ? typeof contentPadding === "object"
        ? contentPadding
        : { padding: contentPadding }
      : {}),
    ...(elevated
      ? {
          boxShadow:
            "0 var(--ien-elevation-raised) calc(var(--ien-elevation-raised) * 2) rgb(0 0 0 / 0.12)",
        }
      : {}),
    ...(!isEnabled
      ? {
          backgroundColor: active
            ? colors?.disabledSelectedContainer ?? "var(--ien-surface-variant)"
            : colors?.disabledContainer ?? "var(--ien-surface-variant)",
          color: active
            ? colors?.disabledSelectedContent ?? "var(--ien-text-disabled)"
            : colors?.disabledContent ?? "var(--ien-text-disabled)",
          borderColor: active
            ? colors?.disabledSelectedBorder ?? "transparent"
            : colors?.disabledBorder ?? "transparent",
          backgroundImage: "none",
        }
      : {
          ...(currentContainer !== undefined
            ? { backgroundColor: currentContainer }
            : {}),
          ...(currentContent !== undefined
            ? { color: currentContent }
            : {}),
          ...(currentBorder !== undefined
            ? { borderColor: currentBorder }
            : active
              ? { borderColor: "var(--ien-button-tone)" }
              : { borderColor: "transparent" }),
          ...(currentBrush !== undefined
            ? { backgroundImage: currentBrush ?? "none" }
            : {}),
        }),
  };
  const chipLabel = label ?? children;
  const resolvedLeading = kind === "input" ? avatar ?? leadingIcon : leadingIcon ?? icon;
  React.useImperativeHandle(ref, () => chipRef.current!, []);

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    if (!canInteract) {
      shakeElement(chipRef.current);
      event.preventDefault();
      return;
    }
    if (isSelectable) {
      const next = !isSelected;
      if (!isControlled) setInternalSelected(next);
      onSelectedChange?.(next);
    }
    onClick?.(event);
  };

  const content = isLoading ? (
    <span
      role="status"
      aria-label="처리 중"
      className="size-[var(--ien-icon-sm)] animate-spin rounded-full border-2 border-current border-r-transparent"
    />
  ) : (
    <span className="inline-flex items-center gap-ien-xxs">
      {resolvedLeading != null && (
        <span className="inline-flex shrink-0 items-center">{resolvedLeading}</span>
      )}
      <span>{chipLabel}</span>
      {trailingIcon != null && (
        <span className="inline-flex shrink-0 items-center">{trailingIcon}</span>
      )}
    </span>
  );

  return (
    <span
      {...rootProps}
      ref={(node) => {
        chipRef.current = node;
      }}
      data-kind={kind}
      data-selected={active}
      data-tone={tone}
      data-variant={resolvedVariant}
      data-enabled={canInteract}
      className={cn(
        "inline-flex min-h-[var(--ien-state-minimum-touch-target)] min-w-[var(--ien-state-minimum-touch-target)] items-center justify-center",
        !canInteract && "cursor-not-allowed",
        className,
      )}
    >
      {isAction || isSelectable ? (
        <button
          type="button"
          aria-pressed={isSelectable ? active : undefined}
          aria-disabled={!canInteract}
          data-slot="chip-action"
          onClick={handleClick}
          className={cn(
            "relative inline-flex min-h-8 items-center justify-center gap-ien-xxs border text-ien-label2 transition-[scale,background-color,color,border-color,border-radius] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] outline-none focus-visible:ring-[length:var(--ien-control-focus-ring-width)] focus-visible:ring-ien-brand/50 aria-disabled:cursor-not-allowed active:scale-[var(--ien-button-pressed-scale)] motion-reduce:transition-none",
            variantClasses,
            "text-ien-label2",
            "h-auto min-h-8 px-ien-sm py-[calc(var(--ien-spacing-xxs)+var(--ien-spacing-xxxs))]",
            !isEnabled && "opacity-[var(--ien-state-disabled-alpha)]",
          )}
          style={visualStyle}
        >
          {content}
        </button>
      ) : (
        <span
          data-slot="chip-content"
          className={cn(
            "relative inline-flex min-h-8 items-center justify-center gap-ien-xxs border text-ien-label2",
            variantClasses,
            "text-ien-label2",
            "h-auto min-h-8 px-ien-sm py-[calc(var(--ien-spacing-xxs)+var(--ien-spacing-xxxs))]",
            !isEnabled && "opacity-[var(--ien-state-disabled-alpha)]",
          )}
          style={visualStyle}
        >
          {content}
        </span>
      )}
      {kind === "input" && onRemove && !isLoading && (
        <button
          type="button"
          aria-label={removeLabel}
          aria-disabled={!canInteract}
          onClick={() => {
            if (!canInteract) {
              shakeElement(chipRef.current);
              return;
            }
            onRemove();
          }}
          className="ml-ien-xxs grid size-5 shrink-0 place-items-center rounded-ien-full hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-ien-brand aria-disabled:cursor-not-allowed"
        >
          <X aria-hidden="true" className="size-3.5" />
        </button>
      )}
    </span>
  );
}

export { Chip, Chip as IenChip };
