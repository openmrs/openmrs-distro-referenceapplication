import React from 'react';
import { vi, describe, it, expect, beforeEach, type Mock } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { type FieldValues, useForm } from 'react-hook-form';
import { type Drug } from '../../../core/api/types/concept/Drug';
import { fetchStockItem } from '../../stock-items.resource';
import { useDrugsHook } from './drug-selector.resource';
import DrugSelector from './drug-selector.component';

vi.mock('../../stock-items.resource', () => ({
  fetchStockItem: vi.fn(),
}));

vi.mock('./drug-selector.resource', () => ({
  useDrugsHook: vi.fn(),
}));

const mockUseDrugsHook = vi.mocked(useDrugsHook);
const mockFetchStockItem = vi.mocked(fetchStockItem);

const mockDrugs: Array<Partial<Drug>> = [
  { uuid: 'drug-1', name: 'Aspirin', concept: { display: 'ASA' } },
  { uuid: 'drug-2', name: 'Paracetamol', concept: { display: 'Acetaminophen' } },
  { uuid: 'drug-3', name: 'Ibuprofen', concept: { display: 'NSAID' } },
] as Array<Partial<Drug>>;

function DrugSelectorWrapper({
  defaultDrugUuid = '',
  initialDrugName,
  readOnly,
  onDrugChanged,
}: {
  defaultDrugUuid?: string;
  initialDrugName?: string;
  readOnly?: boolean;
  onDrugChanged?: Mock;
}) {
  const methods = useForm<FieldValues>({ defaultValues: { drugUuid: defaultDrugUuid } });
  return (
    <DrugSelector
      name="drugUuid"
      controllerName="drugUuid"
      control={methods.control}
      title="Please Specify"
      placeholder="Choose a drug"
      initialDrugName={initialDrugName}
      readOnly={readOnly}
      onDrugChanged={onDrugChanged}
    />
  );
}

beforeEach(() => {
  mockUseDrugsHook.mockReturnValue({ drugList: mockDrugs as Drug[], isLoading: false });
  mockFetchStockItem.mockResolvedValue({ results: [] });
});

describe('DrugSelector', () => {
  it('renders the combobox with placeholder text', () => {
    render(<DrugSelectorWrapper />);
    expect(screen.getByText('Please Specify')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Choose a drug')).toBeInTheDocument();
  });

  it('displays drug items from the hook', async () => {
    const user = userEvent.setup();
    render(<DrugSelectorWrapper />);

    const input = screen.getByRole('combobox');
    await user.click(input);

    expect(screen.getByText('Aspirin (ASA)')).toBeInTheDocument();
    expect(screen.getByText('Paracetamol (Acetaminophen)')).toBeInTheDocument();
    expect(screen.getByText('Ibuprofen (NSAID)')).toBeInTheDocument();
  });

  it('shows a loading indicator while drugs are being fetched', () => {
    mockUseDrugsHook.mockReturnValue({ drugList: [], isLoading: true });
    render(<DrugSelectorWrapper />);
    expect(screen.getByText('Searching...')).toBeInTheDocument();
  });

  it('displays the selected drug in read-only mode via initialDrugName placeholder', () => {
    mockUseDrugsHook.mockReturnValue({ drugList: [], isLoading: false });
    render(<DrugSelectorWrapper defaultDrugUuid="drug-2" initialDrugName="Paracetamol" readOnly />);

    const input = screen.getByRole('combobox');
    expect(input).toHaveValue('Paracetamol');
    expect(input).toBeDisabled();
  });

  it('disables drug fetching in read-only mode', () => {
    render(<DrugSelectorWrapper defaultDrugUuid="drug-2" initialDrugName="Paracetamol" readOnly />);
    expect(mockUseDrugsHook).toHaveBeenCalledWith('Paracetamol', undefined, false);
  });

  it('enables drug fetching when not read-only', () => {
    render(<DrugSelectorWrapper />);
    expect(mockUseDrugsHook).toHaveBeenCalledWith('', undefined, true);
  });

  it('does not show the existence error in read-only mode', async () => {
    mockFetchStockItem.mockResolvedValue({ results: [{ uuid: 'existing' }] });
    render(<DrugSelectorWrapper defaultDrugUuid="drug-1" initialDrugName="Aspirin" readOnly />);

    await waitFor(() => {
      expect(screen.queryByText('Item already exists')).not.toBeInTheDocument();
    });
  });

  it('does not call fetchStockItem in read-only mode when a drug is selected', async () => {
    const user = userEvent.setup();
    mockUseDrugsHook.mockReturnValue({ drugList: mockDrugs as Drug[], isLoading: false });

    const { rerender } = render(<DrugSelectorWrapper defaultDrugUuid="drug-1" initialDrugName="Aspirin" readOnly />);

    expect(mockFetchStockItem).not.toHaveBeenCalled();
  });

  it('shows the existence error when a duplicate drug is selected in non-read-only mode', async () => {
    const user = userEvent.setup();
    mockFetchStockItem.mockResolvedValue({ results: [{ uuid: 'existing-item' }] });

    render(<DrugSelectorWrapper />);

    const input = screen.getByRole('combobox');
    await user.click(input);
    await user.click(screen.getByText('Aspirin (ASA)'));

    await waitFor(() => {
      expect(screen.getByText('Item already exists')).toBeInTheDocument();
    });
  });
});
