export interface PlaygroundProp {
  name: string;
  type: string;
  kind:
    | "select"
    | "boolean"
    | "number"
    | "text"
    | "json"
    | "date"
    | "callback"
    | "slot"
    | "reference";
  optional: boolean;
  options?: (string | number | boolean | null)[];
  defaultValue?: unknown;
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
