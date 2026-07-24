import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type FieldValues, useFormContext, type UseFormReturn } from 'react-hook-form';
import { useConfig } from '@openmrs/esm-framework';
import { useConcept } from '../../../stock-lookups/stock-lookups.resource';
import StockOperationReasonSelector from './stock-operation-reason-selector.component';

const mockUseConfig = vi.mocked(useConfig);
const mockUseConcept = vi.mocked(useConcept);
const mockUseFormContext = vi.mocked(useFormContext);

vi.mock('react-hook-form', () => ({
  useFormContext: vi.fn(),
  Controller: ({ render }) => render({ field: {}, fieldState: {} }),
}));

vi.mock('../../../stock-lookups/stock-lookups.resource', () => ({
  useConcept: vi.fn(),
}));

describe('StockoperationReasonSelector', () => {
  const mockConcept = {
    answers: [
      {
        uuid: '165420AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        display: 'Drug not available due to expired medication',
      },
      {
        uuid: '160584AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        display: 'Lost or ran out of medication',
      },
      {
        uuid: '122835AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        display: 'Work Shift Change',
      },
      {
        uuid: '160561AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
        display: 'New drug available',
      },
    ],
  };

  beforeEach(() => {
    mockUseConfig.mockReturnValue({
      stockAdjustmentReasonUUID: '3bbfaa44-d5b8-404d-b4c1-2bf49ad8ce25',
    });
    mockUseFormContext.mockReturnValue({
      control: {},
      formState: { errors: {} },
    } as UseFormReturn<FieldValues>);
    mockUseConcept.mockReturnValue({
      isLoading: false,
      items: mockConcept as any,
      error: null,
    });
  });

  it('should display loading state while loading reason concepts', () => {
    mockUseFormContext.mockReturnValue({ control: {} } as UseFormReturn<FieldValues>);
    mockUseConcept.mockReturnValue({
      isLoading: true,
      items: mockConcept as any,
      error: null,
    });

    render(<StockOperationReasonSelector stockOperationType={''} />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('should display error notification when error encountered while fetching concepts', () => {
    const errorMessahe = 'Error message';
    mockUseConcept.mockReturnValue({
      isLoading: false,
      items: mockConcept as any,
      error: new Error(errorMessahe),
    });

    render(<StockOperationReasonSelector stockOperationType={''} />);

    expect(screen.getByRole('status')).toBeInTheDocument();
    expect(screen.getByText(errorMessahe)).toBeInTheDocument();
  });

  it('renders ComboBox with reasons', async () => {
    const user = userEvent.setup();

    render(<StockOperationReasonSelector stockOperationType={''} />);

    const combobox = screen.getByRole('combobox');
    await user.click(combobox);

    mockConcept.answers.forEach((ans) => {
      expect(screen.getByText(`${ans?.display}`)).toBeInTheDocument();
    });
  });
});
