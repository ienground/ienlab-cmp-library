import * as React from "react"
import { cn } from "cn"
import { ListHeader, ListFooter } from "@/components/ui/list-row"

export type SectionProps = Omit<React.ComponentProps<"section">, "title"> & { title?: React.ReactNode; description?: React.ReactNode; footer?: React.ReactNode }
function Section({ title, description, footer, children, className, ...props }: SectionProps) {
  const id = React.useId()
  return <section {...props} aria-labelledby={props["aria-labelledby"] ?? (title ? id : undefined)} data-slot="section" className={cn("overflow-hidden rounded-2xl bg-(--ien-surface)", className)}>
    {title && <ListHeader><h2 id={id}>{title}</h2>{description && <p className="mt-1 text-[length:var(--ien-font-size-body2)] font-normal text-(--ien-text-secondary)">{description}</p>}</ListHeader>}
    {children}{footer && <ListFooter>{footer}</ListFooter>}
  </section>
}
export { Section }
