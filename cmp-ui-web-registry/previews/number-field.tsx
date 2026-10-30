import {
  NumberField,
  NumberFieldDecrement,
  NumberFieldGroup,
  NumberFieldIncrement,
  NumberFieldInput,
  NumberFieldScrubArea,
} from "@/components/reui/number-field"

export function Pattern() {
  return (
    <div className="flex w-full flex-wrap items-end gap-6">
      {(["tiny", "small", "medium", "large"] as const).map((size) => (
        <NumberField
          key={size}
          size={size}
          className="w-auto"
          defaultValue={5}
          min={0}
          max={100}
        >
          <NumberFieldScrubArea label={size} />
          <NumberFieldGroup>
            <NumberFieldDecrement />
            <NumberFieldInput />
            <NumberFieldIncrement />
          </NumberFieldGroup>
        </NumberField>
      ))}
    </div>
  )
}
export default Pattern;
