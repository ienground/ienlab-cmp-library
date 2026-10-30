"use client";
import * as React from "react";
import { IenBottomCTA } from "@/components/ui/bottom-cta";
export default function Preview() {
  const [saved, setSaved] = React.useState(false);
  const [loading, setLoading] = React.useState(false);
  return (
    <div className="relative min-h-36 rounded-ien-lg border border-ien-border p-4">
      <p>{saved ? "저장되었습니다." : "변경 사항을 저장할 수 있습니다."}</p>
      <IenBottomCTA
        text={loading ? "저장 중" : "저장하기"}
        loading={loading}
        onClick={() => {
          setLoading(true);
          window.setTimeout(() => {
            setLoading(false);
            setSaved(true);
          }, 500);
        }}
      />
    </div>
  );
}
