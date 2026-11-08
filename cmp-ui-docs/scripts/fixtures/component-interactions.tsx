import { useState } from "react";
import { createRoot } from "react-dom/client";
import { WheelPicker } from "@/components/ui/wheel-picker";
import { DatePicker } from "@/components/reui/date-picker";
import { TimePicker, TimePickerPanel } from "@/components/reui/time-picker";
import { DurationPicker } from "@/components/reui/duration-picker";
import { SwipeBox } from "@/components/ui/swipe-box";
import { XScrollArea, XScrollAreaContent, XScrollAreaNext } from "@/components/ui/x-scroll-area";
import { Input } from "@/components/ui/input";
import { SplitInput } from "@/components/ui/split-input";
import { Agreement } from "@/components/ui/agreement";
import "../../web/styles.css";

function Fixture() {
  const params = new URLSearchParams(location.search);
  const kind = params.get("fixture");
  const [wheel, setWheel] = useState<string | number>(1);
  const [date, setDate] = useState<string | null>("2024-02-29");
  const [time, setTime] = useState<string | null>("14:30");
  const [duration, setDuration] = useState<number | null>(3661);
  const [submits, setSubmits] = useState(0);
  const [actions, setActions] = useState(0);
  if (kind === "date") return <><DatePicker inline value={date} onValueChange={setDate} aria-label="날짜" /><output>{date}</output></>;
  if (kind === "time") return <><TimePicker value={time} onValueChange={setTime} hourCycle={24}><TimePickerPanel /></TimePicker><output>{time}</output></>;
  if (kind === "duration") return <><DurationPicker inline value={duration} onValueChange={setDuration} /><output>{duration}</output></>;
  if (kind === "confirm") return <><DatePicker value={date} onValueChange={setDate} requireConfirm aria-label="날짜" /><output>{date}</output></>;
  if (kind === "fields" || kind === "fields-cancel") return <form onReset={(event) => { if (kind === "fields-cancel") event.preventDefault(); }}>
    <Input name="text" aria-label="텍스트" defaultValue="초기" clearable lengthLimit={{ mode: "block", maxLength: 10 }} />
    <SplitInput name="code" labels={["앞자리", "뒷자리"]} lengths={[2, 2]} defaultValue={["12", "34"]} />
    <Agreement name="terms" items={[{ value: "terms", label: "이용약관" }, { value: "marketing", label: "마케팅" }]} defaultValue={["terms"]} />
    <button type="reset">폼 초기화</button>
  </form>;
  if (kind === "actions") return <form onSubmit={(event) => { event.preventDefault(); setSubmits((value) => value + 1); }}>
    <XScrollArea style={{ width: 256 }}><XScrollAreaContent><div style={{ width: 1000 }}>가로 콘텐츠</div></XScrollAreaContent><XScrollAreaNext aria-label="다음 항목" /></XScrollArea>
    <SwipeBox defaultOpen="end" endActions={[{ id: "archive", label: "보관", onClick: () => setActions((value) => value + 1) }]}><div className="h-20">항목</div></SwipeBox>
    <output aria-label="폼 제출">{submits}</output><output aria-label="보관 횟수">{actions}</output>
  </form>;
  const long = kind === "long";
  return <>
    <WheelPicker label="선택" options={Array.from({ length: long ? 1000 : 8 }, (_, value) => ({ value, label: `항목 ${value}`, disabled: value === 3 }))}
      value={long ? undefined : wheel} defaultValue={long ? 500 : undefined} onValueChange={setWheel}
      disabled={kind === "disabled"} readOnly={kind === "readonly"} />
    <output>{wheel}</output><button type="button" onClick={() => setWheel(6)}>외부 값 변경</button>
  </>;
}

createRoot(document.getElementById("root")!).render(<Fixture />);
