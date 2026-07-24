import dayjs from 'dayjs';

export const formatDisplayDate = (date: Date | null | undefined, format?: string) => {
  return date ? dayjs(date).format(format ?? 'DD-MMM-YYYY') : '';
};

export const formatBatchExpiryDate = (date: Date | null | undefined, format?: string) => {
  return date ? dayjs(date).format(format ?? 'YYYY-MMM') : '';
};

export const formatDisplayDateTime = (date: Date | null | undefined, format?: string) => {
  return date ? dayjs(date).format(format ?? 'DD-MMM-YYYY HH:mm') : '';
};

export const DATE_PICKER_FORMAT = 'DD/MM/YYYY';
export const formatForDatePicker = (date: Date | null | undefined) => {
  return formatDisplayDate(date, DATE_PICKER_FORMAT);
};

export const today = () => {
  const date = new Date();
  return new Date(date.getFullYear(), date.getMonth(), date.getDate());
};

export const DATE_PICKER_CONTROL_FORMAT = 'd/m/Y';

export const isDateAfterToday = (date: Date | null | undefined) => {
  if (date) {
    if (typeof date === 'string' || date instanceof String) {
      date = new Date(date);
    }

    return date.getTime() > today().getTime();
  }
  return false;
};

export const ParseDate = (value: string | null | undefined) => {
  let date = null;
  try {
    if (!value) return date;
    value = value?.toLowerCase();
    if (value.includes('jan')) value = value.replace('jan', '01');
    else if (value.includes('feb')) value = value.replace('feb', '02');
    else if (value.includes('mar')) value = value.replace('mar', '03');
    else if (value.includes('apr')) value = value.replace('apr', '04');
    else if (value.includes('may')) value = value.replace('may', '05');
    else if (value.includes('jun')) value = value.replace('jun', '06');
    else if (value.includes('jul')) value = value.replace('jul', '07');
    else if (value.includes('aug')) value = value.replace('aug', '08');
    else if (value.includes('sep')) value = value.replace('sep', '09');
    else if (value.includes('oct')) value = value.replace('oct', '10');
    else if (value.includes('nov')) value = value.replace('nov', '11');
    else if (value.includes('dec')) value = value.replace('dec', '12');
    value = value.replace(/-/g, '/');
    let strMonth;
    if (value!.substring(3, 5).indexOf('0') === 0) strMonth = value!.substring(4, 5);
    else strMonth = value!.substring(3, 5);
    const month = parseInt(strMonth) - 1;

    if (value) {
      date = new Date(parseInt(value!.substring(6, 10)), month, parseInt(value!.substring(0, 2)), 0, 0);
    }
  } catch (e) {
    // Err
  }
  return date;
};
