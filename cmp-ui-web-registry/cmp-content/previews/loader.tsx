import { IenLoader } from "@/components/ui/loader";
export default function Preview() {
  return (
    <div className="flex items-center gap-8">
      <IenLoader size="small" />
      <IenLoader label="콘텐츠를 불러오는 중" />
      <IenLoader size="large" label="업로드 중" />
    </div>
  );
}
