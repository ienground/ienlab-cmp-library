"use client";

import * as React from "react";
import { ChevronDown, Eye, EyeOff, LoaderCircle, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

export type TextFieldStatus =
  | { type: "normal" }
  | { type: "error"; message: string }
  | { type: "success"; message?: string };

export type TextFieldProps = Omit<
  React.ComponentProps<"input">,
  "value" | "defaultValue" | "onChange" | "prefix"
> & {
  label?: React.ReactNode;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  helperText?: React.ReactNode;
  error?: React.ReactNode;
  status?: TextFieldStatus;
  requiredMark?: boolean;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  className?: string;
};

function TextField({
  label,
  value,
  defaultValue,
  onValueChange,
  helperText,
  error,
  status,
  requiredMark,
  prefix,
  suffix,
  id,
  className,
  readOnly,
  ...props
}: TextFieldProps) {
  const generatedId = React.useId();
  const fieldId = id ?? generatedId;
  const messageId = fieldId + "-message";
  const [internalValue, setInternalValue] = React.useState(defaultValue ?? "");
  const current = value ?? internalValue;
  const errorMessage =
    error ?? (status?.type === "error" ? status.message : undefined);
  const feedback =
    errorMessage ??
    (status?.type === "success" ? status.message : undefined) ??
    helperText;
  const invalid = Boolean(errorMessage);
  return (
    <div className={cn("grid gap-[var(--ien-control-gap)]", className)}>
      {label && (
        <label
          htmlFor={fieldId}
          className="text-ien-label2 text-ien-text-primary"
        >
          {label}
          {requiredMark && (
            <span aria-hidden="true" className="ml-ien-xxs text-ien-danger">
              *
            </span>
          )}
        </label>
      )}
      <div
        className={cn(
          "flex min-w-0 items-center gap-ien-xs rounded-ien-default border border-ien-border bg-ien-surface px-[var(--ien-control-padding-inline)] focus-within:border-ien-brand focus-within:ring-2 focus-within:ring-ien-brand/20",
          invalid && "border-ien-danger focus-within:ring-ien-danger/20",
          status?.type === "success" && "border-ien-success",
        )}
      >
        {prefix && (
          <span className="shrink-0 text-ien-body2 text-ien-text-secondary">
            {prefix}
          </span>
        )}
        <Input
          {...props}
          id={fieldId}
          value={current}
          readOnly={readOnly}
          aria-invalid={invalid || undefined}
          aria-describedby={feedback ? messageId : undefined}
          onChange={(event) => {
            if (value === undefined)
              setInternalValue(event.currentTarget.value);
            onValueChange?.(event.currentTarget.value);
          }}
          className="h-10 min-w-0 border-0 bg-transparent px-ien-none text-ien-body2 shadow-none focus-visible:ring-0"
        />
        {suffix && (
          <span className="shrink-0 text-ien-body2 text-ien-text-secondary">
            {suffix}
          </span>
        )}
      </div>
      {feedback && (
        <p
          id={messageId}
          className={cn(
            "text-ien-caption",
            invalid
              ? "text-ien-danger"
              : status?.type === "success"
                ? "text-ien-success"
                : "text-ien-text-secondary",
          )}
        >
          {feedback}
        </p>
      )}
    </div>
  );
}

export type ClearableTextFieldProps = Omit<TextFieldProps, "suffix"> & {
  onClear?: () => void;
};

function ClearableTextField({
  value,
  defaultValue,
  onValueChange,
  onClear,
  disabled,
  readOnly,
  ...props
}: ClearableTextFieldProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue ?? "");
  const current = value ?? internalValue;
  const update = (next: string) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
  };
  return (
    <TextField
      {...props}
      value={current}
      disabled={disabled}
      readOnly={readOnly}
      onValueChange={update}
      suffix={
        current && !disabled && !readOnly ? (
          <TextFieldClearButton
            visible
            onClick={() => {
              update("");
              onClear?.();
            }}
          />
        ) : null
      }
    />
  );
}

export type PasswordTextFieldProps = Omit<TextFieldProps, "type" | "suffix"> & {
  visible?: boolean;
  defaultVisible?: boolean;
  onVisibilityChange?: (visible: boolean) => void;
};

function PasswordTextField({
  visible,
  defaultVisible = false,
  onVisibilityChange,
  disabled,
  readOnly,
  ...props
}: PasswordTextFieldProps) {
  const [internalVisible, setInternalVisible] = React.useState(defaultVisible);
  const isVisible = visible ?? internalVisible;
  const toggle = () => {
    if (disabled || readOnly) return;
    const next = !isVisible;
    if (visible === undefined) setInternalVisible(next);
    onVisibilityChange?.(next);
  };
  return (
    <TextField
      {...props}
      type={isVisible ? "text" : "password"}
      disabled={disabled}
      readOnly={readOnly}
      suffix={
        <TextFieldIconButton
          label={isVisible ? "비밀번호 숨기기" : "비밀번호 표시"}
          disabled={disabled || readOnly}
          onClick={toggle}
        >
          {isVisible ? (
            <EyeOff aria-hidden="true" />
          ) : (
            <Eye aria-hidden="true" />
          )}
        </TextFieldIconButton>
      }
    />
  );
}

export type TextFieldButtonProps = Omit<
  React.ComponentProps<"button">,
  "value" | "onChange"
> & {
  label?: React.ReactNode;
  value?: React.ReactNode;
  placeholder?: React.ReactNode;
  helperText?: React.ReactNode;
  error?: React.ReactNode;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
};

function TextFieldButton({
  label,
  value,
  placeholder,
  helperText,
  error,
  prefix,
  suffix,
  id,
  className,
  disabled,
  ...props
}: TextFieldButtonProps) {
  const generatedId = React.useId();
  const fieldId = id ?? generatedId;
  const messageId = fieldId + "-message";
  return (
    <div className="grid gap-[var(--ien-control-gap)]">
      {label && (
        <label
          htmlFor={fieldId}
          className="text-ien-label2 text-ien-text-primary"
        >
          {label}
        </label>
      )}
      <button
        {...props}
        id={fieldId}
        type={props.type ?? "button"}
        disabled={disabled}
        aria-describedby={error || helperText ? messageId : undefined}
        aria-invalid={Boolean(error) || undefined}
        className={cn(
          "flex min-h-[var(--ien-state-minimum-touch-target)] w-full items-center gap-ien-xs rounded-ien-default border border-ien-border bg-ien-surface px-ien-sm text-left text-ien-body2 outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:cursor-not-allowed disabled:opacity-50",
          error && "border-ien-danger",
          className,
        )}
      >
        {prefix && (
          <span className="shrink-0 text-ien-text-secondary">{prefix}</span>
        )}
        <span
          className={cn(
            "min-w-0 flex-1 truncate",
            value ? "text-ien-text-primary" : "text-ien-text-tertiary",
          )}
        >
          {value ?? placeholder}
        </span>
        {suffix ?? (
          <ChevronDown
            aria-hidden="true"
            className="size-4 shrink-0 text-ien-text-tertiary"
          />
        )}
      </button>
      {(error || helperText) && (
        <p
          id={messageId}
          className={cn(
            "text-ien-caption",
            error ? "text-ien-danger" : "text-ien-text-secondary",
          )}
        >
          {error ?? helperText}
        </p>
      )}
    </div>
  );
}

export type TextFieldIconButtonProps = React.ComponentProps<typeof Button> & {
  label: string;
  loading?: boolean;
  onLongPress?: () => void;
};

function TextFieldIconButton({
  label,
  loading = false,
  onLongPress,
  disabled,
  onContextMenu,
  onPointerDown,
  onPointerUp,
  onPointerLeave,
  onPointerCancel,
  children,
  ...props
}: TextFieldIconButtonProps) {
  const timer = React.useRef<number | undefined>(undefined);
  const clearLongPress = () => {
    window.clearTimeout(timer.current);
    timer.current = undefined;
  };
  React.useEffect(() => clearLongPress, []);
  return (
    <Button
      {...props}
      type={props.type ?? "button"}
      variant={props.variant ?? "ghost"}
      tone={props.tone ?? "neutral"}
      size={props.size ?? "icon-sm"}
      aria-label={label}
      disabled={disabled || loading}
      onPointerDown={(event) => {
        onPointerDown?.(event);
        if (!event.defaultPrevented && !disabled && !loading && onLongPress)
          timer.current = window.setTimeout(onLongPress, 500);
      }}
      onPointerUp={(event) => {
        clearLongPress();
        onPointerUp?.(event);
      }}
      onPointerLeave={(event) => {
        clearLongPress();
        onPointerLeave?.(event);
      }}
      onPointerCancel={(event) => {
        clearLongPress();
        onPointerCancel?.(event);
      }}
      onContextMenu={(event) => {
        clearLongPress();
        onContextMenu?.(event);
        if (!onContextMenu) event.preventDefault();
      }}
    >
      {loading ? (
        <LoaderCircle aria-hidden="true" className="size-4 animate-spin" />
      ) : (
        children
      )}
    </Button>
  );
}

export type TextFieldClearButtonProps = Omit<
  TextFieldIconButtonProps,
  "children" | "label" | "loading" | "onLongPress"
> & { visible: boolean; onClick: () => void; label?: string };

function TextFieldClearButton({
  visible,
  onClick,
  disabled = false,
  label = "입력 지우기",
  ...buttonProps
}: TextFieldClearButtonProps) {
  if (!visible) return null;
  return (
    <TextFieldIconButton
      {...buttonProps}
      label={label}
      disabled={disabled}
      onClick={onClick}
    >
      <X aria-hidden="true" />
    </TextFieldIconButton>
  );
}

export {
  TextField,
  TextField as IenTextField,
  ClearableTextField,
  ClearableTextField as IenClearableTextField,
  PasswordTextField,
  PasswordTextField as IenPasswordTextField,
  TextFieldButton,
  TextFieldButton as IenTextFieldButton,
  TextFieldIconButton,
  TextFieldIconButton as IenTextFieldIconButton,
  TextFieldClearButton,
  TextFieldClearButton as IenTextFieldClearButton,
};
