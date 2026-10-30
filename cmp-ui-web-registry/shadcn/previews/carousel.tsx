import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
export default function Preview() {
  return (
    <Carousel opts={{ loop: true }} className="mx-auto w-full max-w-sm">
      <CarouselContent>
        {["첫 번째", "두 번째", "세 번째"].map((x) => (
          <CarouselItem key={x}>
            <div className="grid aspect-video place-items-center rounded-ien-default bg-ien-surface-variant">
              {x}
            </div>
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious />
      <CarouselNext />
    </Carousel>
  );
}
