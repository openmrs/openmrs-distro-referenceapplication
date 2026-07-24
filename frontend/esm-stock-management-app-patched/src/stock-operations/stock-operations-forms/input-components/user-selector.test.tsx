import React from 'react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { useFormContext, type Control, type FieldValues, type UseFormReturn } from 'react-hook-form';
import { otherUser } from '../../../core/utils/utils';
import { useUser } from '../../../stock-lookups/stock-lookups.resource';
import useSearchUser from '../hooks/useSearchUser';
import UsersSelector from './users-selector.component';

vi.mock('../hooks/useSearchUser', () => ({
  default: vi.fn(),
}));

vi.mock('../../../stock-lookups/stock-lookups.resource', () => ({
  useUser: vi.fn(),
}));

vi.mock('react-hook-form', () => ({
  useFormContext: vi.fn(),
  Controller: ({ render }) => render({ field: {}, fieldState: {} }),
}));

const mockUseSearchUser = vi.mocked(useSearchUser);
const mockUseUser = vi.mocked(useUser);
const mockUseFormContext = vi.mocked(useFormContext);

describe('User Selector', () => {
  beforeEach(() => {
    mockUseFormContext.mockReturnValue({
      control: {} as Control<FieldValues>,
      watch: vi.fn().mockImplementation((field) => {
        if (field === 'responsiblePersonUuid') return 'responsibleperson.uuid';
        if (field === 'responsiblePersonOther') return 'responsiblepersonother.uuid';
        return '';
      }),
      resetField: vi.fn(),
    } as Partial<UseFormReturn<FieldValues>> as UseFormReturn<FieldValues>);
  });

  it('renders loading state', async () => {
    mockUseSearchUser.mockReturnValue({
      isLoading: true,
      userList: [],
      setSearchString: vi.fn(),
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
    });
    mockUseUser.mockReturnValue({ isLoading: true, data: null, error: null });

    render(<UsersSelector />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('renders error state', () => {
    const errorMessage = 'Error message';
    mockUseSearchUser.mockReturnValue({
      isLoading: false,
      userList: [],
      setSearchString: vi.fn(),
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
    });
    mockUseUser.mockReturnValue({ isLoading: false, data: null, error: new Error(errorMessage) });

    render(<UsersSelector />);

    expect(screen.getByText(/error loading responsible person/i)).toBeInTheDocument();
    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('renders ComboBox with user list', async () => {
    const user = userEvent.setup();

    mockUseSearchUser.mockReturnValue({
      isLoading: false,
      userList: [
        {
          uuid: '1',
          person: { display: 'User 1', uuid: '1' },
          display: 'User 1',
          givenName: 'User',
          familyName: '1',
          firstName: 'User',
          lastName: '1',
          privileges: [],
        },
        {
          uuid: '2',
          person: { display: 'User 2', uuid: '2' },
          display: 'User 2',
          givenName: 'User',
          familyName: '2',
          firstName: 'User',
          lastName: '2',
          privileges: [],
        },
      ],
      setSearchString: vi.fn(),
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
    });

    mockUseUser.mockReturnValue({ isLoading: false, data: null, error: null });

    render(<UsersSelector />);

    expect(screen.getByText(/responsible person/i)).toBeInTheDocument();
    const combobox = screen.getByRole('combobox');
    await user.click(combobox);
    expect(screen.getByText('User 1')).toBeInTheDocument();
    expect(screen.getByText('User 2')).toBeInTheDocument();
  });

  it('renders TextInput for other user', async () => {
    mockUseSearchUser.mockReturnValue({
      isLoading: false,
      userList: [],
      setSearchString: vi.fn(),
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
    });
    mockUseFormContext.mockReturnValue({
      control: {} as Control<FieldValues>,
      watch: vi.fn().mockImplementation((field) => {
        if (field === 'responsiblePersonUuid') return otherUser.uuid;
        if (field === 'responsiblePersonOther') return '';
        return '';
      }),
      resetField: vi.fn(),
    } as Partial<UseFormReturn<FieldValues>> as UseFormReturn<FieldValues>);
    mockUseUser.mockReturnValue({ isLoading: false, data: null, error: null });

    render(<UsersSelector />);

    expect(screen.getByRole('textbox')).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/please specify/i)).toBeInTheDocument();
  });

  it('calls setSearchString on input change after delay simulating debounce timout', async () => {
    const user = userEvent.setup();

    const setSearchString = vi.fn();
    mockUseSearchUser.mockReturnValue({
      isLoading: false,
      userList: [],
      setSearchString,
      setLimit: vi.fn(),
      setRepresentation: vi.fn(),
    });

    mockUseUser.mockReturnValue({ isLoading: false, data: null, error: null });

    render(<UsersSelector />);

    const combobox = screen.getByRole('combobox');
    await user.click(combobox);
    await user.type(combobox, 'test');
    await new Promise((resolve) => setTimeout(resolve, 2000)); // Simulate debounce
    expect(setSearchString).toHaveBeenCalledWith('test');
  });
});
