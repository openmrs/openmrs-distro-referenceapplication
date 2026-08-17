import React from 'react';
import { vi, describe, it, expect } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import AddEditStockItem from './add-stock-item.component';

vi.mock('@carbon/react/icons', () => ({
  Save: () => <div>Save Icon</div>,
}));

vi.mock('./stock-item-details/stock-item-details.component', () => ({
  default: ({ stockItem }) => <div data-testid="stock-item-details">Stock Item Details: {stockItem?.uuid}</div>,
}));

vi.mock('./packaging-units/packaging-units.component', () => ({
  default: ({ stockItemUuid }) => <div data-testid="packaging-units">Packaging Units: {stockItemUuid || 'N/A'}</div>,
}));

// Mock window.matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

describe('AddEditStockItem', () => {
  const mockModel: StockItemDTO = {
    uuid: 'test-uuid-123',
    isDrug: true,
    drugUuid: 'drug-uuid-456',
    drugName: 'Test Drug',
    conceptUuid: 'concept-uuid-789',
    commonName: 'Test Common Name',
    acronym: 'TCN',
    conceptName: 'Test Concept Name',
    hasExpiration: true,
    packagingUnits: [
      {
        uuid: 'packaging-uuid-001',
        stockItemUuid: 'test-uuid-123',
        packagingUomName: 'Box',
        packagingUomUuid: 'uom-uuid-001',
        factor: 100,
        isDefaultStockOperationsUoM: true,
        isDispensingUnit: false,
      },
    ],
    permission: {
      canView: true,
      canEdit: true,
      canApprove: false,
      canReceiveItems: true,
      canDisplayReceivedItems: true,
      isRequisitionAndCanIssueStock: false,
      canUpdateBatchInformation: true,
    },
    categoryUuid: 'category-uuid-001',
    categoryName: 'Test Category',
    preferredVendorUuid: 'vendor-uuid-001',
    preferredVendorName: 'Test Vendor',
    purchasePrice: 10.99,
    purchasePriceUoMUuid: 'uom-uuid-002',
    purchasePriceUoMName: 'Each',
    dispensingUnitName: 'Tablet',
    dispensingUnitUuid: 'uom-uuid-003',
    dispensingUnitPackagingUoMUuid: 'uom-uuid-003',
    dispensingUnitPackagingUoMName: 'Tablet',
    defaultStockOperationsUoMUuid: 'uom-uuid-001',
    defaultStockOperationsUoMName: 'Box',
    reorderLevel: 100,
    reorderLevelUoMUuid: 'uom-uuid-001',
    reorderLevelUoMName: 'Box',
    dateCreated: new Date('2023-05-01T10:00:00Z'),
    creatorGivenName: 'John',
    creatorFamilyName: 'Doe',
    voided: false,
    expiryNotice: 30,
  };

  it('renders correctly with initial state and default selected tab', () => {
    render(<AddEditStockItem stockItem={mockModel} />);
    expect(screen.getByTestId('stock-item-details')).toBeInTheDocument();
    expect(screen.getByText('Stock Item Details: test-uuid-123')).toBeInTheDocument();
  });

  it('changes selected tab when clicking on Packaging Units', async () => {
    const user = userEvent.setup();
    render(<AddEditStockItem stockItem={mockModel} />);

    await user.click(screen.getByText(/packaging units/i));
    expect(screen.getByTestId('packaging-units')).toBeInTheDocument();
    expect(screen.getByText('Packaging Units: test-uuid-123')).toBeInTheDocument();
  });

  it('disables the Packaging Units tab when isEditing is false', () => {
    render(<AddEditStockItem />);

    const tab = screen.getByRole('button', { name: /packaging units/i });
    expect(tab).toHaveAttribute('aria-disabled', 'true');
  });

  it('enables the Packaging Units tab when isEditing is true', () => {
    render(<AddEditStockItem stockItem={mockModel} />);

    const tab = screen.getByRole('button', { name: /packaging units/i });
    expect(tab).toHaveAttribute('aria-disabled', 'false');
  });

  it('renders only the Stock Item Details and Packaging Units tabs', () => {
    render(<AddEditStockItem stockItem={mockModel} />);

    expect(screen.getByRole('button', { name: /stock item details/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /packaging units/i })).toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /transactions/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /batch information/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /quantities/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /^rules$/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /references/i })).not.toBeInTheDocument();
  });
});
