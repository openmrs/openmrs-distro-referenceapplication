import React from 'react';
import { useTranslation } from 'react-i18next';
import { ArrowLeft } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import styles from './back-to-reports-link.scss';

interface BackToReportsLinkProps {
  /** Route to navigate back to - defaults to the top-level Reports overview page. */
  to?: string;
  /** Label for that route - defaults to "Reports". */
  label?: string;
}

/**
 * Replaces the old flat tab strip that used to sit atop every report page. Now that the Reports
 * overview page and the Stock Reports hub both group reports into clickable category tiles, that
 * strip was duplicate navigation - this is just a way back up one level.
 */
export default function BackToReportsLink({ to = 'labtestreport-reports', label }: BackToReportsLinkProps) {
  const { t } = useTranslation();
  const resolvedLabel = label ?? t('reports', 'Reports');

  return (
    <button type="button" className={styles.backLink} onClick={() => navigate({ to: `\${openmrsSpaBase}/${to}` })}>
      <ArrowLeft size={16} />
      <span>{resolvedLabel}</span>
    </button>
  );
}
