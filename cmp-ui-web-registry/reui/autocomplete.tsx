"use client"
import type { IenToneProps } from "@/lib/ien-theme"
import { IenThemeContext, useIenTheme } from "@/lib/ien-theme-context"

import { Autocomplete as AutocompletePrimitive } from "@base-ui/react/autocomplete"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "cn"
import { menuSurfaceClassName, menuBaseMotionClassName, menuItemClassName, menuLabelClassName, menuSeparatorClassName } from "@/lib/ien-menu"
import { ScrollArea } from "@/components/ui/scroll-area"
import { IconPlaceholder } from "@/lib/reui-icons"

const inputVariants = cva(
  "outline-none flex w-full text-foreground placeholder:text-muted-foreground disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-(--ien-state-disabled-alpha) [[readonly]]:bg-muted/80 [[readonly]]:cursor-not-allowed border border-input focus-visible:border-ring aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:aria-invalid:border-destructive/50 rounded-lg bg-transparent dark:bg-input/30 text-sm transition-colors focus-visible:ring-ring/50 focus-visible:ring-3 aria-invalid:ring-3",
  {
    variants: {
      size: {
        sm: "h-7 px-2 [&~[data-slot=autocomplete-clear]]:end-1.5 [&~[data-slot=autocomplete-trigger]]:end-1.5",
        default:
          "h-8 px-2.5 [&~[data-slot=autocomplete-clear]]:end-1.75 [&~[data-slot=autocomplete-trigger]]:end-1.75",
        lg: "h-9 px-2.5 [&~[data-slot=autocomplete-clear]]:end-2 [&~[data-slot=autocomplete-trigger]]:end-2",
      },
    },
    defaultVariants: {
      size: "default",
    },
  },
)

function Autocomplete<Items extends readonly { items: readonly unknown[] }[]>(
  props: Omit<
    AutocompletePrimitive.Root.Props<Items[number]["items"][number]>,
    "items"
  > & { items: Items } & IenToneProps,
): React.JSX.Element
function Autocomplete<ItemValue>(
  props: Omit<AutocompletePrimitive.Root.Props<ItemValue>, "items"> & {
    items?: readonly ItemValue[]
  } & IenToneProps,
): React.JSX.Element
function Autocomplete<ItemValue>({
  tone,
  useGradient,
  ...props
}: AutocompletePrimitive.Root.Props<ItemValue> & IenToneProps) {
  const ienTheme = useIenTheme({ tone, useGradient })
  return (
    <IenThemeContext.Provider value={ienTheme}>
      <AutocompletePrimitive.Root
        {...props}
        items={props.items as readonly ItemValue[] | undefined}
      />
    </IenThemeContext.Provider>
  )
}

function AutocompleteValue({ ...props }: AutocompletePrimitive.Value.Props) {
  return (
    <AutocompletePrimitive.Value data-slot="autocomplete-value" {...props} />
  )
}

function AutocompleteInput({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  size = "default",
  showClear = false,
  showTrigger = false,
  ...props
}: Omit<AutocompletePrimitive.Input.Props, "size"> &
  VariantProps<typeof inputVariants> & {
    showClear?: boolean
    showTrigger?: boolean
  } & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <div className="relative w-full">
      <AutocompletePrimitive.Input
        data-slot="autocomplete-input"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        data-size={size}
        className={cn(inputVariants({ size }), className)}
        {...props}
      />
      {showTrigger && <AutocompleteTrigger />}
      {showClear && <AutocompleteClear />}
    </div>
  )
}

function AutocompleteStatus({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: AutocompletePrimitive.Status.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Status
      data-slot="autocomplete-status"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "text-muted-foreground px-2 py-1.5 text-sm empty:m-0 empty:p-0",
        className
      )}
      {...props}
    />
  )
}

function AutocompletePortal({ ...props }: AutocompletePrimitive.Portal.Props) {
  return (
    <AutocompletePrimitive.Portal data-slot="autocomplete-portal" {...props} />
  )
}

function AutocompleteBackdrop({
  ...props
}: AutocompletePrimitive.Backdrop.Props) {
  return (
    <AutocompletePrimitive.Backdrop
      data-slot="autocomplete-backdrop"
      {...props}
    />
  )
}

function AutocompletePositioner({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: AutocompletePrimitive.Positioner.Props & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Positioner
      data-slot="autocomplete-positioner"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("z-50 outline-none", className)}
      {...props}
    />
  )
}

function AutocompleteList({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  scrollAreaClassName,
  ...props
}: AutocompletePrimitive.List.Props & {
  scrollAreaClassName?: string
  scrollFade?: boolean
  scrollbarGutter?: boolean
} & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  // Out of the layout while the list renders nothing. Base UI's scroll area
  // skips measuring 0px tall content, so a list that overflowed and then
  // filtered to empty kept its old thumb, hanging below the popup. Keyed on
  // :empty, not data-empty, which a list without `items` always carries.
  return (
    <ScrollArea
      className={cn(
        "size-full min-h-0 has-[[data-slot=autocomplete-list]:empty]:hidden **:data-[slot=scroll-area-viewport]:h-full **:data-[slot=scroll-area-viewport]:overscroll-contain",
        scrollAreaClassName
      )}
    >
      <AutocompletePrimitive.List
        data-slot="autocomplete-list"
        data-ien=""
        data-ien-tone={ienTheme.tone}
        data-ien-gradient={ienTheme.useGradient}
        className={cn(
          "not-empty:p-(--ien-spacing-xs) not-empty:scroll-py-(--ien-spacing-xs) in-data-has-overflow-y:me-3",
          className
        )}
        {...props}
      />
    </ScrollArea>
  )
}

function AutocompleteCollection({
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Collection>) {
  return (
    <AutocompletePrimitive.Collection
      data-slot="autocomplete-collection"
      {...props}
    />
  )
}

function AutocompleteRow({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Row> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Row
      data-slot="autocomplete-row"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn("flex items-center gap-2", className)}
      {...props}
    />
  )
}

function AutocompleteItem({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Item> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Item
      data-slot="autocomplete-item"
      data-ien-typography="body2"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuItemClassName,
        className,
      )}
      {...props}
    />
  )
}

export interface AutocompleteContentProps
  extends React.ComponentProps<
  typeof AutocompletePrimitive.Popup
> {
  tone?: IenToneProps["tone"]
  useGradient?: boolean

  align?: AutocompletePrimitive.Positioner.Props["align"]
  sideOffset?: AutocompletePrimitive.Positioner.Props["sideOffset"]
  alignOffset?: AutocompletePrimitive.Positioner.Props["alignOffset"]
  side?: AutocompletePrimitive.Positioner.Props["side"]
  anchor?: AutocompletePrimitive.Positioner.Props["anchor"]
  showBackdrop?: boolean
}

function AutocompleteContent({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  children,
  showBackdrop = false,
  align = "start",
  sideOffset = 4,
  alignOffset = 0,
  side = "bottom",
  anchor,
  ...props
}: AutocompleteContentProps & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePortal>
      {showBackdrop && <AutocompleteBackdrop />}
      <AutocompletePositioner
        align={align}
        sideOffset={sideOffset}
        alignOffset={alignOffset}
        side={side}
        anchor={anchor}
      >
        <div className="relative flex max-h-full">
          <AutocompletePrimitive.Popup
            data-slot="autocomplete-popup"
            data-ien=""
            data-ien-tone={ienTheme.tone}
            data-ien-gradient={ienTheme.useGradient}
            className={cn(
        menuSurfaceClassName, menuBaseMotionClassName, "flex max-h-[min(var(--available-height),24rem)] w-(--anchor-width) min-w-40 max-w-(--available-width) origin-(--transform-origin) flex-col overscroll-contain overflow-hidden",
        className
            )}
            {...props}
          >
            {children}
          </AutocompletePrimitive.Popup>
        </div>
      </AutocompletePositioner>
    </AutocompletePortal>
  )
}

function AutocompleteGroup({
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Group>) {
  return (
    <AutocompletePrimitive.Group data-slot="autocomplete-group" {...props} />
  )
}

function AutocompleteGroupLabel({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.GroupLabel> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.GroupLabel
      data-slot="autocomplete-group-label"
      data-ien-typography="caption"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuLabelClassName,
        className
      )}
      {...props}
    />
  )
}

function AutocompleteEmpty({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Empty> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Empty
      data-slot="autocomplete-empty"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "text-muted-foreground px-2 py-1.5 text-sm text-center empty:m-0 empty:p-0",
        className
      )}
      {...props}
    />
  )
}

function AutocompleteClear({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Clear> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Clear
      data-slot="autocomplete-clear"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "ring-offset-background focus:ring-ring absolute top-1/2 -translate-y-1/2 cursor-pointer opacity-70 transition-opacity hover:opacity-100 focus:ring-2 focus:ring-offset-2 focus:outline-none disabled:pointer-events-none data-disabled:pointer-events-none",
        className
      )}
      {...props}
    >
      <IconPlaceholder
        lucide="XIcon"
        tabler="IconX"
        hugeicons="Cancel01Icon"
        phosphor="XIcon"
        remixicon="RiCloseLine"
        className="size-4"
      />
    </AutocompletePrimitive.Clear>
  )
}

function AutocompleteTrigger({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Trigger> & IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Trigger
      data-slot="autocomplete-trigger"
      data-ien-typography="label1"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        "focus:ring-ring ring-offset-background absolute top-1/2 -translate-y-1/2 cursor-pointer focus:ring-2 focus:ring-offset-2 focus:outline-none disabled:pointer-events-none has-[+[data-slot=autocomplete-clear]]:hidden data-disabled:pointer-events-none",
        className
      )}
      {...props}
    >
      <IconPlaceholder
        lucide="ChevronsUpDownIcon"
        tabler="IconSelector"
        hugeicons="UnfoldMoreIcon"
        phosphor="CaretUpDownIcon"
        remixicon="RiExpandUpDownLine"
        className="size-4 opacity-70"
      />
    </AutocompletePrimitive.Trigger>
  )
}

function AutocompleteArrow({
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Arrow>) {
  return (
    <AutocompletePrimitive.Arrow data-slot="autocomplete-arrow" {...props} />
  )
}

function AutocompleteSeparator({
  tone: ienTone,
  useGradient: ienUseGradient,
  className,
  ...props
}: React.ComponentProps<typeof AutocompletePrimitive.Separator> &
  IenToneProps) {
  const ienTheme = useIenTheme({ tone: ienTone, useGradient: ienUseGradient })
  return (
    <AutocompletePrimitive.Separator
      data-slot="autocomplete-separator"
      data-ien=""
      data-ien-tone={ienTheme.tone}
      data-ien-gradient={ienTheme.useGradient}
      className={cn(
        menuSeparatorClassName,
        className
      )}
      {...props}
    />
  )
}

export {
  Autocomplete,
  AutocompleteValue,
  AutocompleteTrigger,
  AutocompleteInput,
  AutocompleteStatus,
  AutocompletePortal,
  AutocompleteBackdrop,
  AutocompletePositioner,
  AutocompleteContent,
  AutocompleteList,
  AutocompleteCollection,
  AutocompleteRow,
  AutocompleteItem,
  AutocompleteGroup,
  AutocompleteGroupLabel,
  AutocompleteEmpty,
  AutocompleteClear,
  AutocompleteArrow,
  AutocompleteSeparator,
}
