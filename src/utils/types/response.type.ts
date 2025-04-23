import { PagingRESP } from './paging.type';

export type BaseResponse<T> = {
  success: boolean;
  message: string;
  data: T;
};

export type PaginationRESP<T> = {
  content: T[];
} & PagingRESP;
