import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
export default function Preview() {
  return (
    <Accordion type="single" collapsible>
      <AccordionItem value="a">
        <AccordionTrigger>시작하기</AccordionTrigger>
        <AccordionContent>
          키보드와 포인터로 열고 닫을 수 있습니다.
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  );
}
