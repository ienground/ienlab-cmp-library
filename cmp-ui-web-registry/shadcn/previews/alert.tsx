import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
export default function Preview() {
  return (
    <div className="grid gap-ien-sm">
      <Alert>
        <AlertTitle>저장 완료</AlertTitle>
        <AlertDescription>변경 내용이 저장되었습니다.</AlertDescription>
      </Alert>
      <Alert variant="destructive">
        <AlertTitle>확인이 필요합니다</AlertTitle>
        <AlertDescription>다시 시도할 수 있습니다.</AlertDescription>
      </Alert>
    </div>
  );
}
