"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import * as React from "react"
import { Combobox as ComboboxPrimitive } from "@base-ui/react"
import { cn } from "cn"
import { menuSurfaceClassName, menuItemClassName, menuCheckedClassName, menuLabelClassName, menuSeparatorClassName, menuIndicatorClassName, menuBaseMotionClassName } from "@/lib/ien-menu"

import { Button } from "@/components/ui/button"
import {
  InputGroup,
  InputGroupAddon,
  InputGroupButton,
  InputGroupInput,
} from "@/components/ui/input-group"
import { IconPlaceholder } from "@/lib/reui-icons"

function Combobox<Value, Multiple extends boolean | undefined = false>({
  tone,
  useGradient,
  ...props
}: ComboboxPrimitive.Root.Props<Value, Multiple> & IenToneProps) {
  const ienTheme = useIenTheme({ tone, useGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <ComboboxPrimitive.Root {...props} />
    </IenThemeContext.Provider>
  )
}

function ComboboxValue({ ...props }: ComboboxPrimitive.Value.Props) {
  return <ComboboxPrimitive.Value data-slot="combobox-value" {...props} />
}

function ComboboxTrigger({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  children,
  ...props
}: ComboboxPrimitive.Trigger.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Trigger
      data-slot="combobox-trigger"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("[&_svg:not([class*='size-'])]:size-4", className)}
      {...props}
    >
      {children}
      <IconPlaceholder
        lucide="ChevronDownIcon"
        tabler="IconChevronDown"
        hugeicons="ArrowDown01Icon"
        phosphor="CaretDownIcon"
        remixicon="RiArrowDownSLine"
        className="text-muted-foreground size-4 pointer-events-none"
      />
    </ComboboxPrimitive.Trigger>
  )
}

function ComboboxClear({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.Clear.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Clear
      data-slot="combobox-clear"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      render={<InputGroupButton variant="ghost" size="icon-xs" />}
      className={cn("", className)}
      {...props}
    >
      <IconPlaceholder
        lucide="XIcon"
        tabler="IconX"
        hugeicons="Cancel01Icon"
        phosphor="XIcon"
        remixicon="RiCloseLine"
        className=" pointer-events-none"
      />
    </ComboboxPrimitive.Clear>
  )
}

function ComboboxInput({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  children,
  disabled = false,
  showTrigger = true,
  showClear = false,
  ...props
}: ComboboxPrimitive.Input.Props & {
  showTrigger?: boolean
  showClear?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <InputGroup className={cn(" w-auto", className)}>
      <ComboboxPrimitive.Input
        render={<InputGroupInput disabled={disabled} />}
        {...props}
      />
      <InputGroupAddon align="inline-end">
        {showTrigger && (
          <InputGroupButton
            size="icon-xs"
            variant="ghost"
            asChild
            data-slot="input-group-button"
            data-ien-typography="label1"
            data-ien=""
            data-ien-tone={ienTheme.tone}
            data-ien-gradient={ienTheme.useGradient}
            className="group-has-data-[slot=combobox-clear]/input-group:hidden data-pressed:bg-transparent"
            disabled={disabled}
          >
            <ComboboxTrigger />
          </InputGroupButton>
        )}
        {showClear && <ComboboxClear disabled={disabled} />}
      </InputGroupAddon>
      {children}
    </InputGroup>
  )
}

function ComboboxContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  side = "bottom",
  sideOffset = 6,
  align = "start",
  alignOffset = 0,
  anchor,
  ...props
}: ComboboxPrimitive.Popup.Props &
  Pick<
    ComboboxPrimitive.Positioner.Props,
    "side" | "align" | "sideOffset" | "alignOffset" | "anchor"
  > &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Portal>
      <ComboboxPrimitive.Positioner
        side={side}
        sideOffset={sideOffset}
        align={align}
        alignOffset={alignOffset}
        anchor={anchor}
        className="isolate z-50"
      >
        <ComboboxPrimitive.Popup
          data-slot="combobox-content"
          data-ien-typography="body2"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
          data-chips={!!anchor}
          className={cn(
        menuSurfaceClassName, menuBaseMotionClassName, "group/combobox-content max-h-(--available-height) w-(--anchor-width) max-w-(--available-width) min-w-40 overflow-hidden origin-(--transform-origin) *:data-[slot=input-group]:m-1 *:data-[slot=input-group]:mb-0 *:data-[slot=input-group]:h-8 *:data-[slot=input-group]:shadow-none",
        className
          )}
          {...props}
        />
      </ComboboxPrimitive.Positioner>
    </ComboboxPrimitive.Portal>
  )
}

function ComboboxList({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.List.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.List
      data-slot="combobox-list"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "no-scrollbar max-h-[min(calc(--spacing(72)---spacing(9)),calc(var(--available-height)---spacing(9)))] scroll-py-1 overflow-y-auto p-1 data-empty:p-0 overflow-y-auto overscroll-contain",
        className
      )}
      {...props}
    />
  )
}

function ComboboxItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  children,
  ...props
}: ComboboxPrimitive.Item.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Item
      data-slot="combobox-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuItemClassName, menuCheckedClassName, "w-full",
        className,
      )}
      {...props}
    >
      <ComboboxPrimitive.ItemIndicator
        keepMounted
        className={(state) => state.selected ? undefined : "invisible"}
        render={<span className={menuIndicatorClassName} />}
      >
        <IconPlaceholder
          lucide="CheckIcon"
          tabler="IconCheck"
          hugeicons="Tick02Icon"
          phosphor="CheckIcon"
          remixicon="RiCheckLine"
          className=" pointer-events-none"
        />
      </ComboboxPrimitive.ItemIndicator>
      {children}
    </ComboboxPrimitive.Item>
  )
}

function ComboboxGroup({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.Group.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Group
      data-slot="combobox-group"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("", className)}
      {...props}
    />
  )
}

function ComboboxLabel({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.GroupLabel.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.GroupLabel
      data-slot="combobox-label"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuLabelClassName,
        className)}
      {...props}
    />
  )
}

function ComboboxCollection({ ...props }: ComboboxPrimitive.Collection.Props) {
  return (
    <ComboboxPrimitive.Collection data-slot="combobox-collection" {...props} />
  )
}

function ComboboxEmpty({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.Empty.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Empty
      data-slot="combobox-empty"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("text-muted-foreground hidden w-full justify-center py-2 text-center text-sm group-data-empty/combobox-content:flex", className)}
      {...props}
    />
  )
}

function ComboboxSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.Separator.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Separator
      data-slot="combobox-separator"
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

function ComboboxChips({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentPropsWithRef<typeof ComboboxPrimitive.Chips> &
  ComboboxPrimitive.Chips.Props &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Chips
      data-slot="combobox-chips"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("dark:bg-input/30 border-input focus-within:border-ring focus-within:ring-ring/50 has-aria-invalid:ring-destructive/20 dark:has-aria-invalid:ring-destructive/40 has-aria-invalid:border-destructive dark:has-aria-invalid:border-destructive/50 flex min-h-8 flex-wrap items-center gap-1 rounded-lg border bg-transparent bg-clip-padding px-2.5 py-1 text-sm transition-colors focus-within:ring-3 has-aria-invalid:ring-3 has-data-[slot=combobox-chip]:px-1", className)}
      {...props}
    />
  )
}

function ComboboxChip({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  children,
  showRemove = true,
  ...props
}: ComboboxPrimitive.Chip.Props & {
  showRemove?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Chip
      data-slot="combobox-chip"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "bg-muted text-foreground flex h-[calc(--spacing(5.25))] w-fit items-center justify-center gap-1 rounded-sm px-1.5 text-xs font-medium whitespace-nowrap has-data-[slot=combobox-chip-remove]:pr-0 has-disabled:pointer-events-none has-disabled:cursor-not-allowed has-disabled:opacity-(--ien-state-disabled-alpha)",
        className,
      )}
      {...props}
    >
      {children}
      {showRemove && (
        <ComboboxPrimitive.ChipRemove
          render={<Button variant="ghost" size="icon-xs" />}
          className="-ml-1 opacity-50 hover:opacity-100"
          data-slot="combobox-chip-remove"
          data-ien=""
          data-ien-tone={ienTheme.tone}
          data-ien-gradient={ienTheme.useGradient}
        >
          <IconPlaceholder
        lucide="XIcon"
        tabler="IconX"
        hugeicons="Cancel01Icon"
        phosphor="XIcon"
        remixicon="RiCloseLine"
        className=" pointer-events-none"
      />
        </ComboboxPrimitive.ChipRemove>
      )}
    </ComboboxPrimitive.Chip>
  )
}

function ComboboxChipsInput({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: ComboboxPrimitive.Input.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <ComboboxPrimitive.Input
      data-slot="combobox-chip-input"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        " min-w-16 flex-1 outline-none",
        className
      )}
      {...props}
    />
  )
}

function useComboboxAnchor() {
  return React.useRef<HTMLDivElement | null>(null)
}

export {
  Combobox,
  ComboboxInput,
  ComboboxContent,
  ComboboxList,
  ComboboxItem,
  ComboboxGroup,
  ComboboxLabel,
  ComboboxCollection,
  ComboboxEmpty,
  ComboboxSeparator,
  ComboboxChips,
  ComboboxChip,
  ComboboxChipsInput,
  ComboboxTrigger,
  ComboboxValue,
  useComboboxAnchor,
}
