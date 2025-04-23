export type PagingREQ = { page: number; size: number };

export type PagingRESP = {
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
};

export const initialPagingState = {
  page: 0,
  size: 8,
};
