"use client";
import { DataTable, type DataTableColumn } from "@/components/ui/data-table";
type Person = { name: string; role: string };
const columns: DataTableColumn<Person>[] = [
  {
    id: "name",
    header: "이름",
    accessor: (r) => r.name,
    sortValue: (r) => r.name,
    sortable: true,
  },
  { id: "role", header: "역할", accessor: (r) => r.role },
];
export default function Preview() {
  return (
    <DataTable
      columns={columns}
      data={[
        { name: "김하늘", role: "디자인" },
        { name: "박지민", role: "개발" },
        { name: "이서준", role: "기획" },
      ]}
      pageSize={2}
    />
  );
}
