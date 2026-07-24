import { PrintCss } from './PrintStyles';
import { PRINT_LOGO } from '../../constants';
import { GetPrintLogo, type PrintLogoData } from '../utils/imageUtils';
import { getConfig } from '@openmrs/esm-framework';

export const GetPrintTemplate = (
  body: string,
  title: string | null | undefined,
  printOnLoad = false,
  closeAfterPrint = true,
) => {
  return `<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=utf8">
<title>${title ?? ''}</title>
<style>
${PrintCss}
</style>
</head>
<body onload="${printOnLoad ? 'window.print()' : ''}" style='word-wrap:break-word' ${
    closeAfterPrint ? 'onafterprint="self.close()"' : ''
  }>
    ${body}
</body>
</html>`;
};

export const GetLogoSection = async () => {
  const config = await getConfig('@openmrs/esm-stock-management-app');
  const logoText = config?.logo?.name;
  let printLogoData: PrintLogoData | null = null;
  if (PRINT_LOGO) {
    try {
      printLogoData = await GetPrintLogo();
    } catch (e) {
      console.info(e);
    }
  }
  return printLogoData || logoText
    ? `
<div class="logo" >
    ${printLogoData ? (printLogoData.isSvg ? printLogoData.image : `<img alt='' src='${printLogoData.image}' />`) : ''}
    ${logoText ? `<span class='logo-text text'>${logoText}</span>` : ''}
</div>    
    `
    : '';
};

export const GetHeaderSection = async () => {
  const logoSection = await GetLogoSection();
  return `
<div class="logo-row right">
${logoSection}
</div>    
    `;
};
