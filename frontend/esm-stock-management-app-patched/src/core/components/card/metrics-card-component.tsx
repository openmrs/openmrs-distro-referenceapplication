import React from 'react';
import dayjs from 'dayjs';
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore';
import { useTranslation } from 'react-i18next';
import { isEmpty } from 'lodash-es';
import { Tile } from '@carbon/react';
import Sparkline, { TrendIndicator } from '../sparkline/sparkline.component';
import styles from './metrics-card.scss';

dayjs.extend(isSameOrBefore);

interface MetricsCardProps {
  label: string;
  value: number;
  headerLabel: string;
  children?: React.ReactNode;
  count?: { expiry6months: Array<any> };
  // No max-stock-level field exists anywhere in the stock item data model, so there's
  // no honest way to compute an "overstocked" count - only understocked is shown here.
  outOfStockCount?: { itemsBelowMin: Array<any> };
  disposedCount?: { expired: Array<any>; poorQuality: Array<any> };
  onClick?: () => void;
  /** Change in `value` versus 7 days ago, when available. */
  trend?: number | null;
  /** Weekly historical values (oldest first) used to draw the mini trend line. */
  sparklineValues?: Array<number>;
}
const MetricsCard: React.FC<MetricsCardProps> = ({
  label,
  value,
  headerLabel,
  children,
  count,
  outOfStockCount,
  disposedCount,
  onClick,
  trend,
  sparklineValues,
}) => {
  const { t } = useTranslation();

  return (
    <Tile
      className={onClick ? `${styles.tileContainer} ${styles.clickableTile}` : styles.tileContainer}
      onClick={onClick}
      role={onClick ? 'button' : undefined}
      tabIndex={onClick ? 0 : undefined}
      onKeyDown={
        onClick
          ? (e) => {
              if (e.key === 'Enter' || e.key === ' ') onClick();
            }
          : undefined
      }
    >
      <div className={styles.tileHeader}>
        <div className={styles.headerLabelContainer}>
          <label className={styles.headerLabel}>{headerLabel}</label>
          {sparklineValues && sparklineValues.length > 1 && <Sparkline values={sparklineValues} />}
          {children}
        </div>
      </div>
      <div className={styles.metricsGrid}>
        <div>
          <label className={styles.totalsLabel}>{label}</label>
          <div className={styles.totalsRow}>
            <p className={styles.totalsValue}>{value}</p>
            <TrendIndicator delta={trend} label={t('vsLastWeek', 'vs last week')} />
          </div>
        </div>
        {!isEmpty(count) && (
          <div className={styles.countGrid}>
            <span />
            <span className={styles.in6MonthsLabel}>{t('in6Months', 'In 6 months')}</span>
            <p />
            <p className={styles.in6MonthsValue}>{count.expiry6months?.length}</p>
          </div>
        )}
        {!isEmpty(outOfStockCount) && (
          <div className={styles.countGrid}>
            <span />
            <span className={styles.belowMinLabel}>{t('understockedItems', 'Understocked items')}</span>
            <p />
            <p className={styles.belowMinValue}>{outOfStockCount.itemsBelowMin?.length}</p>
          </div>
        )}
        {!isEmpty(disposedCount) && (
          <div className={styles.countGrid}>
            <span className={styles.expiredLabel}>{t('expired', 'Expired')}</span>
            <span className={styles.poorQualityLabel}>{t('poorQuality', 'Poor Quality')}</span>
            <p className={styles.expiredValue}>{disposedCount.expired?.length}</p>
            <p className={styles.poorQualityValue}>{disposedCount.poorQuality?.length}</p>
          </div>
        )}
      </div>
    </Tile>
  );
};
export default MetricsCard;
