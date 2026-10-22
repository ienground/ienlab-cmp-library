import * as React from "react";
import { cn } from "@/lib/ien-utils";

export interface IenPostProps extends React.ComponentPropsWithRef<"article"> {
  title: string;
  author?: string;
  description?: string;
  media?: React.ReactNode;
  metadata?: React.ReactNode;
  action?: React.ReactNode;
}

export function IenPost({
  title,
  author,
  description,
  media,
  metadata,
  action,
  className,
  ...props
}: IenPostProps) {
  return (
    <article
      className={cn(
        "flex w-full flex-col gap-ien-sm bg-ien-surface p-ien-md text-ien-text-primary",
        className,
      )}
      {...props}
    >
      {(author != null || metadata != null) && (
        <div className="flex w-full items-center gap-ien-xs text-ien-caption text-ien-text-tertiary">
          {author != null && <span className="min-w-0 flex-1">{author}</span>}
          {metadata}
        </div>
      )}
      <h3 className="line-clamp-2 text-ien-title3 font-semibold">{title}</h3>
      {description != null && (
        <p className="text-ien-body2 text-ien-text-secondary">{description}</p>
      )}
      {media != null && (
        <div className="overflow-hidden">{media}</div>
      )}
      {action != null && <div>{action}</div>}
    </article>
  );
}

export { IenPost as Post };
