"use client";
import * as React from "react";
import { Home, Search, User } from "lucide-react";
import { IenNavigationBar } from "@/components/ui/navigation-bar";
export default function Preview() {
  const [index, setIndex] = React.useState(0);
  const items = [
    { label: "홈", icon: <Home className="size-5" /> },
    { label: "검색", icon: <Search className="size-5" />, badge: 3 },
    { label: "내 정보", icon: <User className="size-5" /> },
  ];
  return (
    <div className="relative min-h-32 rounded-ien-lg bg-ien-surface-weak">
      <IenNavigationBar
        items={items}
        selectedIndex={index}
        onSelectionChange={setIndex}
        type="Type2"
      />
      <p className="p-3">선택한 탭: {items[index]?.label}</p>
    </div>
  );
}
