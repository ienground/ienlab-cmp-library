import {
  RadioGroup,
  RadioGroupItem,
} from "@/components/ui/radio-group"

export function Pattern() {
  return (
    <RadioGroup defaultValue="comfortable" className="w-fit">
      <RadioGroupItem value="default" label="Default" />
      <RadioGroupItem value="comfortable" label="Comfortable" />
      <RadioGroupItem value="compact" label="Compact" />
      <RadioGroupItem value="disabled" label="Disabled" enabled={false} />
    </RadioGroup>
  )
}
export default Pattern;
