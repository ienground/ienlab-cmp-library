"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { Stepper } from "@/components/ui/stepper";

export default function Preview() {
  const [step, setStep] = React.useState(0);
  const [quantity, setQuantity] = React.useState(2);
  const steps = [
    { title: "정보 입력", description: "기본 정보를 작성합니다." },
    { title: "배송 선택", description: "받을 방법을 선택합니다." },
    { title: "확인", description: "주문 내용을 확인합니다." },
  ];
  return (
    <div className="grid gap-ien-md">
      <Stepper
        steps={steps}
        currentStep={step}
        onStepChange={setStep}
        allowNavigation
        orientation="vertical"
        play
        delay={0.08}
        staggerDelay={0.12}
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
        {steps[step].title} 단계
      </p>
      <Stepper
        label="수량"
        value={quantity}
        range={{ min: 0, max: 10, step: 1 }}
        enabled
        onValueChange={setQuantity}
      />
    </div>
  );
}
