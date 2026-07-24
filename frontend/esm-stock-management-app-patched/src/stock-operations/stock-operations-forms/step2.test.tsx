import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { useFormContext, type UseFormReturn } from 'react-hook-form';
import { useConfig, useSession } from '@openmrs/esm-framework';
import { formatForDatePicker } from '../../constants';
import { receiptOperationTypeMock } from '@mocks';
import { type BaseStockOperationItemFormData } from '../validation-schema';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import { useFilterableStockItems } from './hooks/useFilterableStockItems';
import { useStockItem } from '../../stock-items/stock-items.resource';
import { useStockOperations } from '../stock-operations.resource';
import { useStockOperationTypes } from '../../stock-lookups/stock-lookups.resource';
import useParties from './hooks/useParties';
import StockOperationForm from './stock-operation-form.component';

const mockUseConfig = vi.mocked(useConfig);
const mockUseFilterableStockItems = vi.mocked(useFilterableStockItems);
const mockUseFormContext = vi.mocked(useFormContext);
const mockUseParties = vi.mocked(useParties);
const mockUseSession = vi.mocked(useSession);
const mockUseStockItem = vi.mocked(useStockItem);
const mockUseStockOperations = vi.mocked(useStockOperations);
const mockUseStockOperationTypes = vi.mocked(useStockOperationTypes);

vi.mock('../../stock-lookups/stock-lookups.resource', () => ({
  useStockOperationTypes: vi.fn(),
  useUsers: vi.fn().mockReturnValue({ items: { results: [] }, isLoading: false }),
  useUser: vi.fn().mockReturnValue({ data: { display: 'Test User' }, isLoading: false, error: null }),
}));

vi.mock('../stock-operations.resource', () => ({
  operationStatusColor: vi.fn(() => 'some-color'),
  getStockOperationLinks: vi.fn(),
  useStockOperations: vi.fn().mockReturnValue({
    items: {
      results: [],
      links: [],
      totalCount: 0,
    },
    isLoading: false,
    error: null,
  }),
  useStockOperationAndItems: vi.fn().mockReturnValue({
    items: { results: [] },
    isLoading: false,
    error: null,
  }),
}));

vi.mock('../../stock-items/stock-items.resource', () => ({
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

vi.mock('./hooks/useFilterableStockItems', () => ({
  useFilterableStockItems: vi.fn().mockReturnValue({
    stockItemsList: [],
    setLimit: vi.fn(),
    setRepresentation: vi.fn(),
    setSearchString: vi.fn(),
    isLoading: false,
  }),
}));

vi.mock('./hooks/useParties', () => ({ default: vi.fn() }));

vi.mock('react-hook-form', () => ({
  useForm: vi.fn().mockReturnValue({
    watch: vi.fn(),
    formState: {
      errors: {},
    },
    resetField: vi.fn(),
    getValues: vi.fn(),
    setValue: vi.fn(),
    handleSubmit: vi.fn(),
    trigger: vi.fn().mockReturnValue(true),
  }),
  useFormContext: vi.fn().mockReturnValue({
    watch: vi.fn(),
    formState: {
      errors: {},
      isDirty: false,
      isLoading: false,
      isSubmitted: false,
      isSubmitSuccessful: false,
      isSubmitting: false,
      isValid: true,
      isValidating: false,
      submitCount: 0,
      touchedFields: {},
      dirtyFields: {},
      disabled: false,
    },
    resetField: vi.fn(),
    getValues: vi.fn(),
    setValue: vi.fn(),
    handleSubmit: vi.fn(),
    trigger: vi.fn().mockReturnValue(true),
  }),
  Controller: ({ render }) => render({ field: {}, fieldState: {} }),
  FormProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
}));

vi.mock('../../stock-items/add-stock-item/batch-information/batch-information.resource', () => ({
  useStockItemBatchInformationHook: vi.fn().mockReturnValue({
    items: [],
    totalCount: 0,
    currentPage: 1,
    currentPageSize: 10,
    setCurrentPage: vi.fn(),
    setPageSize: vi.fn(),
    pageSizes: [],
    isLoading: false,
    error: undefined,
    setSearchString: vi.fn(),
    setStockItemUuid: vi.fn(),
    setLocationUuid: vi.fn(),
    setPartyUuid: vi.fn(),
    setStockBatchUuid: vi.fn(),
  }),
}));

describe('Stock Operation step 2 (stock operation items details)', () => {
  beforeEach(() => {
    const mockStockOperationTypes = {
      types: {
        results: [],
        links: [],
        totalCount: 0,
      },
      isLoading: false,
      error: null,
    };

    mockUseStockOperationTypes.mockReturnValue(mockStockOperationTypes);

    mockUseStockOperations.mockReturnValue({
      items: {
        results: [],
        links: [],
        totalCount: 0,
      },
      isLoading: false,
      error: null,
    });

    mockUseConfig.mockReturnValue({ autoPopulateResponsiblePerson: true });

    mockUseParties.mockReturnValue({
      destinationParties: [],
      destinationPartiesFilter: vi.fn(),
      destinationTags: [],
      error: undefined,
      isLoading: false,
      mutate: vi.fn(),
      parties: [],
      sourceParties: [],
      sourcePartiesFilter: vi.fn(),
      sourceTags: [],
    });

    mockUseSession.mockReturnValue({
      authenticated: true,
      sessionId: 'test-session-id',
      user: {
        uuid: 'test-user-uuid',
        display: 'Test User',
        username: 'testuser',
        systemId: 'test-system-id',
        userProperties: {},
        person: { uuid: 'test-person-uuid' },
        privileges: [],
        roles: [],
        retired: false,
        links: [],
        locale: 'en',
        allowedLocales: ['en'],
      },
      sessionLocation: {
        uuid: 'test-location-uuid',
        display: 'Test Location',
        links: [],
      },
    });
  });

  it('should have both previous and next btns', async () => {
    const user = userEvent.setup();

    render(
      <StockOperationForm
        stockOperationType={receiptOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    // MOVE TO STEP 2
    await user.click(screen.getByRole('button', { name: /Next/i }));

    expect(screen.getByRole('button', { name: /Next/i })).toBeInTheDocument();
    expect(screen.getByTestId('previous-btn')).toBeInTheDocument();
  });

  it('should render stock operation items table with item search component', async () => {
    const user = userEvent.setup();

    render(
      <StockOperationForm
        stockOperationType={receiptOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    const nextButton = screen.getByRole('button', { name: /Next/i });
    expect(nextButton).toBeInTheDocument();
    await user.click(nextButton);
    expect(screen.getByRole('table')).toBeInTheDocument();
    expect(
      screen.getByRole('searchbox', {
        name(accessibleName, element) {
          return element.getAttribute('id') === 'search-stock-operation-item';
        },
      }),
    ).toBeInTheDocument();
  });

  it('should search stock operation item and render results', async () => {
    const user = userEvent.setup();

    const mocksetSearchString = vi.fn();
    mockUseFilterableStockItems.mockReturnValue({
      stockItemsList: [
        { uuid: 'mock-uuid', commonName: 'mock-common-name', drugName: 'mock-common-name' },
      ] as Array<StockItemDTO>,
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
      isLoading: false,
      setSearchString: mocksetSearchString,
    });

    render(
      <StockOperationForm
        stockOperationType={receiptOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    // ----- CLICK NEXT TO MOVE TO STEP 2 ---------
    await user.click(screen.getByRole('button', { name: /Next/i }));
    // -------------------------------
    const searchInput = screen.getByRole('searchbox', {
      name: /search/i,
    });
    expect(searchInput).toBeInTheDocument();
    await user.click(searchInput);
    await user.type(searchInput, 'stock');
    expect(mocksetSearchString).toHaveBeenCalledWith('stock');
    expect(screen.getByText('mock-common-name'));
  });

  it('should properly handle stock operation item selection', async () => {
    const user = userEvent.setup();

    mockUseFilterableStockItems.mockReturnValue({
      stockItemsList: [
        { uuid: 'mock-uuid', commonName: 'mock-common-name', drugName: 'mock-common-name' },
      ] as Array<StockItemDTO>,
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
      isLoading: false,
      setSearchString: vi.fn(),
    });

    render(
      <StockOperationForm
        stockOperationType={receiptOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    // ----- CLICK NEXT TO MOVE TO STEP 2 ---------
    await user.click(screen.getByRole('button', { name: /Next/i }));
    // -------------------------------
    const searchInput = screen.getByRole('searchbox', {
      name: (_, element) => element.getAttribute('id') === 'search-stock-operation-item',
    });
    await user.click(searchInput);
    await user.type(searchInput, 'stock');
    await user.click(screen.getByText('mock-common-name'));
    // Look for common name at the top of workspace
    expect(screen.getByText(/no drug name available|no common name available|mock-common-name/i)).toBeInTheDocument();
  });

  it('should render stock operation items in data table', async () => {
    const user = userEvent.setup();

    mockUseStockItem.mockReturnValue({
      isLoading: false,
      error: null,
      item: { commonName: 'mock-stock-item-common name', uuid: 'mock-uuid' } as StockItemDTO,
    });

    const mockQuantity = 99999;
    const mockExpiration = new Date();
    mockUseFormContext.mockReturnValue({
      watch: vi.fn().mockReturnValue([
        {
          quantity: mockQuantity,
          expiration: mockExpiration,
        },
      ] as BaseStockOperationItemFormData),
      resetField: vi.fn(),
      formState: {
        errors: {},
        isDirty: false,
        isLoading: false,
        isSubmitted: false,
        isSubmitSuccessful: false,
        isSubmitting: false,
        isValid: true,
        isValidating: false,
        submitCount: 0,
        touchedFields: {},
        dirtyFields: {},
        disabled: false,
      },
      getValues: vi.fn(),
      setValue: vi.fn(),
      handleSubmit: vi.fn(),
      trigger: vi.fn().mockReturnValue(true),
    } as unknown as UseFormReturn<BaseStockOperationItemFormData>);

    render(
      <StockOperationForm
        stockOperationType={receiptOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    // ----- CLICK NEXT TO MOVE TO STEP 2 ---------
    await user.click(screen.getByRole('button', { name: /Next/i }));
    // -------------------------------
    expect(screen.getByText(mockQuantity.toLocaleString())).toBeInTheDocument(); //Find by quentity
    expect(screen.getByText(formatForDatePicker(mockExpiration))).toBeInTheDocument(); //Find by quentity
  });
});
