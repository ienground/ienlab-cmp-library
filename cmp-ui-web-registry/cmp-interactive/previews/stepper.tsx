"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { Stepper } from "@/components/ui/stepper";

export default function Preview() {
  const [step, setStep] = React.useState(0);
  const steps = [
    { label: "정보 입력" },
    { label: "배송 선택" },
    { label: "확인" },
  ];
  return (
    <div className="grid gap-ien-md">
      <Stepper
        steps={steps}
        currentStep={step}
        onStepChange={setStep}
        allowNavigation
      />
      <div className="flex gap-ien-xs">
        <Button
          variant="weak"
          tone="neutral"
          disabled={step === 0}
          onClick={() => setStep((value) => value - 1)}
        >
          이전
        </Button>
        <Button
          disabled={step === steps.length - 1}
          onClick={() => setStep((value) => value + 1)}
        >
          다음
        </Button>
      </div>
      <p aria-live="polite" className="text-ien-caption">
        {steps[step].label} 단계
      </p>
    </div>
  );
}
