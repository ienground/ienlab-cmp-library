import { Kbd, KbdGroup } from "@/components/ui/kbd";
export default function Preview() {
  return (
    <div className="flex items-center gap-ien-sm">
      명령 실행{" "}
      <KbdGroup>
        <Kbd>⌘</Kbd>
        <Kbd>K</Kbd>
      </KbdGroup>
    </div>
  );
}
