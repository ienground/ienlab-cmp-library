import { Bar, BarChart, CartesianGrid, XAxis } from "recharts";
import { ChartContainer, type ChartConfig } from "@/components/ui/chart";
const config: ChartConfig = {
  visits: { label: "방문", color: "var(--ien-brand)" },
};
export default function Preview() {
  return (
    <ChartContainer config={config} className="min-h-48 w-full">
      <BarChart
        accessibilityLayer
        data={[
          { day: "월", visits: 12 },
          { day: "화", visits: 19 },
          { day: "수", visits: 15 },
        ]}
      >
        <CartesianGrid vertical={false} stroke="var(--ien-border)" />
        <XAxis dataKey="day" tickLine={false} axisLine={false} />
        <Bar dataKey="visits" fill="var(--color-visits)" radius={4} />
      </BarChart>
    </ChartContainer>
  );
}
