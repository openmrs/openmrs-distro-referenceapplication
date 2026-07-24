import { CLOSE_PRINT_AFTER_PRINT, STOCK_OPERATION_PRINT_DISABLE_COSTS } from '../../constants';
import { GetHeaderSection, GetPrintTemplate } from '../../core/print/PrintTemplate';
import { printDocument } from '../../core/print/printUtils';
import { formatDisplayDate } from '../../core/utils/datetimeUtils';
import { type StockOperationPrintData } from './StockOperationReport';

export const FormatTransferDocument = async (data: StockOperationPrintData): Promise<string> => {
  const emptyRowCount: number = Math.max(0, 25 - (data?.items?.length ?? 0));
  const headerSection = await GetHeaderSection();
  return `
    <div>
        ${headerSection}
        <div class="heading text center" style="text-transform: uppercase;font-size: 20pt;">
            <b>Inventory Transfer</b>
        </div>
        <div class="heading-row text">
            <span>Name of Health Unit: </span>
            <b><span>${data?.organizationName ?? ''}</span></b>
        </div>
        <div class="heading-row text">
            <table style="width:99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td style="text-align: left;">
                        <span>Transfer From: </span>
                        <b><span>${data?.location ?? ''}</span></b>
                    </td>
                    <td style="text-align: right;">
                        <span>Transfer#: </span>
                        <b><span>${data?.operationNumber}</span></b>
                    </td>
                </tr>
            </table>
        </div>
        <div class="heading-row text">
            <table style="width:99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td style="text-align: left;">
                        <span>Transfer To: </span>
                        <b><span>${data?.destination ?? ''}</span></b>
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
                <th valign="middle" class="left" style="border-top:solid black 1.0pt;"><b>Item Code No.</b></th>
                <th valign="middle" class="left" style="border-top:solid black 1.0pt;"><b>Item Description (name, formulation, strength)</b></th>
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Batch No.</b></th>
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Expiry</b></th>
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Quantity</b></th>
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Unit Cost</b></th>
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Total Cost</b></th>
            </tr>            
            ${
              data?.items
                ? data?.items
                    .map((p) => {
                      return `
                <tr class="data-row">
                    <td valign="middle">${p.itemCode ?? ''}</td>
                    <td valign="middle">${p.itemDescription ?? ''}</td>                    
                    <td valign="middle" class="center">${p.batchNumber ?? ''}</td>                    
                    <td valign="middle" class="center">${formatDisplayDate(p.expiryDate)}</td>                    
                    <td valign="middle" class="center">${p.quantityRequired?.toLocaleString() ?? ''} ${
                        p.quantityRequiredUoM ?? ''
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
                <td valign="middle" colspan="7" style="border:0;padding-top: 15pt;">
                    Remarks:<br/>
                    ${data?.remarks ?? ''}
                </td>                
            </tr>   
            <tr class="footer-field">
                <td valign="middle" colspan="4" style="border:0;padding-top: 15pt;">
                    Prepared By:<br/>
                    <b>${data?.responsiblePerson ?? ''}</b>
                </td>
                <td valign="middle" colspan="3" style="border:0;padding-top: 15pt;">
                   Authorized By:<br/>
                    &nbsp;
                </td>
            </tr>
            <tr class="footer-field">
                <td valign="middle" colspan="4" style="border:0;"><br/><br/>Signature</td>
                <td valign="middle" colspan="3" style="border:0;"><br/><br/>Signature</td>
            </tr>        
        </table>        
    </div>
    `;
};

export const PrintTransferOutStockOperation = async (data: StockOperationPrintData) => {
  const printData = await FormatTransferDocument(data);
  printDocument(GetPrintTemplate(printData, data?.documentTitle, true, CLOSE_PRINT_AFTER_PRINT));
};
