import React from 'react';
import { Tile } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { type SectionDefinition } from '../../config-schema';
import { Section } from './section.component';
import styles from './section.scss';

export interface SectionWrapperProps {
  sectionDefinition: SectionDefinition;
  index: number;
}

export const SectionWrapper = ({ sectionDefinition, index }: SectionWrapperProps) => {
  const { t } = useTranslation();

  /*
   * This comment exists to provide translation keys for the default section names.
   *
   * DO NOT REMOVE THESE UNLESS A DEFAULT SECTION IS REMOVED
   * t('demographicsSection', 'Basic Info')
   * t('contactSection', 'Contact Details')
   * t('deathSection', 'Death Info')
   * t('relationshipsSection', 'Relationships')
   */
  return (
    <div id={sectionDefinition.id} style={{ scrollMarginTop: '1rem' }}>
      <h3 className={styles.productiveHeading02} style={{ color: '#161616' }}>
        {index + 1}. {t(`${sectionDefinition.id}Section`, sectionDefinition.name)}
      </h3>
      <span className={styles.label01}>
        {t('allFieldsRequiredText', 'All fields are required unless marked optional')}
      </span>
      <div style={{ margin: '1rem 0 1rem' }}>
        <Tile>
          <Section sectionDefinition={sectionDefinition} />
        </Tile>
      </div>
    </div>
  );
};
