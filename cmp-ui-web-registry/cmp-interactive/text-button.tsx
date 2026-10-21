"use client";

import * as React from "react";
import { ArrowRight } from "lucide-react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "cn";

export type TextButtonProps = Omit<ButtonProps, "variant"> & {
  emphasis?: "normal" | "underline" | "arrow";
  size?: ButtonProps["size"];
};

function TextButton({
  emphasis = "normal",
  className,
  children,
  ...props
}: TextButtonProps) {
  return (
    <Button
      {...props}
      variant="ghost"
      className={cn(
        "min-h-[var(--ien-state-minimum-touch-target)] px-ien-xs text-ien-label1 text-ien-brand hover:bg-ien-brand-weak",
        emphasis === "underline" && "underline underline-offset-4",
        emphasis === "arrow" && "gap-ien-xxs",
        className,
      )}
    >
      {children}
      {emphasis === "arrow" && (
        <ArrowRight aria-hidden="true" className="size-4" />
      )}
    </Button>
  );
}

export { TextButton, TextButton as IenTextButton };
