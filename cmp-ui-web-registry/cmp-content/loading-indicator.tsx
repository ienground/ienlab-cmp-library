import * as React from "react";
import { cn } from "cn";

export interface IenLoadingIndicatorProps extends React.ComponentPropsWithRef<"svg"> {
  color?: string;
  label?: string;
}

export function IenLoadingIndicator({
  color = "var(--ien-brand)",
  label = "불러오는 중",
  className,
  ...props
}: IenLoadingIndicatorProps) {
  return (
    <svg
      viewBox="0 0 48 48"
      className={cn(
        "size-12 animate-spin motion-reduce:animate-none",
        className,
      )}
      role="status"
      aria-label={label}
      {...props}
    >
      <path
        d="M24 2 28.8 15.2 42.5 9.5 36.8 23.2 46 24 36.8 28.8 42.5 42.5 28.8 36.8 24 46 19.2 36.8 5.5 42.5 11.2 28.8 2 24 11.2 19.2 5.5 5.5 19.2 11.2Z"
        fill={color}
      />
    </svg>
  );
}

export { IenLoadingIndicator as LoadingIndicator };
