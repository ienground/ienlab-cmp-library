import { CodeBlock } from "@/components/ui/code-block";
export default function Preview() {
  return (
    <CodeBlock
      filename="hello.ts"
      language="TypeScript"
      showLineNumbers
      code={"function hello(name: string) {\n  return `안녕하세요, ${name}`\n}"}
    />
  );
}
