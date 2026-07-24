import React from 'react';
import { vi, describe, it, expect } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type FetchResponse, showModal, showSnackbar } from '@openmrs/esm-framework';
import { deleteStockSource } from '../stock-sources.resource';
import { handleMutate } from '../../utils';
import DeleteConfirmation from '../../stock-user-role-scopes/delete-stock-user-scope.modal';
import StockSourcesDeleteActionMenu from './stock-sources-delete.component';

const mockDeleteStockSource = vi.mocked(deleteStockSource);
const mockHandleMutate = vi.mocked(handleMutate);
const mockShowModal = vi.mocked(showModal);
const mockShowSnackbar = vi.mocked(showSnackbar);

vi.mock('../stock-sources.resource', () => ({
  deleteStockSource: vi.fn(),
}));

vi.mock('../../utils', () => ({
  handleMutate: vi.fn(),
}));

describe('StockSourcesDeleteActionMenu', () => {
  const uuid = '1234-5678';
  const uuids: string[] = ['1234-5678'];

  it('renders the delete button correctly', () => {
    render(<StockSourcesDeleteActionMenu uuid={uuid} />);

    const button = screen.getByRole('button', { name: /delete source/i });
    expect(button).toBeInTheDocument();
  });

  it('opens the delete modal when the delete button is clicked', async () => {
    const user = userEvent.setup();
    render(<StockSourcesDeleteActionMenu uuid={uuid} />);

    const button = screen.getByRole('button', { name: /delete source/i });
    await user.click(button);

    expect(mockShowModal).toHaveBeenCalledWith(
      'delete-stock-modal',
      expect.objectContaining({
        close: expect.any(Function),
        uuid: uuid,
        onConfirmation: expect.any(Function),
      }),
    );
  });

  it('calls onConfirmation when delete is clicked', async () => {
    const user = userEvent.setup();
    const mockOnConfirmation = vi.fn();
    const mockClose = vi.fn();

    render(<DeleteConfirmation close={mockClose} onConfirmation={mockOnConfirmation} />);

    expect(screen.getByText(/delete stock user scope/i)).toBeInTheDocument();

    const deleteButton = screen.getByRole('button', { name: /danger\s*delete/i });
    await user.click(deleteButton);

    expect(mockOnConfirmation).toHaveBeenCalledTimes(1);
    expect(mockClose).not.toHaveBeenCalled();
  });

  it('calls deleteStockSource with the correct UUID on confirmation', async () => {
    const user = userEvent.setup();
    const mockOnConfirmation = vi.fn();
    const mockClose = vi.fn();

    render(
      <DeleteConfirmation
        close={mockClose}
        onConfirmation={() => {
          mockOnConfirmation();
          deleteStockSource([uuid]);
        }}
      />,
    );

    const deleteButton = screen.getByRole('button', { name: /danger\s*delete/i });
    await user.click(deleteButton);

    expect(mockOnConfirmation).toHaveBeenCalledTimes(1);
    expect(deleteStockSource).toHaveBeenCalledWith([uuid]);
  });

  it('calls handleMutate with the correct URL on successful deletion', async () => {
    const user = userEvent.setup();
    mockDeleteStockSource.mockResolvedValueOnce({} as FetchResponse<any>);

    const mockOnConfirmation = vi.fn();
    const mockClose = vi.fn();

    render(
      <DeleteConfirmation
        close={mockClose}
        onConfirmation={async () => {
          await deleteStockSource([uuid]);
          handleMutate('/openmrs/ws/rest/v1/stocksource');
        }}
      />,
    );

    const deleteButton = screen.getByRole('button', { name: /danger\s*delete/i });
    await user.click(deleteButton);

    expect(mockDeleteStockSource).toHaveBeenCalledWith([uuid]);
    expect(mockHandleMutate).toHaveBeenCalledWith('/openmrs/ws/rest/v1/stocksource');
  });

  it('calls showSnackbar with the correct parameters on deletion error', async () => {
    const user = userEvent.setup();
    mockDeleteStockSource.mockRejectedValueOnce(new Error('Deletion failed'));

    const mockClose = vi.fn();

    render(
      <DeleteConfirmation
        close={mockClose}
        onConfirmation={async () => {
          try {
            await deleteStockSource([uuid]);
          } catch (error) {
            showSnackbar({
              title: 'stockSourceDeleteError',
              kind: 'error',
            });
          }
        }}
      />,
    );

    const deleteButton = screen.getByRole('button', { name: /danger\s*delete/i });
    await user.click(deleteButton);

    expect(mockDeleteStockSource).toHaveBeenCalledWith([uuid]);
    expect(mockShowSnackbar).toHaveBeenCalledWith({
      title: 'stockSourceDeleteError',
      kind: 'error',
    });
  });

  it('handles the error state correctly when the delete action fails', async () => {
    const user = userEvent.setup();
    mockDeleteStockSource.mockRejectedValueOnce(new Error('Deletion failed'));

    const mockClose = vi.fn();

    render(
      <DeleteConfirmation
        close={mockClose}
        onConfirmation={async () => {
          try {
            await deleteStockSource([uuid]);
          } catch (error) {
            showSnackbar({
              title: 'stockSourceDeleteError',
              kind: 'error',
            });
          }
        }}
      />,
    );

    const deleteButton = screen.getByRole('button', { name: /danger\s*delete/i });
    await user.click(deleteButton);

    expect(mockDeleteStockSource).toHaveBeenCalledWith([uuid]);
    expect(mockShowSnackbar).toHaveBeenCalledWith({
      title: 'stockSourceDeleteError',
      kind: 'error',
    });
    expect(deleteButton).toBeInTheDocument();
  });
});
