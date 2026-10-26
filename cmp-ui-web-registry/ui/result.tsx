import * as React from "react"
import { IconCheck, IconX, IconInfoCircle, IconInbox } from "@tabler/icons-react"
import { Empty, EmptyHeader, EmptyTitle, EmptyDescription, EmptyContent, EmptyMedia } from "@/components/ui/empty"
import { AssetFrame } from "@/components/ui/asset-frame"
import { cn } from "cn"

export type ResultProps = Omit<React.ComponentProps<typeof Empty>, "title" | "tone"> & {
  title: React.ReactNode
  description?: React.ReactNode
  tone?: "success" | "failure" | "empty" | "info"
  icon?: React.ReactNode
  primaryAction?: React.ReactNode
  secondaryAction?: React.ReactNode
}
function Result({ title, description, tone = "info", icon, primaryAction, secondaryAction, ...props }: ResultProps) {
  const Icon = { success: IconCheck, failure: IconX, empty: IconInbox, info: IconInfoCircle }[tone]
  const semanticTone = tone === "failure" ? "danger" : tone === "empty" ? "neutral" : tone
  return <Empty {...props} tone={semanticTone} className={cn("gap-4 p-8", props.className)}>
    <EmptyHeader><EmptyMedia><AssetFrame tone={semanticTone} size="large" shape="circle">{icon ?? <Icon />}</AssetFrame></EmptyMedia><EmptyTitle>{title}</EmptyTitle>{description && <EmptyDescription>{description}</EmptyDescription>}</EmptyHeader>
    {(primaryAction || secondaryAction) && <EmptyContent>{primaryAction}{secondaryAction}</EmptyContent>}
  </Empty>
}
export { Result }
