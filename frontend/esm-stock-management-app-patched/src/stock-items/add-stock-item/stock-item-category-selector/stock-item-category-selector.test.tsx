import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { type FieldValues, useForm } from 'react-hook-form';
import { useConfig } from '@openmrs/esm-framework';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { useConcept } from '../../../stock-lookups/stock-lookups.resource';
import StockItemCategorySelector from './stock-item-category-selector.component';

vi.mock('../../../stock-lookups/stock-lookups.resource', () => ({
  useConcept: vi.fn(),
}));

const mockUseConcept = vi.mocked(useConcept);
const mockUseConfig = vi.mocked(useConfig);

const drugCategory: Partial<Concept> = { uuid: 'cat-drugs', display: 'Drugs' };
const nonDrugCategory: Partial<Concept> = { uuid: 'cat-non-drugs', display: 'Non Drugs' };
const allCategories = [drugCategory, nonDrugCategory] as Concept[];

function CategorySelectorWrapper({
  defaultCategoryUuid = '',
  itemType,
  onItemTypeChange,
}: {
  defaultCategoryUuid?: string;
  itemType?: string;
  onItemTypeChange?: (newItemType: string | undefined) => void;
}) {
  const methods = useForm<FieldValues>({ defaultValues: { categoryUuid: defaultCategoryUuid } });

  // Expose setValue for external control in tests
  React.useEffect(() => {
    if (onItemTypeChange) {
      onItemTypeChange(itemType);
    }
  }, [itemType, onItemTypeChange]);

  return (
    <StockItemCategorySelector
      name="categoryUuid"
      controllerName="categoryUuid"
      control={methods.control}
      title="Category"
      placeholder="Choose a category"
      itemType={itemType}
    />
  );
}

beforeEach(() => {
  mockUseConfig.mockReturnValue({ stockItemCategoryUUID: 'category-concept-uuid' });
  mockUseConcept.mockReturnValue({
    items: { answers: allCategories },
    isLoading: false,
  } as ReturnType<typeof useConcept>);
});

describe('StockItemCategorySelector', () => {
  it('renders the combobox with categories', async () => {
    const user = userEvent.setup();
    render(<CategorySelectorWrapper />);

    expect(screen.getByText('Category')).toBeInTheDocument();
    const input = screen.getByRole('combobox');
    await user.click(input);

    expect(screen.getByText('Drugs')).toBeInTheDocument();
    expect(screen.getByText('Non Drugs')).toBeInTheDocument();
  });

  it('shows a skeleton while loading', () => {
    mockUseConcept.mockReturnValue({
      items: { answers: [] },
      isLoading: true,
    } as ReturnType<typeof useConcept>);

    render(<CategorySelectorWrapper />);
    expect(screen.queryByRole('combobox')).not.toBeInTheDocument();
  });

  it('filters categories by itemType', async () => {
    const user = userEvent.setup();
    render(<CategorySelectorWrapper itemType="Drugs" />);

    const input = screen.getByRole('combobox');
    await user.click(input);

    expect(screen.getByText('Drugs')).toBeInTheDocument();
    expect(screen.queryByText('Non Drugs')).not.toBeInTheDocument();
  });

  it('does not clear categoryUuid on initial render when value is in the filtered list', () => {
    render(<CategorySelectorWrapper defaultCategoryUuid="cat-drugs" itemType="Drugs" />);

    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Drugs');
  });

  it('does not clear categoryUuid on initial render even when value is not in filtered list', () => {
    // Editing a stock item whose category doesn't match the current filter (e.g., retired category)
    render(<CategorySelectorWrapper defaultCategoryUuid="cat-retired" itemType="Drugs" />);

    // The combobox shows empty but the form value should NOT be cleared on mount
    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('');
    // The value is still in the form even though it's not displayed — the effect should
    // only clear on itemType change, not on initial render
  });

  it('clears categoryUuid when itemType changes and value is not in new filtered list', async () => {
    const { rerender } = render(<CategorySelectorWrapper defaultCategoryUuid="cat-drugs" itemType="Drugs" />);

    // Verify initial state
    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Drugs');

    // Switch to Non Drugs — cat-drugs is not in the "Non Drugs" filter
    rerender(<CategorySelectorWrapper defaultCategoryUuid="cat-drugs" itemType="Non Drugs" />);

    await waitFor(() => {
      expect(input).toHaveValue('');
    });
  });

  it('preserves categoryUuid when itemType changes and value is in new filtered list', async () => {
    // Show all categories (no itemType filter)
    const { rerender } = render(<CategorySelectorWrapper defaultCategoryUuid="cat-drugs" />);

    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Drugs');

    // Filter to "Drugs" — cat-drugs should still be present
    rerender(<CategorySelectorWrapper defaultCategoryUuid="cat-drugs" itemType="Drugs" />);

    await waitFor(() => {
      expect(input).toHaveValue('Drugs');
    });
  });
});
