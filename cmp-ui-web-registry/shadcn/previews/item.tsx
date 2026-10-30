import {
  Item,
  ItemContent,
  ItemDescription,
  ItemMedia,
  ItemTitle,
} from "@/components/ui/item";
import { SettingsIcon } from "lucide-react";
export default function Preview() {
  return (
    <Item>
      <ItemMedia variant="icon">
        <SettingsIcon />
      </ItemMedia>
      <ItemContent>
        <ItemTitle>환경 설정</ItemTitle>
        <ItemDescription>언어와 알림 설정</ItemDescription>
      </ItemContent>
    </Item>
  );
}
