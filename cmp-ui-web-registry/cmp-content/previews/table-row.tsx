import { IenTableRow } from "@/components/ui/table-row";
export default function Preview() {
  return (
    <div className="divide-y divide-ien-border rounded-ien-default border border-ien-border">
      <IenTableRow left="주문 번호" right="#2026-1082" />
      <IenTableRow
        label="결제 금액"
        value="₩32,000"
        description="세금 포함"
        trailing={<span>결제 완료</span>}
      />
      <IenTableRow left="배송 상태" right="배송 중" leftRatio={40} />
    </div>
  );
}
