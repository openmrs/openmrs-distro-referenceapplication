import { showSnackbar } from '@openmrs/esm-framework';

export function errorAlert(title: string, msg: string) {
  showSnackbar({
    isLowContrast: true,
    title: title,
    kind: 'error',
    subtitle: msg,
  });
}
