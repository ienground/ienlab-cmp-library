/** 브라우저 스크롤과 분리한 당김 제스처 상태입니다. */
export class PullToRefreshGesture {
  private startX = 0
  private startY = 0
  private tracking = false
  dragging = false
  distance = 0
  readonly threshold: number

  constructor(threshold: number) {
    if (!Number.isFinite(threshold) || threshold <= 0) {
      throw new RangeError("threshold는 유한한 양수여야 합니다.")
    }
    this.threshold = threshold
  }

  start(x: number, y: number, allowed: boolean) {
    this.cancel()
    this.startX = x
    this.startY = y
    this.tracking = allowed
  }

  move(x: number, y: number) {
    if (!this.tracking) return 0
    const dx = Math.abs(x - this.startX)
    const dy = y - this.startY
    if (!this.dragging) {
      if (Math.max(dx, Math.abs(dy)) < 6) return 0
      if (dy <= 0 || dx >= dy) {
        this.cancel()
        return 0
      }
      this.dragging = true
    }
    this.distance = Math.min(Math.max(96, this.threshold), Math.max(0, dy) * 0.65)
    return this.distance
  }

  finish() {
    const refresh = this.dragging && this.distance >= this.threshold
    this.cancel()
    return refresh
  }

  wheel(deltaY: number) {
    this.distance = Math.min(Math.max(96, this.threshold), Math.max(0, this.distance - deltaY * 0.65))
    this.tracking = this.distance > 0
    this.dragging = this.tracking
    return this.distance
  }

  cancel() {
    this.tracking = false
    this.dragging = false
    this.distance = 0
  }
}
