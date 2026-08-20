import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Grid, Column, InlineLoading, Tag } from '@carbon/react';
import { WarningAltFilled, CheckmarkFilled } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import { getDateDaysAgoString, getTodayDateString } from '../reports-shell/date-utils';
import { useStockReorderReport } from '../stock-reorder/stock-reorder.resource';
import { useStockExpiryRiskReport } from '../stock-expiry-risk/stock-expiry-risk.resource';
import { useStockDaysRemainingReport } from '../stock-days-remaining/stock-days-remaining.resource';
import { useStockoutFrequencyReport } from '../stock-stockout-frequency/stock-stockout-frequency.resource';
import pageStyles from '../reports-shell/reports-page.scss';
import styles from './stock-reports-home.scss';

const LOW_DAYS_THRESHOLD = 14;

interface StockReportTile {
  key: string;
  title: string;
  description: string;
  route?: string;
  alertCount?: number;
  alertLoading?: boolean;
}

/**
 * Landing page for the "Stock Ledger" tab: a category grid of every stock report, live and
 * planned, so growing the stock report set doesn't mean growing the top-level tab bar (which
 * would overflow) - new stock reports get a tile here instead of a new tab. Tiles with no
 * `route` aren't built yet and render as disabled, so the roadmap is visible without pretending
 * they're clickable.
 */
export default function StockReportsHome() {
  const { t } = useTranslation();

  const { rows: reorderRows, isLoading: reorderLoading } = useStockReorderReport();
  const { rows: expiryRows, isLoading: expiryLoading } = useStockExpiryRiskReport(30, undefined);
  const { rows: daysRemainingRows, isLoading: daysRemainingLoading } = useStockDaysRemainingReport();
  const { rows: stockoutRows, isLoading: stockoutLoading } = useStockoutFrequencyReport(
    getDateDaysAgoString(29),
    getTodayDateString(),
  );

  const daysRemainingLowCount = useMemo(
    () => daysRemainingRows.filter((row) => row.daysRemaining !== null && row.daysRemaining <= LOW_DAYS_THRESHOLD).length,
    [daysRemainingRows],
  );
  const stockoutCount = useMemo(() => stockoutRows.filter((row) => row.stockoutDays > 0).length, [stockoutRows]);

  const tiles: Array<StockReportTile> = [
    {
      key: 'ledger',
      title: t('stockLedgerReportTitle', 'Stock Inventory Ledger Report'),
      description: t('stockLedgerReportTileDesc', 'Daily opening, incoming, outgoing and closing stock balances per item.'),
      route: 'stock-ledger-report',
    },
    {
      key: 'consumption',
      title: t('stockConsumptionReportTitle', 'Stock Consumption by Location Report'),
      description: t(
        'stockConsumptionReportTileDesc',
        'How much of each item each location has issued/consumed, with month-over-month comparison to see which items are trending up.',
      ),
      route: 'stock-consumption-report',
    },
    {
      key: 'distribution',
      title: t('stockDistributionReportTitle', 'Stock Distribution Report'),
      description: t(
        'stockDistributionReportTileDesc',
        'How much of each item was transferred from a source location (e.g. Main Store) out to each destination.',
      ),
      route: 'stock-distribution-report',
    },
    {
      key: 'days-of-stock',
      title: t('daysOfStockReportTitle', 'Days of Stock Remaining'),
      description: t(
        'daysOfStockReportTileDesc',
        'Current quantity divided by average daily consumption, per item per location - flags locations about to run out.',
      ),
      route: 'stock-days-remaining-report',
      alertCount: daysRemainingLowCount,
      alertLoading: daysRemainingLoading,
    },
    {
      key: 'reorder',
      title: t('reorderReportTitle', 'Reorder / Low-Stock Report'),
      description: t('reorderReportTileDesc', 'Items currently below their reorder level at each location.'),
      route: 'stock-reorder-report',
      alertCount: reorderRows.length,
      alertLoading: reorderLoading,
    },
    {
      key: 'stockout-frequency',
      title: t('stockoutFrequencyReportTitle', 'Stockout Frequency Report'),
      description: t(
        'stockoutFrequencyReportTileDesc',
        'How often, and for how long, each item sat at zero stock per location.',
      ),
      route: 'stock-stockout-frequency-report',
      alertCount: stockoutCount,
      alertLoading: stockoutLoading,
    },
    {
      key: 'expiry-risk',
      title: t('expiryRiskReportTitle', 'Expiry Risk by Location'),
      description: t('expiryRiskReportTileDesc', 'Batches nearing expiry, broken down by location.'),
      route: 'stock-expiry-risk-report',
      alertCount: expiryRows.length,
      alertLoading: expiryLoading,
    },
    {
      key: 'wastage',
      title: t('wastageReportTitle', 'Wastage / Disposal Report'),
      description: t(
        'wastageReportTileDesc',
        'Quantity and value disposed (expired/damaged) per location per item.',
      ),
      route: 'stock-wastage-report',
    },
  ];

  return (
    <div>
      <BackToReportsLink />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockReports', 'Stock Reports')}</h2>
        <Grid className={styles.tileGrid}>
          {tiles.map((tile) => (
            <Column sm={4} md={4} lg={5} key={tile.key}>
              <ClickableTile
                className={styles.tile}
                disabled={!tile.route}
                onClick={tile.route ? () => navigate({ to: `\${openmrsSpaBase}/${tile.route}` }) : undefined}
              >
                <div className={styles.tileTitle}>
                  {tile.title}
                  {!tile.route && (
                    <Tag type="gray" size="sm" className={styles.tileBadge}>
                      {t('comingSoon', 'Coming soon')}
                    </Tag>
                  )}
                  {tile.route && tile.alertLoading && <InlineLoading />}
                  {tile.route && !tile.alertLoading && tile.alertCount !== undefined && tile.alertCount > 0 && (
                    <Tag type="red" size="sm" renderIcon={WarningAltFilled} className={styles.tileBadge}>
                      {t('nNeedAttention', '{{count}} need attention', { count: tile.alertCount })}
                    </Tag>
                  )}
                  {tile.route && !tile.alertLoading && tile.alertCount === 0 && (
                    <Tag type="green" size="sm" renderIcon={CheckmarkFilled} className={styles.tileBadge}>
                      {t('allClear', 'All clear')}
                    </Tag>
                  )}
                </div>
                <p className={styles.tileDescription}>{tile.description}</p>
              </ClickableTile>
            </Column>
          ))}
        </Grid>
      </div>
    </div>
  );
}
