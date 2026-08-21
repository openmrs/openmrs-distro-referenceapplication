/** Appends the item's dispensing unit (e.g. "Strip", "Tablet") to a bare quantity number. */
export function formatQuantity(value: number, unitName: string | null | undefined): string {
  return unitName ? `${value} ${unitName}` : String(value);
}
