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
import { cn } from "@/lib/ien-utils";

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
        "group pointer-events-auto relative flex w-full items-start justify-between gap-ien-md overflow-hidden rounded-ien-default border p-ien-md shadow-xl outline-none duration-[var(--ien-motion-normal)] ease-[var(--ien-motion-standard-easing)] transition-transform data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=closed]:slide-out-to-bottom-2 data-[state=open]:animate-in data-[state=open]:fade-in-0 data-[state=open]:slide-in-from-bottom-2 data-[swipe=move]:translate-x-[var(--radix-toast-swipe-move-x)] data-[swipe=move]:transition-none data-[swipe=cancel]:translate-x-0 data-[swipe=end]:translate-x-[var(--radix-toast-swipe-end-x)] data-[swipe=end]:animate-out motion-reduce:animate-none motion-reduce:transition-none",
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
  children,
  ...props
}: React.ComponentProps<typeof ToastPrimitive.Close>) {
  return (
    <ToastPrimitive.Close
      data-slot="toast-close"
      aria-label="닫기"
      className={cn(
        "shrink-0 rounded-ien-sm p-ien-xxs opacity-70 transition-opacity hover:opacity-100 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-current",
        className,
      )}
      {...props}
    >
      {children ?? <XIcon aria-hidden="true" className="size-4" />}
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
  const toast = state.currentToast;
  const [lastToast, setLastToast] = React.useState(toast);
  if (toast && toast !== lastToast) setLastToast(toast);
  const displayedToast = toast ?? lastToast;
  return (
    <ToastProvider>
      {displayedToast && (
        <Toast
          key={displayedToast.id}
          open={toast !== null}
          onOpenChange={(open) => {
            if (!open) state.dismiss();
          }}
          duration={
            displayedToast.duration === "indefinite"
              ? Infinity
              : displayedToast.duration === "long"
                ? 10000
                : 4000
          }
          tone={displayedToast.tone}
          className="items-center py-ien-sm"
          asChild
        >
          <IenToast data={displayedToast} onDismiss={state.dismiss} />
        </Toast>
      )}
      <ToastViewport
        data-slot="ien-toast-host"
        className={cn(
          "pointer-events-none inset-x-0 bottom-ien-none mx-auto w-full max-w-[35rem] items-center p-ien-lg",
          className,
        )}
      />
    </ToastProvider>
  );
}
function IenToast({
  data,
  onDismiss,
  className,
  ...props
}: {
  data: IenToastData;
  onDismiss?: () => void;
} & React.ComponentPropsWithRef<"div">) {
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
      {...props}
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
