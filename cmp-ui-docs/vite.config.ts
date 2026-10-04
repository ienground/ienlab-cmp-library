import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import { defineConfig } from "vite";
import { fileURLToPath } from "node:url";
import { readFileSync } from "node:fs";

const versionCatalog = readFileSync(
  new URL("../gradle/libs.versions.toml", import.meta.url),
  "utf8",
);
const libraryVersion = versionCatalog.match(
  /^lib-version-name\s*=\s*"([^"\n]+)"/m,
)?.[1];
if (!libraryVersion) throw new Error("라이브러리 버전을 찾을 수 없습니다.");

export default defineConfig(({ command }) => ({
  root: "web",
  base: "/",
  plugins: [react(), tailwindcss()],
  define: {
    "import.meta.env.VITE_LIBRARY_VERSION": JSON.stringify(libraryVersion),
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./web", import.meta.url)),
    },
  },
  publicDir: command === "serve" ? "../build/site" : false,
  build: {
    outDir: "../build/site",
    emptyOutDir: true,
  },
}));
