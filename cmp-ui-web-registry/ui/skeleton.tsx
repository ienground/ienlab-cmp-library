import type { IenToneProps } from "@/lib/ien-theme"
import { cn } from "cn"

function Skeleton({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"div"> & IenToneProps) {
  return (
    <div
      data-slot="skeleton"
      data-ien=""
      data-ien-tone={ienTone}
      data-ien-gradient={ienUseGradient}
      className={cn("bg-muted rounded-md animate-pulse", className)}
      {...props}
    />
  )
}

export { Skeleton }
