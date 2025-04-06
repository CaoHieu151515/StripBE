export type PagingREQ = { pageSize: number; currentPage: number };

export type PagingRESP = {
  page: number;
  limit: number;
  totalPages: number;
  totalRows: number;
};

export const initialPagingState = {
  pageSize: 10,
  currentPage: 1,
};
