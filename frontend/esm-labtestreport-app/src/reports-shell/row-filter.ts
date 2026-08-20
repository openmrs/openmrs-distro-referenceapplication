export interface ItemLocationRow {
  itemName: string;
  locationName?: string | null;
}

/**
 * Shared item-name filter + global text search (matches item or location name) used across the
 * stock reports, so "search by item or location" behaves identically everywhere.
 */
export function filterByItemAndSearch<T extends ItemLocationRow>(
  rows: Array<T>,
  itemFilter: string,
  searchText: string,
): Array<T> {
  const search = searchText.trim().toLowerCase();
  return rows.filter((row) => {
    if (itemFilter && row.itemName !== itemFilter) {
      return false;
    }
    if (!search) {
      return true;
    }
    return row.itemName.toLowerCase().includes(search) || (row.locationName ?? '').toLowerCase().includes(search);
  });
}

export function distinctItemNames<T extends ItemLocationRow>(rows: Array<T>): Array<string> {
  return Array.from(new Set(rows.map((row) => row.itemName))).sort((a, b) => a.localeCompare(b));
}
