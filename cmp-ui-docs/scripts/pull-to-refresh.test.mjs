import test from "node:test";
import assert from "node:assert/strict";
import { PullToRefreshGesture } from "../../cmp-ui-web-registry/lib/ien-pull-to-refresh.ts";

test("맨 위에서 시작한 세로 당김이 기준을 넘겨야 갱신한다", () => {
  const gesture = new PullToRefreshGesture(56);
  gesture.start(100, 100, false);
  assert.equal(gesture.move(100, 240), 0);
  assert.equal(gesture.finish(), false);
  gesture.start(100, 100, true);
  assert.equal(gesture.move(100, 180), 52);
  assert.equal(gesture.finish(), false);
  gesture.start(100, 100, true);
  assert.equal(gesture.move(100, 200), 65);
  assert.equal(gesture.finish(), true);
  assert.equal(gesture.finish(), false);
});

test("가로 이동과 위쪽 스크롤은 이후 아래로 움직여도 당김으로 전환하지 않는다", () => {
  const gesture = new PullToRefreshGesture(56);
  for (const point of [[140, 105], [100, 80]]) {
    gesture.start(100, 100, true);
    assert.equal(gesture.move(...point), 0);
    assert.equal(gesture.move(100, 300), 0);
    assert.equal(gesture.finish(), false);
  }
});

test("최대 거리를 제한하고 되돌아오거나 취소하면 갱신하지 않는다", () => {
  const gesture = new PullToRefreshGesture(56);
  gesture.start(0, 0, true);
  assert.equal(gesture.move(0, 1000), 96);
  assert.equal(gesture.move(0, 20), 13);
  assert.equal(gesture.finish(), false);
  gesture.start(0, 0, true);
  gesture.move(0, 1000);
  gesture.cancel();
  assert.equal(gesture.distance, 0);
  assert.equal(gesture.finish(), false);
});

test("작은 손떨림은 스크롤을 가로채지 않고 사용자 지정 기준도 도달할 수 있다", () => {
  const gesture = new PullToRefreshGesture(120);
  gesture.start(0, 0, true);
  assert.equal(gesture.move(1, 4), 0);
  assert.equal(gesture.dragging, false);
  assert.equal(gesture.move(0, 1000), 120);
  assert.equal(gesture.finish(), true);
  for (const threshold of [0, -1, NaN, Infinity]) {
    assert.throws(() => new PullToRefreshGesture(threshold), RangeError);
  }
});

test("상단의 위쪽 오버스크롤을 누적하고 반대 방향으로 되돌릴 수 있다", () => {
  const gesture = new PullToRefreshGesture(56);
  assert.equal(gesture.wheel(-40), 26);
  assert.equal(gesture.wheel(-40), 52);
  assert.equal(gesture.finish(), false);
  assert.equal(gesture.wheel(-100), 65);
  assert.equal(gesture.finish(), true);
  gesture.wheel(-1000);
  assert.equal(gesture.distance, 96);
  assert.equal(gesture.wheel(80), 44);
  assert.equal(gesture.finish(), false);
  assert.equal(gesture.wheel(100), 0);
  assert.equal(gesture.dragging, false);
  gesture.wheel(-100);
  gesture.cancel();
  assert.equal(gesture.wheel(-40), 26);
  assert.equal(gesture.finish(), false);
});
