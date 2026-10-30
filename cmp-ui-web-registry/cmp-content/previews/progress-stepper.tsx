"use client";
import * as React from "react";
import { IenProgressStepper } from "@/components/ui/progress-stepper";
export default function Preview() {
  const [step, setStep] = React.useState(1);
  return (
    <div className="space-y-4">
      <IenProgressStepper
        activeStepIndex={step}
        variant="icon"
        checkForFinish
        steps={[
          { title: "정보", icon: "1" },
          { title: "배송", icon: "2" },
          { title: "완료", icon: "3" },
        ]}
      />
      <button onClick={() => setStep((value) => (value + 1) % 3)}>
        다음 단계
      </button>
    </div>
  );
}
