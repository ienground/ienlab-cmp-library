"use client";

import { ChevronRight } from "lucide-react";
import { Button, type ButtonProps } from "@/components/ui/button";
import { cn } from "@/lib/ien-utils";

export type TextButtonSize = "xsmall" | "small" | "medium" | "large" | "xlarge" | "xxlarge";
export type TextButtonProps = Omit<ButtonProps, "variant" | "size"> & {
  variant?: "clear" | "arrow" | "underline";
  emphasis?: "normal" | "underline" | "arrow";
  size?: TextButtonSize | ButtonProps["size"];
};
const sizes = {
  xsmall: "min-h-7 p-ien-xxs text-ien-caption [&_svg]:size-[10px]",
  small: "min-h-8 p-[5px] text-ien-label2 [&_svg]:size-3",
  medium: "min-h-9 p-[6px] text-ien-label1 [&_svg]:size-3.5",
  large: "min-h-10 p-[7px] text-ien-body2 [&_svg]:size-4",
  xlarge: "min-h-11 p-ien-xs text-ien-body1 [&_svg]:size-[18px]",
  xxlarge: "min-h-12 p-ien-xs text-ien-title3 [&_svg]:size-5",
};
function TextButton({ variant, emphasis = "normal", size = "medium", className, children, content, style, contentPadding, ...props }: TextButtonProps) {
  const resolvedVariant = variant ?? (emphasis === "normal" ? "clear" : emphasis);
  const resolvedSize = size === "xs" ? "xsmall" : size === "sm" ? "small" : size === "lg" ? "large" : size && size in sizes ? size as TextButtonSize : "medium";
  const index = (["xsmall", "small", "medium", "large", "xlarge", "xxlarge"] as const).indexOf(resolvedSize);
  const typography = ["caption", "label2", "label1", "body2", "body1", "title3"][index];
  return <Button {...props} contentPadding={contentPadding ?? [4, 5, 6, 7, 8, 8][index]} style={{ fontSize: `var(--ien-font-size-${typography})`, lineHeight: `var(--ien-line-height-${typography})`, fontWeight: `var(--ien-font-weight-${typography})`, minHeight: [28, 32, 36, 40, 44, 48][index], ...style }} size="medium" variant="ghost" shape="var(--ien-radius-sm)" useGradient={false} className={cn("h-auto active:scale-[0.97]", sizes[resolvedSize], resolvedVariant === "underline" && "underline underline-offset-4", className)}>
    <span className={cn("inline-flex items-center", ["xsmall", "small", "medium"].includes(resolvedSize) ? "gap-ien-xxxs" : "gap-[3px]")}>{content ?? children}{resolvedVariant === "arrow" && <ChevronRight aria-hidden="true" />}</span>
  </Button>;
}
export { TextButton, TextButton as IenTextButton };
