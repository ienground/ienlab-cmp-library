import { useState } from "react";
import { Check, Copy } from "lucide-react";
import { Button } from "./components/ui/button";
import { CodeBlock } from "../../cmp-ui-web-registry/reui/code-block/code-block";
import { copyTextToClipboard } from "./clipboard";

export function CodeExample({ label, code }: { label: string; code: string }) {
  const [copyStatus, setCopyStatus] = useState("");

  async function copyCode() {
    const copied = await copyTextToClipboard(code);
    setCopyStatus(
      copied
        ? "복사했습니다."
        : "복사하지 못했습니다. 코드를 선택해 복사해 주세요.",
    );
  }

  return (
    <div className="web-code-example">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <h3 className="text-sm font-medium">{label}</h3>
        <Button variant="outline" size="sm" onClick={copyCode}>
          {copyStatus === "복사했습니다." ? (
            <Check aria-hidden="true" />
          ) : (
            <Copy aria-hidden="true" />
          )}
          복사
        </Button>
      </div>
      <CodeBlock className="api-signature" code={code} language={label.endsWith(".json") ? "json" : "tsx"} variant="ghost" label={label} />
      <p role="status" className="text-xs text-muted-foreground">
        {copyStatus}
      </p>
    </div>
  );
}
