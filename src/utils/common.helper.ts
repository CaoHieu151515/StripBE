export const getErrorMessage = (error: any) => {
  const msg = error?.data?.message?.split(': ')?.at(-1) || error?.error?.split(': ')?.at(-1);
  return msg ?? 'An error occurred. Please try again.';
};
