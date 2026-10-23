"use client"

import { createContext, useContext, useMemo } from "react"
import type { IenToneProps } from "@/lib/ien-theme"

export const IenThemeContext = createContext<IenToneProps>({})

export function useIenTheme({ tone, useGradient }: IenToneProps): IenToneProps {
  const parent = useContext(IenThemeContext)
  const resolvedTone = tone ?? parent.tone
  const resolvedGradient = useGradient ?? parent.useGradient
  return useMemo(
    () => ({ tone: resolvedTone, useGradient: resolvedGradient }),
    [resolvedTone, resolvedGradient],
  )
}
