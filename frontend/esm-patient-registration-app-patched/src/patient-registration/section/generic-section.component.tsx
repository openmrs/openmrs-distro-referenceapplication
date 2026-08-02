import React from 'react';
import { type SectionDefinition } from '../../config-schema';
import { Field } from '../field/field.component';

export interface GenericSectionProps {
  sectionDefinition: SectionDefinition;
}

export const GenericSection = ({ sectionDefinition }: GenericSectionProps) => {
  return (
    <section aria-label={`${sectionDefinition.name} Section`}>
      {sectionDefinition.fields.map((name) => (
        <Field key={`${sectionDefinition.name}-${name}`} name={name} />
      ))}
    </section>
  );
};
