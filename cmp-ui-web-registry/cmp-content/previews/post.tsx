import { IenPost } from "@/components/ui/post";
export default function Preview() {
  return (
    <IenPost
      title="디자인 시스템 업데이트"
      author="디자인 팀"
      description="공통 컴포넌트와 토큰을 정리했습니다."
      metadata={<span>방금 전</span>}
      media={
        <div className="grid aspect-video place-items-center bg-ien-brand-weak text-ien-brand">
          미디어 미리보기
        </div>
      }
      action={<button>자세히 보기</button>}
    />
  );
}
