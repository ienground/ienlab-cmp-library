"use client";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
export default function Preview() {
  return (
    <Tabs defaultValue="account">
      <TabsList>
        <TabsTrigger value="account">계정</TabsTrigger>
        <TabsTrigger value="security">보안</TabsTrigger>
      </TabsList>
      <TabsContent value="account">프로필 정보를 관리합니다.</TabsContent>
      <TabsContent value="security">비밀번호를 관리합니다.</TabsContent>
    </Tabs>
  );
}
