import React from 'react';
import { vi, describe, it, expect, beforeEach, type Mock } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { type FieldValues, useForm } from 'react-hook-form';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { useConcepts } from '../../../stock-lookups/stock-lookups.resource';
import ConceptsSelector from './concepts-selector.component';

vi.mock('../../../stock-lookups/stock-lookups.resource', () => ({
  useConcepts: vi.fn(),
}));

const mockUseConcepts = vi.mocked(useConcepts);

const mockConcepts: Array<Partial<Concept>> = [
  { uuid: 'concept-1', display: 'Bandage' },
  { uuid: 'concept-2', display: 'Syringe' },
  { uuid: 'concept-3', display: 'Gloves' },
] as Array<Partial<Concept>>;

function ConceptsSelectorWrapper({
  defaultConceptUuid = '',
  onConceptUuidChange,
}: {
  defaultConceptUuid?: string;
  onConceptUuidChange?: Mock;
}) {
  const methods = useForm<FieldValues>({ defaultValues: { conceptUuid: defaultConceptUuid } });
  return (
    <ConceptsSelector
      name="conceptUuid"
      controllerName="conceptUuid"
      control={methods.control}
      title="Please Specify"
      placeholder="Choose an item"
      onConceptUuidChange={onConceptUuidChange}
    />
  );
}

beforeEach(() => {
  mockUseConcepts.mockReturnValue({
    items: { results: mockConcepts },
    isLoading: false,
  } as ReturnType<typeof useConcepts>);
});

describe('ConceptsSelector', () => {
  it('renders the combobox with concepts', async () => {
    const user = userEvent.setup();
    render(<ConceptsSelectorWrapper />);

    expect(screen.getByText('Please Specify')).toBeInTheDocument();
    const input = screen.getByRole('combobox');
    await user.click(input);

    expect(screen.getByText('Bandage')).toBeInTheDocument();
    expect(screen.getByText('Syringe')).toBeInTheDocument();
    expect(screen.getByText('Gloves')).toBeInTheDocument();
  });

  it('shows a skeleton while loading', () => {
    mockUseConcepts.mockReturnValue({
      items: { results: [] },
      isLoading: true,
    } as ReturnType<typeof useConcepts>);

    render(<ConceptsSelectorWrapper />);
    expect(screen.queryByRole('combobox')).not.toBeInTheDocument();
  });

  it('displays the selected concept when editing with a pre-set value', () => {
    render(<ConceptsSelectorWrapper defaultConceptUuid="concept-2" />);

    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Syringe');
  });

  it('calls onConceptUuidChange when a concept is selected', async () => {
    const user = userEvent.setup();
    const onConceptUuidChange = vi.fn();

    render(<ConceptsSelectorWrapper onConceptUuidChange={onConceptUuidChange} />);

    const input = screen.getByRole('combobox');
    await user.click(input);
    await user.click(screen.getByText('Gloves'));

    expect(onConceptUuidChange).toHaveBeenCalledWith(expect.objectContaining({ uuid: 'concept-3' }));
  });

  it('clears the form value when the selection is cleared', async () => {
    const user = userEvent.setup();
    render(<ConceptsSelectorWrapper defaultConceptUuid="concept-1" />);

    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Bandage');

    const clearButton = screen.getByRole('button', { name: /clear/i });
    await user.click(clearButton);

    expect(input).toHaveValue('');
  });
});
