"use client";

import * as React from "react";
import { Check, Circle, LoaderCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { TextField, PasswordTextField } from "@/components/ui/text-field";
import { cn } from "cn";

export type AuthFormMode = "login" | "sign-up" | "password-reset";
export type AuthFormModeCopy = {
  title: string;
  description?: string;
  submitLabel: string;
  modePrompt?: string;
  modeActionLabel?: string;
};
export type AuthFormCopy = {
  login: AuthFormModeCopy;
  signUp: AuthFormModeCopy;
  passwordReset?: AuthFormModeCopy;
  emailLabel: string;
  emailPlaceholder?: string;
  passwordLabel: string;
  passwordPlaceholder?: string;
  confirmPasswordLabel: string;
  confirmPasswordPlaceholder?: string;
  passwordRulesTitle?: string;
  socialLoginTitle?: string;
};
export type AuthFormPasswordRule = {
  label: string;
  satisfied: boolean;
  statusDescription?: string;
};
export type AuthFormProps = Omit<
  React.ComponentProps<"form">,
  "children" | "onSubmit" | "ref" | "className"
> & {
  ref?: React.Ref<HTMLFormElement>;
  mode: AuthFormMode;
  email: string;
  password: string;
  copy: AuthFormCopy;
  onEmailChange: (value: string) => void;
  onPasswordChange: (value: string) => void;
  onSubmit: () => void;
  onModeChange: (mode: AuthFormMode) => void;
  confirmPassword?: string;
  onConfirmPasswordChange?: (value: string) => void;
  passwordRules?: AuthFormPasswordRule[];
  providers?: React.ReactNode;
  loading?: boolean;
  status?: { type: "error" | "success"; message: string };
  guestAction?: { label: string; onClick: () => void };
  modePromptActionLabel?: string;
  onModePromptActionClick?: () => void;
  className?: string;
};

function AuthForm({
  mode,
  email,
  password,
  copy,
  onEmailChange,
  onPasswordChange,
  onSubmit,
  onModeChange,
  confirmPassword,
  onConfirmPasswordChange,
  passwordRules = [],
  providers,
  loading = false,
  status,
  guestAction,
  modePromptActionLabel,
  onModePromptActionClick,
  className,
  ref,
  ...formProps
}: AuthFormProps) {
  const [internalConfirmPassword, setInternalConfirmPassword] =
    React.useState("");
  const resolvedConfirmPassword = confirmPassword ?? internalConfirmPassword;
  const updateConfirmPassword = (next: string) => {
    if (confirmPassword === undefined) setInternalConfirmPassword(next);
    onConfirmPasswordChange?.(next);
  };
  const modeCopy =
    mode === "login"
      ? copy.login
      : mode === "sign-up"
        ? copy.signUp
        : (copy.passwordReset ?? copy.login);
  const showPassword = mode !== "password-reset";
  const showConfirm = mode === "sign-up";
  const confirmationMismatch =
    showConfirm &&
    resolvedConfirmPassword.length > 0 &&
    resolvedConfirmPassword !== password;
  const promptMode: AuthFormMode = mode === "login" ? "sign-up" : "login";
  const promptLabel = modePromptActionLabel ?? modeCopy.modeActionLabel;
  const submit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (loading || confirmationMismatch) return;
    onSubmit();
  };
  return (
    <form
      {...formProps}
      ref={ref}
      data-slot="auth-form"
      onSubmit={submit}
      className={cn("grid gap-ien-md", className)}
    >
      <header className="grid gap-ien-xxs">
        <h2 className="text-ien-title1 text-ien-text-primary">
          {modeCopy.title}
        </h2>
        {modeCopy.description && (
          <p className="text-ien-body2 text-ien-text-secondary">
            {modeCopy.description}
          </p>
        )}
      </header>
      <div className="grid gap-ien-sm">
        <TextField
          type="email"
          autoComplete="email"
          required
          label={copy.emailLabel}
          placeholder={copy.emailPlaceholder}
          value={email}
          onValueChange={onEmailChange}
          disabled={loading}
        />
        {showPassword && (
          <PasswordTextField
            autoComplete={
              mode === "sign-up" ? "new-password" : "current-password"
            }
            required
            label={copy.passwordLabel}
            placeholder={copy.passwordPlaceholder}
            value={password}
            onValueChange={onPasswordChange}
            disabled={loading}
          />
        )}
        {showConfirm && (
          <TextField
            type="password"
            autoComplete="new-password"
            required
            label={copy.confirmPasswordLabel}
            placeholder={copy.confirmPasswordPlaceholder}
            value={resolvedConfirmPassword}
            onValueChange={updateConfirmPassword}
            disabled={loading}
            error={
              confirmationMismatch ? "비밀번호가 일치하지 않습니다." : undefined
            }
          />
        )}
      </div>
      {showConfirm && passwordRules.length > 0 && (
        <section
          aria-label={copy.passwordRulesTitle ?? "비밀번호 조건"}
          className="grid gap-ien-xs text-ien-caption"
        >
          <h3 className="text-ien-label2 text-ien-text-secondary">
            {copy.passwordRulesTitle ?? "비밀번호 조건"}
          </h3>
          <ul className="grid gap-ien-xxs">
            {passwordRules.map((rule) => (
              <li
                key={rule.label}
                aria-label={rule.statusDescription ?? rule.label}
                className={cn(
                  "flex items-center gap-ien-xs",
                  rule.satisfied
                    ? "text-ien-success"
                    : "text-ien-text-secondary",
                )}
              >
                {rule.satisfied ? (
                  <Check aria-hidden="true" className="size-4" />
                ) : (
                  <Circle aria-hidden="true" className="size-3" />
                )}
                {rule.label}
              </li>
            ))}
          </ul>
        </section>
      )}
      {providers && (
        <section className="grid gap-ien-xs">
          <p className="text-center text-ien-caption text-ien-text-secondary">
            {copy.socialLoginTitle}
          </p>
          {providers}
        </section>
      )}
      {status && (
        <p
          role={status.type === "error" ? "alert" : "status"}
          aria-live={status.type === "error" ? "assertive" : "polite"}
          className={cn(
            "rounded-ien-sm p-ien-sm text-ien-body2",
            status.type === "error"
              ? "bg-ien-danger-weak text-ien-danger"
              : "bg-ien-success-weak text-ien-success",
          )}
        >
          {status.message}
        </p>
      )}
      <Button
        type="submit"
        disabled={loading || confirmationMismatch}
        className="w-full"
      >
        {loading && (
          <LoaderCircle aria-hidden="true" className="size-4 animate-spin" />
        )}
        {modeCopy.submitLabel}
      </Button>
      {guestAction && (
        <Button
          type="button"
          variant="weak"
          tone="neutral"
          disabled={loading}
          onClick={guestAction.onClick}
          className="w-full"
        >
          {guestAction.label}
        </Button>
      )}
      {(modeCopy.modePrompt || promptLabel) && (
        <p className="text-center text-ien-body2 text-ien-text-secondary">
          {modeCopy.modePrompt}{" "}
          {promptLabel && (
            <button
              type="button"
              disabled={loading}
              onClick={
                onModePromptActionClick ?? (() => onModeChange(promptMode))
              }
              className="ml-ien-xxs font-semibold text-ien-brand underline underline-offset-2 disabled:opacity-50"
            >
              {promptLabel}
            </button>
          )}
        </p>
      )}
    </form>
  );
}

export { AuthForm, AuthForm as IenAuthForm };
