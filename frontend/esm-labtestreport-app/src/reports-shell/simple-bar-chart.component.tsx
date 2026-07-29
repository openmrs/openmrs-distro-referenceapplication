import React from 'react';
import { useTranslation } from 'react-i18next';
import styles from './simple-bar-chart.scss';

export interface BarChartDatum {
  label: string;
  value: number;
  compareValue?: number;
}

interface SimpleBarChartProps {
  data: Array<BarChartDatum>;
  emptyMessage?: string;
  currentLabel?: string;
  compareLabel?: string;
}

export default function SimpleBarChart({ data, emptyMessage, currentLabel, compareLabel }: SimpleBarChartProps) {
  const { t } = useTranslation();
  const resolvedEmptyMessage = emptyMessage ?? t('noDataToDisplay', 'No data to display.');
  const resolvedCurrentLabel = currentLabel ?? t('currentPeriod', 'Current period');
  const resolvedCompareLabel = compareLabel ?? t('comparisonPeriod', 'Comparison period');

  if (data.length === 0) {
    return <p className={styles.emptyState}>{resolvedEmptyMessage}</p>;
  }

  const hasComparison = data.some((datum) => datum.compareValue !== undefined);
  const maxValue = Math.max(...data.map((datum) => Math.max(datum.value, datum.compareValue ?? 0)), 1);

  return (
    <div className={styles.chartContainer}>
      {hasComparison && (
        <div className={styles.legend}>
          <span className={styles.legendItem}>
            <span className={`${styles.legendSwatch} ${styles.legendSwatchCurrent}`} />
            {resolvedCurrentLabel}
          </span>
          <span className={styles.legendItem}>
            <span className={`${styles.legendSwatch} ${styles.legendSwatchCompare}`} />
            {resolvedCompareLabel}
          </span>
        </div>
      )}
      {data.map((datum) => (
        <div className={styles.barGroup} key={datum.label}>
          <span className={styles.barLabel} title={datum.label}>
            {datum.label}
          </span>
          <div className={styles.barTracks}>
            <div className={styles.barRow}>
              <div className={styles.barTrack}>
                <div className={styles.barFill} style={{ width: `${(datum.value / maxValue) * 100}%` }} />
              </div>
              <span className={styles.barValue}>{datum.value}</span>
            </div>
            {datum.compareValue !== undefined && (
              <div className={styles.barRow}>
                <div className={styles.barTrack}>
                  <div
                    className={`${styles.barFill} ${styles.barFillCompare}`}
                    style={{ width: `${(datum.compareValue / maxValue) * 100}%` }}
                  />
                </div>
                <span className={styles.barValue}>{datum.compareValue}</span>
              </div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
