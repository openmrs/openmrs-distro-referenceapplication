import React from 'react';
import { ArrowUp, ArrowDown } from '@carbon/react/icons';
import styles from './sparkline.scss';

interface SparklineProps {
  values: Array<number>;
  width?: number;
  height?: number;
}

export const Sparkline: React.FC<SparklineProps> = ({ values, width = 64, height = 24 }) => {
  if (!values || values.length < 2) {
    return null;
  }

  const max = Math.max(...values, 1);
  const min = Math.min(...values, 0);
  const range = max - min || 1;
  const stepX = width / (values.length - 1);

  const points = values
    .map((value, index) => {
      const x = index * stepX;
      const y = height - ((value - min) / range) * height;
      return `${x.toFixed(1)},${y.toFixed(1)}`;
    })
    .join(' ');

  return (
    <svg className={styles.sparkline} width={width} height={height} viewBox={`0 0 ${width} ${height}`}>
      <polyline points={points} fill="none" strokeWidth="1.5" className={styles.sparklineLine} />
    </svg>
  );
};

interface TrendIndicatorProps {
  delta: number | null | undefined;
  label: string;
  /** When true, an increase is shown as favorable (green) rather than unfavorable (red). */
  higherIsBetter?: boolean;
}

export const TrendIndicator: React.FC<TrendIndicatorProps> = ({ delta, label, higherIsBetter = false }) => {
  if (delta === null || delta === undefined || delta === 0) {
    return null;
  }

  const isIncrease = delta > 0;
  const isFavorable = higherIsBetter ? isIncrease : !isIncrease;
  const Icon = isIncrease ? ArrowUp : ArrowDown;

  return (
    <span
      className={isFavorable ? styles.trendFavorable : styles.trendUnfavorable}
      title={`${isIncrease ? '+' : ''}${delta} ${label}`}
    >
      <Icon size={14} />
      {Math.abs(delta)}
    </span>
  );
};

export default Sparkline;
