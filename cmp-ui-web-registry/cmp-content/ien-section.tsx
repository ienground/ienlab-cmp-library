import * as React from "react";
import { cn } from "@/lib/ien-utils";

export const ienSectionBackground = "bg-ien-surface-weak";

export interface IenProvideSectionStyleProps extends Omit<
  React.ComponentPropsWithRef<"section">,
  "title"
> {
  fullHeight?: boolean;
  scrollable?: boolean;
  shape?: React.CSSProperties["borderRadius"];
  title?: React.ReactNode;
}

export function IenProvideSectionStyle({
  fullHeight = true,
  scrollable = true,
  shape,
  title,
  className,
  children,
  style,
  ...props
}: IenProvideSectionStyleProps) {
  return (
    <section
      className={cn(
        "flex flex-col gap-ien-md",
        fullHeight && "h-full",
        scrollable && "overflow-y-auto",
        className,
      )}
      style={{ ...style, borderRadius: shape ?? style?.borderRadius }}
      {...props}
    >
      {title}
      {children}
    </section>
  );
}

export interface IenSectionProps extends Omit<
  React.ComponentPropsWithRef<"section">,
  "title"
> {
  title?: React.ReactNode;
  caption?: React.ReactNode;
}

export function IenSection({
  title,
  caption,
  className,
  children,
  ...props
}: IenSectionProps) {
  return (
    <section className={cn("w-full", className)} {...props}>
      {title != null && (
        <div className="mb-ien-xs px-ien-xl text-ien-label1 text-ien-text-secondary">
          {title}
        </div>
      )}
      <div className="mx-ien-md flex flex-col gap-[var(--ien-stroke-hairline)] overflow-hidden rounded-ien-lg border border-ien-border/[.72] bg-ien-surface-raised">
        {children}
      </div>
      {caption != null && (
        <div className="mt-ien-xs px-ien-xl text-ien-caption text-ien-text-tertiary">
          {caption}
        </div>
      )}
    </section>
  );
}

export { IenSection as Section };
