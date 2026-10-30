import { Spinner } from "@/components/ui/spinner";
export default function Preview() {
  return (
    <div className="flex items-center gap-ien-sm">
      <Spinner aria-label="불러오는 중" />
      <span>불러오는 중</span>
    </div>
  );
}
