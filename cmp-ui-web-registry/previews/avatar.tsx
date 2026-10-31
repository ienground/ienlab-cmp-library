import {
  Avatar,
  AvatarFallback,
  AvatarImage,
} from "@/components/ui/avatar"

export function Pattern() {
  return (
    <div className="flex items-center gap-4">
      <Avatar>
        <AvatarImage
          src="https://images.unsplash.com/photo-1584308972272-9e4e7685e80f?w=96&h=96&dpr=2&q=80"
          alt="Michael Rodriguez"
        />
        <AvatarFallback>MR</AvatarFallback>
      </Avatar>
      <Avatar radius="sm"><AvatarFallback>SM</AvatarFallback></Avatar>
      <Avatar radius="xs"><AvatarFallback>XS</AvatarFallback></Avatar>
      <Avatar radius="none"><AvatarFallback>각</AvatarFallback></Avatar>
    </div>
  )
}
export default Pattern;
