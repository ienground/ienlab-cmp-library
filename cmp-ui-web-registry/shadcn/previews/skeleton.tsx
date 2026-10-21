import { IenSkeleton } from "@/components/ui/skeleton";
export default function Preview() {
  return (
    <div className="grid gap-ien-md">
      <IenSkeleton pattern="TopListWithIcon" />
      <IenSkeleton
        pattern="AmountTopList"
        background="White"
        repeatLastItemCount={2}
      />
      <IenSkeleton height="5rem" width="9rem" radius="default" />
    </div>
  );
}
