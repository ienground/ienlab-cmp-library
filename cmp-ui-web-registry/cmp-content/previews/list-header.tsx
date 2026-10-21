import {
  IenListHeader,
  IenListHeaderDefaults,
} from "@/components/ui/list-header";
export default function Preview() {
  return (
    <IenListHeader
      title={<IenListHeaderDefaults.Title text="최근 활동" />}
      description={
        <IenListHeaderDefaults.Description text="최근 7일 동안의 활동입니다." />
      }
      right={<button>모두 보기</button>}
    />
  );
}
