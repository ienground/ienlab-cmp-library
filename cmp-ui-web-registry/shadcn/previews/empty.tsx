import {
  Empty,
  EmptyContent,
  EmptyDescription,
  EmptyHeader,
  EmptyMedia,
  EmptyTitle,
  IenEmpty,
} from "@/components/ui/empty";
import { SearchIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
export default function Preview() {
  return (
    <div className="grid gap-ien-xl">
      <Empty>
        <EmptyHeader>
          <EmptyMedia variant="icon">
            <SearchIcon />
          </EmptyMedia>
          <EmptyTitle>결과가 없습니다</EmptyTitle>
          <EmptyDescription>
            검색어를 바꾸거나 필터를 초기화해 보세요.
          </EmptyDescription>
        </EmptyHeader>
        <EmptyContent>
          <Button variant="line" tone="neutral">
            필터 초기화
          </Button>
        </EmptyContent>
      </Empty>
      <IenEmpty
        icon={<SearchIcon />}
        title="검색 결과가 없습니다"
        content="검색어를 바꾸거나 필터를 초기화해 보세요."
        buttons={
          <Button variant="line" tone="neutral">
            필터 초기화
          </Button>
        }
      />
    </div>
  );
}
