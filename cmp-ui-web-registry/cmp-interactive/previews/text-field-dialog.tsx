"use client";

import * as React from "react";
import { Button } from "@/components/ui/button";
import { TextFieldDialog } from "@/components/ui/text-field-dialog";

export default function Preview() {
  const [open, setOpen] = React.useState(false);
  const [values, setValues] = React.useState({ name: "IEN", email: "" });
  return (
    <div className="grid justify-items-start gap-ien-sm">
      <Button variant="weak" tone="neutral" onClick={() => setOpen(true)}>
        이름 수정
      </Button>
      <TextFieldDialog
        open={open}
        onOpenChange={setOpen}
        textFields={{
          name: {
            label: "표시 이름",
            initialValue: "IEN",
            maxLines: 1,
            valid: (value) => value.trim().length > 0,
          },
          email: {
            label: "이메일",
            initialValue: "",
            keyboardType: "email",
            placeholder: "name@example.com",
          },
        }}
        values={values}
        onConfirmFields={(next) =>
          setValues({ name: next.name ?? "", email: next.email ?? "" })
        }
      />
      <p aria-live="polite" className="text-ien-caption">
        이름: {values.name} · 이메일: {values.email || "미입력"}
      </p>
    </div>
  );
}
