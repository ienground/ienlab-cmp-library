import { IenBadge } from "@/components/ui/badge";
import { CheckIcon } from "lucide-react";
export default function Preview() {
  return (
    <div className="flex flex-wrap gap-ien-xs">
      <IenBadge text="기본" />
      <IenBadge text="활성" tone="success" leadingIcon={<CheckIcon />} />
      <IenBadge text="주의" tone="warning" variant="Weak" />
      <IenBadge text="오류" tone="danger" variant="Line" />
    </div>
  );
}
