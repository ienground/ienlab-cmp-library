"use client";

import * as React from "react";
import { cn } from "@/lib/ien-utils";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";

export type BottomSheetDetent = "content" | "medium" | "full";

export type BottomSheetState = {
  visible: boolean;
  detent: BottomSheetDetent;
  show: (detent?: BottomSheetDetent) => void;
  hide: () => void;
};

function useBottomSheetState(
  initialVisible = false,
  initialDetent: BottomSheetDetent = "content",
): BottomSheetState {
  const [visible, setVisible] = React.useState(initialVisible);
  const [detent, setDetent] = React.useState(initialDetent);
  return {
    visible,
    detent,
    show: (nextDetent = "content") => {
      setDetent(nextDetent);
      setVisible(true);
    },
    hide: () => setVisible(false),
  };
}

export type BottomSheetProps = Omit<
  React.ComponentProps<typeof SheetContent>,
  | "children"
  | "side"
  | "showCloseButton"
  | "showOverlay"
  | "overlayClassName"
  | "onEscapeKeyDown"
  | "onInteractOutside"
  | "ref"
  | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  state?: BottomSheetState;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  header?: React.ReactNode;
  headerDescription?: React.ReactNode;
  cta?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
  dismissible?: boolean;
  dismissOnScrimClick?: boolean;
  showDragHandle?: boolean;
  showCloseButton?: boolean;
  disableDimmer?: boolean;
  disableFocusLock?: boolean;
  contentPadding?: React.CSSProperties["padding"];
  detent?: BottomSheetDetent;
  defaultDetent?: BottomSheetDetent;
  onDetentChange?: (detent: BottomSheetDetent) => void;
  closeOnBackEvent?: boolean;
  onEscapeKeyDown?: React.ComponentProps<typeof SheetContent>["onEscapeKeyDown"];
  onInteractOutside?: React.ComponentProps<typeof SheetContent>["onInteractOutside"];
};

function BottomSheet({
  state,
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismiss,
  onDismissRequest,
  onClose,
  title,
  description,
  header,
  headerDescription,
  cta,
  children,
  className,
  dismissible = true,
  dismissOnScrimClick,
  showDragHandle = true,
  showCloseButton = false,
  disableDimmer = false,
  disableFocusLock = false,
  contentPadding = "var(--ien-spacing-xs) var(--ien-spacing-xl)",
  detent,
  defaultDetent = "content",
  onDetentChange,
  closeOnBackEvent,
  ref,
  ...contentProps
}: BottomSheetProps) {
  const [internalOpen, setInternalOpen] = React.useState(defaultOpen ?? false);
  const [internalDetent, setInternalDetent] = React.useState(defaultDetent);
  const dragStartY = React.useRef<number | null>(null);
  const isOpen = state?.visible ?? visible ?? open ?? internalOpen;
  const currentDetent = state?.detent ?? detent ?? internalDetent;
  const canDismissOnScrim = dismissOnScrimClick ?? dismissible;
  const canDismissOnBack = closeOnBackEvent ?? dismissible;
  const handleDismiss = onDismissRequest ?? onDismiss ?? onClose;

  const changeDetent = (nextDetent: BottomSheetDetent) => {
    if (state) state.show(nextDetent);
    else if (detent === undefined) setInternalDetent(nextDetent);
    onDetentChange?.(nextDetent);
  };

  const changeOpen = (nextOpen: boolean) => {
    if (state) {
      if (nextOpen) state.show(currentDetent);
      else state.hide();
    } else if (open === undefined && visible === undefined) {
      setInternalOpen(nextOpen);
    }
    onOpenChange?.(nextOpen);
    if (!nextOpen) handleDismiss?.();
  };

  const handleDragEnd = (endY: number) => {
    const startY = dragStartY.current;
    dragStartY.current = null;
    if (startY === null) return;
    const distance = endY - startY;
    if (distance > 150 && dismissible) {
      changeOpen(false);
      return;
    }
    if (currentDetent === "content" || Math.abs(distance) < 24) return;
    changeDetent(distance > 0 ? "medium" : "full");
  };

  const sheetHeight =
    currentDetent === "medium"
      ? "50dvh"
      : currentDetent === "full"
        ? "92dvh"
        : undefined;

  return (
    <Sheet open={isOpen} modal={!disableFocusLock} onOpenChange={changeOpen}>
      <SheetContent
        {...contentProps}
        ref={ref}
        side="bottom"
        showCloseButton={showCloseButton && dismissible}
        showOverlay={!disableDimmer}
        overlayClassName={
          disableDimmer
            ? "bg-transparent supports-backdrop-filter:backdrop-blur-none"
            : undefined
        }
        onEscapeKeyDown={(event) => {
          contentProps.onEscapeKeyDown?.(event);
          if (!canDismissOnBack) event.preventDefault();
        }}
        onInteractOutside={(event) => {
          contentProps.onInteractOutside?.(event);
          if (!canDismissOnScrim) event.preventDefault();
        }}
        className={cn(
          "mx-auto max-h-[92dvh] w-full max-w-[520px] rounded-t-ien-lg border-ien-border bg-ien-surface-raised text-ien-text-primary",
          className,
        )}
        style={{
          ...contentProps.style,
          height: contentProps.style?.height ?? sheetHeight,
          maxHeight: contentProps.style?.maxHeight ?? "92dvh",
        }}
      >
        {showDragHandle && (
          <div
            role="separator"
            aria-label="바텀 시트 크기 조절"
            aria-orientation="horizontal"
            tabIndex={0}
            onPointerDown={(event) => {
              dragStartY.current = event.clientY;
              event.currentTarget.setPointerCapture(event.pointerId);
            }}
            onPointerUp={(event) => handleDragEnd(event.clientY)}
            onPointerCancel={() => {
              dragStartY.current = null;
            }}
            onKeyDown={(event) => {
              if (event.key === "ArrowUp") {
                changeDetent(currentDetent === "content" ? "medium" : "full");
              } else if (event.key === "ArrowDown") {
                if (currentDetent === "full") changeDetent("medium");
                else if (currentDetent === "medium") changeDetent("content");
                else if (dismissible) changeOpen(false);
              }
            }}
            className="mx-auto mt-ien-xs flex h-7 w-full touch-none cursor-grab items-center justify-center active:cursor-grabbing"
          >
            <span
              aria-hidden="true"
              className="h-1 w-9 rounded-ien-full bg-ien-border-strong"
            />
          </div>
        )}
        {(header || title || description || headerDescription) && (
          <SheetHeader className="px-ien-lg pb-ien-none">
            {header ?? (title && (
              <SheetTitle className="text-ien-title3 text-ien-text-primary">
                {title}
              </SheetTitle>
            ))}
            {(headerDescription ?? description) && (
              <SheetDescription className="text-ien-body2 text-ien-text-secondary">
                {headerDescription ?? description}
              </SheetDescription>
            )}
          </SheetHeader>
        )}
        <div
          className="min-h-0 flex-1 overflow-y-auto"
          style={{ padding: contentPadding }}
        >
          {children}
        </div>
        {cta && (
          <SheetFooter className="mt-0 px-ien-xl pb-[max(var(--ien-spacing-md),env(safe-area-inset-bottom))]">
            {cta}
          </SheetFooter>
        )}
      </SheetContent>
    </Sheet>
  );
}

export type BottomSheetOption<T extends string = string> = {
  value: T;
  label: React.ReactNode;
  description?: React.ReactNode;
  disabled?: boolean;
};
export type BottomSheetSelectProps<T extends string = string> = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: T;
  defaultValue?: T;
  onValueChange?: (value: T) => void;
  options: BottomSheetOption<T>[];
  title?: React.ReactNode;
  description?: React.ReactNode;
  placeholder?: string;
  disabled?: boolean;
  className?: string;
};

function BottomSheetSelect<T extends string>({
  value,
  defaultValue,
  onValueChange,
  options,
  title = "선택",
  description,
  placeholder = "항목 선택",
  disabled = false,
  className,
  ref,
  ...rootProps
}: BottomSheetSelectProps<T>) {
  const [internalValue, setInternalValue] = React.useState<T | undefined>(
    defaultValue,
  );
  const [open, setOpen] = React.useState(false);
  const idPrefix = React.useId().replaceAll(":", "");
  const selected = value ?? internalValue;
  const selectedOption = options.find((option) => option.value === selected);
  const choose = (next: T) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
    setOpen(false);
  };
  return (
    <div {...rootProps} ref={ref} className={cn("grid gap-ien-xs", className)}>
      <Button
        type="button"
        variant="line"
        tone="neutral"
        disabled={disabled}
        aria-haspopup="dialog"
        aria-expanded={open}
        onClick={() => setOpen(true)}
      >
        {selectedOption?.label ?? placeholder}
      </Button>
      <BottomSheet
        open={open}
        onOpenChange={setOpen}
        title={title}
        description={description}
      >
        <RadioGroup
          value={selected}
          onValueChange={choose}
          aria-label={typeof title === "string" ? title : "선택 항목"}
          className="grid gap-ien-xs py-ien-sm"
        >
          {options.map((option, index) => {
            const optionId = idPrefix + "-option-" + index;
            const optionLabelId = optionId + "-label";
            const optionDescriptionId = optionId + "-description";
            return (
              <label
                key={option.value}
                htmlFor={optionId}
                className={cn(
                  "flex min-h-12 cursor-pointer items-start gap-ien-sm rounded-ien-default border border-ien-border p-ien-sm text-ien-body2 transition-colors duration-[var(--ien-motion-fast)] has-[:disabled]:cursor-not-allowed has-[:disabled]:opacity-50",
                  option.value === selected
                    ? "border-ien-brand bg-ien-brand-weak text-ien-on-brand-weak"
                    : "bg-ien-surface text-ien-text-primary",
                  option.disabled && "cursor-not-allowed opacity-50",
                )}
              >
                <RadioGroupItem
                  id={optionId}
                  value={option.value}
                  disabled={disabled || option.disabled}
                  aria-labelledby={optionLabelId}
                  aria-describedby={
                    option.description ? optionDescriptionId : undefined
                  }
                  className="mt-ien-xxxs"
                />
                <span className="grid gap-[var(--ien-spacing-xxxs)]">
                  <span id={optionLabelId}>{option.label}</span>
                  {option.description && (
                    <span
                      id={optionDescriptionId}
                      className="text-ien-caption text-ien-text-secondary"
                    >
                      {option.description}
                    </span>
                  )}
                </span>
              </label>
            );
          })}
        </RadioGroup>
      </BottomSheet>
    </div>
  );
}

export {
  BottomSheet,
  BottomSheet as IenBottomSheet,
  useBottomSheetState,
  useBottomSheetState as useIenBottomSheetState,
  BottomSheetSelect,
  BottomSheetSelect as IenBottomSheetSelect,
};
