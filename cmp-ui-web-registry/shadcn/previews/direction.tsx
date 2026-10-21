import { DirectionProvider } from "@/components/ui/direction";
export default function Preview() {
  return (
    <DirectionProvider direction="rtl">
      <div className="rounded-ien-default bg-ien-surface-variant p-ien-md">
        오른쪽에서 왼쪽으로 배치합니다.
      </div>
    </DirectionProvider>
  );
}
