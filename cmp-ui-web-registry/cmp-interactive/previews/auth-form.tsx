"use client";

import * as React from "react";
import { AuthForm, type AuthFormMode } from "@/components/ui/auth-form";

const copy = {
  login: {
    title: "로그인",
    description: "계정으로 계속하세요.",
    submitLabel: "로그인",
    modePrompt: "계정이 없으신가요?",
    modeActionLabel: "회원가입",
  },
  signUp: {
    title: "회원가입",
    submitLabel: "계정 만들기",
    modePrompt: "이미 계정이 있으신가요?",
    modeActionLabel: "로그인",
  },
  passwordReset: {
    title: "비밀번호 재설정",
    description: "재설정 안내를 보낼 이메일을 입력하세요.",
    submitLabel: "재설정 링크 보내기",
    modePrompt: "로그인으로 돌아가기",
    modeActionLabel: "로그인",
  },
  emailLabel: "이메일",
  emailPlaceholder: "name@example.com",
  passwordLabel: "비밀번호",
  confirmPasswordLabel: "비밀번호 확인",
  passwordRulesTitle: "비밀번호 조건",
};

export default function Preview() {
  const [mode, setMode] = React.useState<AuthFormMode>("login");
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [confirm, setConfirm] = React.useState("");
  const [message, setMessage] = React.useState("");
  return (
    <div className="mx-auto max-w-sm">
      <AuthForm
        mode={mode}
        email={email}
        password={password}
        confirmPassword={confirm}
        onEmailChange={setEmail}
        onPasswordChange={setPassword}
        onConfirmPasswordChange={setConfirm}
        onModeChange={setMode}
        onSubmit={() => setMessage("제출 요청을 보냈습니다.")}
        copy={copy}
        passwordRules={[
          { label: "8자 이상", satisfied: password.length >= 8 },
          { label: "숫자 포함", satisfied: /\d/.test(password) },
        ]}
        status={message ? { type: "success", message } : undefined}
      />
    </div>
  );
}
