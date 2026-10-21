/*
 * MIT License
 *
 * Copyright (c) 2023 shadcn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"use client";

import * as React from "react";
import { ArrowDownIcon, ArrowUpIcon, ChevronsUpDownIcon } from "lucide-react";
import { cn } from "cn";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";

export type DataTableColumn<T> = {
  id: string;
  header: React.ReactNode;
  accessor: (row: T) => React.ReactNode;
  sortValue?: (row: T) => string | number | Date | null | undefined;
  className?: string;
  sortable?: boolean;
};
export type DataTableProps<T> = Omit<
  React.ComponentProps<"div">,
  "children"
> & {
  columns: DataTableColumn<T>[];
  data: T[];
  getRowId?: (row: T, index: number) => string;
  pageSize?: number;
  emptyMessage?: string;
};
type Sort = { id: string; direction: "asc" | "desc" } | null;

function DataTable<T>({
  columns,
  data,
  getRowId,
  pageSize = 10,
  emptyMessage = "표시할 항목이 없습니다.",
  className,
  ref,
  ...rootProps
}: DataTableProps<T>) {
  const [sort, setSort] = React.useState<Sort>(null);
  const [page, setPage] = React.useState(0);
  const resolvedPageSize = Number.isFinite(pageSize)
    ? Math.max(1, Math.floor(pageSize))
    : 10;
  const sorted = React.useMemo(() => {
    if (!sort) return data;
    const column = columns.find((item) => item.id === sort.id);
    if (!column?.sortValue) return data;
    return [...data].sort((left, right) => {
      const a = column.sortValue?.(left);
      const b = column.sortValue?.(right);
      const result =
        a == null
          ? b == null
            ? 0
            : -1
          : b == null
            ? 1
            : a instanceof Date && b instanceof Date
              ? a.getTime() - b.getTime()
              : typeof a === "number" && typeof b === "number"
                ? a - b
                : String(a).localeCompare(String(b), undefined, {
                    numeric: true,
                    sensitivity: "base",
                  });
      return sort.direction === "asc" ? result : -result;
    });
  }, [columns, data, sort]);
  const pages = Math.max(1, Math.ceil(sorted.length / resolvedPageSize));
  React.useEffect(
    () => setPage((current) => Math.min(current, pages - 1)),
    [pages],
  );
  const toggleSort = (column: DataTableColumn<T>) => {
    if (!column.sortable || !column.sortValue) return;
    setSort((current) =>
      current?.id === column.id
        ? {
            id: column.id,
            direction: current.direction === "asc" ? "desc" : "asc",
          }
        : { id: column.id, direction: "asc" },
    );
  };
  return (
    <div
      {...rootProps}
      ref={ref}
      className={cn("grid gap-ien-sm", className)}
      data-slot="data-table"
    >
      <div className="overflow-x-auto rounded-ien-default border border-ien-border">
        <Table>
          <TableHeader>
            <TableRow>
              {columns.map((column) => (
                <TableHead
                  key={column.id}
                  className={column.className}
                  aria-sort={
                    column.sortable && column.sortValue
                      ? sort?.id === column.id
                        ? sort.direction === "asc"
                          ? "ascending"
                          : "descending"
                        : "none"
                      : undefined
                  }
                >
                  {column.sortable && column.sortValue ? (
                    <button
                      type="button"
                      onClick={() => toggleSort(column)}
                      className="inline-flex min-h-8 items-center gap-ien-xs text-left text-ien-label2 text-ien-text-primary focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ien-brand"
                    >
                      {column.header}
                      {sort?.id === column.id ? (
                        sort.direction === "asc" ? (
                          <ArrowUpIcon
                            aria-hidden="true"
                            className="size-3.5"
                          />
                        ) : (
                          <ArrowDownIcon
                            aria-hidden="true"
                            className="size-3.5"
                          />
                        )
                      ) : (
                        <ChevronsUpDownIcon
                          aria-hidden="true"
                          className="size-3.5 text-ien-text-tertiary"
                        />
                      )}
                    </button>
                  ) : (
                    column.header
                  )}
                </TableHead>
              ))}
            </TableRow>
          </TableHeader>
          <TableBody>
            {sorted.length === 0 ? (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center text-ien-body2 text-ien-text-secondary"
                >
                  {emptyMessage}
                </TableCell>
              </TableRow>
            ) : (
              sorted
                .slice(page * resolvedPageSize, (page + 1) * resolvedPageSize)
                .map((row, index) => (
                  <TableRow key={getRowId?.(row, index) ?? index}>
                    {columns.map((column) => (
                      <TableCell key={column.id} className={column.className}>
                        {column.accessor(row)}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
            )}
          </TableBody>
        </Table>
      </div>
      {pages > 1 && (
        <nav
          aria-label="표 페이지"
          className="flex items-center justify-between gap-ien-sm"
        >
          <span className="text-ien-caption text-ien-text-secondary">
            {page + 1} / {pages} 페이지 · {sorted.length}개 항목
          </span>
          <div className="flex gap-ien-xs">
            <Button
              type="button"
              variant="line"
              tone="neutral"
              size="sm"
              disabled={page === 0}
              onClick={() => setPage((current) => current - 1)}
            >
              이전
            </Button>
            <Button
              type="button"
              variant="line"
              tone="neutral"
              size="sm"
              disabled={page >= pages - 1}
              onClick={() => setPage((current) => current + 1)}
            >
              다음
            </Button>
          </div>
        </nav>
      )}
    </div>
  );
}

export { DataTable };
