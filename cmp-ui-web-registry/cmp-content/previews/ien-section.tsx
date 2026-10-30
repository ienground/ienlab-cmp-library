import { IenSection } from "@/components/ui/ien-section";
import { IenListRow } from "@/components/ui/list-row";
export default function Preview() {
  return (
    <IenSection title="계정" caption="계정 설정을 관리합니다.">
      <IenListRow title="프로필" subtitle="이름과 사진 관리" withArrow />
      <IenListRow title="알림" subtitle="알림 설정" withArrow />
    </IenSection>
  );
}
