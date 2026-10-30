import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
  IenCard,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
export default function Preview() {
  return (
    <div className="grid gap-ien-md">
      <Card className="max-w-sm">
        <CardHeader>
          <CardTitle>계정 요약</CardTitle>
          <CardDescription>최근 활동과 상태</CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-ien-title2">12개 항목</p>
        </CardContent>
        <CardFooter>
          <Button size="sm">자세히 보기</Button>
        </CardFooter>
      </Card>
      <IenCard variant="Filled" tone="Brand" className="max-w-sm">
        <CardHeader>
          <CardTitle>활성 구독</CardTitle>
          <CardDescription>다음 결제일까지 12일 남았습니다.</CardDescription>
        </CardHeader>
        <CardContent>자동 결제가 켜져 있습니다.</CardContent>
      </IenCard>
    </div>
  );
}
