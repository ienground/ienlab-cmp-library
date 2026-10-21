import { IenAssetFrame } from "@/components/ui/asset";
export default function Preview() {
  return (
    <div className="flex flex-wrap items-center gap-3">
      {(
        ["neutral", "brand", "success", "warning", "danger", "info"] as const
      ).map((tone) => (
        <IenAssetFrame key={tone} tone={tone} size="large" bordered>
          {tone.slice(0, 1).toUpperCase()}
        </IenAssetFrame>
      ))}
    </div>
  );
}
