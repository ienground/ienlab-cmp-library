"use client"

import { AssetFrame } from "@/components/ui/asset-frame"

export function Pattern() {
  return <div className="flex w-full max-w-lg flex-col gap-4"><><AssetFrame size="small">★</AssetFrame><AssetFrame tone="brand">★</AssetFrame><AssetFrame size="large" shape="circle" tone="success">✓</AssetFrame><AssetFrame size="extra-large" bordered>IEN</AssetFrame></></div>
}
export default Pattern
