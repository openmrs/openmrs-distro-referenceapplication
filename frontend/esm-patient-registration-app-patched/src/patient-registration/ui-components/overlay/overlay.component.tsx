import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button, Header } from '@carbon/react';
import { ArrowLeft, Close } from '@carbon/react/icons';
import { useLayoutType, isDesktop } from '@openmrs/esm-framework';
import styles from './overlay.scss';

interface OverlayProps {
  close: () => void;
  header: string;
  buttonsGroup?: React.ReactElement;
  children?: React.ReactNode;
}

const Overlay: React.FC<OverlayProps> = ({ close, children, header, buttonsGroup }) => {
  const { t } = useTranslation();
  const layout = useLayoutType();

  return (
    <div className={isDesktop(layout) ? styles.desktopOverlay : styles.tabletOverlay}>
      {isDesktop(layout) ? (
        <div className={styles.desktopHeader}>
          <div className={styles.headerContent}>{header}</div>
          <Button
            className={styles.closeButton}
            iconDescription={t('closeOverlay', 'Close overlay')}
            onClick={close}
            kind="ghost"
            hasIconOnly
            renderIcon={(props) => <Close size={16} {...props} />}
          />
        </div>
      ) : (
        <Header className={styles.tabletOverlayHeader} aria-label="Overlay header">
          <Button
            kind="ghost"
            onClick={close}
            hasIconOnly
            iconDescription={t('closeOverlay', 'Close overlay')}
            renderIcon={(props) => <ArrowLeft size={16} onClick={close} {...props} />}
          />
          <div className={styles.headerContent}>{header}</div>
        </Header>
      )}
      <div className={styles.overlayContent}>{children}</div>
      <div>{buttonsGroup}</div>
    </div>
  );
};

export default Overlay;
