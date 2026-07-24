import React from 'react';
import styles from './simple-bar-chart.scss';

export interface BarChartDatum {
  label: string;
  value: number;
}

interface SimpleBarChartProps {
  data: Array<BarChartDatum>;
  emptyMessage?: string;
}

export default function SimpleBarChart({ data, emptyMessage = 'No data to display.' }: SimpleBarChartProps) {
  if (data.length === 0) {
    return <p className={styles.emptyState}>{emptyMessage}</p>;
  }

  const maxValue = Math.max(...data.map((datum) => datum.value), 1);

  return (
    <div className={styles.chartContainer}>
      {data.map((datum) => (
        <div className={styles.barRow} key={datum.label}>
          <span className={styles.barLabel} title={datum.label}>
            {datum.label}
          </span>
          <div className={styles.barTrack}>
            <div className={styles.barFill} style={{ width: `${(datum.value / maxValue) * 100}%` }} />
          </div>
          <span className={styles.barValue}>{datum.value}</span>
        </div>
      ))}
    </div>
  );
}
