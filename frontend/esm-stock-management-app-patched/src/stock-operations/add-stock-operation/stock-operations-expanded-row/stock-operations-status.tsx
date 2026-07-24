import { formatDate, parseDate } from '@openmrs/esm-framework';
import React from 'react';
import { useTranslation } from 'react-i18next';
import styles from './stock-operation-expanded-row.scss';

type Props = {
  status: string;
  statusFilledDate: string;
  statusFillerGivenName?: string;
  statusFillerFamilyName?: string;
  extraStatusinfo?: React.ReactNode;
};

const StockOpertationStatus: React.FC<Props> = ({
  status,
  statusFillerFamilyName,
  statusFillerGivenName,
  statusFilledDate,
  extraStatusinfo,
}) => {
  const { t } = useTranslation();
  return (
    <div>
      <span className={styles.textHeading}>{status}:</span>
      <div className={styles.statusDescriptions}>
        <span className={styles.text}>
          {formatDate(parseDate(statusFilledDate), {
            time: true,
            mode: 'standard',
          })}
        </span>

        <span className={styles.text}>{t('by', 'By')}</span>

        <span className={styles.text}>
          {statusFillerFamilyName} &nbsp;
          {statusFillerGivenName}
        </span>
        {extraStatusinfo}
      </div>
    </div>
  );
};

export default StockOpertationStatus;
