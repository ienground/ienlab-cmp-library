"use client";

import * as React from "react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type FabProps = Omit<ButtonProps, "size" | "state"> & {
  label: string;
  size?: "small" | "regular" | "large" | "extended";
  isExtended?: boolean;
  state?: {
    enabled?: boolean;
    loading?: boolean;
  };
};

function Fab({
  label,
  size = "regular",
  isExtended,
  state,
  className,
  children,
  disabled,
  style,
  "aria-label": ariaLabel,
  ...props
}: FabProps) {
  const extended = isExtended ?? size === "extended";
  const labelRef = React.useRef<HTMLSpanElement>(null);
  const [labelWidth, setLabelWidth] = React.useState(0);
  const resolvedSize = size === "extended" ? "regular" : size;
  const dimensions = {
    small: { diameter: 40, icon: 20 },
    regular: { diameter: 56, icon: 24 },
    large: { diameter: 96, icon: 36 },
  }[resolvedSize];
  const buttonState = {
    enabled: state?.enabled ?? true,
    loading: state?.loading ?? false,
  };
  const iconStyle: React.CSSProperties = {
    width: dimensions.icon,
    height: dimensions.icon,
  };
  React.useLayoutEffect(() => {
    const element = labelRef.current;
    if (!element) return;
    const updateWidth = () => setLabelWidth(element.scrollWidth);
    updateWidth();
    if (typeof ResizeObserver !== "function") return;
    const observer = new ResizeObserver(updateWidth);
    observer.observe(element);
    return () => observer.disconnect();
  }, [label]);
  const extendedWidth = `calc(${dimensions.diameter}px + ${labelWidth}px + var(--ien-spacing-sm))`;
  const buttonStyle: React.CSSProperties & {
    "--ien-button-loading-size": string;
  } = {
    ...style,
    "--ien-button-loading-size": `${dimensions.icon}px`,
    width: extended ? (style?.width ?? extendedWidth) : dimensions.diameter,
    height: dimensions.diameter,
    paddingInline: extended ? (dimensions.diameter - dimensions.icon) / 2 : 0,
    columnGap: extended ? "var(--ien-spacing-sm)" : 0,
  };
  return (
    <Button
      {...props}
      data-slot="fab"
      size="icon-lg"
      state={buttonState}
      disabled={disabled || !buttonState.enabled}
      aria-label={ariaLabel ?? (extended ? undefined : label)}
      className={cn(
        "rounded-full transition-[width,column-gap,border-radius,background-color] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*2)_rgb(0_0_0/0.16)] motion-reduce:transition-none",
        extended && "rounded-ien-full",
        className,
      )}
      style={buttonStyle}
    >
      <span className="grid shrink-0 place-items-center [&_svg]:!size-full" style={iconStyle}>
        {children}
      </span>
      <span
        ref={labelRef}
        aria-hidden={!extended}
        className={cn(
          "grid min-w-0 transition-[grid-template-columns,opacity] duration-[var(--ien-motion-fast)] ease-[var(--ien-motion-standard-easing)] motion-reduce:transition-none",
          extended ? "grid-cols-[1fr] opacity-100" : "grid-cols-[0fr] opacity-0",
        )}
      >
        <span className="overflow-hidden whitespace-nowrap">{label}</span>
      </span>
    </Button>
  );
}

function ExtendedFab(props: Omit<FabProps, "size">) {
  return <Fab {...props} isExtended />;
}

export { Fab, Fab as IenFab, ExtendedFab, ExtendedFab as IenExtendedFab };
