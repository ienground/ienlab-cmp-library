import {
  Avatar,
  AvatarFallback,
  AvatarImage,
  AvatarGroup,
  AvatarGroupCount,
} from "@/components/ui/avatar";
export default function Preview() {
  return (
    <AvatarGroup>
      <Avatar>
        <AvatarImage
          src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=80&h=80&fit=crop&crop=faces"
          alt="프로필"
        />
        <AvatarFallback>김</AvatarFallback>
      </Avatar>
      <Avatar>
        <AvatarFallback>이</AvatarFallback>
      </Avatar>
      <AvatarGroupCount>+3</AvatarGroupCount>
    </AvatarGroup>
  );
}
