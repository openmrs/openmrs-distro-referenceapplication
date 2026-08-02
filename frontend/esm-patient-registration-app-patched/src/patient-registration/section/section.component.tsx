import React from 'react';
import { type SectionDefinition } from '../../config-schema';
import { GenericSection } from './generic-section.component';
import { DeathInfoSection } from './death-info/death-info-section.component';
import { DemographicsSection } from './demographics/demographics-section.component';
import { RelationshipsSection } from './patient-relationships/relationships-section.component';

export interface SectionProps {
  sectionDefinition: SectionDefinition;
}

export function Section({ sectionDefinition }: SectionProps) {
  switch (sectionDefinition.id) {
    case 'demographics':
      return <DemographicsSection fields={sectionDefinition.fields} />;
    case 'death':
      return <DeathInfoSection fields={sectionDefinition.fields} />;
    case 'relationships':
      return <RelationshipsSection />;
    default: // includes 'contact'
      return <GenericSection sectionDefinition={sectionDefinition} />;
  }
}
