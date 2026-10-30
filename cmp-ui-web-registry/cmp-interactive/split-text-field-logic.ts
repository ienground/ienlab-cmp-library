type SplitTextFieldInputType = "text" | "numeric";

type SplitTextFieldChange = {
  current: string;
  input: string;
  index: number;
  length: number;
  type: SplitTextFieldInputType;
  replaceWholeValue?: boolean;
};

export function normalizeSplitTextFieldChange({
  current,
  input,
  index,
  length,
  type,
  replaceWholeValue = false,
}: SplitTextFieldChange) {
  const filtered = type === "numeric" ? input.replace(/\D/g, "") : input;
  const bounded = filtered.slice(0, length);
  const replaceValue = replaceWholeValue || bounded.length > 1;
  const characters = Array.from({ length }, (_, position) =>
    replaceValue ? (bounded[position] ?? "") : (current[position] ?? ""),
  );
  if (!replaceValue) characters[index] = bounded;

  return {
    value: characters.join(""),
    focusIndex: bounded
      ? Math.min(index + bounded.length, Math.max(0, length - 1))
      : index,
  };
}
