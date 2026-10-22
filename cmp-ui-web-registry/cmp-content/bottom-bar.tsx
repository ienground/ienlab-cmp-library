import * as React from "react";
import { cn } from "@/lib/ien-utils";

export interface IenBottomBarDefaultsType {
  contentHeight: number;
}
export const IenBottomBarDefaults: IenBottomBarDefaultsType = {
  contentHeight: 64,
};

export interface IenBottomBarProps extends React.ComponentPropsWithRef<"footer"> {
  actions?: React.ReactNode;
  floatingActionButton?: React.ReactNode;
  containerColor?: string;
  contentColor?: string;
  contentPadding?: string;
  elevation?: number | string;
  shape?: React.CSSProperties["borderRadius"];
  visible?: boolean;
}

export function IenBottomBar({
  actions,
  floatingActionButton,
  containerColor = "var(--ien-surface)",
  contentColor = "var(--ien-text-primary)",
  contentPadding = "var(--ien-spacing-xs)",
  elevation = "var(--ien-elevation-floating)",
  visible = true,
  shape,
  className,
  children,
  style,
  ...props
}: IenBottomBarProps) {
  return (
    <div
      className={cn(
        "w-full transition-[opacity,transform] duration-[var(--ien-motion-normal)] motion-reduce:transition-none",
        visible
          ? "translate-y-0 opacity-100"
          : "pointer-events-none translate-y-full opacity-0",
        className,
      )}
      aria-hidden={!visible}
      inert={!visible}
    >
      <footer
        className="w-full px-ien-md py-ien-xs"
        style={{
          paddingBottom:
            "max(var(--ien-spacing-xs), env(safe-area-inset-bottom))",
        }}
        {...props}
      >
        <div
          className="flex min-h-[var(--ien-bottom-bar-height)] w-full items-center gap-ien-xxs rounded-full border border-ien-border bg-ien-surface px-ien-xs"
          style={
            {
              backgroundColor: containerColor,
              color: contentColor,
              padding: contentPadding,
              borderRadius: shape,
              boxShadow: `0 calc(${typeof elevation === "number" ? `${elevation}px` : elevation} / 3) ${typeof elevation === "number" ? `${elevation}px` : elevation} rgb(0 0 0 / 12%)`,
              "--ien-bottom-bar-height": `${IenBottomBarDefaults.contentHeight}px`,
              ...style,
            } as React.CSSProperties
          }
        >
          <div className="flex min-w-0 flex-1 items-center gap-ien-xxs">
            {actions ?? children}
          </div>
          {floatingActionButton != null && (
            <div className="ml-ien-sm flex shrink-0 items-center">
              {floatingActionButton}
            </div>
          )}
        </div>
      </footer>
    </div>
  );
}

export { IenBottomBar as BottomBar };
