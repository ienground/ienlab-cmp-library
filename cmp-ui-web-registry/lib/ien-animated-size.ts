"use client"

import * as React from "react"
import { refreshIenContinuousCorners } from "@/lib/ien-continuous-corners"

export function useIenAnimatedSize<T extends HTMLElement>(forwardedRef?: React.Ref<T>, enabled = true) {
  return React.useCallback((element: T | null) => {
    const releaseRef = typeof forwardedRef === "function" ? forwardedRef(element) : undefined
    if (forwardedRef && typeof forwardedRef !== "function") forwardedRef.current = element
    const clearRef = () => {
      if (typeof releaseRef === "function") releaseRef()
      else if (typeof forwardedRef === "function") forwardedRef(null)
      else if (forwardedRef) forwardedRef.current = null
    }
    if (!element || !enabled) return clearRef

    let animation: Animation | undefined
    const measure = () => {
      const style = getComputedStyle(element)
      return { width: style.width, height: style.height }
    }
    let previous = measure()
    const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)")
    const update = () => {
      if (animation) return
      const next = measure()
      const from = previous
      previous = next
      if (reducedMotion.matches || (from.width === next.width && from.height === next.height)) return
      const style = getComputedStyle(element)
      const token = style.getPropertyValue("--ien-motion-fast").trim()
      const duration = Number.parseFloat(token) * (token.endsWith("ms") ? 1 : 1000)
      if (!Number.isFinite(duration) || duration <= 0) return
      animation = element.animate([from, next], {
        duration,
        easing: style.getPropertyValue("--ien-motion-standard-easing").trim() || "ease",
      })
      refreshIenContinuousCorners()
      animation.onfinish = () => {
        animation = undefined
        update()
      }
    }
    const observer = new ResizeObserver(update)
    observer.observe(element, { box: "border-box" })
    const onMotionChange = () => {
      animation?.cancel()
      animation = undefined
      previous = measure()
      refreshIenContinuousCorners()
    }
    reducedMotion.addEventListener("change", onMotionChange)

    return () => {
      observer.disconnect()
      animation?.cancel()
      reducedMotion.removeEventListener("change", onMotionChange)
      clearRef()
    }
  }, [forwardedRef, enabled])
}
