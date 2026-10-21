"use client";
import {
  Drawer,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer";
import { Button } from "@/components/ui/button";
export default function Preview() {
  return (
    <Drawer>
      <DrawerTrigger asChild>
        <Button variant="line" tone="neutral">
          아래에서 열기
        </Button>
      </DrawerTrigger>
      <DrawerContent>
        <DrawerHeader>
          <DrawerTitle>필터</DrawerTitle>
          <DrawerDescription>
            목록을 원하는 기준으로 좁혀 보세요.
          </DrawerDescription>
        </DrawerHeader>
        <DrawerFooter>
          <Button>적용</Button>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
}
