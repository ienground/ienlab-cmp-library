"use client"

import * as React from "react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Typography } from "@/components/ui/typography"
import { cn } from "cn"

export type AuthFormProps = Omit<React.ComponentProps<"form">, "title" | "onSubmit"> & {
  title: React.ReactNode
  description?: React.ReactNode
  submitLabel: string
  loading?: boolean
  error?: string
  onSubmit: (values: { email: string; password: string }) => void
}
/** 인증 서비스 연결은 호출자가 담당하는 이메일·비밀번호 폼입니다. */
function AuthForm({ title, description, submitLabel, loading, error, onSubmit, children, className, ...props }: AuthFormProps) {
  const errorId = React.useId()
  return <form {...props} data-slot="auth-form" aria-busy={loading} aria-describedby={error ? errorId : undefined} className={cn("flex w-full max-w-sm flex-col gap-4", className)} onSubmit={(event) => {
    event.preventDefault()
    if (loading) return
    const fields = new FormData(event.currentTarget)
    onSubmit({ email: String(fields.get("email") ?? ""), password: String(fields.get("password") ?? "") })
  }}>
    <Typography variant="title2">{title}</Typography>{description && <Typography variant="body2">{description}</Typography>}
    <Input type="email" name="email" label="이메일" labelOption="sustain" autoComplete="username" required disabled={loading} />
    <Input type="password" name="password" label="비밀번호" labelOption="sustain" autoComplete="current-password" required disabled={loading} />
    {error && <p id={errorId} role="alert" className="text-(--ien-danger)">{error}</p>}{children}
    <Button type="submit" loading={loading}>{submitLabel}</Button>
  </form>
}
export { AuthForm }
