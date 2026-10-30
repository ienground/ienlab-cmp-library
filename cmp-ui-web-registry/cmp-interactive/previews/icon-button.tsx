"use client";

import * as React from "react";
import { Heart } from "lucide-react";
import { IconButton } from "@/components/ui/icon-button";

export default function Preview() {
  const [liked, setLiked] = React.useState(false);
  return (
    <div className="flex items-center gap-ien-sm">
      <IconButton label="즐겨찾기" pressed={liked} onPressedChange={setLiked}>
        <Heart aria-hidden="true" />
      </IconButton>
      <span aria-live="polite">
        {liked ? "즐겨찾기에 추가됨" : "즐겨찾기 해제됨"}
      </span>
    </div>
  );
}
