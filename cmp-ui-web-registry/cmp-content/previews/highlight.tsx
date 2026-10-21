import { IenHighlightText } from "@/components/ui/highlight";
export default function Preview() {
  return (
    <IenHighlightText
      text="검색한 디자인 시스템 결과에서 IEN 컴포넌트를 찾았습니다."
      highlights={["디자인 시스템", "IEN"]}
      bold
    />
  );
}
