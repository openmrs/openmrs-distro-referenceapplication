import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { useConfig, useSession } from '@openmrs/esm-framework';
import { receiptOperationTypeMock, returnOperationTypeMock, stockIssueOperationtypeMock } from '@mocks';
import { useStockOperations } from '../stock-operations.resource';
import { useStockOperationTypes } from '../../stock-lookups/stock-lookups.resource';
import useParties from './hooks/useParties';
import StockOperationForm from './stock-operation-form.component';

const mockUseParties = vi.mocked(useParties);
const mockUseStockOperationTypes = vi.mocked(useStockOperationTypes);
const mockUseStockOperations = vi.mocked(useStockOperations);
const mockUseConfig = vi.mocked(useConfig);
const mockUseSession = vi.mocked(useSession);

vi.mock('../../stock-lookups/stock-lookups.resource', () => ({
  useStockOperationTypes: vi.fn(),
  useUsers: vi.fn().mockReturnValue({ items: { results: [] }, isLoading: false }),
  useUser: vi.fn().mockReturnValue({ data: { display: 'Test User' }, isLoading: false, error: null }),
}));

vi.mock('../stock-operations.resource', () => ({
  operationStatusColor: vi.fn(() => 'some-color'),
  getStockOperationLinks: vi.fn(),
  useStockOperations: vi.fn().mockReturnValue({
    items: { results: [] },
    isLoading: false,
    error: null,
  }),
  useStockOperation: vi.fn().mockReturnValue({
    items: undefined,
    isLoading: false,
    error: null,
  }),
  useStockOperationAndItems: vi.fn().mockReturnValue({
    items: undefined,
    isLoading: false,
    error: null,
  }),
}));

vi.mock('../../stock-items/stock-items.resource', () => ({
  useStockItem: vi.fn(),
  useStockItems: vi.fn().mockReturnValue({
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

describe('Stock Operation form step 3 (stock submision)', () => {
  beforeEach(() => {
    mockUseStockOperationTypes.mockReturnValue({
      types: { results: [], links: [], totalCount: 0 },
      isLoading: false,
      error: null,
    });

    mockUseStockOperations.mockReturnValue({
      items: { results: [], links: [], totalCount: 0 },
      isLoading: false,
      error: null,
    });

    mockUseConfig.mockReturnValue({ autoPopulateResponsiblePerson: true });

    mockUseParties.mockReturnValue({
      destinationParties: [],
      sourceParties: [],
      isLoading: false,
      error: undefined,
      sourceTags: [],
      destinationTags: [],
      parties: [],
      mutate: vi.fn(),
      sourcePartiesFilter: () => true,
      destinationPartiesFilter: () => true,
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

  it('should have previous btn and not next btn', async () => {
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
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    expect(screen.queryByRole('button', { name: /next/i })).not.toBeInTheDocument();
    expect(screen.getByTestId('previous-btn')).toBeInTheDocument();
  });

  it('should render require approval radio button and save button', async () => {
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
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    // Shouls have save button
    expect(screen.getByRole('button', { name: /save/i })).toBeInTheDocument();
    expect(screen.getAllByRole('radio', { name: /yes|no/i })).toHaveLength(2);
  });

  it('should render submitForReview button when require aprroval radion button is checked yes', async () => {
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
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    const yesRadioButton = screen.getByRole('radio', { name: /yes/i });
    expect(yesRadioButton).toBeInTheDocument();
    // Submit for review shouldnt be in doc
    expect(screen.queryByRole('button', { name: /submit for review/i })).not.toBeInTheDocument();
    await userEvent.click(yesRadioButton);
    // On require aprooval should now show
    expect(screen.getByRole('button', { name: /submit for review/i })).toBeInTheDocument();
  });

  it('should render complete button when require aprroval radion button is checked no', async () => {
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
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    const noRadioButton = screen.getByRole('radio', { name: /no/i });
    expect(noRadioButton).toBeInTheDocument();
    await userEvent.click(noRadioButton);
    // On require aprooval should now show complete btn
    expect(screen.getByTestId('complete-button')).toBeInTheDocument();
  });

  it('should render dispatch btn for stock return operation and dont require aproval', async () => {
    render(
      <StockOperationForm
        stockOperationType={returnOperationTypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    // MOVE TO STEP 2
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    const noRadioButton = screen.getByRole('radio', { name: /no/i });
    expect(noRadioButton).toBeInTheDocument();
    await userEvent.click(noRadioButton);
    // On require aprooval should now show complete btn
    expect(screen.getByTestId('dipatch-button')).toBeInTheDocument();
  });

  it('should render dispatch btn for stock issue operation and dont require aproval', async () => {
    render(
      <StockOperationForm
        stockOperationType={stockIssueOperationtypeMock as any}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    // MOVE TO STEP 2
    await userEvent.click(screen.getByRole('button', { name: /next/i }));
    // MOVE TO STEP3
    await userEvent.click(screen.getByRole('button', { name: /next/i }));

    const noRadioButton = screen.getByRole('radio', { name: /no/i });
    expect(noRadioButton).toBeInTheDocument();
    await userEvent.click(noRadioButton);
    // On require approval should now show complete btn
    expect(screen.getByTestId('dipatch-button')).toBeInTheDocument();
  });
});
