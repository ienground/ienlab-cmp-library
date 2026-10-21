import { Progress } from "@/components/ui/progress";
export default function Preview() {
  return (
    <div className="grid gap-ien-xs">
      <div className="flex justify-between text-ien-caption">
        <span>업로드</span>
        <span>64%</span>
      </div>
      <Progress value={64} aria-label="업로드 진행률" />
    </div>
  );
}
