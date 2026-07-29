import React from 'react';
import { useTranslation } from 'react-i18next';
import { Tile } from '@carbon/react';
import { ArrowUp, ArrowDown } from '@carbon/react/icons';
import styles from './kpi-tiles.scss';

export interface KpiTileDatum {
  label: string;
  value: React.ReactNode;
  compareValue?: React.ReactNode;
  compareLabel?: string;
}

interface KpiTilesProps {
  items: Array<KpiTileDatum>;
}

function CompareLine({ value, compareValue, compareLabel }: Required<Pick<KpiTileDatum, 'value' | 'compareValue'>> & {
  compareLabel?: string;
}) {
  const { t } = useTranslation();
  const suffix = compareLabel
    ? t('vsCompareLabel', 'vs {{compareLabel}}', { compareLabel })
    : t('vsComparisonPeriod', 'vs comparison period');

  if (typeof value !== 'number' || typeof compareValue !== 'number') {
    return (
      <p className={styles.kpiCompare}>
        {suffix}: {compareValue}
      </p>
    );
  }

  const delta = value - compareValue;
  const percent = compareValue !== 0 ? (delta / Math.abs(compareValue)) * 100 : null;
  const direction = delta > 0 ? 'up' : delta < 0 ? 'down' : 'flat';
  const sign = delta > 0 ? '+' : '';

  return (
    <p className={`${styles.kpiCompare} ${styles[`kpiCompare-${direction}`]}`}>
      {direction === 'up' && <ArrowUp size={14} />}
      {direction === 'down' && <ArrowDown size={14} />}
      {sign}
      {delta}
      {percent !== null && ` (${sign}${percent.toFixed(1)}%)`} {suffix}
    </p>
  );
}

export default function KpiTiles({ items }: KpiTilesProps) {
  return (
    <div className={styles.kpiGrid}>
      {items.map((item) => (
        <Tile key={item.label} className={styles.kpiTile}>
          <p className={styles.kpiLabel}>{item.label}</p>
          <p className={styles.kpiValue}>{item.value}</p>
          {item.compareValue !== undefined && (
            <CompareLine value={item.value} compareValue={item.compareValue} compareLabel={item.compareLabel} />
          )}
        </Tile>
      ))}
    </div>
  );
}
