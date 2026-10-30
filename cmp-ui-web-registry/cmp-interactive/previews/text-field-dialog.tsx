"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { TextFieldDialog } from "@/components/ui/text-field-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [name, setName] = React.useState("IEN");
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button variant="weak" tone="neutral" onClick={() => setOpen(true)}>
        이름 수정
      </Button>
      <TextFieldDialog
        open={open}
        onOpenChange={setOpen}
        value={name}
        onConfirm={setName}
        label="표시 이름"
      />
      <p aria-live="polite" className="text-ien-caption">
        이름: {name}
      </p>
    </div>
  );
}
