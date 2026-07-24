import React from 'react';
import { Calendar, Location } from '@carbon/react/icons';
import { useTranslation } from 'react-i18next';
import {
  formatDate,
  PageHeader,
  PageHeaderContent,
  StockManagementPictogram,
  useSession,
} from '@openmrs/esm-framework';
import styles from './stock-management-header.scss';

export const StockManagementHeader: React.FC = () => {
  const { t } = useTranslation();
  const userSession = useSession();
  const userLocation = userSession?.sessionLocation?.display;

  return (
    <PageHeader className={styles.header} data-testid="stock-management-header">
      <PageHeaderContent illustration={<StockManagementPictogram />} title={t('stockManagement', 'Stock management')} />
      <div className={styles['right-justified-items']}>
        <div className={styles['date-and-location']}>
          <Location size={16} />
          <span className={styles.value}>{userLocation}</span>
          <span className={styles.middot}>&middot;</span>
          <Calendar size={16} />
          <span className={styles.value}>{formatDate(new Date(), { mode: 'standard' })}</span>
        </div>
      </div>
    </PageHeader>
  );
};
