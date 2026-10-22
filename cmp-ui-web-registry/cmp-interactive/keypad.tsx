"use client";

import * as React from "react";
import { Delete, Languages, RotateCcw } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type KeyboardAction =
  | { type: "input"; text: string }
  | { type: "backspace" }
  | { type: "space" }
  | { type: "clear" }
  | { type: "done" };
export type KeypadLayout =
  | "number"
  | "secureNumber"
  | "alphabet"
  | "alphabetKeyboard"
  | "numberKeyboard"
  | "fullSecure"
  | "secureKeypad";
export type KeypadLanguage = "English" | "Korean" | "english" | "korean";
export type KeypadState = {
  value?: string;
  language?: KeypadLanguage;
  maskValue?: boolean;
  randomSeed?: number;
};

export type KeypadProps = Omit<
  React.ComponentProps<"div">,
  "children" | "ref" | "className" | "onKeyDown"
> & {
  ref?: React.Ref<HTMLDivElement>;
  value?: string;
  defaultValue?: string;
  state?: KeypadState;
  onValueChange?: (value: string) => void;
  onAction?: (action: KeyboardAction) => void;
  onKeyClick?: (value: string) => void;
  onBackspaceClick?: () => void;
  onSpaceClick?: () => void;
  onSpecialClick?: () => void;
  onSecureNoiseKeyClick?: (value: string) => void;
  onSubmit?: (value: string) => void;
  onLanguageChange?: (language: KeypadLanguage) => void;
  maxLength?: number;
  masked?: boolean;
  maskValue?: boolean;
  secure?: boolean;
  randomized?: boolean;
  layout?: KeypadLayout;
  mode?: KeypadLayout;
  numbers?: number[];
  alphabets?: string[];
  columns?: number;
  keyHeight?: number;
  submitDisabled?: boolean;
  submitButtonText?: string;
  language?: KeypadLanguage;
  enabled?: boolean;
  disabled?: boolean;
  className?: string;
  onKeyDown?: React.KeyboardEventHandler<HTMLDivElement>;
};

const alphabetRows = [
  [..."qwertyuiop"],
  [..."asdfghjkl"],
  [..."zxcvbnm"],
];
const koreanRows = [
  [..."ㅂㅈㄷㄱㅅㅛㅕㅑ"],
  [..."ㅁㄴㅇㄹㅎㅗㅓㅏㅣ"],
  [..."ㅋㅌㅊㅍㅠㅜㅡ"],
];
type FullSecureKey = { value: string; secondaryLabel?: string };
const fullSecureRows: { id: string; keys: FullSecureKey[] }[] = [
  {
    id: "firstAlphabet",
    keys: [..."qwertyuiop"].map((value, index) => ({
      value,
      secondaryLabel: [..."ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ"][index],
    })),
  },
  {
    id: "secondAlphabet",
    keys: [..."asdfghjkl"].map((value, index) => ({
      value,
      secondaryLabel: [..."ㅁㄴㅇㄹㅎㅗㅓㅏㅣ"][index],
    })),
  },
  {
    id: "thirdAlphabet",
    keys: [..."zxcvbnm"].map((value, index) => ({
      value,
      secondaryLabel: [..."ㅋㅌㅊㅍㅠㅜㅡ"][index],
    })),
  },
];

function shuffled<T>(values: T[]): T[] {
  const result = [...values];
  for (let index = result.length - 1; index > 0; index -= 1) {
    const swapIndex = Math.floor(Math.random() * (index + 1));
    [result[index], result[swapIndex]] = [result[swapIndex], result[index]];
  }
  return result;
}

function seededShuffle<T>(values: T[], seed: number): T[] {
  const result = [...values];
  let randomSeed = seed >>> 0;
  for (let index = result.length - 1; index > 0; index -= 1) {
    randomSeed = (Math.imul(randomSeed, 1664525) + 1013904223) >>> 0;
    const swapIndex = randomSeed % (index + 1);
    [result[index], result[swapIndex]] = [result[swapIndex], result[index]];
  }
  return result;
}

function rowSeed(seed: number, rowId: string): number {
  return [...rowId].reduce((current, character) =>
    Math.imul(current ^ character.charCodeAt(0), 16777619), seed | 0,
  );
}

function stringSeed(value: string): number {
  return [...value].reduce((current, character) =>
    Math.imul(current ^ character.charCodeAt(0), 16777619), 2166136261,
  );
}

function KeyboardActionButton({
  label,
  onClick,
  disabled,
  keyHeight,
  tone = "neutral",
  children,
  className,
}: {
  label: string;
  onClick: () => void;
  disabled: boolean;
  keyHeight: number;
  tone?: "neutral" | "brand";
  children?: React.ReactNode;
  className?: string;
}) {
  return (
    <Button
      type="button"
      variant={tone === "brand" ? "fill" : "weak"}
      tone={tone}
      aria-label={label}
      disabled={disabled}
      onClick={onClick}
      className={cn("min-w-0 flex-1 px-ien-xs", className)}
      style={{ height: keyHeight }}
    >
      {children ?? label}
    </Button>
  );
}

function Keypad({
  value,
  defaultValue = "",
  onValueChange,
  onAction,
  onKeyClick,
  onBackspaceClick,
  onSpaceClick,
  onSpecialClick,
  onSecureNoiseKeyClick,
  onSubmit,
  onLanguageChange,
  maxLength = 16,
  masked = true,
  maskValue,
  secure = false,
  randomized,
  layout,
  mode,
  numbers,
  alphabets,
  columns = 3,
  keyHeight,
  submitDisabled = false,
  submitButtonText = "확인",
  language: requestedLanguage,
  state,
  enabled = true,
  disabled = false,
  className,
  ref,
  onKeyDown,
  tabIndex,
  ...rootProps
}: KeypadProps) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const [internalLanguage, setInternalLanguage] = React.useState<KeypadLanguage>(state?.language ?? "English");
  const instanceId = React.useId();
  const current = value ?? state?.value ?? internalValue;
  const language = requestedLanguage ?? state?.language ?? internalLanguage;
  const languageControlled = requestedLanguage !== undefined || state?.language !== undefined;
  const isEnabled = enabled && !disabled;
  const resolvedLayout = mode ?? layout ?? "number";
  const resolvedRandomized = randomized ?? (resolvedLayout === "fullSecure" || resolvedLayout === "secureKeypad");
  const resolvedKeyHeight = keyHeight ?? (
    resolvedLayout === "alphabet" ? 56 : resolvedLayout === "fullSecure" || resolvedLayout === "secureKeypad" ? 44 : 64
  );
  const resolvedRandomSeed = state?.randomSeed ?? stringSeed(instanceId);
  const resolvedMasked = state?.maskValue ?? maskValue ?? masked;
  const numberList = numbers?.filter((number) => Number.isInteger(number) && number >= 0 && number <= 9);
  const safeNumbers = numberList?.length ? numberList : [1, 2, 3, 4, 5, 6, 7, 8, 9, 0];
  const safeAlphabets = alphabets?.length ? alphabets : [..."ABCDEFGHIJKLMNOPQRSTUVWXYZ"];
  const randomizedNumbers = React.useMemo(
    () => resolvedRandomized ? seededShuffle(safeNumbers, rowSeed(resolvedRandomSeed, "numbers")) : [...safeNumbers],
    [resolvedRandomSeed, resolvedRandomized, safeNumbers.join(",")],
  );
  const randomizedAlphabets = React.useMemo(
    () => resolvedRandomized ? seededShuffle(safeAlphabets, rowSeed(resolvedRandomSeed, "alphabets")) : [...safeAlphabets],
    [resolvedRandomSeed, resolvedRandomized, safeAlphabets.join("\u0000")],
  );
  const keyRows = React.useMemo(() => {
    if (resolvedLayout === "numberKeyboard") {
      const digits = resolvedRandomized
        ? seededShuffle(["1", "2", "3", "4", "5", "6", "7", "8", "9"], rowSeed(resolvedRandomSeed, "numberKeyboard"))
        : ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
      return [digits.slice(0, 3), digits.slice(3, 6), digits.slice(6, 9), ["clear", "0", "backspace"], ["done"]];
    }
    if (resolvedLayout === "alphabetKeyboard" || resolvedLayout === "fullSecure") {
      const sourceRows = language.toLowerCase() === "korean" ? koreanRows : alphabetRows;
      const rows = sourceRows.map((row, rowIndex) => resolvedRandomized
        ? seededShuffle(row, rowSeed(resolvedRandomSeed, `keyboard-${rowIndex}`))
        : [...row]);
      if (resolvedLayout === "fullSecure") {
        const digits = resolvedRandomized
          ? seededShuffle(["1", "2", "3", "4", "5", "6", "7", "8", "9"], rowSeed(resolvedRandomSeed, "fullSecureNumbers"))
          : ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
        return [
          ...rows,
          ["space", "backspace", "done"],
          digits.slice(0, 3),
          digits.slice(3, 6),
          digits.slice(6, 9),
          ["clear", "0", "backspace"],
        ];
      }
      return [...rows, ["space", "backspace", "done"]];
    }
    return [];
  }, [resolvedLayout, resolvedRandomSeed, resolvedRandomized, language]);
  const secureKeyRows = React.useMemo(() => {
    const numberKeys = [..."1234567890"].map((value) => ({ value }));
    return [
      { id: "numbers", keys: numberKeys },
      ...fullSecureRows,
    ].map(({ id, keys }) => {
      const emptyCount = 10 - keys.length;
      const emptyIndexes = resolvedRandomized
        ? new Set(seededShuffle(
            Array.from({ length: 10 }, (_, index) => index),
            rowSeed(resolvedRandomSeed, id),
          ).slice(0, emptyCount))
        : new Set(Array.from({ length: emptyCount }, (_, index) => 10 - emptyCount + index));
      let keyIndex = 0;
      return Array.from({ length: 10 }, (_, cellIndex) => {
        if (emptyIndexes.has(cellIndex)) return null;
        return keys[keyIndex++] ?? null;
      });
    });
  }, [resolvedRandomSeed, resolvedRandomized]);
  const boundedMaxLength = Number.isFinite(maxLength) ? Math.max(0, Math.floor(maxLength)) : 16;

  const emitAction = (action: KeyboardAction) => onAction?.(action);
  const update = (next: string) => {
    const bounded = next.slice(0, boundedMaxLength);
    if (value === undefined && state?.value === undefined) setInternalValue(bounded);
    onValueChange?.(bounded);
  };
  const append = (text: string) => {
    if (!isEnabled || current.length >= boundedMaxLength) return;
    update(current + text);
    onKeyClick?.(text);
    emitAction({ type: "input", text });
  };
  const backspace = () => {
    if (!isEnabled) return;
    update(current.slice(0, -1));
    onBackspaceClick?.();
    emitAction({ type: "backspace" });
  };
  const clear = () => {
    if (!isEnabled) return;
    update("");
    emitAction({ type: "clear" });
  };
  const space = () => {
    if (!isEnabled || current.length >= boundedMaxLength) return;
    update(`${current} `);
    onSpaceClick?.();
    emitAction({ type: "space" });
  };
  const done = () => {
    if (!isEnabled || submitDisabled) return;
    onSubmit?.(current);
    emitAction({ type: "done" });
  };
  const dispatchKey = (key: string) => {
    if (key === "backspace") backspace();
    else if (key === "clear") clear();
    else if (key === "space") space();
    else if (key === "done") done();
    else append(key);
  };
  const changeLanguage = () => {
    const next = language.toLowerCase() === "english" ? "Korean" : "English";
    if (!languageControlled) setInternalLanguage(next);
    onLanguageChange?.(next);
  };
  const output = resolvedMasked ? "•".repeat(current.length) : current || " ";
  const gridNumbers = resolvedLayout === "secureNumber" && resolvedRandomized
    ? randomizedNumbers
    : safeNumbers;
  const numericRows = [
    ...Array.from({ length: Math.ceil(gridNumbers.length / 3) }, (_, row) => gridNumbers.slice(row * 3, row * 3 + 3).map(String)),
  ];
  const lastRow = numericRows.pop() ?? [];
  const fixedNumericRows = [
    ...numericRows,
    lastRow.length === 3
      ? lastRow
      : lastRow.length === 2
        ? [...lastRow, "backspace"]
        : lastRow.length === 1
          ? ["", lastRow[0], "backspace"]
          : ["", "", "backspace"],
  ];
  if (lastRow.length === 3) fixedNumericRows.push(["", "", "backspace"]);
  const secureNoise = (number: number) => {
    if (!(secure || resolvedLayout === "secureNumber") || !onSecureNoiseKeyClick) return;
    const positions = new Map<number, [number, number]>();
    fixedNumericRows.forEach((row, rowIndex) => row.forEach((key, columnIndex) => {
      if (/^\d$/.test(key)) positions.set(Number(key), [rowIndex, columnIndex]);
    }));
    const selectedPosition = positions.get(number);
    if (!selectedPosition) return;
    const [selectedRow, selectedColumn] = selectedPosition;
    const excluded = new Set([
      `${selectedRow}:${selectedColumn}`,
      `${selectedRow - 1}:${selectedColumn}`,
      `${selectedRow + 1}:${selectedColumn}`,
      `${selectedRow}:${selectedColumn - 1}`,
      `${selectedRow}:${selectedColumn + 1}`,
    ]);
    const noise = [...positions]
      .filter(([, [row, column]]) => !excluded.has(`${row}:${column}`))
      .map(([candidate]) => candidate);
    for (const candidate of shuffled(noise).slice(0, 2)) onSecureNoiseKeyClick(String(candidate));
  };
  const alphaKeys = randomizedAlphabets;
  const alphaRows = Array.from(
    { length: Math.ceil(alphaKeys.length / Math.max(1, columns)) },
    (_, index) => alphaKeys.slice(index * Math.max(1, columns), index * Math.max(1, columns) + Math.max(1, columns)),
  );
  const useInputHandler = resolvedLayout === "number" || resolvedLayout === "secureNumber";

  return (
    <div
      {...rootProps}
      ref={ref}
      data-slot="keypad"
      data-layout={resolvedLayout}
      data-randomized={resolvedRandomized}
      data-enabled={isEnabled}
      tabIndex={tabIndex ?? (isEnabled ? 0 : -1)}
      onKeyDown={(event) => {
        onKeyDown?.(event);
        if (event.defaultPrevented || !isEnabled) return;
        if (/^[0-9]$/.test(event.key) && useInputHandler) {
          event.preventDefault();
          append(event.key);
        } else if (event.key === "Backspace" && current) {
          event.preventDefault();
          backspace();
        } else if (event.key === "Enter" && onSubmit) {
          event.preventDefault();
          done();
        }
      }}
      className={cn(
        "grid w-full gap-ien-sm outline-none focus-visible:ring-2 focus-visible:ring-ien-brand",
        resolvedLayout === "secureKeypad" ? "max-w-5xl" : "max-w-xs",
        className,
      )}
    >
      {(useInputHandler || resolvedLayout === "fullSecure") && (
        <div className="flex min-h-12 items-center gap-ien-xs rounded-ien-default border border-ien-border bg-ien-surface px-ien-md py-ien-sm">
          <output
            aria-label="입력한 내용"
            aria-live="polite"
            className="min-w-0 flex-1 text-center font-mono text-ien-title2 tracking-[0.35em] text-ien-text-primary"
          >
            {output}
          </output>
          {resolvedLayout === "fullSecure" && (
            <Button
              type="button"
              variant="ghost"
              tone="neutral"
              size="icon-sm"
              disabled={!isEnabled}
              aria-label="입력 언어 변경"
              onClick={changeLanguage}
            >
              <Languages aria-hidden="true" />
            </Button>
          )}
        </div>
      )}
      {(resolvedLayout === "number" || resolvedLayout === "secureNumber") && (
        <div className="grid gap-ien-xs" role="group" aria-label="숫자 키패드">
          {fixedNumericRows.map((row, rowIndex) => (
            <div key={`number-row-${rowIndex}`} className="grid grid-cols-3 gap-ien-xs">
              {row.map((key, index) => key === "" ? (
                <span key={`empty-${rowIndex}-${index}`} aria-hidden="true" />
              ) : key === "backspace" ? (
                <KeyboardActionButton
                  key={key}
                  label="마지막 숫자 지우기"
                  onClick={backspace}
                  disabled={!isEnabled || !current}
                  keyHeight={resolvedKeyHeight}
                >
                  <Delete aria-hidden="true" />
                </KeyboardActionButton>
              ) : (
                <KeyboardActionButton
                  key={`${rowIndex}-${key}`}
                  label={`${key} 입력`}
                  onClick={() => {
                    append(key);
                    secureNoise(Number(key));
                  }}
                  disabled={!isEnabled || current.length >= boundedMaxLength}
                  keyHeight={resolvedKeyHeight}
                  className="text-ien-title2"
                >
                  {key}
                </KeyboardActionButton>
              ))}
            </div>
          ))}
        </div>
      )}
      {resolvedLayout === "alphabet" && (
        <div className="grid gap-ien-xs" role="group" aria-label="알파벳 키패드">
          {alphaRows.map((row, rowIndex) => (
            <div key={`alphabet-row-${rowIndex}`} className="grid gap-ien-xs" style={{ gridTemplateColumns: `repeat(${Math.max(1, columns)}, minmax(0, 1fr))` }}>
              {row.map((key, index) => (
                <KeyboardActionButton
                  key={`${rowIndex}-${index}-${key}`}
                  label={`${key} 입력`}
                  onClick={() => append(key)}
                  disabled={!isEnabled || current.length >= boundedMaxLength}
                  keyHeight={resolvedKeyHeight}
                  className="text-ien-title3"
                >
                  {key}
                </KeyboardActionButton>
              ))}
            </div>
          ))}
          <KeyboardActionButton label="마지막 글자 지우기" onClick={backspace} disabled={!isEnabled || !current} keyHeight={resolvedKeyHeight}>
            <Delete aria-hidden="true" />
          </KeyboardActionButton>
        </div>
      )}
      {(resolvedLayout === "alphabetKeyboard" || resolvedLayout === "numberKeyboard" || resolvedLayout === "fullSecure") && (
        <div className="grid gap-ien-xs" role="group" aria-label={resolvedLayout === "numberKeyboard" ? "숫자 키보드" : "문자 키보드"}>
          {keyRows.map((row, rowIndex) => (
            <div key={`keyboard-row-${rowIndex}`} className="flex gap-ien-xs">
              {row.map((key, index) => {
                const isAction = ["space", "backspace", "clear", "done"].includes(key);
                const label = key === "space" ? "공백" : key === "backspace" ? "지우기" : key === "clear" ? "전체 지우기" : key === "done" ? submitButtonText : `${key} 입력`;
                return (
                  <KeyboardActionButton
                    key={`${rowIndex}-${index}-${key}`}
                    label={label}
                    onClick={() => dispatchKey(key)}
                    disabled={!isEnabled || (!isAction && current.length >= boundedMaxLength) || (key === "done" && submitDisabled) || (key === "backspace" && !current)}
                    keyHeight={resolvedKeyHeight}
                    tone={key === "done" ? "brand" : "neutral"}
                    className={cn(
                      "text-ien-label1",
                      (key === "space" || key === "done") ? "flex-[2]" : "flex-1",
                      key === "done" && "font-semibold",
                    )}
                  >
                    {key === "backspace" ? <Delete aria-hidden="true" /> : key === "clear" ? <RotateCcw aria-hidden="true" /> : key === "space" ? "Space" : key === "done" ? submitButtonText : key}
                  </KeyboardActionButton>
                );
              })}
            </div>
          ))}
        </div>
      )}
      {resolvedLayout === "fullSecure" && onSpecialClick && (
        <KeyboardActionButton label="특수 문자" onClick={onSpecialClick} disabled={!isEnabled} keyHeight={resolvedKeyHeight}>
          ?123
        </KeyboardActionButton>
      )}
      {resolvedLayout === "secureKeypad" && (
        <div className="grid gap-ien-xs rounded-ien-default bg-ien-surface-weak p-ien-xs" role="group" aria-label="풀 보안 키패드">
          {secureKeyRows.map((row, rowIndex) => (
            <div key={`secure-row-${rowIndex}`} className="grid grid-cols-10 gap-ien-xs">
              {row.map((key, cellIndex) => key ? (
                <KeyboardActionButton
                  key={`${rowIndex}-${cellIndex}-${key.value}`}
                  label={`${key.value} 입력${key.secondaryLabel ? `, ${key.secondaryLabel}` : ""}`}
                  onClick={() => append(key.value)}
                  disabled={!isEnabled || current.length >= boundedMaxLength}
                  keyHeight={resolvedKeyHeight}
                  className="gap-0 px-0 text-ien-label1"
                >
                  <span>{key.value}</span>
                  {key.secondaryLabel && (
                    <span className="text-ien-caption text-ien-text-tertiary">{key.secondaryLabel}</span>
                  )}
                </KeyboardActionButton>
              ) : (
                <span key={`empty-${rowIndex}-${cellIndex}`} aria-hidden="true" />
              ))}
            </div>
          ))}
          <div className="flex gap-ien-xs">
            <KeyboardActionButton
              label="특수 문자"
              onClick={() => onSpecialClick?.()}
              disabled={!isEnabled || !onSpecialClick}
              keyHeight={resolvedKeyHeight}
              className="flex-[1.2]"
            >
              ?123
            </KeyboardActionButton>
            <KeyboardActionButton
              label="공백"
              onClick={space}
              disabled={!isEnabled || current.length >= boundedMaxLength}
              keyHeight={resolvedKeyHeight}
              className="flex-[2.4]"
            >
              Space
            </KeyboardActionButton>
            <KeyboardActionButton
              label="지우기"
              onClick={backspace}
              disabled={!isEnabled}
              keyHeight={resolvedKeyHeight}
              className="flex-[1.2]"
            >
              <Delete aria-hidden="true" />
            </KeyboardActionButton>
            <KeyboardActionButton
              label={submitButtonText}
              onClick={done}
              disabled={!isEnabled || submitDisabled}
              keyHeight={resolvedKeyHeight}
              tone="brand"
              className="flex-[2.4] font-semibold"
            >
              {submitButtonText}
            </KeyboardActionButton>
          </div>
        </div>
      )}
      {(resolvedLayout === "number" || resolvedLayout === "secureNumber") && (
        <div className="flex gap-ien-xs">
          <KeyboardActionButton
            label="입력 지우기"
            onClick={clear}
            disabled={!isEnabled || !current}
            keyHeight={44}
            className="text-ien-label2"
          >
            <RotateCcw aria-hidden="true" /> 지우기
          </KeyboardActionButton>
          {onSubmit && (
            <KeyboardActionButton
              label={submitButtonText}
              onClick={done}
              disabled={!isEnabled || submitDisabled || !current}
              keyHeight={44}
              tone="brand"
              className="text-ien-label2"
            >
              {submitButtonText}
            </KeyboardActionButton>
          )}
        </div>
      )}
    </div>
  );
}

export { Keypad, Keypad as IenKeypad };
