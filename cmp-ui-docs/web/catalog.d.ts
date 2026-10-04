export interface CatalogItem {
  id: string;
  name: string;
  category: string;
  description: string;
  api: string[];
  module: string;
  packageName: string;
  source: string;
  sample: string;
}

export const catalog: CatalogItem[];
export const categories: string[];
