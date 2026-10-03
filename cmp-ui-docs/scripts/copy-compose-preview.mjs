import { cp, mkdir } from "node:fs/promises";
import { fileURLToPath } from "node:url";
import path from "node:path";

const projectDirectory = fileURLToPath(new URL("../", import.meta.url));
const distributionDirectory = path.join(
  projectDirectory,
  "build/dist/wasmJs/productionExecutable",
);
const previewDirectory = path.join(projectDirectory, "build/site/compose");

await mkdir(previewDirectory, { recursive: true });
await cp(distributionDirectory, previewDirectory, { recursive: true, force: true });
await cp(
  path.join(projectDirectory, "web/assets/jetbrains-mono-LICENSE.txt"),
  path.join(projectDirectory, "build/site/jetbrains-mono-LICENSE.txt"),
);
