// The stock-management backend module has no dedicated columns for Purchase Order No,
// Purchase Request No, or Project Fund Code - the only free-text slot left on a stock
// operation is `externalReference` (varchar(50)). Rather than lose the distinction between
// the three, we pack them into that one field with short, parseable prefixes, and split them
// back apart wherever they need to be shown/edited individually.
const PURCHASE_ORDER_PREFIX = 'PO:';
const PURCHASE_REQUEST_PREFIX = 'PR:';
const PROJECT_FUND_CODE_PREFIX = 'FC:';
const SEPARATOR = '|';

export interface ExternalReferenceParts {
  purchaseOrderNo: string;
  purchaseRequestNo: string;
  projectFundCode: string;
}

export function buildExternalReference({
  purchaseOrderNo,
  purchaseRequestNo,
  projectFundCode,
}: Partial<ExternalReferenceParts>): string {
  const parts: Array<string> = [];
  if (purchaseOrderNo) parts.push(`${PURCHASE_ORDER_PREFIX}${purchaseOrderNo}`);
  if (purchaseRequestNo) parts.push(`${PURCHASE_REQUEST_PREFIX}${purchaseRequestNo}`);
  if (projectFundCode) parts.push(`${PROJECT_FUND_CODE_PREFIX}${projectFundCode}`);
  return parts.join(SEPARATOR);
}

export function parseExternalReference(externalReference?: string | null): ExternalReferenceParts {
  const result: ExternalReferenceParts = { purchaseOrderNo: '', purchaseRequestNo: '', projectFundCode: '' };
  if (!externalReference) {
    return result;
  }
  externalReference.split(SEPARATOR).forEach((part) => {
    if (part.startsWith(PURCHASE_ORDER_PREFIX)) {
      result.purchaseOrderNo = part.slice(PURCHASE_ORDER_PREFIX.length);
    } else if (part.startsWith(PURCHASE_REQUEST_PREFIX)) {
      result.purchaseRequestNo = part.slice(PURCHASE_REQUEST_PREFIX.length);
    } else if (part.startsWith(PROJECT_FUND_CODE_PREFIX)) {
      result.projectFundCode = part.slice(PROJECT_FUND_CODE_PREFIX.length);
    }
  });
  return result;
}

export const EXTERNAL_REFERENCE_MAX_LENGTH = 50;
