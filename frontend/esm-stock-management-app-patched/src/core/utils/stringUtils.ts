export function toErrorMessage(payload: any) {
  if (!payload) return payload;
  const errorMessage =
    payload?.error?.data?.error?.message ??
    payload?.data?.error?.message ??
    payload?.message ??
    payload?.error ??
    payload;
  const fieldErrors = payload?.error?.fieldErrors ?? payload?.data?.error?.fieldErrors;

  if (fieldErrors) {
    let field: keyof typeof fieldErrors;
    const errors: any[] = [];
    for (field in fieldErrors) {
      errors.push(
        fieldErrors[field]?.reduce((p: any, c: any, i: number) => {
          if (i === 0) return c?.message ?? '';
          p += (p.length > 0 ? ' \r\n' : '') + c?.message;
          return p;
        }, ''),
      );
    }
    return `${errorMessage} ${errors.join(' \r\n')}`;
  }
  return errorMessage;
}
