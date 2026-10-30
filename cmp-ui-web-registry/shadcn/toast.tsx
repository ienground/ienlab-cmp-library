"use client";

/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import * as React from "react";
import {
  CheckIcon,
  InfoIcon,
  OctagonXIcon,
  TriangleAlertIcon,
  XIcon,
} from "lucide-react";
import { Toast as ToastPrimitive } from "radix-ui";
import { cn } from "cn";

const toneClasses = {
  neutral: "border-ien-border bg-ien-surface-raised text-ien-text-primary",
  brand: "border-ien-brand bg-ien-brand-weak text-ien-on-brand-weak",
  success: "border-ien-success bg-ien-success-weak text-ien-on-success-weak",
  warning: "border-ien-warning bg-ien-warning-weak text-ien-on-warning-weak",
  danger: "border-ien-danger bg-ien-danger-weak text-ien-on-danger-weak",
  info: "border-ien-info bg-ien-info-weak text-ien-on-info-weak",
} as const;
type IenToastTone = keyof typeof toneClasses;
type ToastProviderProps = React.ComponentProps<typeof ToastPrimitive.Provider>;
function ToastProvider({
  swipeDirection = "right",
  ...props
}: ToastProviderProps) {
  return <ToastPrimitive.Provider swipeDirection={swipeDirection} {...props} />;
}
function ToastViewport({
  className,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Viewport>) {
  return (
    <ToastPrimitive.Viewport
      data-slot="toast-viewport"
      className={cn(
        "fixed right-ien-md bottom-ien-md z-[100] flex max-h-screen w-[min(100vw-2rem,24rem)] flex-col gap-ien-xs outline-none",
        className,
      )}
      {...props}
    />
  );
}
function Toast({
  className,
  tone = "neutral",
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Root> & { tone?: IenToastTone }) {
  return (
    <ToastPrimitive.Root
      data-slot="toast"
      data-tone={tone}
      className={cn(
        "group pointer-events-auto relative flex w-full items-start justify-between gap-ien-md overflow-hidden rounded-ien-default border p-ien-md shadow-xl outline-none transition-all data-[state=closed]:animate-out data-[state=open]:animate-in data-[swipe=move]:translate-x-[var(--radix-toast-swipe-move-x)] data-[swipe=cancel]:translate-x-0 data-[swipe=end]:animate-out",
        toneClasses[tone],
        className,
      )}
      {...props}
    />
  );
}
function ToastTitle({
  className,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Title>) {
  return (
    <ToastPrimitive.Title
      data-slot="toast-title"
      className={cn("text-ien-label1", className)}
      {...props}
    />
  );
}
function ToastDescription({
  className,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Description>) {
  return (
    <ToastPrimitive.Description
      data-slot="toast-description"
      className={cn("mt-ien-xxs text-ien-body2 opacity-80", className)}
      {...props}
    />
  );
}
function ToastAction({
  className,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Action>) {
  return (
    <ToastPrimitive.Action
      data-slot="toast-action"
      className={cn(
        "inline-flex min-h-8 shrink-0 items-center rounded-ien-sm px-ien-xs text-ien-label2 underline underline-offset-2 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-current",
        className,
      )}
      {...props}
    />
  );
}
function ToastClose({
  className,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Close>) {
  return (
    <ToastPrimitive.Close
      data-slot="toast-close"
      aria-label="닫기"
      className={cn(
        "absolute top-ien-xs right-ien-xs rounded-ien-sm p-ien-xxs opacity-70 transition-opacity hover:opacity-100 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-current",
        className,
      )}
      {...props}
    >
      <XIcon aria-hidden="true" className="size-4" />
    </ToastPrimitive.Close>
  );
}

type IenToastDuration = "short" | "long" | "indefinite";
type IenToastData = {
  message: string;
  tone: IenToastTone;
  duration: IenToastDuration;
  id: number;
};
type IenToastState = {
  currentToast: IenToastData | null;
  show: (
    message: string,
    tone?: IenToastTone,
    duration?: IenToastDuration,
  ) => void;
  dismiss: () => void;
  showIenToast: (
    message: string,
    tone?: IenToastTone,
    duration?: IenToastDuration,
  ) => void;
};
const IenToastContext = React.createContext<IenToastState | null>(null);
function useIenToastState(): IenToastState {
  const [currentToast, setCurrentToast] = React.useState<IenToastData | null>(
    null,
  );
  const nextId = React.useRef(0);
  const dismiss = React.useCallback(() => setCurrentToast(null), []);
  const show = React.useCallback(
    (
      message: string,
      tone: IenToastTone = "neutral",
      duration: IenToastDuration = "short",
    ) => setCurrentToast({ message, tone, duration, id: ++nextId.current }),
    [],
  );
  return React.useMemo(
    () => ({ currentToast, show, dismiss, showIenToast: show }),
    [currentToast, show, dismiss],
  );
}
function IenToastProvider({
  state,
  children,
}: {
  state?: IenToastState;
  children: React.ReactNode;
}) {
  const localState = useIenToastState();
  const currentState = state ?? localState;
  return (
    <IenToastContext.Provider value={currentState}>
      <>
        {children}
        <IenToastHost state={currentState} />
      </>
    </IenToastContext.Provider>
  );
}
function IenToastHost({
  state,
  className,
}: {
  state: IenToastState;
  className?: string;
}) {
  React.useEffect(() => {
    const toast = state.currentToast;
    if (!toast || toast.duration === "indefinite") return;
    const timeout = window.setTimeout(
      state.dismiss,
      toast.duration === "long" ? 10000 : 4000,
    );
    return () => window.clearTimeout(timeout);
  }, [state.currentToast, state.dismiss]);
  const toast = state.currentToast;
  return (
    <div
      data-slot="ien-toast-host"
      aria-live="polite"
      aria-relevant="additions text"
      className={cn(
        "pointer-events-none fixed inset-x-0 bottom-ien-none z-[100] flex justify-center p-ien-lg",
        className,
      )}
    >
      {toast && <IenToast data={toast} onDismiss={state.dismiss} />}
    </div>
  );
}
function IenToast({
  data,
  onDismiss,
  className,
}: {
  data: IenToastData;
  onDismiss?: () => void;
  className?: string;
}) {
  const Icon =
    data.tone === "success"
      ? CheckIcon
      : data.tone === "warning"
        ? TriangleAlertIcon
        : data.tone === "danger"
          ? OctagonXIcon
          : InfoIcon;
  return (
    <div
      role="status"
      data-tone={data.tone}
      className={cn(
        "pointer-events-auto flex min-h-12 max-w-[min(100%,32rem)] items-center gap-ien-sm rounded-ien-default border px-ien-md py-ien-sm shadow-xl",
        toneClasses[data.tone],
        className,
      )}
    >
      <Icon aria-hidden="true" className="size-4 shrink-0" />
      <span className="flex-1 text-ien-body2">{data.message}</span>
      {onDismiss && (
        <button
          type="button"
          aria-label="알림 닫기"
          onClick={onDismiss}
          className="rounded-ien-sm p-ien-xxs focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-current"
        >
          <XIcon aria-hidden="true" className="size-4" />
        </button>
      )}
    </div>
  );
}
function useIenToast() {
  const state = React.useContext(IenToastContext);
  if (!state)
    throw new Error("useIenToast는 IenToastProvider 내부에서 사용해야 합니다.");
  return state;
}
export type { IenToastData, IenToastDuration, IenToastState, IenToastTone };
export {
  ToastProvider,
  ToastViewport,
  Toast,
  ToastTitle,
  ToastDescription,
  ToastAction,
  ToastClose,
  IenToastProvider,
  IenToastHost,
  IenToast,
  useIenToast,
  useIenToastState,
  toneClasses,
};
