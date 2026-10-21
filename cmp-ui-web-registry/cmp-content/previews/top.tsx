"use client";
import * as React from "react";
import {
  IenTop,
  IenTopAppBar,
  IenTopSubtitleBadges,
  IenBackButton,
} from "@/components/ui/top";
export default function Preview() {
  const [count, setCount] = React.useState(0);
  return (
    <div className="space-y-6">
      <IenTopAppBar
        title="설정"
        navigationIcon={<IenBackButton onClick={() => setCount(0)} />}
        actions={
          <button onClick={() => setCount((value) => value + 1)}>저장</button>
        }
        showDivider
      />
      <IenTop
        title="내 프로젝트"
        subtitle="주요 정보를 확인하세요"
        subtitleTop={
          <IenTopSubtitleBadges
            badges={[
              { text: "진행 중", tone: "brand" },
              { text: "3개 항목", tone: "neutral" },
            ]}
          />
        }
        right={
          <button onClick={() => setCount((value) => value + 1)}>열기</button>
        }
      />
      <p>선택 횟수: {count}</p>
    </div>
  );
}
