/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { Progress } from "@/components/ui/progress";
import { cn } from "cn";

export type QuestionnaireOption = {
  value: string;
  label: React.ReactNode;
  description?: React.ReactNode;
};
export type QuestionnaireQuestion = {
  id: string;
  title: React.ReactNode;
  description?: React.ReactNode;
  options: QuestionnaireOption[];
};
export type QuestionnaireProps = {
  questions: QuestionnaireQuestion[];
  value?: Record<string, string>;
  defaultValue?: Record<string, string>;
  onValueChange?: (answers: Record<string, string>) => void;
  onComplete?: (answers: Record<string, string>) => void;
  className?: string;
};
function Questionnaire({
  questions,
  value,
  defaultValue = {},
  onValueChange,
  onComplete,
  className,
}: QuestionnaireProps) {
  const [answers, setAnswers] = React.useState(defaultValue);
  const [step, setStep] = React.useState(0);
  const currentAnswers = value ?? answers;
  const question = questions[step];
  const update = (answer: string) => {
    const next = { ...currentAnswers, [question.id]: answer };
    if (value === undefined) setAnswers(next);
    onValueChange?.(next);
  };
  if (!question)
    return (
      <div
        role="status"
        className={cn("text-ien-body2 text-ien-text-secondary", className)}
      >
        질문이 없습니다.
      </div>
    );
  const finish = () => onComplete?.(currentAnswers);
  return (
    <section className={cn("grid gap-ien-lg", className)} aria-label="질문지">
      <div className="grid gap-ien-xs">
        <div className="flex justify-between text-ien-caption text-ien-text-secondary">
          <span>
            질문 {step + 1} / {questions.length}
          </span>
          <span>{Math.round(((step + 1) / questions.length) * 100)}%</span>
        </div>
        <Progress
          value={((step + 1) / questions.length) * 100}
          aria-label="질문 진행률"
        />
      </div>
      <fieldset className="grid gap-ien-md">
        <legend className="text-ien-title3 text-ien-text-primary">
          {question.title}
        </legend>
        {question.description && (
          <p className="text-ien-body2 text-ien-text-secondary">
            {question.description}
          </p>
        )}
        <RadioGroup
          value={currentAnswers[question.id] ?? ""}
          onValueChange={update}
          className="grid gap-ien-xs"
        >
          {question.options.map((option) => (
            <Label
              key={option.value}
              htmlFor={`${question.id}-${option.value}`}
              className="flex cursor-pointer items-start gap-ien-sm rounded-ien-default border border-ien-border p-ien-md has-[:checked]:border-ien-brand has-[:checked]:bg-ien-brand-weak"
            >
              <RadioGroupItem
                id={`${question.id}-${option.value}`}
                value={option.value}
              />
              <span className="grid gap-ien-xxs">
                <span className="text-ien-body2 text-ien-text-primary">
                  {option.label}
                </span>
                {option.description && (
                  <span className="text-ien-caption text-ien-text-secondary">
                    {option.description}
                  </span>
                )}
              </span>
            </Label>
          ))}
        </RadioGroup>
      </fieldset>
      <footer className="flex justify-between gap-ien-sm">
        <Button
          type="button"
          variant="line"
          tone="neutral"
          disabled={step === 0}
          onClick={() => setStep((current) => current - 1)}
        >
          이전
        </Button>
        {step === questions.length - 1 ? (
          <Button
            type="button"
            disabled={!currentAnswers[question.id]}
            onClick={finish}
          >
            완료
          </Button>
        ) : (
          <Button
            type="button"
            disabled={!currentAnswers[question.id]}
            onClick={() => setStep((current) => current + 1)}
          >
            다음
          </Button>
        )}
      </footer>
    </section>
  );
}

export { Questionnaire };
