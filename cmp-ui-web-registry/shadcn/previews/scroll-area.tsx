import { ScrollArea } from "@/components/ui/scroll-area";
export default function Preview() {
  return (
    <ScrollArea className="h-40 rounded-ien-default border border-ien-border p-ien-md">
      <div className="grid gap-ien-sm">
        {Array.from({ length: 12 }, (_, i) => (
          <p key={i}>스크롤 가능한 항목 {i + 1}</p>
        ))}
      </div>
    </ScrollArea>
  );
}
