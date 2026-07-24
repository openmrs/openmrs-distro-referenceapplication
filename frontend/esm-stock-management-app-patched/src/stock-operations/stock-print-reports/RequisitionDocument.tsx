import { formatDisplayDate } from '../../core/utils/datetimeUtils';
import { GetHeaderSection, GetPrintTemplate } from '../../core/print/PrintTemplate';
import {
  CLOSE_PRINT_AFTER_PRINT,
  STOCK_OPERATION_PRINT_DISABLE_BALANCE_ON_HAND,
  STOCK_OPERATION_PRINT_DISABLE_COSTS,
} from '../../constants';
import { printDocument } from '../../core/print/printUtils';
import { type StockOperationPrintData } from './StockOperationReport';

export const FormatRequisitionDocument = async (data: StockOperationPrintData): Promise<string> => {
  const emptyRowCount: number = Math.max(0, 28 - (data?.items?.length ?? 0));
  const headerSection = await GetHeaderSection();
  return `
    <div>
        ${headerSection}
        <div class="heading text">
            <b>HMIS FORM 017: REQUISITION AND ISSUE VOUCHER</b>
        </div>
        <div class="heading-row text">
            <span>Name of Health Unit: </span>
            <b><span>${data?.organizationName ?? ''}</span></b>
        </div>
        <div class="heading-row text">
            <table style="width:99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td style="text-align: left;">
                        <span>Dept/section/ward/dispensary: </span>
                        <b><span>${data?.location ?? ''}</span></b>
                    </td>
                    <td style="text-align: right;">                    
                        <span>Date: </span>
                        <b><span>${formatDisplayDate(data?.operationDate)}</span></b>
                    </td>
                </tr>
            </table>
        </div>
        <table class="table-data" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td colspan="4" valign="top" style='border:solid black 1.0pt;height:40pt'>
                    <div class='text'><b>Ordered by (Name and signature):</b></div>
                    <p class='text' style='margin-top: 1pt;'>${data.orderedBy ?? '&nbsp;'}</p>
                </td>
                <td colspan="4" valign="top" style='border:solid black 1.0pt;border-left:none;height:40pt'>
                    <div class='text'><b>Authorized by (Name and Signature):</b></div>
                    <p class='text' style='margin-top: 1pt;'>${data.authorizedBy ?? '&nbsp;'}</p>
                </td>
            </tr>            
            <tr>
                <th valign="middle" class="left"><b>Item Code No.</b></th>
                <th valign="middle" class="left"><b>Item Description (name, formulation, strength)</b></th>
                <th valign="middle"><b>Balance on Hand</b></th>
                <th valign="middle"><b>Quantity Required</b></th>
                <th valign="middle"><b>Quantity Issued</b></th>
                <th valign="middle"><b>Unit Cost</b></th>
                <th valign="middle"><b>Total Cost</b></th>
            </tr>            
            ${
              data?.items
                ? data?.items
                    .map((p) => {
                      return `
                <tr class="data-row">
                    <td valign="middle">${p.itemCode ?? ''}</td>
                    <td valign="middle">${p.itemDescription ?? ''}</td>
                    <td valign="middle" class="center">${
                      STOCK_OPERATION_PRINT_DISABLE_BALANCE_ON_HAND || !p.balanceOnHand
                        ? ''
                        : `${p.balanceOnHand?.toLocaleString()}  ${p.balanceOnHandUoM ?? ''}`
                    }</td>
                    <td valign="middle" class="center">${p.quantityRequired?.toLocaleString() ?? ''} ${
                        p.quantityRequiredUoM ?? ''
                      }</td>
                    <td valign="middle" class="center">${p.quantityIssued?.toLocaleString() ?? ''} ${
                        p.quantityIssuedUoM ?? ''
                      }</td>
                    <td valign="middle" class="center">${
                      STOCK_OPERATION_PRINT_DISABLE_COSTS
                        ? ''
                        : `${p.unitCost?.toLocaleString() ?? ''}${p.unitCostUoM ? `/${p.unitCostUoM}` : ''}`
                    }</td>
                    <td valign="middle" class="center">${
                      STOCK_OPERATION_PRINT_DISABLE_COSTS ? '' : `${p.totalCost?.toLocaleString() ?? ''}`
                    }</td>
                </tr> 
                `;
                    })
                    .join('')
                : ''
            }
            ${
              emptyRowCount > 0
                ? Array(emptyRowCount)
                    .fill(0)
                    .map(
                      (p) => `
        <tr class="data-row">
            <td valign="middle">&nbsp;</td>
            <td valign="middle">&nbsp;</td>
            <td valign="middle" class="center">&nbsp;</td>
            <td valign="middle" class="center">&nbsp;</td>
            <td valign="middle" class="center">&nbsp;</td>
            <td valign="middle" class="center">&nbsp;</td>
            <td valign="middle" class="center">&nbsp;</td>
        </tr>`,
                    )
                    .join('')
                : ''
            }
            <tr class="footer-field">
                <td valign="middle" colspan="3">Issue date:</td>
                <td valign="middle" colspan="4">Receipt date:</td>
            </tr>
            <tr class="footer-field">
                <td valign="middle" colspan="3">Name &amp; Signature receiver:</td>
                <td valign="middle" colspan="4">Name &amp; Signature issuer:</td>
            </tr>
        </table>
    </div>
    `;
};

export const PrintRequisitionStockOperation = async (data: StockOperationPrintData) => {
  const printData = await FormatRequisitionDocument(data);
  printDocument(GetPrintTemplate(printData, data?.documentTitle, true, CLOSE_PRINT_AFTER_PRINT));
};
