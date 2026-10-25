"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { cn } from "cn"
import { DropdownMenu as DropdownMenuPrimitive } from "radix-ui"

import { menuSurfaceClassName, menuMotionClassName, menuItemClassName, menuSelectedClassName, menuCheckedClassName, menuLabelClassName, menuSeparatorClassName, menuIndicatorClassName, useDisabledMenuShake } from "@/lib/ien-menu"

import { IconPlaceholder } from "@/lib/reui-icons"

const menuContentClassName = cn(menuSurfaceClassName, menuMotionClassName, "w-max min-w-40 max-w-[min(280px,var(--radix-dropdown-menu-content-available-width))] max-h-(--radix-dropdown-menu-content-available-height) overflow-x-hidden overflow-y-auto p-(--ien-spacing-xxs) origin-(--radix-dropdown-menu-content-transform-origin)")

function DropdownMenu({
  tone: ienTone,
  useGradient: ienUseGradient,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Root> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <DropdownMenuPrimitive.Root data-slot="dropdown-menu" {...props} />
    </IenThemeContext.Provider>
  )
}

function DropdownMenuPortal({
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Portal>) {
  return (
    <DropdownMenuPrimitive.Portal data-slot="dropdown-menu-portal" {...props} />
  )
}

function DropdownMenuTrigger({
  tone,
  useGradient,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Trigger> & IenToneProps) {
  const ienTheme = useIenTheme({ tone, useGradient })
  return (
    <DropdownMenuPrimitive.Trigger
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-slot="dropdown-menu-trigger"
      data-ien-typography="label1"
      {...props}
    />
  )
}

function DropdownMenuContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  align = "start",
  sideOffset = 4,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Content> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <DropdownMenuPrimitive.Portal>
      <DropdownMenuPrimitive.Content
        data-slot="dropdown-menu-content"
        data-ien-typography="body2"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        sideOffset={sideOffset}
        align={align}
        className={cn(
        menuContentClassName,
        className
        )}
        {...props}
      />
    </DropdownMenuPrimitive.Portal>
  )
}

function DropdownMenuGroup({
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Group>) {
  return (
    <DropdownMenuPrimitive.Group data-slot="dropdown-menu-group" {...props} />
  )
}

function DropdownMenuItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  inset,
  selected = false,
  variant = "default",
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Item> & {
  inset?: boolean
  selected?: boolean
  variant?: "default" | "destructive"
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <DropdownMenuPrimitive.Item
      onPointerDown={handlePointerDown}
      data-slot="dropdown-menu-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      data-selected={selected || undefined}
      data-variant={variant}
      className={cn(
        menuItemClassName,
        menuSelectedClassName,
        "data-[variant=destructive]:text-(--ien-danger) data-[variant=destructive]:[&_svg]:text-(--ien-danger)",
        className,
      )}
      {...props}
    />
  )
}

function DropdownMenuCheckboxItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  children,
  checked,
  inset,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.CheckboxItem> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <DropdownMenuPrimitive.CheckboxItem
      onPointerDown={handlePointerDown}
      data-slot="dropdown-menu-checkbox-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuItemClassName,
        menuCheckedClassName, "data-[state=checked]:not-data-disabled:text-(--ien-text-primary)",
        className,
      )}
      checked={checked}
      {...props}
    >
      <span
        className={menuIndicatorClassName}
        data-slot="dropdown-menu-checkbox-item-indicator"
        data-ien-typography="label1"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
      >
        <DropdownMenuPrimitive.ItemIndicator>
          <IconPlaceholder
            lucide="CheckIcon"
            tabler="IconCheck"
            hugeicons="Tick02Icon"
            phosphor="CheckIcon"
            remixicon="RiCheckLine"
          />
        </DropdownMenuPrimitive.ItemIndicator>
      </span>
      {children}
    </DropdownMenuPrimitive.CheckboxItem>
  )
}

function DropdownMenuRadioGroup({
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.RadioGroup>) {
  return (
    <DropdownMenuPrimitive.RadioGroup
      data-slot="dropdown-menu-radio-group"
      {...props}
    />
  )
}

function DropdownMenuRadioItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  children,
  inset,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.RadioItem> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <DropdownMenuPrimitive.RadioItem
      onPointerDown={handlePointerDown}
      data-slot="dropdown-menu-radio-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(
        menuItemClassName,
        menuCheckedClassName,
        className,
      )}
      {...props}
    >
      <span
        className={menuIndicatorClassName}
        data-slot="dropdown-menu-radio-item-indicator"
        data-ien-typography="label1"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
      >
        <DropdownMenuPrimitive.ItemIndicator>
          <IconPlaceholder
            lucide="CheckIcon"
            tabler="IconCheck"
            hugeicons="Tick02Icon"
            phosphor="CheckIcon"
            remixicon="RiCheckLine"
          />
        </DropdownMenuPrimitive.ItemIndicator>
      </span>
      {children}
    </DropdownMenuPrimitive.RadioItem>
  )
}

function DropdownMenuLabel({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  inset,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Label> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <DropdownMenuPrimitive.Label
      data-slot="dropdown-menu-label"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      data-inset={inset}
      className={cn(menuLabelClassName, className)}
      {...props}
    />
  )
}

function DropdownMenuSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Separator> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <DropdownMenuPrimitive.Separator
      data-slot="dropdown-menu-separator"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(menuSeparatorClassName, className)}
      {...props}
    />
  )
}

function DropdownMenuShortcut({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<"span"> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <span
      data-slot="dropdown-menu-shortcut"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("ms-auto text-(--ien-text-tertiary) [[data-disabled]_&]:text-(--ien-text-disabled)", className)}
      {...props}
    />
  )
}

function DropdownMenuSub({
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.Sub>) {
  return <DropdownMenuPrimitive.Sub data-slot="dropdown-menu-sub" {...props} />
}

function DropdownMenuSubTrigger({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  onPointerDown,
  inset,
  children,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.SubTrigger> & {
  inset?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  const handlePointerDown = useDisabledMenuShake(onPointerDown)
  return (
    <DropdownMenuPrimitive.SubTrigger
      onPointerDown={handlePointerDown}
      data-slot="dropdown-menu-sub-trigger"
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
    </DropdownMenuPrimitive.SubTrigger>
  )
}

function DropdownMenuSubContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof DropdownMenuPrimitive.SubContent> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <DropdownMenuPrimitive.Portal>
      <DropdownMenuPrimitive.SubContent
        data-slot="dropdown-menu-sub-content"
        data-ien-typography="body2"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        className={cn(
          menuContentClassName,
          className
        )}
        {...props}
      />
    </DropdownMenuPrimitive.Portal>
  )
}

export {
  DropdownMenu,
  DropdownMenuPortal,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuLabel,
  DropdownMenuItem,
  DropdownMenuCheckboxItem,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubTrigger,
  DropdownMenuSubContent,
}
