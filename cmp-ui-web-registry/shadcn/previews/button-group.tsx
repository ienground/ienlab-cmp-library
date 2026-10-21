import { Button } from "@/components/ui/button";
import {
  ButtonGroup,
  ButtonGroupSeparator,
  ButtonGroupText,
} from "@/components/ui/button-group";
import { MinusIcon, PlusIcon } from "lucide-react";
export default function Preview() {
  return (
    <ButtonGroup>
      <Button variant="line" tone="neutral" size="icon-sm" aria-label="줄이기">
        <MinusIcon />
      </Button>
      <ButtonGroupSeparator />
      <ButtonGroupText className="px-ien-sm">3</ButtonGroupText>
      <ButtonGroupSeparator />
      <Button variant="line" tone="neutral" size="icon-sm" aria-label="늘리기">
        <PlusIcon />
      </Button>
    </ButtonGroup>
  );
}
