import * as React from "react"
import { IconTile } from "@/components/reui/icon-tile"

export type AssetFrameProps = Omit<React.ComponentProps<typeof IconTile>, "size" | "radius" | "variant"> & { size?: "small" | "medium" | "large" | "extra-large"; shape?: "rounded" | "circle"; bordered?: boolean }
function AssetFrame({ size = "medium", shape = "rounded", bordered = false, tone = "neutral", style, ...props }: AssetFrameProps) {
  const length = { small: 32, medium: 44, large: 56, "extra-large": 72 }[size]
  return <IconTile {...props} tone={tone} variant="weak" radius={shape === "circle" ? "full" : "default"} style={{ "--icon-tile-size": `${length}px`, "--icon-tile-icon-size": `${length / 2}px`, "--icon-tile-radius": shape === "circle" ? "999px" : "var(--ien-radius-lg)", border: bordered ? "var(--ien-stroke-thin) solid var(--ien-border)" : undefined, ...style } as React.CSSProperties} />
}
export { AssetFrame }
