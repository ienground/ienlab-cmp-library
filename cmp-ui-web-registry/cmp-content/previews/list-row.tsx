"use client";
import * as React from "react";
import { IenListRow, IenListRowAssetText } from "@/components/ui/list-row";
export default function Preview() {
  const [selected, setSelected] = React.useState(false);
  return (
    <div className="rounded-ien-default border border-ien-border">
      <IenListRow
        title="프로필 사진"
        subtitle="마지막 변경: 오늘"
        leading={<IenListRowAssetText text="IEN" />}
        selected={selected}
        withArrow
        onClick={() => setSelected((value) => !value)}
      />
      <IenListRow
        title="알림"
        subtitle="새 알림 3개"
        trailing={
          <span className="rounded-full bg-ien-brand-weak px-2 text-ien-brand">
            3
          </span>
        }
        border="none"
      />
    </div>
  );
}
