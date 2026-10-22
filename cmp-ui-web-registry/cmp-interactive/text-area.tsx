"use client";

import { TextField, type TextFieldProps } from "@/components/ui/text-field";

export type TextAreaProps = Omit<TextFieldProps, "singleLine" | "type">;

function TextArea({ minLines = 4, maxLines = 8, className, inputClassName, ...props }: TextAreaProps) {
  return <TextField {...props} singleLine={false} minLines={minLines} maxLines={maxLines} inputClassName={inputClassName ?? className} />;
}

export { TextArea, TextArea as IenTextArea };
