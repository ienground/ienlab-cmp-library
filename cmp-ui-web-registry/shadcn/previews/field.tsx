import {
  Field,
  FieldDescription,
  FieldLabel,
  FieldSet,
  FieldTitle,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
export default function Preview() {
  return (
    <FieldSet>
      <FieldTitle>연락처</FieldTitle>
      <Field>
        <FieldLabel htmlFor="preview-email">이메일</FieldLabel>
        <Input id="preview-email" type="email" placeholder="name@example.com" />
        <FieldDescription>업데이트 안내에 사용합니다.</FieldDescription>
      </Field>
    </FieldSet>
  );
}
