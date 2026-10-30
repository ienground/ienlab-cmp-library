"use client";
import { useIenToast, IenToastProvider } from "@/components/ui/toast";
import { Button } from "@/components/ui/button";
function Actions() {
  const { showIenToast } = useIenToast();
  return (
    <div className="flex gap-ien-xs">
      <Button onClick={() => showIenToast("저장했습니다.", "success")}>
        성공
      </Button>
      <Button
        variant="weak"
        tone="warning"
        onClick={() => showIenToast("입력값을 확인하세요.", "warning", "long")}
      >
        주의
      </Button>
    </div>
  );
}
export default function Preview() {
  return (
    <IenToastProvider>
      <Actions />
    </IenToastProvider>
  );
}
