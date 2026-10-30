import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import { defineConfig, type UserConfig } from "vite";
import { fileURLToPath } from "node:url";
import { readFileSync } from "node:fs";
import { collectRegistry, registryModuleAliases } from "./scripts/registry.mjs";

const runtimeDependencies = Object.keys(
  JSON.parse(readFileSync(new URL("./package.json", import.meta.url), "utf8"))
    .dependencies,
);

const versionCatalog = readFileSync(
  new URL("../gradle/libs.versions.toml", import.meta.url),
  "utf8",
);
const libraryVersion = versionCatalog.match(
  /^lib-version-name\s*=\s*"([^"\n]+)"/m,
)?.[1];
if (!libraryVersion) throw new Error("라이브러리 버전을 찾을 수 없습니다.");

export default defineConfig(async ({ command }) => {
  const repository = fileURLToPath(new URL("../", import.meta.url));
  const aliases = registryModuleAliases(
    await collectRegistry(`${repository}/registry.json`, repository),
  );
  return {
    root: "web",
    base: "/",
    plugins: [react(), tailwindcss()],
    define: {
      "import.meta.env.VITE_LIBRARY_VERSION": JSON.stringify(libraryVersion),
    },
    resolve: {
      dedupe: runtimeDependencies,
      alias: [
        ...Object.entries(aliases).map(([find, replacement]) => ({
          find,
          replacement,
        })),
        {
          find: "@",
          replacement: fileURLToPath(new URL("./web", import.meta.url)),
        },
      ],
    },
    publicDir: command === "serve" ? "../build/site" : false,
    build: {
      outDir: "../build/site",
      emptyOutDir: command === "build",
    },
  } satisfies UserConfig;
});
