"use client";
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar";
export default function Preview() {
  return (
    <SidebarProvider defaultOpen>
      <div className="flex min-h-56 w-full">
        <Sidebar collapsible="none" className="w-48 border-r border-ien-border">
          <SidebarContent>
            <SidebarGroup>
              <SidebarGroupLabel>워크스페이스</SidebarGroupLabel>
              <SidebarGroupContent>
                <SidebarMenu>
                  <SidebarMenuItem>
                    <SidebarMenuButton isActive>프로젝트</SidebarMenuButton>
                  </SidebarMenuItem>
                  <SidebarMenuItem>
                    <SidebarMenuButton>설정</SidebarMenuButton>
                  </SidebarMenuItem>
                </SidebarMenu>
              </SidebarGroupContent>
            </SidebarGroup>
          </SidebarContent>
          <SidebarFooter>계정</SidebarFooter>
        </Sidebar>
        <main className="grid flex-1 content-start gap-ien-sm p-ien-md">
          <SidebarTrigger />
          콘텐츠 영역
        </main>
      </div>
    </SidebarProvider>
  );
}
