import { IenBottomInfo } from "@/components/ui/bottom-info";
export default function Preview() {
  return (
    <IenBottomInfo>
      <p className="text-ien-body2">
        이용 안내와 보조 정보를 하단에 배치합니다.
      </p>
      <p className="mt-2 text-ien-caption text-ien-text-secondary">
        마지막 업데이트: 오늘
      </p>
    </IenBottomInfo>
  );
}
