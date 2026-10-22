"use client";

import * as React from "react";
import { ArrowUp } from "lucide-react";
import { Textarea } from "@/components/ui/textarea";
import { Fab } from "@/components/ui/fab";
import { cn } from "@/lib/ien-utils";

export type ChatBottomBarInputState = {
  enabled?: boolean;
  readOnly?: boolean;
  status?:
    | { type: "normal" }
    | { type: "error"; message: string }
    | { type: "success"; message?: string };
};
export type ChatBottomBarSendState = { enabled?: boolean; loading?: boolean };

export type ChatBottomBarProps = Omit<
  React.ComponentProps<"form">,
  "children" | "onSubmit" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLFormElement>;
  textareaRef?: React.Ref<HTMLTextAreaElement>;
  value?: string;
  defaultValue?: string;
  onValueChange?: (value: string) => void;
  onSend: (message: string) => void | Promise<void>;
  placeholder?: string;
  disabled?: boolean;
  leading?: React.ReactNode;
  leadingContent?: React.ReactNode;
  trailingContent?: React.ReactNode;
  sendButtonContent?: React.ReactNode;
  inputState?: ChatBottomBarInputState;
  sendState?: ChatBottomBarSendState;
  maxLines?: number;
  visible?: boolean;
  windowInsets?: boolean;
  inputClassName?: string;
  inputStyle?: React.CSSProperties;
  keyboardOptions?: {
    capitalization?: "none" | "characters" | "words" | "sentences";
    imeAction?: "send" | "default";
  };
  className?: string;
  onSendError?: (error: unknown) => void;
};

function ChatBottomBar({
  value,
  defaultValue = "",
  onValueChange,
  onSend,
  placeholder = "메시지를 입력하세요",
  disabled = false,
  leading,
  leadingContent,
  trailingContent,
  sendButtonContent,
  inputState,
  sendState,
  maxLines = 4,
  visible = true,
  windowInsets = true,
  inputClassName,
  inputStyle,
  keyboardOptions = { capitalization: "sentences", imeAction: "send" },
  className,
  onSendError,
  ref,
  textareaRef,
  style: rootStyle,
  ...formProps
}: ChatBottomBarProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [sending, setSending] = React.useState(false);
  const [sendError, setSendError] = React.useState<string | undefined>(
    undefined,
  );
  const current = value ?? internalValue;
  const inputEnabled = !disabled && inputState?.enabled !== false;
  const inputReadOnly = inputState?.readOnly === true;
  const statusError =
    inputState?.status?.type === "error" ? inputState.status.message : undefined;
  const statusSuccess =
    inputState?.status?.type === "success" ? inputState.status.message : undefined;
  const sendEnabled = !disabled && (sendState?.enabled ?? current.trim().length > 0);
  const isSending = sending || sendState?.loading === true;
  const rowLimit = Number.isFinite(maxLines)
    ? Math.max(1, Math.floor(maxLines))
    : 4;
  const update = (next: string) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
    setSendError(undefined);
  };
  const submit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const message = current.trim();
    if ((!message && sendState?.enabled !== true) || isSending || !sendEnabled) {
      return;
    }
    setSending(true);
    try {
      await onSend(message);
      update("");
    } catch (error) {
      setSendError(
        error instanceof Error && error.message
          ? error.message
          : "메시지를 전송하지 못했습니다.",
      );
      onSendError?.(error);
    } finally {
      setSending(false);
    }
  };
  return (
    <form
      {...formProps}
      ref={ref}
      data-slot="chat-bottom-bar"
      onSubmit={submit}
      className={cn(
        "overflow-hidden rounded-ien-full border border-ien-border bg-ien-surface p-ien-xs shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.12)] transition-[margin,padding,border-color,box-shadow] duration-[var(--ien-motion-normal)] motion-reduce:transition-none",
        !visible && "m-0 border-transparent p-0 shadow-none",
        className,
      )}
      style={{
        ...rootStyle,
        marginInline: !visible
          ? "0px"
          : rootStyle?.marginInline ??
            (windowInsets
              ? "max(var(--ien-spacing-md), env(safe-area-inset-left), env(safe-area-inset-right))"
              : "var(--ien-spacing-md)"),
        marginBottom: !visible
          ? "0px"
          : rootStyle?.marginBottom ??
            (windowInsets
              ? "max(var(--ien-spacing-xs), env(safe-area-inset-bottom))"
              : "var(--ien-spacing-xs)"),
      }}
    >
      <div
        aria-hidden={!visible || undefined}
        inert={!visible || undefined}
        className="grid transition-[grid-template-rows,opacity,translate] duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none"
        style={{
          gridTemplateRows: visible ? "1fr" : "0fr",
          opacity: visible ? 1 : 0,
          translate: visible ? "0 0" : "0 100%",
        }}
      >
        <div className="min-h-0 overflow-hidden">
          <div className="flex min-h-16 items-end gap-ien-xxs px-ien-sm py-ien-xs">
            {leadingContent ?? leading}
            <Textarea
              ref={textareaRef}
              value={current}
              disabled={!inputEnabled || isSending}
              readOnly={inputReadOnly}
              rows={1}
              placeholder={placeholder}
              aria-label="메시지"
              aria-invalid={statusError ? true : undefined}
              aria-describedby={
                statusError || statusSuccess || sendError
                  ? "chat-bottom-bar-input-status"
                  : undefined
              }
              autoCapitalize={keyboardOptions.capitalization}
              onChange={(event) => update(event.currentTarget.value)}
              onKeyDown={(event) => {
                if (
                  event.key === "Enter" &&
                  keyboardOptions.imeAction === "send" &&
                  !event.shiftKey &&
                  !event.nativeEvent.isComposing
                ) {
                  event.preventDefault();
                  event.currentTarget.form?.requestSubmit();
                }
              }}
              className={cn(
                "min-h-14 min-w-0 flex-1 resize-none rounded-ien-xl bg-ien-surface-weak",
                inputClassName,
              )}
              style={{
                ...inputStyle,
                maxHeight: `${rowLimit * 24}px`,
              }}
            />
            {trailingContent}
            <Fab
              type="submit"
              size="regular"
              label="메시지 전송"
              aria-label="메시지 전송"
              state={{ enabled: sendEnabled, loading: isSending }}
              disabled={!sendEnabled || isSending}
              className="shrink-0"
            >
              {sendButtonContent ?? <ArrowUp aria-hidden="true" />}
            </Fab>
          </div>
          {(statusError || statusSuccess || sendError) && (
            <p
              id="chat-bottom-bar-input-status"
              role={statusError || sendError ? "alert" : "status"}
              aria-live={statusError || sendError ? "assertive" : "polite"}
              className={cn(
                "px-ien-sm pb-ien-xs text-ien-caption",
                statusError || sendError ? "text-ien-danger" : "text-ien-success",
              )}
            >
              {sendError ?? statusError ?? statusSuccess}
            </p>
          )}
        </div>
      </div>
    </form>
  );
}

export { ChatBottomBar, ChatBottomBar as IenChatBottomBar };
