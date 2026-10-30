import * as React from "react";
import { Check, CircleHelp, SearchX, X } from "lucide-react";
import { IenAssetFrame } from "@/components/ui/asset";
import { cn } from "cn";

export type IenResultTone = "success" | "failure" | "empty" | "info";

export interface IenResultProps extends Omit<
  React.ComponentPropsWithRef<"div">,
  "title"
> {
  title: React.ReactNode;
  description?: React.ReactNode;
  tone?: IenResultTone;
  icon?: React.ReactNode;
  primaryAction?: React.ReactNode;
  secondaryAction?: React.ReactNode;
}

const toneMap = {
  success: {
    semantic: "success" as const,
    icon: <Check aria-hidden className="size-6" />,
  },
  failure: {
    semantic: "danger" as const,
    icon: <X aria-hidden className="size-6" />,
  },
  empty: {
    semantic: "neutral" as const,
    icon: <SearchX aria-hidden className="size-6" />,
  },
  info: {
    semantic: "info" as const,
    icon: <CircleHelp aria-hidden className="size-6" />,
  },
};

export function IenResult({
  title,
  description,
  tone = "info",
  icon,
  primaryAction,
  secondaryAction,
  className,
  ...props
}: IenResultProps) {
  const config = toneMap[tone];
  return (
    <section
      className={cn(
        "flex w-full flex-col items-center justify-center gap-ien-md p-ien-xl text-center",
        className,
      )}
      {...props}
    >
      <IenAssetFrame size="large" shape="circle" tone={config.semantic}>
        {icon ?? config.icon}
      </IenAssetFrame>
      <div className="pt-ien-xs text-ien-title2 font-bold text-ien-text-primary">
        {title}
      </div>
      {description != null && (
        <div className="max-w-prose text-ien-body2 text-ien-text-secondary">
          {description}
        </div>
      )}
      {primaryAction}
      {secondaryAction}
    </section>
  );
}

export { IenResult as Result };
