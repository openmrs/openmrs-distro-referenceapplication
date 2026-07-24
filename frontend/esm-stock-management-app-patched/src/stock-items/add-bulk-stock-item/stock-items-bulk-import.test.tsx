import React from 'react';
import { vi, describe, it, expect } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '@testing-library/react';
import { type FetchResponse, showSnackbar } from '@openmrs/esm-framework';
import { uploadStockItems } from './stock-items-bulk-import.resource';
import ImportBulkStockItemsModal from './stock-items-bulk-import.modal';

const mockShowSnackbar = vi.mocked(showSnackbar);
const mockUploadStockItems = vi.mocked(uploadStockItems);

vi.mock('./stock-items-bulk-import.resource', () => ({
  uploadStockItems: vi.fn(),
}));

describe('ImportBulkStockItemsModal', () => {
  const mockCloseModal = vi.fn();

  it('renders with initial state and UI elements', () => {
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    expect(screen.getByText(/import stock items/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /select file/i })).toBeInTheDocument();
    expect(screen.getByText(/only .csv files at 2mb or less/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /upload stock items/i })).toBeInTheDocument();
  });

  it('allows only CSV files', async () => {
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    const fileInput = screen.getByLabelText(/select file/i) as HTMLInputElement;
    expect(fileInput.accept).toBe('.csv');
  });

  it('closes modal when cancel button is clicked', async () => {
    const user = userEvent.setup();
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    const cancelButton = screen.getByRole('button', { name: /cancel/i });
    await user.click(cancelButton);
    expect(mockCloseModal).toHaveBeenCalledTimes(1);
  });

  it('does nothing when upload is clicked without a file', async () => {
    const user = userEvent.setup();
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    const uploadButton = screen.getByRole('button', { name: /upload stock items/i });
    await user.click(uploadButton);

    expect(uploadStockItems).not.toHaveBeenCalled();
    expect(mockShowSnackbar).not.toHaveBeenCalled();
    expect(mockCloseModal).not.toHaveBeenCalled();
  });

  it('uploads file successfully and shows success snackbar', async () => {
    const user = userEvent.setup();

    mockUploadStockItems.mockResolvedValue({
      data: {},
      status: 200,
      ok: true,
    } as FetchResponse<unknown>);
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    const validFile = new File(['test content'], 'valid.csv', { type: 'text/csv' });
    const fileInput = screen.getByLabelText('Select file') as HTMLInputElement;
    await userEvent.upload(fileInput, validFile);

    const uploadButton = screen.getByRole('button', { name: /upload stock items/i });
    await user.click(uploadButton);

    expect(uploadStockItems).toHaveBeenCalled();
    expect(mockShowSnackbar).toHaveBeenCalledWith(
      expect.objectContaining({
        kind: 'success',
        title: 'Stock items uploaded successfully',
      }),
    );
    expect(mockCloseModal).toHaveBeenCalledTimes(1);
  });

  it('shows error snackbar on upload failure', async () => {
    const user = userEvent.setup();

    mockUploadStockItems.mockRejectedValue(new Error('Upload failed'));
    render(<ImportBulkStockItemsModal closeModal={mockCloseModal} />);

    const validFile = new File(['test content'], 'valid.csv', { type: 'text/csv' });
    const fileInput = screen.getByLabelText(/select file/i) as HTMLInputElement;
    await userEvent.upload(fileInput, validFile);

    const uploadButton = screen.getByRole('button', { name: /upload stock items/i });
    await user.click(uploadButton);

    expect(uploadStockItems).toHaveBeenCalled();
    expect(mockShowSnackbar).toHaveBeenCalledWith(
      expect.objectContaining({
        kind: 'error',
        title: 'An error occurred uploading stock items',
      }),
    );
    expect(mockCloseModal).not.toHaveBeenCalled();
  });
});
