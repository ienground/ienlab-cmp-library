"use client"

import * as React from "react"
import { Toggle } from "radix-ui"
import { Button } from "@/components/ui/button"
import type { IenVariant } from "@/lib/ien-theme"

export type ToggleButtonProps = Omit<React.ComponentProps<typeof Button>, "asChild" | "value" | "defaultValue" | "onChange"> & {
  pressed?: boolean
  defaultPressed?: boolean
  onPressedChange?: (pressed: boolean) => void
  checkedVariant?: IenVariant
  uncheckedVariant?: IenVariant
}

function ToggleButton({ pressed, defaultPressed = false, onPressedChange, checkedVariant = "fill", uncheckedVariant = "weak", variant, disabled, loading, ...props }: ToggleButtonProps) {
  const [local, setLocal] = React.useState(defaultPressed)
  const selected = pressed ?? local
  return <Toggle.Root asChild pressed={selected} disabled={disabled || loading} onPressedChange={(next) => { if (pressed === undefined) setLocal(next); onPressedChange?.(next) }}>
    <Button {...props} disabled={disabled} loading={loading} variant={variant ?? (selected ? checkedVariant : uncheckedVariant)} />
  </Toggle.Root>
}

function IconToggleButton({ size = "icon", ...props }: ToggleButtonProps) {
  return <ToggleButton {...props} size={size} />
}

export { ToggleButton, IconToggleButton }
