"use client"

import { createContext, useContext, useEffect, useMemo } from "react"
import { observeIenContinuousCorners } from "@/lib/ien-continuous-corners"
import type { IenToneProps } from "@/lib/ien-theme"

export const IenThemeContext = createContext<IenToneProps>({})

export function useIenTheme({ tone, useGradient }: IenToneProps): IenToneProps {
  useEffect(() => observeIenContinuousCorners(), [])
  const parent = useContext(IenThemeContext)
  const resolvedTone = tone ?? parent.tone
  const resolvedGradient = useGradient ?? parent.useGradient
  return useMemo(
    () => ({ tone: resolvedTone, useGradient: resolvedGradient }),
    [resolvedTone, resolvedGradient],
  )
}

export function IenContinuousCorners() {
  useEffect(() => observeIenContinuousCorners(), [])
  return null
}
