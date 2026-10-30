"use client";
import {
  Menubar,
  MenubarContent,
  MenubarItem,
  MenubarMenu,
  MenubarSeparator,
  MenubarTrigger,
} from "@/components/ui/menubar";
export default function Preview() {
  return (
    <Menubar>
      <MenubarMenu>
        <MenubarTrigger>파일</MenubarTrigger>
        <MenubarContent>
          <MenubarItem>새 문서</MenubarItem>
          <MenubarItem>열기</MenubarItem>
          <MenubarSeparator />
          <MenubarItem>내보내기</MenubarItem>
        </MenubarContent>
      </MenubarMenu>
      <MenubarMenu>
        <MenubarTrigger>편집</MenubarTrigger>
        <MenubarContent>
          <MenubarItem>복사</MenubarItem>
          <MenubarItem>붙여넣기</MenubarItem>
        </MenubarContent>
      </MenubarMenu>
    </Menubar>
  );
}
