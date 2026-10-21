import {
  NativeSelect,
  NativeSelectOption,
} from "@/components/ui/native-select";
export default function Preview() {
  return (
    <NativeSelect aria-label="지역" defaultValue="seoul">
      <NativeSelectOption value="seoul">서울</NativeSelectOption>
      <NativeSelectOption value="busan">부산</NativeSelectOption>
      <NativeSelectOption value="jeju">제주</NativeSelectOption>
    </NativeSelect>
  );
}
