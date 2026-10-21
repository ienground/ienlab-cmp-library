export interface RegistrySourceFile {
  path: string;
  type: string;
  target?: string;
  absolutePath: string;
  content: string;
}
export interface RegistrySourceItem {
  name: string;
  type: string;
  title?: string;
  description?: string;
  files: RegistrySourceFile[];
  manifestPath: string;
  dependencies?: string[];
  registryDependencies?: string[];
  meta?: Record<string, unknown>;
}
export function collectRegistry(
  manifestPath: string,
  repository: string,
  ancestors?: string[],
): Promise<RegistrySourceItem[]>;
export function validateRegistry(items: RegistrySourceItem[]): void;
export function registryModuleAliases(
  items: RegistrySourceItem[],
): Record<string, string>;
