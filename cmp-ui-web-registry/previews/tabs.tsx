import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"

export function Pattern() {
  return (
    <div className="flex w-full max-w-lg flex-col gap-6">
      <Tabs defaultValue="summary" size="large" fluid={false} ariaLabel="주문 정보 탭">
        <TabsList aria-label="주문 정보 탭">
          <TabsTrigger value="summary">요약</TabsTrigger>
          <TabsTrigger value="detail" badge={-1}>상세</TabsTrigger>
          <TabsTrigger value="history" badge={120}>내역</TabsTrigger>
        </TabsList>
        <TabsContent value="summary">주문 요약을 확인합니다.</TabsContent>
        <TabsContent value="detail">상세 정보를 확인합니다.</TabsContent>
        <TabsContent value="history">이전 내역을 확인합니다.</TabsContent>
      </Tabs>
      <Tabs size="small" fluid ariaLabel="가로 스크롤 탭"
        items={[
          { text: "전체", key: "all" },
          { text: "진행 중인 주문", key: "progress", badge: 8 },
          { text: "배송을 기다리는 주문", key: "waiting", redBean: true },
          { text: "완료된 주문", key: "complete" },
          { text: "취소된 주문", key: "cancelled" },
          { text: "사용할 수 없음", key: "disabled", enabled: false },
        ]} />
    </div>
  )
}
export default Pattern
