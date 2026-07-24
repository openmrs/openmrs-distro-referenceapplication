import { CLOSE_PRINT_AFTER_PRINT } from '../../constants';
import { formatDisplayDate } from '../../core/utils/datetimeUtils';
import { GetHeaderSection, GetPrintTemplate } from '../../core/print/PrintTemplate';
import { printDocument } from '../../core/print/printUtils';
import { type StockOperationPrintData } from './StockOperationReport';

export const FormatGoodsReceivedDocument = async (data: StockOperationPrintData): Promise<string> => {
  const emptyRowCount: number = Math.max(0, 25 - (data?.items?.length ?? 0));
  const headerSection = await GetHeaderSection();
  return `
    <div>
        ${headerSection}
        <div class="heading text center" style="text-transform: uppercase;font-size: 20pt;">
            <b>Goods Received Note</b>
        </div>
        <div class="heading-row text">
            <span>Name of Health Unit: </span>
            <b><span>${data?.organizationName ?? ''}</span></b>
        </div>
        <div class="heading-row text">
            <table style="width:99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td style="text-align: left;">
                        <span>Delivered To: </span>
                        <b><span>${data?.location ?? ''}</span></b>
                    </td>
                    <td style="text-align: right;">
                        <span>GRN#: </span>
                        <b><span>${data?.operationNumber}</span></b>
                    </td>
                </tr>
            </table>
        </div>
        <div class="heading-row text">
            <table style="width:99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td style="text-align: left;">
                        <span>Received From: </span>
                        <b><span>${data?.source ?? ''}</span></b>
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
                <th valign="middle" style="border-top:solid black 1.0pt;"><b>Purchase Price</b></th>
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
                    <td valign="middle" class="center">${p.purchasePrice?.toLocaleString() ?? ''}</td>
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
        </tr>`,
                    )
                    .join('')
                : ''
            }    
            <tr class="footer-field">
                <td valign="middle" colspan="6" style="border:0;padding-top: 15pt;">
                    Remarks:<br/>
                    ${data?.remarks ?? ''}
                </td>                
            </tr>   
            <tr class="footer-field">
                <td valign="middle" colspan="3" style="border:0;padding-top: 15pt;">
                    Received By:<br/>
                    <b>${data?.responsiblePerson ?? ''}</b>
                </td>
                <td valign="middle" colspan="3" style="border:0;padding-top: 15pt;">
                    Checked By:<br/>
                    &nbsp;
                </td>
            </tr>
            <tr class="footer-field">
                <td valign="middle" colspan="3" style="border:0;"><br/><br/>Signature</td>
                <td valign="middle" colspan="3" style="border:0;"><br/><br/>Signature</td>
            </tr>        
        </table>        
    </div>
    `;
};

export const PrintGoodsReceivedNoteStockOperation = async (data: StockOperationPrintData) => {
  const printData = await FormatGoodsReceivedDocument(data);
  printDocument(GetPrintTemplate(printData, data?.documentTitle, true, CLOSE_PRINT_AFTER_PRINT));
};
