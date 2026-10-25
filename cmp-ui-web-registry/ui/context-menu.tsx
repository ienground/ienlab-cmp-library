"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { cn } from "cn"
import { menuSurfaceClassName, menuMotionClassName, menuItemClassName, menuSelectedClassName, menuCheckedClassName, menuLabelClassName, menuSeparatorClassName, menuIndicatorClassName, useDisabledMenuShake } from "@/lib/ien-menu"

import { ContextMenu as ContextMenuPrimitive } from "radix-ui"

import { IconPlaceholder } from "@/lib/reui-icons"

function ContextMenu({
  tone: ienTone,
  useGradient: ienUseGradient,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Root> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <ContextMenuPrimitive.Root data-slot="context-menu" {...props} />
    </IenThemeContext.Provider>
  )
}

function ContextMenuTrigger({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Trigger> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ContextMenuPrimitive.Trigger
      data-slot="context-menu-trigger"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(" select-none", className)}
      {...props}
    />
  )
}

function ContextMenuGroup({
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Group>) {
  return (
    <ContextMenuPrimitive.Group data-slot="context-menu-group" {...props} />
  )
}

function ContextMenuPortal({
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Portal>) {
  return (
    <ContextMenuPrimitive.Portal data-slot="context-menu-portal" {...props} />
  )
}

function ContextMenuSub({
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Sub>) {
  return <ContextMenuPrimitive.Sub data-slot="context-menu-sub" {...props} />
}

function ContextMenuRadioGroup({
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.RadioGroup>) {
  return (
    <ContextMenuPrimitive.RadioGroup
      data-slot="context-menu-radio-group"
      {...props}
    />
  )
}

function ContextMenuContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Content> & {
  side?: "top" | "right" | "bottom" | "left"
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ContextMenuPrimitive.Portal>
      <ContextMenuPrimitive.Content
        data-slot="context-menu-content"
        data-ien-typography="body2"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        className={cn(
        menuSurfaceClassName, menuMotionClassName, "min-w-40 max-h-(--radix-context-menu-content-available-height) max-w-(--radix-context-menu-content-available-width) overflow-x-hidden overflow-y-auto p-(--ien-spacing-xxs) origin-(--radix-context-menu-content-transform-origin)",
        className
        )}
        {...props}
      />
    </ContextMenuPrimitive.Portal>
  )
}

function ContextMenuItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  inset,
  selected = false,
  variant = "default",
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Item> & {
  inset?: boolean
  selected?: boolean
  variant?: "default" | "destructive"
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <ContextMenuPrimitive.Item
      onPointerDown={handlePointerDown}
      data-slot="context-menu-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      data-selected={selected || undefined}
      data-variant={variant}
      className={cn(
        menuItemClassName, menuSelectedClassName, "data-[variant=destructive]:text-(--ien-danger) data-[variant=destructive]:[&_svg]:text-(--ien-danger)",
        className,
      )}
      {...props}
    />
  )
}

function ContextMenuSubTrigger({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  inset,
  children,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.SubTrigger> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <ContextMenuPrimitive.SubTrigger
      onPointerDown={handlePointerDown}
      data-slot="context-menu-sub-trigger"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuItemClassName,
        className
      )}
      {...props}
    >
      {children}
      <IconPlaceholder
        lucide="ChevronRightIcon"
        tabler="IconChevronRight"
        hugeicons="ArrowRight01Icon"
        phosphor="CaretRightIcon"
        remixicon="RiArrowRightSLine"
        className="ms-auto"
      />
    </ContextMenuPrimitive.SubTrigger>
  )
}

function ContextMenuSubContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.SubContent> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ContextMenuPrimitive.Portal>
    <ContextMenuPrimitive.SubContent
      data-slot="context-menu-sub-content"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuSurfaceClassName, menuMotionClassName, "min-w-40 max-h-(--radix-context-menu-content-available-height) max-w-(--radix-context-menu-content-available-width) overflow-x-hidden overflow-y-auto p-(--ien-spacing-xxs) origin-(--radix-context-menu-content-transform-origin)",
        className
      )}
      {...props}
    />
    </ContextMenuPrimitive.Portal>
  )
}

function ContextMenuCheckboxItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  children,
  checked,
  inset,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.CheckboxItem> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <ContextMenuPrimitive.CheckboxItem
      onPointerDown={handlePointerDown}
      data-slot="context-menu-checkbox-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuItemClassName, menuCheckedClassName, "data-[state=checked]:not-data-disabled:text-(--ien-text-primary)",
        className,
      )}
      checked={checked}
      {...props}
    >
      <span className={menuIndicatorClassName}>
        <ContextMenuPrimitive.ItemIndicator>
          <IconPlaceholder
            lucide="CheckIcon"
            tabler="IconCheck"
            hugeicons="Tick02Icon"
            phosphor="CheckIcon"
            remixicon="RiCheckLine"
          />
        </ContextMenuPrimitive.ItemIndicator>
      </span>
      {children}
    </ContextMenuPrimitive.CheckboxItem>
  )
}

function ContextMenuRadioItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  children,
  inset,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.RadioItem> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <ContextMenuPrimitive.RadioItem
      onPointerDown={handlePointerDown}
      data-slot="context-menu-radio-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuItemClassName, menuCheckedClassName,
        className,
      )}
      {...props}
    >
      <span className={menuIndicatorClassName}>
        <ContextMenuPrimitive.ItemIndicator>
          <IconPlaceholder
            lucide="CheckIcon"
            tabler="IconCheck"
            hugeicons="Tick02Icon"
            phosphor="CheckIcon"
            remixicon="RiCheckLine"
          />
        </ContextMenuPrimitive.ItemIndicator>
      </span>
      {children}
    </ContextMenuPrimitive.RadioItem>
  )
}

function ContextMenuLabel({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  inset,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Label> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ContextMenuPrimitive.Label
      data-slot="context-menu-label"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuLabelClassName,
        className)}
      {...props}
    />
  )
}

function ContextMenuSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof ContextMenuPrimitive.Separator> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ContextMenuPrimitive.Separator
      data-slot="context-menu-separator"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuSeparatorClassName,
        className)}
      {...props}
    />
  )
}

function ContextMenuShortcut({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"span"> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <span
      data-slot="context-menu-shortcut"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "ms-auto text-(--ien-text-tertiary) [[data-disabled]_&]:text-(--ien-text-disabled)",
        className)}
      {...props}
    />
  )
}

export {
  ContextMenu,
  ContextMenuTrigger,
  ContextMenuContent,
  ContextMenuItem,
  ContextMenuCheckboxItem,
  ContextMenuRadioItem,
  ContextMenuLabel,
  ContextMenuSeparator,
  ContextMenuShortcut,
  ContextMenuGroup,
  ContextMenuPortal,
  ContextMenuSub,
  ContextMenuSubContent,
  ContextMenuSubTrigger,
  ContextMenuRadioGroup,
}
