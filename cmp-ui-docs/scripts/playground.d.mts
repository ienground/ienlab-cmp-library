import type { RegistrySourceItem } from "./registry.mjs";
import type { PlaygroundDefinition } from "../web/web-playground-types";
export function createPlaygroundCatalog(
  items: RegistrySourceItem[],
  previews: Record<string, string>,
): Record<string, PlaygroundDefinition>;
export function instrumentPlayground(
  source: string,
  runtimeModule: string,
  previewName?: string,
): string;
