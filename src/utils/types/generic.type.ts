/* Nil */
export type Nil<T> = T | null | undefined;

/* Nullable */
export type Nullable<T> = T | null;

export type WithOptional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;
