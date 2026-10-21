import * as React from "react";
import { cn } from "cn";

export type IenListHeaderDescriptionPosition = "top" | "bottom";

export const IenListHeaderDefaults = {
  Title: ({
    text,
    className,
    ...props
  }: React.ComponentPropsWithRef<"h3"> & { text: string }) => (
    <h3
      className={cn(
        "text-ien-title3 font-bold text-ien-text-primary",
        className,
      )}
      {...props}
    >
      {text}
    </h3>
  ),
  Description: ({
    text,
    className,
    ...props
  }: React.ComponentPropsWithRef<"p"> & { text: string }) => (
    <p
      className={cn("text-ien-caption text-ien-text-secondary", className)}
      {...props}
    >
      {text}
    </p>
  ),
} as const;

export interface IenListHeaderProps extends Omit<
  React.ComponentPropsWithRef<"header">,
  "title"
> {
  title: React.ReactNode;
  description?: React.ReactNode;
  descriptionPosition?: IenListHeaderDescriptionPosition;
  right?: React.ReactNode;
  rightAlignment?: "start" | "center" | "end";
}

export function IenListHeader({
  title,
  description,
  descriptionPosition = "top",
  right,
  rightAlignment = "center",
  className,
  ...props
}: IenListHeaderProps) {
  return (
    <header
      className={cn(
        "flex w-full items-center gap-ien-sm px-ien-md py-ien-sm",
        className,
      )}
      {...props}
    >
      <div className="min-w-0 flex-1">
        {description != null && descriptionPosition === "top" && (
          <div className="mb-0.5">{description}</div>
        )}
        {title}
        {description != null && descriptionPosition === "bottom" && (
          <div className="mt-0.5">{description}</div>
        )}
      </div>
      {right != null && (
        <div
          className={cn(
            "flex shrink-0 items-center gap-1",
            rightAlignment === "start" && "self-start",
            rightAlignment === "end" && "self-end",
          )}
        >
          {right}
        </div>
      )}
    </header>
  );
}

export { IenListHeader as ListHeader };
