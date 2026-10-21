import { IenParagraph } from "@/components/ui/paragraph";
export default function Preview() {
  return (
    <IenParagraph
      title="안전하게 시작하기"
      body="계정 정보를 확인하고 필요한 권한을 설정하세요."
      emphasis={["권한"]}
      footer="설정은 언제든 변경할 수 있습니다."
    />
  );
}
