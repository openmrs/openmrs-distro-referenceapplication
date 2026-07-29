import React from 'react';
import { useTranslation } from 'react-i18next';
import { Toggle } from '@carbon/react';
import pageStyles from './reports-page.scss';
import styles from './month-compare-controls.scss';
import type { MonthComparison } from './month-compare';

export default function MonthCompareControls({
  enabled,
  setEnabled,
  primaryMonth,
  setPrimaryMonth,
  comparisonMonth,
  setComparisonMonth,
  primary,
  comparison,
}: MonthComparison) {
  const { t } = useTranslation();

  return (
    <div className={pageStyles.filterTile}>
      <div className={styles.toggleField}>
        <Toggle
          id="compare-months-toggle"
          labelText={t('compareWithAnotherMonth', 'Compare with another month')}
          labelA={t('off', 'Off')}
          labelB={t('on', 'On')}
          toggled={enabled}
          onToggle={setEnabled}
          size="sm"
        />
      </div>
      {enabled && (
        <>
          <div className={pageStyles.filterField}>
            <label htmlFor="primaryMonth">{t('currentPeriodWithLabel', 'Current period ({{label}})', { label: primary.label })}</label>
            <input
              id="primaryMonth"
              type="month"
              value={primaryMonth}
              onChange={(e) => setPrimaryMonth(e.target.value)}
            />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="comparisonMonth">
              {t('compareToWithLabel', 'Compare to ({{label}})', { label: comparison.label })}
            </label>
            <input
              id="comparisonMonth"
              type="month"
              value={comparisonMonth}
              onChange={(e) => setComparisonMonth(e.target.value)}
            />
          </div>
        </>
      )}
    </div>
  );
}
