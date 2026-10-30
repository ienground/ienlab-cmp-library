import { AspectRatio } from "@/components/ui/aspect-ratio";
export default function Preview() {
  return (
    <AspectRatio
      ratio={16 / 9}
      className="grid place-items-center rounded-ien-default bg-ien-brand-weak text-ien-on-brand-weak"
    >
      16:9 콘텐츠
    </AspectRatio>
  );
}
