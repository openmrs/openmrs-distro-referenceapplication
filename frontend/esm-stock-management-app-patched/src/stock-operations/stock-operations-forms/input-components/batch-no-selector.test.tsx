import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type StockBatchDTO } from '../../../core/api/types/stockItem/StockBatchDTO';
import { type StockItemInventory } from '../../../core/api/types/stockItem/StockItemInventory';
import { formatForDatePicker } from '../../../constants';
import { useStockItemBatchInformationHook } from '../../../stock-items/add-stock-item/batch-information/batch-information.resource';
import { useStockItemBatchNumbers } from '../hooks/useStockItemBatchNumbers';
import BatchNoSelector from './batch-no-selector.component';

vi.mock('../hooks/useStockItemBatchNumbers', () => ({
  useStockItemBatchNumbers: vi.fn(),
}));

vi.mock('../../../stock-items/add-stock-item/batch-information/batch-information.resource', () => ({
  useStockItemBatchInformationHook: vi.fn(),
}));

const mockUseStockItemBatchNumbers = vi.mocked(useStockItemBatchNumbers);
const mockUseStockItemBatchInformationHook = vi.mocked(useStockItemBatchInformationHook);

describe('BatchNoSelector', () => {
  const mockOnValueChange = vi.fn();
  const mockStockItemUuid = 'test-uuid';
  const mockExpiration = new Date();

  beforeEach(() => {
    mockUseStockItemBatchNumbers.mockReturnValue({
      isLoading: false,
      stockItemBatchNos: [
        {
          uuid: '1',
          batchNo: 'BATCH-001',
          quantity: '10',
          expiration: mockExpiration,
          stockItemUuid: 'item1',
          voided: false,
        },
        {
          uuid: '2',
          batchNo: 'BATCH-002',
          quantity: '20',
          expiration: mockExpiration,
          stockItemUuid: 'item2',
          voided: false,
        },
      ],
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
      setSearchString: vi.fn(),
    });

    mockUseStockItemBatchInformationHook.mockReturnValue({
      items: [
        { batchNumber: 'BATCH-001', quantity: 10 },
        { batchNumber: 'BATCH-002', quantity: 20 },
      ] as StockItemInventory[],
      totalCount: 2,
      currentPage: 1,
      currentPageSize: 10,
      setCurrentPage: vi.fn(),
      setStockBatchUuid: vi.fn(),
      setStockItemUuid: vi.fn(),
      setPageSize: vi.fn(),
      pageSizes: [10, 20, 50],
      isLoading: false,
      error: null,
      setLocationUuid: vi.fn(),
      setPartyUuid: vi.fn(),
      setSearchString: vi.fn(),
    });
  });

  it('should render a loading skeleton when "isLoading" is true', () => {
    mockUseStockItemBatchNumbers.mockReturnValue({
      isLoading: true,
      stockItemBatchNos: [] as StockBatchDTO[],
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
      setSearchString: vi.fn(),
    });

    mockUseStockItemBatchInformationHook.mockReturnValue({
      isLoading: true,
      items: [],
      setStockItemUuid: vi.fn(),
      totalCount: 0,
      currentPage: 1,
      currentPageSize: 10,
      setCurrentPage: vi.fn(),
      setStockBatchUuid: vi.fn(),
      setPageSize: vi.fn(),
      pageSizes: [10, 20, 50],
      error: null,
      setLocationUuid: vi.fn(),
      setPartyUuid: vi.fn(),
      setSearchString: vi.fn(),
    });

    render(<BatchNoSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('should render a combobox with batch numbers', async () => {
    render(<BatchNoSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    expect(screen.getByRole('combobox')).toBeInTheDocument();
    expect(screen.getByText(/batch number/i)).toBeInTheDocument();
  });

  it('should handle batch selection', async () => {
    const user = userEvent.setup();

    render(<BatchNoSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    const combobox = screen.getByRole('combobox');
    await user.click(combobox);
    await user.type(combobox, 'BATCH-001');

    const option = screen.getByText(`BATCH-001 | Qty: 10 | Expiry: ${formatForDatePicker(mockExpiration)}`);

    await user.click(option);

    expect(mockOnValueChange).toHaveBeenCalledWith('1');
  });

  it('should display error message when error prop is provided', () => {
    const errorMessage = 'This is an error';
    render(
      <BatchNoSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} error={errorMessage} />,
    );
    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('should filter out batches with zero or undefined quantity', async () => {
    const user = userEvent.setup();

    mockUseStockItemBatchNumbers.mockReturnValue({
      isLoading: false,
      stockItemBatchNos: [
        {
          uuid: '1',
          batchNo: 'BATCH-001',
          quantity: '10',
          expiration: mockExpiration,
          stockItemUuid: 'item1',
          voided: false,
        },
        {
          uuid: '2',
          batchNo: 'BATCH-002',
          quantity: '0',
          expiration: mockExpiration,
          stockItemUuid: 'item2',
          voided: false,
        },
        {
          uuid: '3',
          batchNo: 'BATCH-003',
          quantity: undefined,
          expiration: mockExpiration,
          stockItemUuid: 'item3',
          voided: false,
        },
      ],
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
      setSearchString: vi.fn(),
    });

    render(<BatchNoSelector stockItemUuid={mockStockItemUuid} onValueChange={mockOnValueChange} />);

    const combobox = screen.getByRole('combobox');
    await user.click(combobox);

    expect(screen.queryByText('BATCH-002')).not.toBeInTheDocument();
    expect(screen.queryByText('BATCH-003')).not.toBeInTheDocument();
  });
});
