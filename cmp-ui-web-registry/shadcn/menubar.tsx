"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import * as React from "react";
import { CheckIcon, ChevronRightIcon } from "lucide-react";
import { cn } from "@/lib/ien-utils";
import { Menubar as MenubarPrimitive } from "radix-ui";

function Menubar({
  className,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Root>) {
  return (
    <MenubarPrimitive.Root
      data-slot="menubar"
      className={cn(
        "flex h-8 items-center gap-ien-xxxs rounded-[var(--ien-control-radius-base)] border-ien-border p-[3px]",
        className,
      )}
      {...props}
    />
  );
}

function MenubarMenu({
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Menu>) {
  return <MenubarPrimitive.Menu data-slot="menubar-menu" {...props} />;
}

function MenubarGroup({
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Group>) {
  return <MenubarPrimitive.Group data-slot="menubar-group" {...props} />;
}

function MenubarPortal({
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Portal>) {
  return <MenubarPrimitive.Portal data-slot="menubar-portal" {...props} />;
}

function MenubarRadioGroup({
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.RadioGroup>) {
  return (
    <MenubarPrimitive.RadioGroup data-slot="menubar-radio-group" {...props} />
  );
}

function MenubarTrigger({
  className,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Trigger>) {
  return (
    <MenubarPrimitive.Trigger
      data-slot="menubar-trigger"
      className={cn(
        "flex items-center rounded-ien-xs px-[var(--ien-control-gap)] py-[2px] text-ien-body2 font-[var(--ien-font-weight-label1)] outline-hidden select-none hover:bg-ien-surface-variant aria-expanded:bg-ien-surface-variant",
        className,
      )}
      {...props}
    />
  );
}

function MenubarContent({
  className,
  align = "start",
  alignOffset = -4,
  sideOffset = 8,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Content>) {
  return (
    <MenubarPortal>
      <MenubarPrimitive.Content
        data-slot="menubar-content"
        align={align}
        alignOffset={alignOffset}
        sideOffset={sideOffset}
        className={cn(
          "cn-menu-target cn-menu-translucent z-50 min-w-36 origin-(--radix-menubar-content-transform-origin) overflow-hidden rounded-[var(--ien-control-radius-base)] bg-ien-surface-raised p-ien-xxs text-ien-text-primary shadow-[0_var(--ien-elevation-raised)_calc(var(--ien-elevation-raised)*2)_rgb(0_0_0/0.12)] ring-1 ring-ien-text-primary/10 duration-[var(--ien-motion-instant)] data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2 data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:zoom-in-95",
          className,
        )}
        {...props}
      />
    </MenubarPortal>
  );
}

function MenubarItem({
  className,
  inset,
  variant = "default",
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Item> & {
  inset?: boolean;
  variant?: "default" | "destructive";
}) {
  return (
    <MenubarPrimitive.Item
      data-slot="menubar-item"
      data-inset={inset}
      data-variant={variant}
      className={cn(
        "group/menubar-item relative flex cursor-default items-center gap-[var(--ien-control-gap)] rounded-ien-sm px-[var(--ien-control-gap)] py-ien-xxs text-ien-body2 outline-hidden select-none focus:bg-ien-brand-weak focus:text-ien-on-brand-weak not-data-[variant=destructive]:focus:**:text-ien-on-brand-weak data-inset:pl-ien-xxl data-[variant=destructive]:text-ien-danger data-[variant=destructive]:focus:bg-ien-danger/10 data-[variant=destructive]:focus:text-ien-danger dark:data-[variant=destructive]:focus:bg-ien-danger/20 data-disabled:pointer-events-none data-disabled:opacity-[var(--ien-state-disabled-alpha)] [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-[var(--ien-icon-sm)] data-[variant=destructive]:*:[svg]:text-ien-danger!",
        className,
      )}
      {...props}
    />
  );
}

function MenubarCheckboxItem({
  className,
  children,
  checked,
  inset,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.CheckboxItem> & {
  inset?: boolean;
}) {
  return (
    <MenubarPrimitive.CheckboxItem
      data-slot="menubar-checkbox-item"
      data-inset={inset}
      className={cn(
        "relative flex cursor-default items-center gap-[var(--ien-control-gap)] rounded-ien-sm py-ien-xxs pr-[var(--ien-control-gap)] pl-ien-xl text-ien-body2 outline-hidden select-none focus:bg-ien-brand-weak focus:text-ien-on-brand-weak focus:**:text-ien-on-brand-weak data-inset:pl-ien-xxl data-disabled:pointer-events-none [&_svg]:pointer-events-none [&_svg]:shrink-0",
        className,
      )}
      checked={checked}
      {...props}
    >
      <span className="pointer-events-none absolute left-1.5 flex size-[var(--ien-icon-sm)] items-center justify-center [&_svg:not([class*='size-'])]:size-4">
        <MenubarPrimitive.ItemIndicator>
          <CheckIcon />
        </MenubarPrimitive.ItemIndicator>
      </span>
      {children}
    </MenubarPrimitive.CheckboxItem>
  );
}

function MenubarRadioItem({
  className,
  children,
  inset,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.RadioItem> & {
  inset?: boolean;
}) {
  return (
    <MenubarPrimitive.RadioItem
      data-slot="menubar-radio-item"
      data-inset={inset}
      className={cn(
        "relative flex cursor-default items-center gap-[var(--ien-control-gap)] rounded-ien-sm py-ien-xxs pr-[var(--ien-control-gap)] pl-ien-xl text-ien-body2 outline-hidden select-none focus:bg-ien-brand-weak focus:text-ien-on-brand-weak focus:**:text-ien-on-brand-weak data-inset:pl-ien-xxl data-disabled:pointer-events-none data-disabled:opacity-[var(--ien-state-disabled-alpha)] [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        className,
      )}
      {...props}
    >
      <span className="pointer-events-none absolute left-1.5 flex size-[var(--ien-icon-sm)] items-center justify-center [&_svg:not([class*='size-'])]:size-4">
        <MenubarPrimitive.ItemIndicator>
          <CheckIcon />
        </MenubarPrimitive.ItemIndicator>
      </span>
      {children}
    </MenubarPrimitive.RadioItem>
  );
}

function MenubarLabel({
  className,
  inset,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Label> & {
  inset?: boolean;
}) {
  return (
    <MenubarPrimitive.Label
      data-slot="menubar-label"
      data-inset={inset}
      className={cn(
        "px-[var(--ien-control-gap)] py-ien-xxs text-ien-body2 font-[var(--ien-font-weight-label1)] data-inset:pl-ien-xxl",
        className,
      )}
      {...props}
    />
  );
}

function MenubarSeparator({
  className,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Separator>) {
  return (
    <MenubarPrimitive.Separator
      data-slot="menubar-separator"
      className={cn("-mx-ien-xxs my-ien-xxs h-px bg-ien-border", className)}
      {...props}
    />
  );
}

function MenubarShortcut({
  className,
  ...props
}: React.ComponentProps<"span">) {
  return (
    <span
      data-slot="menubar-shortcut"
      className={cn(
        "ml-auto text-ien-caption tracking-widest text-ien-text-secondary group-focus/menubar-item:text-ien-on-brand-weak",
        className,
      )}
      {...props}
    />
  );
}

function MenubarSub({
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.Sub>) {
  return <MenubarPrimitive.Sub data-slot="menubar-sub" {...props} />;
}

function MenubarSubTrigger({
  className,
  inset,
  children,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.SubTrigger> & {
  inset?: boolean;
}) {
  return (
    <MenubarPrimitive.SubTrigger
      data-slot="menubar-sub-trigger"
      data-inset={inset}
      className={cn(
        "flex cursor-default items-center gap-[var(--ien-control-gap)] rounded-ien-sm px-[var(--ien-control-gap)] py-ien-xxs text-ien-body2 outline-none select-none focus:bg-ien-brand-weak focus:text-ien-on-brand-weak data-inset:pl-ien-xxl data-[state=open]:bg-ien-brand-weak data-[state=open]:text-ien-on-brand-weak [&_svg:not([class*='size-'])]:size-4",
        className,
      )}
      {...props}
    >
      {children}
      <ChevronRightIcon className="cn-rtl-flip ml-auto size-4" />
    </MenubarPrimitive.SubTrigger>
  );
}

function MenubarSubContent({
  className,
  ...props
}: React.ComponentProps<typeof MenubarPrimitive.SubContent>) {
  return (
    <MenubarPrimitive.SubContent
      data-slot="menubar-sub-content"
      className={cn(
        "cn-menu-target cn-menu-translucent z-50 min-w-32 origin-(--radix-menubar-content-transform-origin) overflow-hidden rounded-[var(--ien-control-radius-base)] bg-ien-surface-raised p-ien-xxs text-ien-text-primary shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*1.5)_rgb(0_0_0/0.16)] ring-1 ring-ien-text-primary/10 duration-[var(--ien-motion-instant)] data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2 data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:zoom-in-95 data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=closed]:zoom-out-95",
        className,
      )}
      {...props}
    />
  );
}

export {
  Menubar,
  MenubarPortal,
  MenubarMenu,
  MenubarTrigger,
  MenubarContent,
  MenubarGroup,
  MenubarSeparator,
  MenubarLabel,
  MenubarItem,
  MenubarShortcut,
  MenubarCheckboxItem,
  MenubarRadioGroup,
  MenubarRadioItem,
  MenubarSub,
  MenubarSubTrigger,
  MenubarSubContent,
};
