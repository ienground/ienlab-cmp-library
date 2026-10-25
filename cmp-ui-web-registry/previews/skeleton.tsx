import { Skeleton, SkeletonMotionGroup } from "@/components/ui/skeleton"

export function Pattern() {
  return (
    <div className="grid w-full max-w-3xl gap-8 md:grid-cols-2">
      <section className="flex flex-col gap-4">
        <h3>TopListWithIcon · 마지막 항목 3회</h3>
        <Skeleton pattern="TopListWithIcon" />
      </section>
      <section className="flex flex-col gap-4">
        <h3>ListRowTexts · ThreeRowTypeA</h3>
        <Skeleton pattern="ListRowTexts" listRowTextsType="ThreeRowTypeA" repeatLastItemCount={{ type: "Count", value: 2 }} />
      </section>
      <section className="flex flex-col gap-4">
        <h3>배경 · White / Grey / GreyOpacity100</h3>
        <div className="flex flex-col gap-3 bg-ien-surface-weak p-4">
          <Skeleton height={24} background="White" />
          <Skeleton height={24} background="Grey" />
          <Skeleton height={24} background="GreyOpacity100" />
        </div>
      </section>
      <section className="flex flex-col gap-4">
        <h3>사용자 정의 · 그룹 모션</h3>
        <SkeletonMotionGroup>
          <Skeleton custom={[
            { type: "Row", spacing: 16, align: "center", elements: [
              { type: "Block", width: 48, height: 48, shape: "Circle" },
              { type: "Column", weight: 1, spacing: 8, elements: [
                { type: "Block", width: "70%", typography: "body1" },
                { type: "Block", width: "40%", typography: "caption" },
              ] },
            ] },
          ]} repeatLastItemCount={{ type: "Count", value: 1 }} />
        </SkeletonMotionGroup>
      </section>
    </div>
  )
}
export default Pattern
