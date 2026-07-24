import React from 'react';
import { vi, describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import AddEditStockItem from './add-stock-item.component';

// Mock the API-hitting child components so the integration test can focus on
// the form-staleness behaviour without bringing in their dependencies.
vi.mock('./drug-selector/drug-selector.component', () => ({ default: () => <div data-testid="drug-selector" /> }));
vi.mock('./concepts-selector/concepts-selector.component', () => ({
  default: () => <div data-testid="concepts-selector" />,
}));
vi.mock('./stock-item-category-selector/stock-item-category-selector.component', () => ({
  default: () => <div data-testid="stock-item-category-selector" />,
}));
vi.mock('./dispensing-unit-selector/dispensing-unit-selector.component', () => ({
  default: () => <div data-testid="dispensing-unit-selector" />,
}));
vi.mock('./preferred-vendor-selector/preferred-vendor-selector.component', () => ({
  default: () => <div data-testid="preferred-vendor-selector" />,
}));
vi.mock('./stock-item-units-edit/stock-item-units-edit.component', () => ({
  default: () => <div data-testid="stock-item-units-edit" />,
}));

// Other tabs are only mounted if the user navigates to them; the default tab
// is Stock Item Details, which is what this test exercises. Mock them to keep
// the test surface small.
vi.mock('./packaging-units/packaging-units.component', () => ({
  default: () => <div data-testid="packaging-units" />,
}));
vi.mock('./transactions/transactions.component', () => ({ default: () => <div data-testid="transactions" /> }));
vi.mock('./batch-information/batch-information.component', () => ({
  default: () => <div data-testid="batch-information" />,
}));
vi.mock('./quantities/quantities.component', () => ({ default: () => <div data-testid="quantities" /> }));
vi.mock('./stock-item-rules/stock-item-rules.component', () => ({
  default: () => <div data-testid="stock-item-rules" />,
}));
vi.mock('./stock-item-references/stock-item-references.component', () => ({
  default: () => <div data-testid="stock-item-references" />,
}));

const baseItem: Partial<StockItemDTO> = {
  isDrug: false,
  hasExpiration: false,
  commonName: '',
  acronym: '',
  conceptName: '',
  expiryNotice: 0,
  permission: { canView: true, canEdit: true } as StockItemDTO['permission'],
};

describe('AddEditStockItem (integration)', () => {
  it('shows the newly selected stock item in the Stock Item Details form fields when stockItem changes', () => {
    const itemA: StockItemDTO = {
      ...(baseItem as StockItemDTO),
      uuid: 'item-a',
      commonName: 'Aspirin',
      acronym: 'ASP',
    };
    const itemB: StockItemDTO = {
      ...(baseItem as StockItemDTO),
      uuid: 'item-b',
      commonName: 'Paracetamol',
      acronym: 'PCM',
    };

    const { rerender } = render(<AddEditStockItem stockItem={itemA} />);

    expect(screen.getByDisplayValue('Aspirin')).toBeInTheDocument();
    expect(screen.getByDisplayValue('ASP')).toBeInTheDocument();

    rerender(<AddEditStockItem stockItem={itemB} />);

    expect(screen.getByDisplayValue('Paracetamol')).toBeInTheDocument();
    expect(screen.getByDisplayValue('PCM')).toBeInTheDocument();
    expect(screen.queryByDisplayValue('Aspirin')).not.toBeInTheDocument();
    expect(screen.queryByDisplayValue('ASP')).not.toBeInTheDocument();
  });
});
