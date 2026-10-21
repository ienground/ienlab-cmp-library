import { IenLoadingIndicator } from "@/components/ui/loading-indicator";
export default function Preview() {
  return (
    <div className="flex items-center gap-4">
      <IenLoadingIndicator />
      <IenLoadingIndicator className="size-8 text-ien-info" label="동기화 중" />
    </div>
  );
}
