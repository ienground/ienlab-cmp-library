import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
export default function Preview() {
  return (
    <div className="grid max-w-xs gap-ien-xs">
      <Label htmlFor="preview-name">이름</Label>
      <Input id="preview-name" placeholder="이름을 입력하세요" />
    </div>
  );
}
