import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen, waitFor } from '@testing-library/react';
import { type StockItemDTO } from '../core/api/types/stockItem/StockItem';
import { handleMutate } from '../utils';
import { launchAddOrEditStockItemWorkspace } from './stock-item.utils';
import { useStockItemsPages } from './stock-items-table.resource';
import StockItemsTableComponent from './stock-items-table.component';

const mockUseStockItemsPages = vi.mocked(useStockItemsPages);

vi.mock('./stock-items-table.resource', () => ({
  useStockItemsPages: vi.fn(),
}));

vi.mock('../utils', () => ({
  handleMutate: vi.fn(),
}));

vi.mock('./stock-item.utils', () => ({
  launchAddOrEditStockItemWorkspace: vi.fn(),
}));

describe('StockItemsTableComponent', () => {
  beforeEach(() => {
    mockUseStockItemsPages.mockReturnValue({
      isLoading: false,
      items: Array(25)
        .fill(null)
        .map((_, index) => ({
          uuid: `item-${index}`,
          commonName: `Test Item ${index}`,
          drugUuid: index % 2 === 0 ? `drug-${index}` : null,
          conceptName: `Concept ${index}`,
          dispensingUnitName: `Unit ${index}`,
          defaultStockOperationsUoMName: `UoM ${index}`,
          reorderLevel: index * 10,
          reorderLevelUoMName: 'Units',
        })) as StockItemDTO[],
      pagination: {
        results: [],
        totalPages: 3,
        currentPage: 1,
        paginated: true,
        showNextButton: true,
        showPreviousButton: false,
        goTo: vi.fn(),
        goToNext: vi.fn(),
        goToPrevious: vi.fn(),
      },
      error: null,
      totalCount: 25,
      currentPageSize: 10,
      setPageSize: vi.fn(),
      pageSizes: [10, 20, 30],
      currentPage: 1,
      setCurrentPage: vi.fn(),
      isDrug: '',
      setDrug: vi.fn(),
      setSearchString: vi.fn(),
    });
  });

  it('renders initial state and UI elements correctly', async () => {
    const user = userEvent.setup();

    render(<StockItemsTableComponent />);

    expect(screen.getByRole('radio', { name: /all/i })).toBeInTheDocument();
    expect(screen.getByRole('radio', { name: /^pharmaceuticals$/i })).toBeInTheDocument();
    expect(screen.getByRole('radio', { name: /non pharmaceuticals/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /import/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /settings/i })).toBeInTheDocument();
    expect(screen.getByRole('searchbox', { name: /filter table/i })).toBeInTheDocument();

    const settingsMenuButton = screen.getByRole('button', { name: /settings/i });
    await user.click(settingsMenuButton);
    await screen.findByText(/refresh/i);

    expect(screen.getByRole('button', { name: /generic name/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /common name/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /dispensing uom/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /bulk packaging/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /reorder level/i })).toBeInTheDocument();
  });

  it('displays skeleton loader when isLoading is true', () => {
    mockUseStockItemsPages.mockReturnValue({
      ...mockUseStockItemsPages,
      isLoading: true,
      items: [],
      pagination: undefined,
      totalCount: 0,
      currentPageSize: 0,
      currentPage: 0,
      setCurrentPage: function (value: React.SetStateAction<number>): void {
        throw new Error('Function not implemented.');
      },
      setPageSize: function (value: React.SetStateAction<number>): void {
        throw new Error('Function not implemented.');
      },
      pageSizes: [],
      error: undefined,
      isDrug: '',
      setDrug: undefined,
      setSearchString: function (value: any): void {
        throw new Error('Function not implemented.');
      },
    });

    render(<StockItemsTableComponent />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('renders table rows correctly based on items prop', () => {
    render(<StockItemsTableComponent />);

    expect(screen.getByRole('cell', { name: /test item 0/i })).toBeInTheDocument();
    expect(screen.getAllByRole('cell', { name: /drug/i }).length).toBeGreaterThan(0);
    expect(screen.getAllByRole('cell', { name: /other/i }).length).toBeGreaterThan(0);
  });

  it('updates search input and triggers search function', async () => {
    const user = userEvent.setup();

    render(<StockItemsTableComponent />);

    const searchInput = screen.getByRole('searchbox');
    await user.type(searchInput, 'test search');
    await waitFor(
      () => {
        expect(mockUseStockItemsPages().setSearchString).toHaveBeenCalledWith('test search');
      },
      { timeout: 2000 },
    );
  });

  it('updates pagination when page or page size changes', async () => {
    const user = userEvent.setup();
    render(<StockItemsTableComponent />);

    const nextPageButton = screen.getByLabelText(/next page/i);
    await user.click(nextPageButton);
    expect(mockUseStockItemsPages().setCurrentPage).toHaveBeenCalled();

    const pageSizeSelect = screen.getByLabelText(/items per page/i);
    await user.selectOptions(pageSizeSelect, '20');
    expect(mockUseStockItemsPages().setPageSize).toHaveBeenCalledWith(20);
  });

  it('triggers handleRefresh when refresh button is clicked', async () => {
    const user = userEvent.setup();
    render(<StockItemsTableComponent />);

    const menuButton = screen.getByTestId('stock-items-menu');
    expect(menuButton).toBeInTheDocument();
    await user.click(menuButton);

    const refreshButton = await screen.findByText(/refresh/i);
    expect(refreshButton).toBeInTheDocument();
    await user.click(refreshButton);
    await waitFor(() => {
      expect(handleMutate).toHaveBeenCalledWith(expect.stringContaining('/stockmanagement/stockitem'));
    });
  });

  it('triggers launchAddOrEditStockItemWorkspace when edit button is clicked', async () => {
    const user = userEvent.setup();
    render(<StockItemsTableComponent />);

    const editButtons = screen.getAllByLabelText(/edit stock item/i);
    await user.click(editButtons[0]);

    expect(launchAddOrEditStockItemWorkspace).toHaveBeenCalledWith(
      expect.any(Function),
      expect.objectContaining({ uuid: 'item-0' }),
    );
  });
});
