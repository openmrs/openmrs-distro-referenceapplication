import React from 'react';
import { Tile } from '@carbon/react';
import styles from './kpi-tiles.scss';

export interface KpiTileDatum {
  label: string;
  value: React.ReactNode;
}

interface KpiTilesProps {
  items: Array<KpiTileDatum>;
}

export default function KpiTiles({ items }: KpiTilesProps) {
  return (
    <div className={styles.kpiGrid}>
      {items.map((item) => (
        <Tile key={item.label} className={styles.kpiTile}>
          <p className={styles.kpiLabel}>{item.label}</p>
          <p className={styles.kpiValue}>{item.value}</p>
        </Tile>
      ))}
    </div>
  );
}
