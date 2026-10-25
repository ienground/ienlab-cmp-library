import generated from "./generated-web-catalog.json";
import type { CatalogItem } from "./catalog.js";
import type { PlaygroundDefinition, PlaygroundProp } from "./web-playground-types";

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
  playground: PlaygroundDefinition;
}
function playgroundKind(kind: string): PlaygroundProp["kind"] {
  if (kind === "select" || kind === "boolean" || kind === "number" || kind === "text" || kind === "json" || kind === "object" || kind === "date" || kind === "callback" || kind === "slot" || kind === "icon" || kind === "reference") return kind;
  throw new Error(`지원하지 않는 Playground 속성 종류입니다: ${kind}`);
}
type GeneratedPlaygroundProp = Omit<PlaygroundProp, "kind" | "fields"> & {
  kind: string;
  fields?: GeneratedPlaygroundProp[];
};
function playgroundProp(prop: GeneratedPlaygroundProp): PlaygroundProp {
  return { ...prop, kind: playgroundKind(prop.kind), fields: prop.fields?.map(playgroundProp) };
}
export const webCatalog: WebRegistryItem[] = generated.map((item) => ({
  ...item,
  playground: { ...item.playground, targets: item.playground.targets.map((target) => ({
    ...target, props: target.props.map(playgroundProp),
  })) },
}));
