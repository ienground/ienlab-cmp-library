"use client";

import * as React from "react";
import {
  IenDateWheelPicker,
  IenTimeWheelPicker,
  IenDurationWheelPicker,
} from "@/components/ui/wheel-picker";

export default function Preview() {
  const [date, setDate] = React.useState(() => new Date(2024, 1, 29));
  const [time, setTime] = React.useState("13:30:00");
  const [duration, setDuration] = React.useState(90000);
  return (
    <div className="grid gap-ien-md">
      <section className="grid gap-ien-xs">
        <h3 className="text-ien-label1">날짜 · min/max와 윤년</h3>
        <IenDateWheelPicker
          value={date}
          min={new Date(2024, 0, 1)}
          max={new Date(2025, 11, 31)}
          onValueChange={setDate}
        />
        <p
          aria-live="polite"
          className="text-ien-caption text-ien-text-secondary"
        >
          {date.getFullYear()}년 {date.getMonth() + 1}월 {date.getDate()}일
        </p>
      </section>
      <section className="grid gap-ien-xs">
        <h3 className="text-ien-label1">12시간제 시각</h3>
        <IenTimeWheelPicker
          value={time}
          use24HourFormat={false}
          onValueChange={setTime}
        />
        <p
          aria-live="polite"
          className="text-ien-caption text-ien-text-secondary"
        >
          {time}
        </p>
      </section>
      <section className="grid gap-ien-xs">
        <h3 className="text-ien-label1">72시간 기간</h3>
        <IenDurationWheelPicker
          value={duration}
          maxHours={72}
          onValueChange={setDuration}
        />
        <p
          aria-live="polite"
          className="text-ien-caption text-ien-text-secondary"
        >
          {Math.floor(duration / 3600)}시간 {Math.floor(duration / 60) % 60}분
        </p>
      </section>
    </div>
  );
}
