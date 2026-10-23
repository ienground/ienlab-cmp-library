import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

export function Pattern() {
  return (
    <Card tone="brand" toneVariant="weak" className="w-full max-w-xs">
      <CardHeader>
        <CardTitle>브랜드 카드</CardTitle>
        <CardDescription>
          CMP의 약한 tone과 그라데이션을 적용합니다.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <p>
          The card component supports a size prop that defaults to
          &quot;default&quot; for standard spacing and sizing.
        </p>
      </CardContent>
      <CardFooter>
        <Button variant="outline" className="w-full">
          Action
        </Button>
      </CardFooter>
    </Card>
  )
}
export default Pattern
