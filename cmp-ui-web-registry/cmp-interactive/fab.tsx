"use client";

import * as React from "react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "cn";

export type FabProps = Omit<ButtonProps, "size"> & {
  label: string;
  size?: "small" | "regular" | "large" | "extended";
};

function Fab({
  label,
  size = "regular",
  className,
  children,
  ...props
}: FabProps) {
  const extended = size === "extended";
  return (
    <Button
      data-slot="fab"
      size={extended ? "lg" : "icon-lg"}
      aria-label={extended ? undefined : label}
      className={cn(
        "min-h-14 rounded-ien-xl shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.16)]",
        size === "small" && "min-h-10 rounded-ien-lg",
        size === "large" && "min-h-16 rounded-ien-xl",
        extended && "gap-ien-sm px-ien-lg",
        className,
      )}
      {...props}
    >
      {children}
      {extended && <span>{label}</span>}
    </Button>
  );
}

function ExtendedFab(props: Omit<FabProps, "size">) {
  return <Fab {...props} size="extended" />;
}

export { Fab, Fab as IenFab, ExtendedFab, ExtendedFab as IenExtendedFab };
