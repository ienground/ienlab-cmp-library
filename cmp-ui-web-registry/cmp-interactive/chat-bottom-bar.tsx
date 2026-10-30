"use client";

import * as React from "react";
import { Send } from "lucide-react";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { cn } from "cn";

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
  className,
  onSendError,
  ref,
  textareaRef,
  ...formProps
}: ChatBottomBarProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [sending, setSending] = React.useState(false);
  const [sendError, setSendError] = React.useState<string | undefined>(
    undefined,
  );
  const current = value ?? internalValue;
  const update = (next: string) => {
    if (value === undefined) setInternalValue(next);
    onValueChange?.(next);
    setSendError(undefined);
  };
  const submit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const message = current.trim();
    if (!message || sending || disabled) return;
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
        "flex flex-wrap items-end gap-ien-xs border-t border-ien-border bg-ien-surface p-ien-sm pb-[max(var(--ien-spacing-sm),env(safe-area-inset-bottom))]",
        className,
      )}
    >
      {leading}
      <Textarea
        ref={textareaRef}
        value={current}
        disabled={disabled || sending}
        rows={1}
        placeholder={placeholder}
        aria-label="메시지"
        onChange={(event) => update(event.currentTarget.value)}
        onKeyDown={(event) => {
          if (
            event.key === "Enter" &&
            !event.shiftKey &&
            !event.nativeEvent.isComposing
          ) {
            event.preventDefault();
            event.currentTarget.form?.requestSubmit();
          }
        }}
        className="max-h-32 min-h-[var(--ien-state-minimum-touch-target)] resize-none rounded-ien-xl bg-ien-surface-weak"
      />
      <Button
        type="submit"
        size="icon"
        aria-label="메시지 전송"
        disabled={disabled || sending || !current.trim()}
        className="size-[var(--ien-state-minimum-touch-target)] shrink-0 rounded-ien-full"
      >
        <Send aria-hidden="true" />
      </Button>
      {sendError && (
        <p
          role="alert"
          aria-live="assertive"
          className="basis-full text-ien-caption text-ien-danger"
        >
          {sendError}
        </p>
      )}
    </form>
  );
}

export { ChatBottomBar, ChatBottomBar as IenChatBottomBar };
