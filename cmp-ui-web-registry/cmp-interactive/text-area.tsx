"use client";

import * as React from "react";
import { Textarea } from "@/components/ui/textarea";
import { cn } from "cn";

export type TextAreaProps = React.ComponentProps<typeof Textarea> & {
  label?: React.ReactNode;
  helperText?: React.ReactNode;
  error?: React.ReactNode;
};

function TextArea({
  label,
  helperText,
  error,
  id,
  className,
  ...props
}: TextAreaProps) {
  const generatedId = React.useId();
  const fieldId = id ?? generatedId;
  const messageId = `${fieldId}-message`;
  const message = error ?? helperText;
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
      <Textarea
        {...props}
        id={fieldId}
        aria-invalid={Boolean(error) || undefined}
        aria-describedby={message ? messageId : undefined}
        className={cn(
          "min-h-24 resize-y rounded-ien-default bg-ien-surface text-ien-body2",
          error && "border-ien-danger focus-visible:ring-ien-danger",
          className,
        )}
      />
      {message && (
        <p
          id={messageId}
          className={cn(
            "text-ien-caption",
            error ? "text-ien-danger" : "text-ien-text-secondary",
          )}
        >
          {message}
        </p>
      )}
    </div>
  );
}

export { TextArea, TextArea as IenTextArea };
