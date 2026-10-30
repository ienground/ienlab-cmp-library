import * as React from "react";
import { cn } from "cn";

export const ienSectionBackground = "bg-ien-surface-weak";

export interface IenProvideSectionStyleProps extends Omit<
  React.ComponentPropsWithRef<"section">,
  "title"
> {
  fullHeight?: boolean;
  scrollable?: boolean;
  title?: React.ReactNode;
}

export function IenProvideSectionStyle({
  fullHeight = true,
  scrollable = true,
  title,
  className,
  children,
  ...props
}: IenProvideSectionStyleProps) {
  return (
    <section
      className={cn(
        "flex flex-col gap-ien-md",
        fullHeight && "min-h-full",
        scrollable && "overflow-y-auto",
        className,
      )}
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
      <div className="overflow-hidden rounded-ien-lg border border-ien-border/70 bg-ien-surface-raised [&>*+*]:border-t [&>*+*]:border-ien-border/60">
        <div className="divide-y divide-ien-border/60">{children}</div>
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
