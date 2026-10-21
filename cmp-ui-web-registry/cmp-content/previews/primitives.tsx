"use client";
import * as React from "react";
import {
  IenSurface,
  IenBorderBox,
  IenClickable,
  IenDivider,
  IenAsteriskTextWrapper,
  IenLoaderPrimitive,
} from "@/components/ui/primitives";
export default function Preview() {
  const [count, setCount] = React.useState(0);
  return (
    <div className="space-y-3">
      <IenSurface
        borderColor="var(--ien-border)"
        tonalElevation={4}
        className="p-4"
      >
        IEN Surface <IenAsteriskTextWrapper>필수 항목</IenAsteriskTextWrapper>
      </IenSurface>
      <IenBorderBox>테두리 영역</IenBorderBox>
      <div className="flex h-6 items-center gap-3">
        <IenDivider.Horizontal />
        <IenDivider.Vertical />
      </div>
      <IenClickable onClick={() => setCount((value) => value + 1)}>
        클릭 {count}
      </IenClickable>
      <IenLoaderPrimitive />
    </div>
  );
}
