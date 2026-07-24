import React from 'react';
import { useTranslation } from 'react-i18next';
import { Layer, Tile } from '@carbon/react';
import { useLayoutType } from '@openmrs/esm-framework';
import StockHomeInventoryCard from './stock-home-inventory-card.component';
import StockHomeIssuingCard from './stock-home-issuing-card.component';
import StockHomeReceivingCard from './stock-home-receiving-card.component';
import styles from './stock-home-detail-card.scss';

interface DetailCardProps {
  title: string;
  children: React.ReactNode;
}

const DetailCard: React.FC<DetailCardProps> = ({ title, children }) => {
  const isTablet = useLayoutType() === 'tablet';
  const responsiveStyles = isTablet ? styles.tabletHeading : styles.desktopHeading;

  return (
    <div className={styles.tilesContainer}>
      <Layer>
        <Tile>
          <div className={responsiveStyles}>
            <h4>{title}</h4>
          </div>
          {children}
        </Tile>
      </Layer>
    </div>
  );
};

const StockHomeDetailCards: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className={styles.cardContainer}>
      <DetailCard title={t('inventoryAlerts', 'Inventory alerts')}>
        <StockHomeInventoryCard />
      </DetailCard>
      <DetailCard title={t('receiving', 'Receiving')}>
        <StockHomeReceivingCard />
      </DetailCard>
      <DetailCard title={t('issuing', 'Issuing')}>
        <StockHomeIssuingCard />
      </DetailCard>
    </div>
  );
};

export default StockHomeDetailCards;
