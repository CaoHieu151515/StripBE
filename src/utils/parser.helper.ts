export const safeString = (value?: string | number | null, defaultValue = '') => (value ?? defaultValue).toString();
