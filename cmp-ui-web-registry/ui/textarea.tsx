import type { IenToneProps } from "@/lib/ien-theme"
import * as React from "react"
import { cn } from "cn"

function Textarea({
  variant = "boxed",
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"textarea"> &
  IenToneProps & { variant?: "boxed" | "underline" | "plain" }) {
  return (
    <textarea
      data-slot="textarea"
      data-variant={variant}
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn(
        "border-input dark:bg-input/30 focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:aria-invalid:border-destructive/50 disabled:bg-input/50 dark:disabled:bg-input/80 rounded-lg border bg-transparent px-2.5 py-2 text-base transition-colors focus-visible:ring-3 aria-invalid:ring-3 md:text-sm flex field-sizing-content min-h-16 w-full outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed disabled:opacity-(--ien-state-disabled-alpha)",
        variant === "underline"
          ? "rounded-none border-x-0 border-t-0 bg-transparent px-0 dark:bg-transparent"
          : variant === "plain"
            ? "border-transparent bg-transparent shadow-none focus-visible:border-transparent dark:bg-transparent"
            : "",
        className,
      )}
      {...props}
    />
  )
}

export { Textarea }
