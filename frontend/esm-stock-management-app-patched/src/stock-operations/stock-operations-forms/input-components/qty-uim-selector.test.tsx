import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { useStockItem } from '../../../stock-items/stock-items.resource';
import QtyUomSelector from './quantity-uom-selector.component';

vi.mock('../../../stock-items/stock-items.resource', () => ({
  useStockItem: vi.fn().mockReturnValue({
    isLoading: false,
    item: {},
    error: null,
  }),
  useStockItems: vi.fn().mockReturnValue({
    isLoading: false,
    error: null,
    items: {},
  }),
  useStockBatches: vi.fn().mockReturnValue({
    isLoading: false,
    error: null,
    items: {},
  }),
}));

const mockUseStockItem = vi.mocked(useStockItem);

describe('QtyUOMSelector', () => {
  const mockOnValueChange = vi.fn();
  const mockStockItemUuid = 'test-uuid';

  const mockStockItem = {
    acronym: null,
    categoryName: 'Other Lymph Node Comments',
    categoryUuid: '183f9497-b72e-4ebe-ae17-5dcf110ff3b6',
    commonName: 'Ibuprofen 5 MG/ML Injectable Solution',
    conceptName: 'IBUPROFEN',
    conceptUuid: '77897AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
    creatorFamilyName: 'Miller',
    creatorGivenName: 'Mark',
    dateCreated: new Date('2024-09-03T09:46:47.000+0300'),
    defaultStockOperationsUoMFactor: null,
    defaultStockOperationsUoMName: null,
    defaultStockOperationsUoMUuid: null,
    dispensingUnitName: 'Tablet',
    dispensingUnitPackagingUoMFactor: 10.0,
    dispensingUnitPackagingUoMName: 'Box',
    dispensingUnitPackagingUoMUuid: '65dc9ec7-0663-4cf0-a35c-6a2d2cf99eee',
    dispensingUnitUuid: '1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
    drugName: 'Ibuprofen Injection solution  5mg/mL ',
    drugUuid: '1776AFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF',
    expiryNotice: 180,
    hasExpiration: true,
    isDrug: true,
    links: [
      {
        rel: 'self',
        uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockitem/33225466-93c8-4720-b110-4f445f3764c6',
        resourceAlias: 'stockitem',
      },
    ],
    packagingUnits: [
      {
        factor: 1.0,
        isDefaultStockOperationsUoM: false,
        isDispensingUnit: false,
        links: [
          {
            rel: 'full',
            uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockitempackaginguom/8023ef7d-56da-442f-bc74-319f000840b4?v=full',
            resourceAlias: 'stockitempackaginguom',
          },
        ],
        packagingUomName: 'Vial',
        packagingUomUuid: '162382AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        resourceVersion: '1.8',
        stockItemUuid: '33225466-93c8-4720-b110-4f445f3764c6',
        uuid: '8023ef7d-56da-442f-bc74-319f000840b4',
      },
      {
        factor: 10.0,
        isDefaultStockOperationsUoM: false,
        isDispensingUnit: true,
        links: [
          {
            rel: 'full',
            uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockitempackaginguom/65dc9ec7-0663-4cf0-a35c-6a2d2cf99eee?v=full',
            resourceAlias: 'stockitempackaginguom',
          },
        ],
        packagingUomName: 'Box',
        packagingUomUuid: '162396AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        resourceVersion: '1.8',
        stockItemUuid: '33225466-93c8-4720-b110-4f445f3764c6',
        uuid: '65dc9ec7-0663-4cf0-a35c-6a2d2cf99eee',
      },
    ],
    permission: {
      canApprove: false,
      canDisplayReceivedItems: false,
      canEdit: true,
      canReceiveItems: false,
      canUpdateBatchInformation: false,
      canView: true,
      isRequisitionAndCanIssueStock: false,
    },
    preferredVendorName: 'Kenya Medical Supplies Authority',
    preferredVendorUuid: 'b7e481ef-06e7-46ba-8a7c-ab077aa3355f',
    purchasePrice: 100.0,
    purchasePriceUoMFactor: 10.0,
    purchasePriceUoMName: 'Box',
    purchasePriceUoMUuid: '65dc9ec7-0663-4cf0-a35c-6a2d2cf99eee',
    reorderLevel: null,
    reorderLevelUoMFactor: null,
    reorderLevelUoMName: null,
    reorderLevelUoMUuid: null,
    resourceVersion: '1.8',
    uuid: '33225466-93c8-4720-b110-4f445f3764c6',
    voided: false,
  };

  beforeEach(() => {
    mockUseStockItem.mockReturnValue({
      isLoading: false,
      item: mockStockItem,
      error: null,
    });
  });

  it('should render Skeleton when loading stock item', () => {
    mockUseStockItem.mockReturnValue({ isLoading: true, item: mockStockItem, error: null });

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    expect(screen.queryByRole('combobox')).not.toBeInTheDocument();
  });

  it('should render Inline notification error when error ocuured while fetching item', () => {
    const errorMessage = 'error loading stock item';
    mockUseStockItem.mockReturnValue({ isLoading: false, error: new Error(errorMessage), item: mockStockItem });

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    expect(screen.getByText(errorMessage)).toBeInTheDocument();
    expect(screen.getByRole('status')).toBeInTheDocument();
  });

  it('should render Inline notification error when error ocuured while fetching item', () => {
    const errorMessage = 'error loading stock item';
    mockUseStockItem.mockReturnValue({ isLoading: false, error: new Error(errorMessage), item: mockStockItem });

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    expect(screen.getByText(errorMessage)).toBeInTheDocument();
    expect(screen.getByRole('status')).toBeInTheDocument();
  });

  it('should display error message when error prop is provided', () => {
    const errorMessage = 'This is an error';

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} error={errorMessage} />);

    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('should display error message when error prop is provided', () => {
    const errorMessage = 'This is an error';

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} error={errorMessage} />);

    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('should render packing uom options', async () => {
    const user = userEvent.setup();

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    const combobox = screen.getByRole('combobox');
    await user.click(combobox);

    mockStockItem.packagingUnits.forEach(({ factor, packagingUomName }) => {
      expect(screen.getByText(`${packagingUomName} - ${factor}`)).toBeInTheDocument();
    });
  });

  it('should packing uom selection', async () => {
    const user = userEvent.setup();
    const itemToSelect = mockStockItem.packagingUnits[1];

    render(<QtyUomSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    const combobox = screen.getByRole('combobox');
    await userEvent.click(combobox);

    const option = screen.getByText(`${itemToSelect.packagingUomName} - ${itemToSelect.factor}`);
    await user.click(option);
    expect(mockOnValueChange).toHaveBeenCalledWith(itemToSelect.uuid);
  });
});
