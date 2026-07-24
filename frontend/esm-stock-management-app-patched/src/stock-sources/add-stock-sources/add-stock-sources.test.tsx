import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type FetchResponse, useConfig } from '@openmrs/esm-framework';
import { type StockSource } from '../../core/api/types/stockOperation/StockSource';
import { createOrUpdateStockSource } from '../stock-sources.resource';
import StockSourcesAddOrUpdate from './add-stock-sources.workspace';

const mockCreateOrUpdateStockSource = vi.mocked(createOrUpdateStockSource);
const mockUseConfig = vi.mocked(useConfig);

vi.mock('../stock-sources.resource', () => ({
  createOrUpdateStockSource: vi.fn(),
}));

vi.mock('../../stock-lookups/stock-lookups.resource', () => ({
  useConcept: vi.fn(() => ({
    items: {
      answers: [
        { uuid: 'type1', display: 'Type 1' },
        { uuid: 'type2', display: 'Type 2' },
      ],
    },
  })),
}));

describe('StockSourcesAddOrUpdate', () => {
  beforeEach(() => {
    mockUseConfig.mockReturnValue({ stockSourceTypeUUID: 'mock-uuid' });
  });

  it('renders correctly without model prop', () => {
    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    expect(screen.getByLabelText(/full name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/acronym\/code/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/source type/i)).toBeInTheDocument();
  });

  it('renders correctly with model prop', () => {
    const model: StockSource = {
      uuid: '123',
      name: 'Test Source',
      acronym: 'TS',
      sourceType: {
        uuid: 'type1',
        display: 'Type 1',
        conceptId: 0,
        set: false,
        version: '',
        names: [],
        name: undefined,
        numeric: false,
        complex: false,
        shortNames: [],
        indexTerms: [],
        synonyms: [],
        setMembers: [],
        possibleValues: [],
        preferredName: undefined,
        shortName: undefined,
        fullySpecifiedName: undefined,
        answers: [],
        creator: undefined,
        dateCreated: undefined,
        changedBy: undefined,
        dateChanged: undefined,
        retired: false,
        dateRetired: undefined,
        retiredBy: undefined,
        retireReason: '',
      },
      creator: {
        uuid: 'creator-uuid',
        display: 'Creator Name',
        givenName: '',
        familyName: '',
        firstName: '',
        lastName: '',
        privileges: [],
      },
      dateCreated: new Date(),
      changedBy: null,
      dateChanged: null,
      voided: false,
      voidedBy: null,
      dateVoided: null,
      voidReason: null,
    };
    render(
      <StockSourcesAddOrUpdate
        model={model}
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );
    expect(screen.getByLabelText(/full name/i)).toHaveValue('Test Source');
    expect(screen.getByLabelText(/acronym\/code/i)).toHaveValue('TS');
    expect(screen.getByLabelText(/source type/i)).toHaveValue('type1');
  });

  it('updates form fields correctly on user input', async () => {
    const user = userEvent.setup();
    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    await user.type(screen.getByLabelText(/full name/i), 'New Source');
    await user.type(screen.getByLabelText(/acronym\/code/i), 'NS');

    expect(screen.getByLabelText(/full name/i)).toHaveValue('New Source');
    expect(screen.getByLabelText(/acronym\/code/i)).toHaveValue('NS');
  });

  it('calls createOrUpdateStockSource with correct data on form submission', async () => {
    const user = userEvent.setup();
    mockCreateOrUpdateStockSource.mockResolvedValue({
      data: {},
      ok: true,
      status: 200,
      statusText: 'OK',
    } as unknown as FetchResponse);

    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    await user.type(screen.getByLabelText(/full name/i), 'New Source');
    await user.type(screen.getByLabelText(/acronym\/code/i), 'NS');
    await user.selectOptions(screen.getByLabelText(/source type/i), 'type2');
    await user.click(screen.getByText('Save'));
  });

  it('shows success message and closes the workspace on successful submission', async () => {
    const user = userEvent.setup();
    mockCreateOrUpdateStockSource.mockResolvedValue({
      data: {},
      ok: true,
      status: 200,
      statusText: 'OK',
    } as unknown as FetchResponse);

    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    await user.click(screen.getByText(/save/i));
  });

  it('shows error message on failed submission', async () => {
    const user = userEvent.setup();
    mockCreateOrUpdateStockSource.mockRejectedValue(new Error('API Error'));

    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    await user.click(screen.getByText(/save/i));
  });

  it('closes workspace when cancel button is clicked', async () => {
    const user = userEvent.setup();
    render(
      <StockSourcesAddOrUpdate
        closeWorkspace={vi.fn()}
        setTitle={vi.fn()}
        closeWorkspaceWithSavedChanges={vi.fn()}
        promptBeforeClosing={vi.fn()}
      />,
    );

    await user.click(screen.getByText(/cancel/i));
  });
});
