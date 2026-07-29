import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { Download } from '@carbon/react/icons';
import { downloadCsv, downloadExcel, type ExportSheet } from './export-utils';
import styles from './export-buttons.scss';

interface ExportButtonsProps {
  filenameBase: string;
  mainSheet: ExportSheet;
  extraSheets?: Array<ExportSheet>;
  disabled?: boolean;
}

export default function ExportButtons({ filenameBase, mainSheet, extraSheets = [], disabled }: ExportButtonsProps) {
  const { t } = useTranslation();

  return (
    <div className={styles.exportButtons}>
      <Button
        kind="tertiary"
        size="sm"
        renderIcon={Download}
        disabled={disabled}
        onClick={() => downloadCsv(filenameBase, mainSheet)}
      >
        {t('exportCsv', 'Export CSV')}
      </Button>
      <Button
        kind="tertiary"
        size="sm"
        renderIcon={Download}
        disabled={disabled}
        onClick={() => downloadExcel(filenameBase, [mainSheet, ...extraSheets])}
      >
        {t('exportExcel', 'Export Excel')}
      </Button>
    </div>
  );
}
