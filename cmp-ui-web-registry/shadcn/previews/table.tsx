import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
export default function Preview() {
  return (
    <Table>
      <TableCaption>이번 주 활동</TableCaption>
      <TableHeader>
        <TableRow>
          <TableHead>요일</TableHead>
          <TableHead>작업</TableHead>
          <TableHead className="text-right">상태</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow>
          <TableCell>월요일</TableCell>
          <TableCell>디자인 검토</TableCell>
          <TableCell className="text-right">완료</TableCell>
        </TableRow>
        <TableRow>
          <TableCell>화요일</TableCell>
          <TableCell>개발</TableCell>
          <TableCell className="text-right">진행 중</TableCell>
        </TableRow>
      </TableBody>
    </Table>
  );
}
