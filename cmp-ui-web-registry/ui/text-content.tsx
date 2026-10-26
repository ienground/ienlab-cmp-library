import * as React from "react"
import { Typography } from "@/components/ui/typography"
import { cn } from "cn"
import type { IenTone } from "@/lib/ien-theme"

export type HighlightTextProps = React.ComponentProps<"span"> & { text: string; highlights?: string[]; tone?: IenTone; bold?: boolean }
function HighlightText({ text, highlights = [], tone = "brand", bold = false, ...props }: HighlightTextProps) {
  const words = highlights.filter(Boolean).sort((a, b) => b.length - a.length)
  if (!words.length) return <span {...props}>{text}</span>
  const escaped = words.map((word) => word.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"))
  const pattern = new RegExp(`(${escaped.join("|")})`, "g")
  return <span {...props}>{text.split(pattern).map((part, index) => words.includes(part) ? <mark key={index} data-ien="" data-ien-tone={tone} className={cn("rounded bg-(--ien-control-weak) text-(--ien-control-color)", bold && "font-bold")}>{part}</mark> : part)}</span>
}
export type ParagraphProps = Omit<React.ComponentProps<"section">, "title"> & { title?: React.ReactNode; body: string; footer?: React.ReactNode; emphasis?: string[]; tone?: IenTone; bold?: boolean }
function Paragraph({ title, body, footer, emphasis, tone, bold, className, ...props }: ParagraphProps) {
  return <section {...props} data-slot="paragraph" className={cn("space-y-2", className)}>{title && <Typography variant="title3">{title}</Typography>}<Typography variant="body2" className="whitespace-pre-wrap text-(--ien-text-secondary)"><HighlightText text={body} highlights={emphasis} tone={tone} bold={bold} /></Typography>{footer && <Typography variant="caption" className="text-(--ien-text-tertiary)">{footer}</Typography>}</section>
}
export type PostProps = Omit<React.ComponentProps<"article">, "title"> & { title: React.ReactNode; author?: React.ReactNode; description?: React.ReactNode; media?: React.ReactNode; metadata?: React.ReactNode; action?: React.ReactNode }
function Post({ title, author, description, media, metadata, action, className, ...props }: PostProps) {
  return <article {...props} data-slot="post" className={cn("space-y-3 bg-(--ien-surface) p-4", className)}>{(author || metadata) && <div className="flex items-center justify-between gap-2 text-[length:var(--ien-font-size-caption)] text-(--ien-text-tertiary)"><span>{author}</span>{metadata}</div>}<Typography variant="title3">{title}</Typography>{description && <Typography variant="body2" className="text-(--ien-text-secondary)">{description}</Typography>}{media}{action}</article>
}
function Bubble({ background = "weak", tone = "neutral", className, ...props }: React.ComponentProps<"div"> & { background?: "weak" | "fill" | "surface"; tone?: IenTone }) {
  return <div {...props} data-slot="bubble" data-ien="" data-ien-tone={tone} className={cn("w-fit max-w-full rounded-2xl px-4 py-3 text-[length:var(--ien-font-size-body2)] leading-(--ien-line-height-body2)", background === "surface" ? "bg-(--ien-surface) text-(--ien-text-primary)" : background === "fill" ? "ien-fill" : "ien-weak", className)} />
}
export { HighlightText, Paragraph, Post, Bubble }
