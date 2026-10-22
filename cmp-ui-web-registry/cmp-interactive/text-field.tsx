"use client";

import * as React from "react";
import { ChevronDown, Eye, EyeOff, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type TextFieldStatus =
  | { type: "normal" }
  | { type: "error"; message: string }
  | { type: "success"; message?: string };

export type IenTextFieldVariant = "box" | "line" | "big" | "hero";
export type IenTextFieldLabelOption = "appear" | "sustain";
export type IenTextFieldState = { enabled?: boolean; readOnly?: boolean; status?: TextFieldStatus };
export type IenTextFieldFormat = { transform: (value: string) => string; reset?: (formattedValue: string) => string };
export type IenTextFieldLengthLimit = { type: "none" } | { type: "error" | "block"; maxLength: number };
export type TextFieldProps = Omit<
  React.InputHTMLAttributes<HTMLInputElement | HTMLTextAreaElement>,
  "value" | "defaultValue" | "onChange" | "prefix" | "size"
> & {
  ref?: React.Ref<HTMLInputElement | HTMLTextAreaElement>;
  label?: React.ReactNode;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  onChange?: React.ChangeEventHandler<HTMLInputElement | HTMLTextAreaElement>;
  helperText?: React.ReactNode;
  help?: React.ReactNode;
  supportingText?: React.ReactNode;
  error?: React.ReactNode;
  hasError?: boolean;
  status?: TextFieldStatus;
  state?: IenTextFieldState;
  requiredMark?: boolean;
  variant?: IenTextFieldVariant;
  labelOption?: IenTextFieldLabelOption;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  leading?: React.ReactNode;
  trailing?: React.ReactNode;
  right?: React.ReactNode;
  format?: IenTextFieldFormat;
  lengthLimit?: IenTextFieldLengthLimit;
  paddingTop?: number | string;
  paddingBottom?: number | string;
  singleLine?: boolean;
  minLines?: number;
  maxLines?: number;
  rows?: number;
  readOnlyTextSelectionEnabled?: boolean;
  inputClassName?: string;
};

function TextField({
  label,
  value,
  defaultValue,
  onValueChange,
  onChange,
  helperText,
  help,
  supportingText,
  error,
  hasError = false,
  status,
  state,
  requiredMark,
  required,
  variant = "box",
  labelOption = "appear",
  prefix,
  suffix,
  leading,
  trailing,
  right,
  format,
  lengthLimit = { type: "none" },
  paddingTop,
  paddingBottom,
  singleLine = true,
  minLines = 1,
  maxLines = singleLine ? 1 : 8,
  rows,
  readOnlyTextSelectionEnabled = true,
  inputClassName,
  id,
  className,
  readOnly,
  disabled,
  onFocus,
  onBlur,
  onCompositionStart,
  onCompositionEnd,
  type,
  ref: forwardedRef,
  style,
  ...props
}: TextFieldProps) {
  const generatedId = React.useId();
  const fieldId = id ?? generatedId;
  const messageId = fieldId + "-message";
  const counterId = fieldId + "-counter";
  const [internalValue, setInternalValue] = React.useState(defaultValue ?? "");
  const [focused, setFocused] = React.useState(false);
  const [composing, setComposing] = React.useState(false);
  const inputRef = React.useRef<HTMLInputElement | HTMLTextAreaElement>(null);
  const frameRef = React.useRef<HTMLDivElement>(null);
  const caret = React.useRef<{ start: number; end: number } | null>(null);
  const current = value ?? internalValue;
  const maxLength = lengthLimit.type === "none" ? undefined : Math.max(0, Math.trunc(Number.isFinite(lengthLimit.maxLength) ? lengthLimit.maxLength : 0));
  const exceeded = lengthLimit.type === "error" && maxLength !== undefined && current.length > maxLength;
  const fieldStatus = status ?? state?.status;
  const invalid = hasError || error != null || fieldStatus?.type === "error" || exceeded;
  const feedback = error ?? (fieldStatus?.type === "error" ? fieldStatus.message : fieldStatus?.type === "success" ? fieldStatus.message : undefined) ?? help ?? supportingText ?? helperText;
  const isDisabled = disabled || state?.enabled === false;
  const isReadOnly = readOnly || state?.readOnly;
  const showLabel = labelOption === "sustain" || focused || current.length > 0;
  const displayed = format && !composing ? format.transform(current) : current;
  const setRef = React.useCallback((element: HTMLInputElement | HTMLTextAreaElement | null) => {
    inputRef.current = element;
    if (typeof forwardedRef === "function") return forwardedRef(element);
    if (forwardedRef) forwardedRef.current = element;
  }, [forwardedRef]);
  React.useLayoutEffect(() => {
    const element = inputRef.current;
    if (!caret.current || !element || element.selectionStart === null) return;
    element.setSelectionRange(caret.current.start, caret.current.end);
    caret.current = null;
  }, [displayed]);
  React.useEffect(() => {
    if (!invalid || !frameRef.current || window.matchMedia("(prefers-reduced-motion: reduce)").matches) return;
    const animation = frameRef.current.animate([{ translate: "0" }, { translate: "-8px" }, { translate: "8px" }, { translate: "-6px" }, { translate: "6px" }, { translate: "-3px" }, { translate: "3px" }, { translate: "0" }], { duration: 280, easing: "linear" });
    return () => animation.cancel();
  }, [invalid]);
  const verticalPadding = variant === "hero" ? 22 : variant === "big" ? 18 : 14;
  const typography = variant === "hero" ? "display" : variant === "big" ? "title2" : "body2";
  const supportingColor = invalid ? "text-ien-danger" : fieldStatus?.type === "success" ? "text-ien-success" : "text-ien-text-tertiary";
  const input = {
    ...props,
    id: fieldId,
    ref: setRef,
    value: displayed,
    disabled: isDisabled,
    readOnly: isReadOnly,
    required: required || requiredMark,
    placeholder: props.placeholder ?? (typeof label === "string" && !showLabel ? label : undefined),
    "aria-invalid": invalid || props["aria-invalid"] || undefined,
    "aria-describedby": [props["aria-describedby"], feedback != null ? messageId : undefined, maxLength !== undefined ? counterId : undefined].filter(Boolean).join(" ") || undefined,
    onFocus: (event: React.FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => { setFocused(true); onFocus?.(event); },
    onBlur: (event: React.FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => { setFocused(false); onBlur?.(event); },
    onCompositionStart: (event: React.CompositionEvent<HTMLInputElement | HTMLTextAreaElement>) => { setComposing(true); onCompositionStart?.(event); },
    onCompositionEnd: (event: React.CompositionEvent<HTMLInputElement | HTMLTextAreaElement>) => { setComposing(false); onCompositionEnd?.(event); },
    onChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      onChange?.(event);
      if (event.defaultPrevented || isDisabled || isReadOnly) return;
      const next = format?.reset?.(event.currentTarget.value) ?? event.currentTarget.value;
      if (lengthLimit.type === "block" && maxLength !== undefined && next.length > maxLength && !composing) return;
      if (format && !composing && event.currentTarget.selectionStart !== null) {
        const rawPrefix = (end: number) => format.reset?.(event.currentTarget.value.slice(0, end)) ?? event.currentTarget.value.slice(0, end);
        caret.current = { start: format.transform(rawPrefix(event.currentTarget.selectionStart)).length, end: format.transform(rawPrefix(event.currentTarget.selectionEnd ?? event.currentTarget.selectionStart)).length };
      }
      if (value === undefined) setInternalValue(next);
      onValueChange?.(next);
    },
    className: cn("h-auto min-w-0 flex-1 rounded-none border-0 bg-transparent p-0 shadow-none outline-none placeholder:text-ien-text-tertiary focus-visible:border-transparent focus-visible:ring-0 disabled:bg-transparent disabled:opacity-100", variant === "hero" ? "text-ien-display" : variant === "big" ? "text-ien-title2" : "text-ien-body2", isReadOnly && !readOnlyTextSelectionEnabled && "select-none", !singleLine && "resize-none overflow-y-auto [field-sizing:content]", inputClassName),
    style: { padding: 0, backgroundColor: "transparent", minHeight: singleLine ? undefined : `calc(var(--ien-line-height-${typography}) * ${Math.max(1, minLines)})`, maxHeight: singleLine ? undefined : `calc(var(--ien-line-height-${typography}) * ${Math.max(minLines, maxLines)})`, ...style },
  };
  return (
    <div ref={frameRef} className={cn("grid min-w-0 gap-ien-xxs", className)} data-slot="text-field" data-variant={variant} data-state={invalid ? "error" : fieldStatus?.type ?? "normal"}>
      {label != null && <label htmlFor={fieldId} className={cn("overflow-hidden text-ien-label2 text-ien-text-secondary transition-[max-height,opacity,translate] duration-150 motion-reduce:transition-none", showLabel ? "max-h-8 translate-y-0 opacity-100" : "max-h-0 -translate-y-1 opacity-0")}>
        {label}{(required || requiredMark) && <span aria-hidden="true" className="ml-ien-xxs text-ien-danger">*</span>}
      </label>}
      <div className={cn("flex min-w-0 items-center gap-ien-xs transition-colors duration-[var(--ien-motion-fast)] motion-reduce:transition-none", variant === "line" ? "border-b bg-transparent" : "rounded-ien-default border bg-ien-surface", isDisabled && variant !== "line" && "bg-ien-surface-weak", invalid ? "border-ien-danger" : fieldStatus?.type === "success" ? "border-ien-success" : focused ? "border-ien-brand" : "border-ien-border", isDisabled ? "text-ien-text-disabled" : "text-ien-text-primary")} style={{ minHeight: singleLine && (variant === "box" || variant === "line") ? 52 : undefined, paddingInline: variant === "line" ? 0 : "calc(var(--ien-spacing-sm) + var(--ien-spacing-xxxs))", paddingTop: paddingTop ?? verticalPadding, paddingBottom: paddingBottom ?? verticalPadding }}>
        {leading}
        {prefix != null && <span className="shrink-0 text-ien-text-secondary">{prefix}</span>}
        {singleLine ? <Input {...input} type={type} /> : <Textarea {...input} rows={rows ?? minLines} />}
        {suffix != null && <span className="shrink-0 text-ien-text-secondary">{suffix}</span>}
        {trailing ?? right}
      </div>
      {(feedback != null || maxLength !== undefined) && <div className="flex items-start gap-ien-xs text-ien-caption">
        {feedback != null && <p id={messageId} className={cn("min-w-0 flex-1", supportingColor)}>{feedback}</p>}
        {maxLength !== undefined && <span id={counterId} className={cn("ml-auto shrink-0 tabular-nums", exceeded ? "text-ien-danger" : "text-ien-text-tertiary")}>{current.length}/{maxLength}</span>}
      </div>}
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
        current && !disabled && !readOnly && props.state?.enabled !== false && !props.state?.readOnly ? (
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
    if (disabled || readOnly || props.state?.enabled === false || props.state?.readOnly) return;
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
          disabled={disabled || readOnly || props.state?.enabled === false || props.state?.readOnly}
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

export type TextFieldButtonProps = Omit<React.ComponentProps<"button">, "value" | "onChange" | "prefix"> & {
  label?: React.ReactNode;
  value?: React.ReactNode;
  placeholder?: React.ReactNode;
  helperText?: React.ReactNode;
  help?: React.ReactNode;
  error?: React.ReactNode;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  right?: React.ReactNode;
  required?: boolean;
  labelOption?: IenTextFieldLabelOption;
  variant?: IenTextFieldVariant;
  state?: IenTextFieldState;
  lengthLimit?: IenTextFieldLengthLimit;
};
function TextFieldButton({ label, value, placeholder, helperText, help, error, prefix, suffix, right = <ChevronDown aria-hidden="true" className="size-4 shrink-0 text-ien-text-tertiary" />, required, labelOption = "appear", variant = "box", state, lengthLimit, id, className, disabled, onFocus, onBlur, ...props }: TextFieldButtonProps) {
  const generatedId = React.useId();
  const [focused, setFocused] = React.useState(false);
  const fieldId = id ?? generatedId;
  const messageId = fieldId + "-message";
  const showLabel = labelOption === "sustain" || focused || value != null && value !== "";
  const fieldError = error ?? (state?.status?.type === "error" ? state.status.message : undefined);
  const feedback = fieldError ?? help ?? helperText;
  const count = typeof value === "string" ? value.length : 0;
  const maxLength = lengthLimit && lengthLimit.type !== "none" ? lengthLimit.maxLength : undefined;
  const invalid = fieldError != null || lengthLimit?.type === "error" && maxLength !== undefined && count > maxLength;
  return <div className="grid gap-ien-xxs">
    {label != null && <label htmlFor={fieldId} className={cn("overflow-hidden text-ien-label2 text-ien-text-secondary transition-[max-height,opacity,translate] duration-150 motion-reduce:transition-none", showLabel ? "max-h-8 translate-y-0 opacity-100" : "max-h-0 -translate-y-1 opacity-0")}>{label}{required && <span aria-hidden="true" className="ml-ien-xxs text-ien-danger">*</span>}</label>}
    <button {...props} id={fieldId} type={props.type ?? "button"} disabled={disabled || state?.enabled === false} aria-describedby={feedback != null ? messageId : undefined} aria-invalid={invalid || undefined} onFocus={(event) => { setFocused(true); onFocus?.(event); }} onBlur={(event) => { setFocused(false); onBlur?.(event); }} className={cn("flex w-full items-center gap-ien-xs text-left outline-none transition-colors duration-[var(--ien-motion-fast)] focus-visible:border-ien-brand disabled:cursor-not-allowed disabled:bg-ien-surface-weak disabled:text-ien-text-disabled", variant === "line" ? "border-b px-0 py-[14px]" : "rounded-ien-default border bg-ien-surface px-[14px]", variant === "hero" ? "py-[22px] text-ien-display" : variant === "big" ? "py-[18px] text-ien-title2" : "py-[14px] text-ien-body2", invalid ? "border-ien-danger" : "border-ien-border", className)}>
      {prefix != null && <span className="shrink-0 text-ien-text-secondary">{prefix}</span>}
      <span className={cn("min-w-0 flex-1 truncate", value ? "text-ien-text-primary" : "text-ien-text-tertiary")}>{value ?? placeholder ?? (!showLabel ? label : undefined)}</span>
      {suffix != null && <span className="shrink-0 text-ien-text-secondary">{suffix}</span>}{right}
    </button>
    {(feedback != null || maxLength !== undefined) && <div className="flex items-start justify-between gap-ien-xs text-ien-caption">{feedback != null && <p id={messageId} className={invalid ? "text-ien-danger" : "text-ien-text-tertiary"}>{feedback}</p>}{maxLength !== undefined && <span className={cn("ml-auto tabular-nums", invalid ? "text-ien-danger" : "text-ien-text-tertiary")}>{count}/{maxLength}</span>}</div>}
  </div>;
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
      tone={props.tone ?? "brand"}
      size={props.size ?? "icon-sm"}
      aria-label={label}
      disabled={disabled}
      loading={loading}
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
      {children}
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
