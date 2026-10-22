"use client";

import * as React from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  PasswordTextField,
  TextField,
  type IenTextFieldLengthLimit,
  type IenTextFieldState,
  type IenTextFieldVariant,
} from "@/components/ui/text-field";
import { Button } from "@/components/ui/button";

export type TextFieldDialogField = {
  initialValue?: string;
  onValueChange?: (value: string) => string | null | void;
  valid?: (value: string) => boolean;
  placeholder?: string;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  keyboardType?:
    | "text"
    | "number"
    | "decimal"
    | "email"
    | "phone"
    | "uri"
    | "password"
    | "number-password";
  minLines?: number;
  maxLines?: number;
  enabled?: boolean;
  label?: React.ReactNode;
  state?: IenTextFieldState;
  variant?: IenTextFieldVariant;
  lengthLimit?: IenTextFieldLengthLimit;
};

export type TextFieldDialogProps = Omit<
  React.ComponentProps<typeof DialogContent>,
  "children" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLDivElement>;
  open?: boolean;
  visible?: boolean;
  defaultOpen?: boolean;
  onOpenChange?: (open: boolean) => void;
  onDismiss?: () => void;
  onDismissRequest?: () => void;
  onClose?: () => void;
  value?: string;
  defaultValue?: string;
  textFields?: Record<string, TextFieldDialogField>;
  values?: Record<string, string>;
  defaultValues?: Record<string, string>;
  onValuesChange?: (values: Record<string, string>) => void;
  onConfirm?: (value: string) => void;
  onConfirmFields?: (values: Record<string, string>) => void;
  title?: React.ReactNode;
  description?: React.ReactNode;
  message?: React.ReactNode;
  icon?: React.ReactNode;
  label?: React.ReactNode;
  placeholder?: string;
  confirmLabel?: string;
  cancelLabel?: string;
  textConfirm?: string;
  textDismiss?: string;
  disabled?: boolean;
  enabledConfirm?: boolean;
  enabledDismiss?: boolean;
  inputRef?: React.Ref<HTMLInputElement>;
  buttonLayout?: "horizontal" | "vertical";
  closeOnDimmerClick?: boolean;
  closeOnBackEvent?: boolean;
  onEntered?: () => void;
  onExited?: () => void;
};

function TextFieldDialog({
  open,
  visible,
  defaultOpen,
  onOpenChange,
  onDismiss,
  onDismissRequest,
  onClose,
  value,
  defaultValue = "",
  textFields,
  values,
  defaultValues,
  onValuesChange,
  onConfirm,
  onConfirmFields,
  title = "입력",
  description,
  message,
  icon,
  label = "내용",
  placeholder,
  confirmLabel,
  cancelLabel,
  textConfirm,
  textDismiss,
  disabled = false,
  enabledConfirm = true,
  enabledDismiss = true,
  ref,
  inputRef,
  buttonLayout = "horizontal",
  closeOnDimmerClick = true,
  closeOnBackEvent = true,
  onEntered,
  onExited,
  ...contentProps
}: TextFieldDialogProps) {
  const controlledOpen = visible ?? open;
  const [internalOpen, setInternalOpen] = React.useState(defaultOpen ?? false);
  const isOpenControlled = controlledOpen !== undefined;
  const isOpen = isOpenControlled ? controlledOpen : internalOpen;
  const entries = React.useMemo(
    () => Object.entries(textFields ?? {}),
    [textFields],
  );
  const initialValues = React.useMemo(() => {
    const result = Object.fromEntries(
      entries.map(([key, field]) => [
        key,
        values?.[key] ?? field.initialValue ?? defaultValues?.[key] ?? "",
      ]),
    );
    if (entries.length === 0) result.value = values?.value ?? value ?? defaultValue;
    return result;
  }, [entries, values, defaultValues, value, defaultValue]);
  const [drafts, setDrafts] = React.useState<Record<string, string>>(initialValues);
  const wasOpen = React.useRef(false);

  React.useEffect(() => {
    if (isOpen && !wasOpen.current) setDrafts(initialValues);
    wasOpen.current = isOpen;
  }, [isOpen, initialValues]);

  const isFieldsValid = entries.every(([key, field]) => {
    const current = drafts[key] ?? "";
    const overLimit =
      field.lengthLimit?.type === "error" &&
      current.length > field.lengthLimit.maxLength;
    return !overLimit && (field.valid?.(current) ?? true);
  });
  const canConfirm = enabledConfirm && isFieldsValid && !disabled;
  const handleOpenChange = (nextOpen: boolean, notifyDismiss = false) => {
    if (!isOpenControlled) setInternalOpen(nextOpen);
    onOpenChange?.(nextOpen);
    if (!nextOpen && notifyDismiss) {
      (onDismissRequest ?? onDismiss ?? onClose)?.();
    }
  };
  const dismiss = () => handleOpenChange(false, true);
  const handleConfirm = (event: React.FormEvent) => {
    event.preventDefault();
    if (!canConfirm) return;
    if (entries.length > 0) {
      const result = Object.fromEntries(
        entries.map(([key]) => [key, (drafts[key] ?? "").trim()]),
      );
      onConfirmFields?.(result);
      onValuesChange?.(result);
    } else {
      const nextValue = (drafts.value ?? "").trim();
      onConfirm?.(nextValue);
    }
    handleOpenChange(false);
  };

  const setInputRef: React.RefCallback<HTMLInputElement | HTMLTextAreaElement> =
    (element) => {
      if (typeof inputRef === "function") {
        inputRef(element?.tagName === "TEXTAREA" ? null : element as HTMLInputElement | null);
      } else if (inputRef) {
        inputRef.current = element?.tagName === "TEXTAREA" ? null : element as HTMLInputElement | null;
      }
    };

  return (
    <Dialog
      open={isOpen}
      onOpenChange={(nextOpen) => handleOpenChange(nextOpen, !nextOpen)}
    >
      <DialogContent
        {...contentProps}
        ref={ref}
        showCloseButton={false}
        closeOnDimmerClick={closeOnDimmerClick}
        closeOnBackEvent={closeOnBackEvent}
        onEntered={onEntered}
        onExited={onExited}
        className="max-w-sm rounded-ien-xl border-ien-border bg-ien-surface-raised text-ien-text-primary"
      >
        <form onSubmit={handleConfirm} className="grid gap-ien-md">
          {(icon || title || description || message) && (
            <DialogHeader className={icon ? "items-center text-center" : undefined}>
              {icon}
              {title && <DialogTitle className="text-ien-title2">{title}</DialogTitle>}
              {(description ?? message) && (
                <DialogDescription className="text-ien-body2 text-ien-text-secondary">
                  {description ?? message}
                </DialogDescription>
              )}
            </DialogHeader>
          )}
          <div className="grid gap-ien-sm">
            {entries.length > 0 ? (
              entries.map(([key, field], index) => {
                const commonProps = {
                  ref: index === 0 ? setInputRef : undefined,
                  value: drafts[key] ?? "",
                  label: field.label,
                  placeholder: field.placeholder,
                  prefix: field.prefix,
                  suffix: field.suffix,
                  state: {
                    ...field.state,
                    enabled: !disabled && field.enabled !== false && field.state?.enabled !== false,
                  },
                  variant: field.variant,
                  lengthLimit: field.lengthLimit,
                  singleLine: (field.maxLines ?? Number.MAX_SAFE_INTEGER) === 1,
                  minLines: field.minLines,
                  maxLines: field.maxLines ?? 8,
                  autoFocus: index === 0,
                  onValueChange: (next: string) => {
                    const accepted = field.onValueChange?.(next);
                    if (accepted === null) return;
                    setDrafts((current) => ({
                      ...current,
                      [key]: typeof accepted === "string" ? accepted : next,
                    }));
                  },
                };
                const type = field.keyboardType;
                if (type === "password" || type === "number-password") {
                  return (
                    <PasswordTextField
                      key={key}
                      {...commonProps}
                      inputMode={type === "number-password" ? "numeric" : undefined}
                    />
                  );
                }
                return (
                  <TextField
                    key={key}
                    {...commonProps}
                    type={type === "email" ? "email" : "text"}
                    inputMode={
                      type === "number"
                        ? "numeric"
                        : type === "decimal"
                          ? "decimal"
                          : type === "email"
                            ? "email"
                            : type === "phone"
                              ? "tel"
                              : type === "uri"
                                ? "url"
                                : undefined
                    }
                  />
                );
              })
            ) : (
              <TextField
                ref={setInputRef}
                autoFocus
                label={label}
                value={drafts.value ?? ""}
                placeholder={placeholder}
                state={{ enabled: !disabled }}
                onValueChange={(next) =>
                  setDrafts((current) => ({ ...current, value: next }))
                }
              />
            )}
          </div>
          <DialogFooter
            className={
              buttonLayout === "horizontal"
                ? "-mx-ien-md -mb-ien-md flex-row border-t-0 bg-transparent"
                : "-mx-ien-md -mb-ien-md flex-col border-t-0 bg-transparent"
            }
          >
            <Button
              type="button"
              variant="weak"
              tone="neutral"
              disabled={!enabledDismiss}
              onClick={dismiss}
              className={buttonLayout === "horizontal" ? "flex-1" : undefined}
              size="default"
            >
              {textDismiss ?? cancelLabel ?? "취소"}
            </Button>
            <Button
              type="submit"
              disabled={!canConfirm}
              className={buttonLayout === "horizontal" ? "flex-1" : undefined}
              size="default"
            >
              {textConfirm ?? confirmLabel ?? "확인"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export { TextFieldDialog, TextFieldDialog as IenTextFieldDialog };
