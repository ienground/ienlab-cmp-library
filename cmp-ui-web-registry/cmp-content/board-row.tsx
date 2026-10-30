"use client";

import * as React from "react";
import { ChevronDown } from "lucide-react";
import { cn } from "cn";

export interface IenBoardRowState {
  opened: boolean;
  setOpened: (opened: boolean) => void;
}

export function useIenBoardRowState(initialOpened = false): IenBoardRowState {
  const [opened, setOpened] = React.useState(initialOpened);
  return React.useMemo(() => ({ opened, setOpened }), [opened]);
}

export const rememberIenBoardRowState = useIenBoardRowState;

export interface IenBoardRowProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "title" | "prefix"
> {
  title: React.ReactNode;
  opened?: boolean;
  state?: IenBoardRowState;
  initialOpened?: boolean;
  defaultOpened?: boolean;
  onOpenedChange?: (opened: boolean) => void;
  prefix?: React.ReactNode;
  icon?: React.ReactNode | ((opened: boolean) => React.ReactNode) | null;
  children: React.ReactNode;
}

export function IenBoardRow({
  title,
  opened: openedProp,
  state,
  initialOpened,
  defaultOpened,
  onOpenedChange,
  prefix,
  icon,
  children,
  className,
  ...props
}: IenBoardRowProps) {
  const [uncontrolledOpened, setUncontrolledOpened] = React.useState(
    initialOpened ?? defaultOpened ?? false,
  );
  const opened = openedProp ?? state?.opened ?? uncontrolledOpened;
  const panelId = React.useId();
  const changeOpened = (next: boolean) => {
    if (openedProp === undefined && state == null) setUncontrolledOpened(next);
    state?.setOpened(next);
    onOpenedChange?.(next);
  };

  return (
    <div
      className={cn(
        "overflow-hidden rounded-ien-default bg-ien-surface",
        className,
      )}
      {...props}
    >
      <button
        type="button"
        className="flex min-h-[var(--ien-state-minimum-touch-target)] w-full items-center gap-ien-sm rounded-ien-default px-ien-md py-ien-sm text-left transition-colors hover:bg-ien-surface-weak focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand active:scale-[var(--ien-button-pressed-scale)]"
        aria-expanded={opened}
        aria-controls={panelId}
        onClick={() => changeOpened(!opened)}
      >
        {prefix != null && <span className="shrink-0">{prefix}</span>}
        <span className="min-w-0 flex-1">{title}</span>
        {typeof icon === "function"
          ? icon(opened)
          : (icon ?? (
              <ChevronDown
                aria-hidden
                className={cn(
                  "size-4 text-ien-text-tertiary transition-transform",
                  opened && "rotate-180",
                )}
              />
            ))}
      </button>
      <div
        id={panelId}
        className={cn(
          "grid transition-[grid-template-rows,opacity] duration-[var(--ien-motion-fast)] motion-reduce:transition-none",
          opened ? "grid-rows-[1fr] opacity-100" : "grid-rows-[0fr] opacity-0",
        )}
        aria-hidden={!opened}
        inert={!opened}
      >
        <div className="min-h-0 overflow-hidden bg-ien-brand-weak p-ien-md">
          {children}
        </div>
      </div>
    </div>
  );
}

export { IenBoardRow as BoardRow };
