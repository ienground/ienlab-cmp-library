export interface PlaygroundProp {
  name: string;
  type: string;
  kind:
    | "select"
    | "boolean"
    | "number"
    | "text"
    | "array"
    | "union"
    | "record"
    | "object"
    | "date"
    | "callback"
    | "slot"
    | "icon"
    | "reference";
  optional: boolean;
  options?: (string | number | boolean | null)[];
  defaultValue?: unknown;
  fields?: PlaygroundProp[];
  item?: PlaygroundProp;
  variants?: PlaygroundProp[];
  tuple?: boolean;
  label?: string;
  description?: string;
  condition?: { field: string; options: (string | number | boolean | null)[] };
}
export interface PlaygroundTarget {
  key: string;
  name: string;
  module: string;
  props: PlaygroundProp[];
}
export interface PlaygroundDefinition {
  example: string;
  targets: PlaygroundTarget[];
  edits: {
    key: string;
    offset: number;
    childrenStart: number;
    childrenEnd?: number;
    attributes: { name: string; start: number; end: number }[];
  }[];
}
export type PlaygroundValues = Record<string, Record<string, unknown>>;
