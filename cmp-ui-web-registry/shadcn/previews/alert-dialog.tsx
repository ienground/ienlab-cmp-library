"use client";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
export default function Preview() {
  return (
    <AlertDialog>
      <AlertDialogTrigger>삭제</AlertDialogTrigger>
      <AlertDialogContent closeOnDimmerClick={false}>
        <AlertDialogHeader>
          <AlertDialogTitle>정말 삭제할까요?</AlertDialogTitle>
          <AlertDialogDescription>
            삭제한 항목은 복구할 수 없습니다.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter buttonLayout="horizontal">
          <AlertDialogCancel>취소</AlertDialogCancel>
          <AlertDialogAction>삭제</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
