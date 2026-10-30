import { Marker, MarkerContent, MarkerIcon } from "@/components/ui/marker";
import { CheckIcon } from "lucide-react";
export default function Preview() {
  return (
    <div className="grid gap-ien-md">
      <Marker>
        <MarkerIcon>
          <CheckIcon />
        </MarkerIcon>
        <MarkerContent>작업이 완료되었습니다.</MarkerContent>
      </Marker>
      <Marker variant="separator">또는</Marker>
    </div>
  );
}
