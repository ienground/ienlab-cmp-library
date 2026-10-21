"use client";
import * as React from "react";
import { IenBoardRow } from "@/components/ui/board-row";
export default function Preview() {
  const [opened, setOpened] = React.useState(false);
  return (
    <IenBoardRow
      title="환불은 어떻게 신청하나요?"
      prefix={<span aria-hidden>Q</span>}
      opened={opened}
      onOpenedChange={setOpened}
    >
      <p>주문 내역에서 환불할 상품을 선택한 뒤 신청할 수 있습니다.</p>
      <button onClick={() => setOpened(false)}>확인</button>
    </IenBoardRow>
  );
}
