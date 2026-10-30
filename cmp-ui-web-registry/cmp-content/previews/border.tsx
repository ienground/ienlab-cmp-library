import { IenBorder } from "@/components/ui/border";
export default function Preview() {
  return (
    <div className="space-y-4">
      <IenBorder />
      <IenBorder variant="padding24" />
      <IenBorder variant={{ height: 24 }} />
    </div>
  );
}
