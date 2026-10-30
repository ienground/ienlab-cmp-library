type Decimal = { coefficient: bigint; scale: number };

function decimalParts(value: number): Decimal {
  const match = String(value)
    .toLowerCase()
    .match(/^(-?)(\d+)(?:\.(\d+))?(?:e([+-]?\d+))?$/);
  if (!match)
    throw new Error(
      `유한 숫자를 10진수로 변환할 수 없습니다: ${value}`,
    );

  const fraction = match[3] ?? "";
  return {
    coefficient: BigInt(`${match[1]}${match[2]}${fraction}`),
    scale: fraction.length - Number(match[4] ?? 0),
  };
}

export function stepNumericValue(
  current: number,
  step: number,
  direction: -1 | 1,
  min = -Infinity,
  max = Infinity,
) {
  if (!Number.isFinite(current) || !Number.isFinite(step))
    return current + step * direction;

  const currentDecimal = decimalParts(current);
  const stepDecimal = decimalParts(step);
  const scale = Math.max(0, currentDecimal.scale, stepDecimal.scale);
  const factor = (decimal: Decimal) =>
    decimal.coefficient * BigInt(10) ** BigInt(scale - decimal.scale);
  const stepped =
    factor(currentDecimal) + BigInt(direction) * factor(stepDecimal);
  const value = Number(`${stepped}e${-scale}`);
  return Math.min(max, Math.max(min, value));
}
