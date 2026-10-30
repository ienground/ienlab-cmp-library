"use client";

import * as React from "react";
import { cn } from "cn";

export type IenNavigationBarType = "Type1" | "Type2";
export type IenNavigationBarItemDirection = "Horizontal" | "Vertical";

export interface IenNavigationBarColors {
  containerColor: string;
  selectedItemBackgroundColor: string;
  selectedIconColor: string;
  selectedTextColor: string;
  unselectedIconColor: string;
  unselectedTextColor: string;
}

const defaultColors: IenNavigationBarColors = {
  containerColor: "var(--ien-brand)",
  selectedItemBackgroundColor: "var(--ien-surface)",
  selectedIconColor: "var(--ien-brand)",
  selectedTextColor: "var(--ien-brand)",
  unselectedIconColor: "rgb(255 255 255 / 72%)",
  unselectedTextColor: "rgb(255 255 255 / 72%)",
};

export const IenNavigationBarDefaults = {
  colors: (
    colors: Partial<IenNavigationBarColors> = {},
  ): IenNavigationBarColors => ({ ...defaultColors, ...colors }),
  type2Colors: (
    colors: Partial<IenNavigationBarColors> = {},
  ): IenNavigationBarColors => ({
    ...defaultColors,
    containerColor: "var(--ien-surface)",
    selectedItemBackgroundColor: "var(--ien-surface)",
    selectedIconColor: "var(--ien-text-primary)",
    selectedTextColor: "var(--ien-text-primary)",
    unselectedIconColor: "var(--ien-text-secondary)",
    unselectedTextColor: "var(--ien-text-secondary)",
    ...colors,
  }),
};

export interface IenNavigationItem {
  label: React.ReactNode;
  icon: React.ReactNode;
  selectedIcon?: React.ReactNode;
  badge?: number;
  enabled?: boolean;
  href?: string;
}

export interface IenNavigationBarItemProps
  extends React.HTMLAttributes<HTMLElement>, React.RefAttributes<HTMLElement> {
  index: number;
  selected?: boolean;
  icon: React.ReactNode;
  selectedIcon?: React.ReactNode;
  label: React.ReactNode;
  href?: string;
  onClick?: React.MouseEventHandler<HTMLElement>;
  direction?: IenNavigationBarItemDirection;
  alwaysShowLabel?: boolean;
  badge?: number;
  type?: IenNavigationBarType;
  colors?: IenNavigationBarColors;
  enabled?: boolean;
}

export function IenNavigationBarItem({
  index,
  selected = false,
  icon,
  selectedIcon,
  label,
  href,
  direction = "Horizontal",
  alwaysShowLabel = false,
  badge = 0,
  type = "Type1",
  colors = defaultColors,
  enabled = true,
  className,
  style: customStyle,
  onClick,
  onPointerDown,
  onPointerUp,
  onPointerCancel,
  onBlur,
  ref: forwardedRef,
  ...props
}: IenNavigationBarItemProps) {
  const [pressed, setPressed] = React.useState(false);
  const setRootRef = React.useCallback(
    (element: HTMLElement | null) => {
      if (typeof forwardedRef === "function") return forwardedRef(element);
      if (forwardedRef) forwardedRef.current = element;
    },
    [forwardedRef],
  );
  const content = selected ? (selectedIcon ?? icon) : icon;
  const badgeText = badge >= 100 ? "99+" : badge > 0 ? String(badge) : null;
  const classNames = cn(
    "relative flex min-h-12 flex-1 items-center justify-center gap-ien-xs rounded-full px-ien-sm transition-[background,color,transform] duration-[var(--ien-motion-fast)] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand disabled:opacity-[var(--ien-state-disabled-alpha)]",
    direction === "Vertical" && "flex-col gap-ien-xxs",
    type === "Type2" && "min-w-12",
    selected && "font-semibold",
    pressed && "scale-[var(--ien-button-pressed-scale)]",
    !selected && !alwaysShowLabel && "[&>span[data-label]]:hidden",
    className,
  );
  const style = {
    backgroundColor: selected
      ? colors.selectedItemBackgroundColor
      : "transparent",
    color: selected ? colors.selectedTextColor : colors.unselectedTextColor,
    ...customStyle,
  };
  const handlePointerDown = (event: React.PointerEvent<HTMLElement>) => {
    if (enabled) setPressed(true);
    onPointerDown?.(event);
  };
  const handlePointerEnd = (event: React.PointerEvent<HTMLElement>) => {
    setPressed(false);
    onPointerUp?.(event);
  };
  const handlePointerCancel = (event: React.PointerEvent<HTMLElement>) => {
    setPressed(false);
    onPointerCancel?.(event);
  };
  const handleBlur = (event: React.FocusEvent<HTMLElement>) => {
    setPressed(false);
    onBlur?.(event);
  };
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    if (!enabled) {
      event.preventDefault();
      return;
    }
    onClick?.(event);
  };
  const contentNode = (
    <>
      <span
        className="relative inline-grid place-items-center"
        style={{
          color: selected
            ? colors.selectedIconColor
            : colors.unselectedIconColor,
        }}
      >
        {content}
        {badge < 0 && (
          <span
            role="img"
            aria-label="새 알림"
            className="absolute -right-1 -top-1 size-2 rounded-full bg-ien-danger"
          />
        )}
        {badgeText != null && (
          <span className="absolute -right-3 -top-2 rounded-full bg-ien-danger px-1 text-[10px] leading-4 text-ien-on-danger">
            {badgeText}
          </span>
        )}
      </span>
      <span data-label className="text-ien-caption">
        {label}
      </span>
    </>
  );
  if (href) {
    return (
      <a
        ref={setRootRef}
        {...props}
        href={enabled ? href : undefined}
        aria-disabled={!enabled || undefined}
        aria-current={selected ? "page" : undefined}
        tabIndex={enabled ? props.tabIndex : -1}
        className={classNames}
        style={style}
        onPointerDown={handlePointerDown}
        onPointerUp={handlePointerEnd}
        onPointerCancel={handlePointerCancel}
        onBlur={handleBlur}
        onClick={handleClick}
      >
        {contentNode}
      </a>
    );
  }
  return (
    <button
      ref={setRootRef}
      {...props}
      type="button"
      aria-pressed={selected}
      disabled={!enabled}
      className={classNames}
      style={style}
      onPointerDown={handlePointerDown}
      onPointerUp={handlePointerEnd}
      onPointerCancel={handlePointerCancel}
      onBlur={handleBlur}
      onClick={handleClick}
    >
      {contentNode}
    </button>
  );
}

export interface IenNavigationBarProps extends Omit<
  React.ComponentPropsWithRef<"nav">,
  "onChange"
> {
  items?: readonly IenNavigationItem[];
  selectedIndex: number;
  onSelectionChange?: (index: number) => void;
  itemCount?: number;
  visible?: boolean;
  type?: IenNavigationBarType;
  colors?: Partial<IenNavigationBarColors>;
}

export function IenNavigationBar({
  items,
  selectedIndex,
  onSelectionChange,
  itemCount = items?.length ?? 0,
  visible = true,
  type = "Type1",
  colors,
  className,
  children,
  style,
  ...props
}: IenNavigationBarProps) {
  const resolved = {
    ...(type === "Type2"
      ? IenNavigationBarDefaults.type2Colors()
      : IenNavigationBarDefaults.colors()),
    ...colors,
  };
  return (
    <nav
      className={cn(
        "w-full px-ien-md pb-[max(var(--ien-spacing-xs),env(safe-area-inset-bottom))] pt-ien-xs transition-[opacity,transform] duration-[var(--ien-motion-normal)] motion-reduce:transition-none",
        !visible && "pointer-events-none translate-y-full opacity-0",
        className,
      )}
      aria-label="주요 내비게이션"
      aria-hidden={!visible}
      inert={!visible}
      {...props}
    >
      <div
        data-item-count={itemCount}
        className={cn(
          "mx-auto flex min-h-[4.875rem] items-center justify-around gap-ien-xxs rounded-full px-ien-sm shadow-[0_var(--ien-elevation-floating)_calc(var(--ien-elevation-floating)*1.5)_rgb(0_0_0/0.12)]",
          type === "Type1"
            ? "w-full px-ien-xs"
            : "w-fit min-w-[min(100%,20rem)]",
        )}
        style={{ backgroundColor: resolved.containerColor, ...style }}
      >
        {items
          ? items.map((item, index) => (
              <IenNavigationBarItem
                key={`${item.href ?? "item"}-${index}`}
                index={index}
                selected={selectedIndex === index}
                icon={item.icon}
                selectedIcon={item.selectedIcon}
                label={item.label}
                badge={item.badge}
                enabled={item.enabled}
                href={item.href}
                type={type}
                colors={resolved}
                onClick={() => onSelectionChange?.(index)}
              />
            ))
          : children}
      </div>
    </nav>
  );
}

export function IenNavigationBar2(props: IenNavigationBarProps) {
  return <IenNavigationBar {...props} type="Type2" />;
}

export {
  IenNavigationBar as NavigationBar,
  IenNavigationBar2 as NavigationBar2,
  IenNavigationBarItem as NavigationBarItem,
};
