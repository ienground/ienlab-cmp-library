import generated from "./generated-web-catalog.json";
import type { CatalogItem } from "./catalog.js";

export interface WebRegistryItem extends CatalogItem {
  cmpId: string;
  cmpIds: string[];
  preview: string;
  example: string;
  installCommand: string;
  dependencies: string[];
  packages: string[];
  client: boolean;
  limitations: string[];
  apiEntries: {
    name: string;
    signature: string;
    kind: string;
    props: { name: string; type: string; optional: boolean }[];
  }[];
  manifest: object;
}
export const webCatalog: WebRegistryItem[] = generated;
