import React from 'react';
import { useTranslation } from 'react-i18next';
import styles from './simple-line-chart.scss';

export interface LineChartPoint {
  date: string;
  value: number;
}

interface SimpleLineChartProps {
  data: Array<LineChartPoint>;
  emptyMessage?: string;
}

const WIDTH = 720;
const HEIGHT = 220;
const PADDING_LEFT = 48;
const PADDING_RIGHT = 16;
const PADDING_TOP = 16;
const PADDING_BOTTOM = 32;

export default function SimpleLineChart({ data, emptyMessage }: SimpleLineChartProps) {
  const { t } = useTranslation();
  const resolvedEmptyMessage = emptyMessage ?? t('noDataToDisplay', 'No data to display.');

  if (data.length === 0) {
    return <p className={styles.emptyState}>{resolvedEmptyMessage}</p>;
  }

  const plotWidth = WIDTH - PADDING_LEFT - PADDING_RIGHT;
  const plotHeight = HEIGHT - PADDING_TOP - PADDING_BOTTOM;
  const maxValue = Math.max(...data.map((d) => d.value), 1);
  const minValue = Math.min(...data.map((d) => d.value), 0);
  const valueRange = maxValue - minValue || 1;

  const xFor = (index: number) => (data.length === 1 ? plotWidth / 2 : (index / (data.length - 1)) * plotWidth);
  const yFor = (value: number) => plotHeight - ((value - minValue) / valueRange) * plotHeight;

  const points = data.map((d, i) => `${PADDING_LEFT + xFor(i)},${PADDING_TOP + yFor(d.value)}`).join(' ');

  return (
    <div className={styles.chartContainer}>
      <svg viewBox={`0 0 ${WIDTH} ${HEIGHT}`} className={styles.svg} role="img">
        <line
          x1={PADDING_LEFT}
          y1={PADDING_TOP + plotHeight}
          x2={WIDTH - PADDING_RIGHT}
          y2={PADDING_TOP + plotHeight}
          className={styles.axisLine}
        />
        <text x={4} y={PADDING_TOP + yFor(maxValue) + 4} className={styles.axisLabel}>
          {maxValue}
        </text>
        <text x={4} y={PADDING_TOP + yFor(minValue) + 4} className={styles.axisLabel}>
          {minValue}
        </text>
        <polyline points={points} className={styles.linePath} />
        {data.map((d, i) => (
          <circle key={d.date} cx={PADDING_LEFT + xFor(i)} cy={PADDING_TOP + yFor(d.value)} r={3} className={styles.dot}>
            <title>{`${d.date}: ${d.value}`}</title>
          </circle>
        ))}
        <text x={PADDING_LEFT} y={HEIGHT - 8} className={styles.axisLabel}>
          {data[0].date}
        </text>
        <text x={WIDTH - PADDING_RIGHT} y={HEIGHT - 8} className={styles.axisLabel} textAnchor="end">
          {data[data.length - 1].date}
        </text>
      </svg>
    </div>
  );
}
