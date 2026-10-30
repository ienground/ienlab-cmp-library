"use client"

import * as React from "react"
import { IconDeviceFloppy, IconTrash, IconRefresh, IconCloudOff } from "@tabler/icons-react"
import { AlertDialog, ConfirmDialog, type AlertDialogProps, type ConfirmDialogProps } from "@/components/ui/alert-dialog"
import { Dialog, DialogClose, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input, type InputProps } from "@/components/ui/input"
import { ProgressIndicator } from "@/components/ui/progress-indicator"
import { DatePicker, type DatePickerProps } from "@/components/reui/date-picker"
import { TimePicker, TimePickerPanel, TimePickerColumns, parseTimeValue, formatTimeValue, type TimePickerProps } from "@/components/reui/time-picker"
import { useIenLanguage } from "@/lib/ien-theme-context"

type PresetProps = Omit<AlertDialogProps, "title"> & { title?: React.ReactNode }
type ConfirmPresetProps = Omit<ConfirmDialogProps, "title"> & { title?: React.ReactNode }

export type SaveAlertDialogProps = PresetProps & {
  onSave: () => void
  onUnsave: () => void
  onCancel?: () => void
  saveDisabled?: boolean
  unsaveDisabled?: boolean
}
function SaveAlertDialog({ title, message, onSave, onUnsave, onCancel, saveDisabled, unsaveDisabled, ...props }: SaveAlertDialogProps) {
  const ko = useIenLanguage("ko") === "ko"
  return <AlertDialog buttonLayout="vertical" icon={<IconDeviceFloppy />} {...props} title={title ?? (ko ? "변경 사항을 저장할까요?" : "Save changes?")} message={message ?? (ko ? "저장하지 않은 변경 사항이 있습니다." : "You have unsaved changes.")} actions={<>
    <DialogClose asChild><Button disabled={saveDisabled} onClick={onSave}>{ko ? "저장" : "Save"}</Button></DialogClose>
    <DialogClose asChild><Button variant="weak" tone="danger" disabled={unsaveDisabled} onClick={onUnsave}>{ko ? "저장하지 않음" : "Don't save"}</Button></DialogClose>
    <DialogClose asChild><Button data-dialog-cancel="" variant="ghost" tone="neutral" onClick={onCancel}>{ko ? "취소" : "Cancel"}</Button></DialogClose>
  </>} />
}
function DeleteAlertDialog({ title, message, confirmText, ...props }: ConfirmPresetProps) {
  const ko = useIenLanguage("ko") === "ko"
  return <ConfirmDialog tone="danger" icon={<IconTrash />} {...props} title={title ?? (ko ? "삭제할까요?" : "Delete item?")} message={message ?? (ko ? "삭제한 항목은 복구할 수 없습니다." : "This action cannot be undone.")} confirmText={confirmText ?? (ko ? "삭제" : "Delete")} />
}
function UpdateAlertDialog({ title, message, appName = "", ...props }: PresetProps & { appName?: string }) {
  const ko = useIenLanguage("ko") === "ko"
  return <AlertDialog icon={<IconRefresh />} {...props} title={title ?? (ko ? "업데이트 안내" : "Update available")} message={message ?? (ko ? `${appName || "새로운"} 버전이 준비되었습니다.` : `A new version of ${appName || "the app"} is available.`)} />
}
function NetworkAlertDialog({ title, message, onRetry, ...props }: ConfirmPresetProps & { onRetry?: () => void }) {
  const ko = useIenLanguage("ko") === "ko"
  const text = { title: title ?? (ko ? "네트워크 연결 확인" : "Check your connection"), message: message ?? (ko ? "연결 상태를 확인한 뒤 다시 시도해주세요." : "Check your connection and try again.") }
  return onRetry
    ? <ConfirmDialog icon={<IconCloudOff />} confirmText={ko ? "다시 시도" : "Retry"} cancelText={ko ? "닫기" : "Close"} {...props} {...text} onConfirm={onRetry} />
    : <AlertDialog icon={<IconCloudOff />} confirmText={ko ? "닫기" : "Close"} {...props} {...text} />
}

export type ProgressDialogProps = Pick<AlertDialogProps, "open" | "onOpenChange" | "message"> & {
  title?: React.ReactNode
  progress?: number
  onCancel?: () => void
}
function ProgressDialog({ title, message, progress, onCancel, onOpenChange, ...props }: ProgressDialogProps) {
  const ko = useIenLanguage("ko") === "ko"
  return <Dialog {...props} onOpenChange={(open) => { if (!open) onCancel?.(); onOpenChange?.(open) }}>
    <DialogContent showCloseButton={false} dismissible={Boolean(onCancel)}>
      <DialogHeader><DialogTitle>{title ?? (ko ? "처리 중" : "Processing")}</DialogTitle><DialogDescription>{message ?? (ko ? "잠시 기다려주세요." : "Please wait.")}</DialogDescription></DialogHeader>
      <ProgressIndicator variant="linear" progress={progress} aria-label={ko ? "진행 상태" : "Progress"} />
      {onCancel && <DialogFooter><DialogClose asChild><Button variant="weak">{ko ? "취소" : "Cancel"}</Button></DialogClose></DialogFooter>}
    </DialogContent>
  </Dialog>
}

type ValueDialogProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  title?: React.ReactNode
  message?: React.ReactNode
}
export type TextFieldDialogField = Omit<InputProps, "value" | "defaultValue" | "onChange" | "onValueChange" | "name"> & {
  name: string
  initialValue?: string
  validate?: (value: string) => string | undefined
}
export type TextFieldDialogProps = ValueDialogProps & {
  fields?: TextFieldDialogField[]
  onConfirm: (values: Record<string, string>) => void
  confirmText?: string
}
function TextFieldDialog({ title, message, fields = [], onConfirm, confirmText, ...props }: TextFieldDialogProps) {
  const ko = useIenLanguage("ko") === "ko"
  return <AlertDialog {...props} title={title ?? (ko ? "내용 입력" : "Enter details")} message={message} actions={null}>
    <TextFieldDialogForm fields={fields} confirmText={confirmText} onConfirm={(values) => { onConfirm(values); props.onOpenChange(false) }} />
  </AlertDialog>
}
function TextFieldDialogForm({ fields, onConfirm, confirmText }: Pick<TextFieldDialogProps, "fields" | "onConfirm" | "confirmText">) {
  const ko = useIenLanguage("ko") === "ko"
  const [values, setValues] = React.useState<Record<string, string>>(() => Object.fromEntries((fields ?? []).map((field) => [field.name, field.initialValue ?? ""])))
  const [submitted, setSubmitted] = React.useState(false)
  const errors = Object.fromEntries((fields ?? []).map((field) => [field.name, field.validate?.(values[field.name] ?? "")]))
  return <form className="flex flex-col gap-ien-md" onSubmit={(event) => {
    event.preventDefault()
    setSubmitted(true)
    if (Object.values(errors).some(Boolean)) return
    onConfirm(Object.fromEntries((fields ?? []).map((field) => [field.name, values[field.name] ?? ""])))
  }}>
    {(fields ?? []).map(({ name, initialValue, validate, ...field }) => <Input {...field} key={name} name={name} label={field.label ?? name} value={values[name] ?? ""} hasError={Boolean(submitted && errors[name])} supportingText={submitted && errors[name] ? errors[name] : field.supportingText} onValueChange={(value) => setValues((previous) => ({ ...previous, [name]: value }))} />)}
    <DialogFooter><DialogClose asChild><Button type="button" data-dialog-cancel="" variant="weak" tone="neutral">{ko ? "취소" : "Cancel"}</Button></DialogClose><Button type="submit">{confirmText ?? (ko ? "확인" : "Confirm")}</Button></DialogFooter>
  </form>
}

export type DatePickerDialogProps = ValueDialogProps & {
  value?: string | null
  onConfirm: (value: string) => void
  pickerProps?: Omit<DatePickerProps, "value" | "defaultValue" | "onValueChange" | "inline" | "requireConfirm" | "open" | "onOpenChange">
}
export type TimePickerDialogProps = ValueDialogProps & {
  value?: string | null
  onConfirm: (value: string) => void
  pickerProps?: Omit<TimePickerProps, "value" | "defaultValue" | "onValueChange" | "requireConfirm" | "open" | "onOpenChange" | "children">
}
function DatePickerDialog({ title, value, pickerProps, onConfirm, ...props }: DatePickerDialogProps) {
  const ko = useIenLanguage("ko") === "ko"
  const isValid = (next: string) => {
    if (!/^\d{4}-\d{2}-\d{2}$/.test(next)) return false
    const date = new Date(`${next}T00:00:00Z`)
    if (!Number.isFinite(date.getTime()) || date.toISOString().slice(0, 10) !== next) return false
    const year = Number(next.slice(0, 4)), range = pickerProps?.yearRange ?? { start: 1900, end: 2100 }
    return !pickerProps?.disabled && year >= range.start && year <= range.end && (!pickerProps?.min || next >= pickerProps.min) && (!pickerProps?.max || next <= pickerProps.max) && !pickerProps?.isYearDisabled?.(year) && !pickerProps?.isDateDisabled?.(next)
  }
  return <AlertDialog {...props} title={title ?? (ko ? "날짜 선택" : "Choose a date")} actions={null}><PickerDialogBody initialValue={value} isValid={isValid} onConfirm={(next) => { onConfirm(next); props.onOpenChange(false) }}>{(draft, setDraft) => <DatePicker {...pickerProps} inline value={draft} onValueChange={setDraft} />}</PickerDialogBody></AlertDialog>
}
function TimePickerDialog({ title, value, pickerProps, onConfirm, ...props }: TimePickerDialogProps) {
  const ko = useIenLanguage("ko") === "ko"
  const granularity = pickerProps?.granularity ?? "minute"
  const isValid = (next: string) => {
    const time = parseTimeValue(next)
    if (!time || pickerProps?.disabled) return false
    const onStep = (unit: number, step: number | undefined) => Number.isFinite(step ?? 1) ? unit % Math.max(1, Math.floor(step ?? 1)) === 0 : unit === 0
    if (!onStep(time.hour, pickerProps?.hourStep) || !onStep(time.minute, pickerProps?.minuteStep) || !onStep(time.second, pickerProps?.secondStep)) return false
    if ((granularity === "hour" && time.minute !== 0) || (granularity !== "second" && time.second !== 0)) return false
    const seconds = time.hour * 3600 + time.minute * 60 + time.second
    const min = parseTimeValue(pickerProps?.min), max = parseTimeValue(pickerProps?.max)
    const low = min ? min.hour * 3600 + min.minute * 60 + min.second : null
    const high = max ? max.hour * 3600 + max.minute * 60 + max.second : null
    const inRange = low != null && high != null && low > high ? seconds >= low || seconds <= high : (low == null || seconds >= low) && (high == null || seconds <= high)
    return inRange && !pickerProps?.isTimeDisabled?.(formatTimeValue(time, granularity), time)
  }
  return <AlertDialog {...props} title={title ?? (ko ? "시각 선택" : "Choose a time")} actions={null}><PickerDialogBody initialValue={value} isValid={isValid} onConfirm={(next) => { const time = parseTimeValue(next); if (time) { onConfirm(formatTimeValue(time, granularity)); props.onOpenChange(false) } }}>{(draft, setDraft) => <TimePicker {...pickerProps} value={draft} onValueChange={setDraft}><TimePickerPanel><TimePickerColumns /></TimePickerPanel></TimePicker>}</PickerDialogBody></AlertDialog>
}
function PickerDialogBody({ initialValue, onConfirm, isValid, children }: { initialValue?: string | null; onConfirm: (value: string) => void; isValid: (value: string) => boolean; children: (value: string | null, setValue: (value: string | null) => void) => React.ReactNode }) {
  const ko = useIenLanguage("ko") === "ko"
  const [draft, setDraft] = React.useState(initialValue ?? null)
  const valid = draft != null && isValid(draft)
  return <><div className="max-w-full overflow-x-auto">{children(draft, setDraft)}</div><DialogFooter><DialogClose asChild><Button data-dialog-cancel="" variant="weak" tone="neutral">{ko ? "취소" : "Cancel"}</Button></DialogClose><Button disabled={!valid} onClick={() => { if (draft != null && valid) onConfirm(draft) }}>{ko ? "확인" : "Confirm"}</Button></DialogFooter></>
}

export { SaveAlertDialog, DeleteAlertDialog, UpdateAlertDialog, NetworkAlertDialog, ProgressDialog, TextFieldDialog, DatePickerDialog, TimePickerDialog }
