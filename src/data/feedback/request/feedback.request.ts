import { WithOptional } from '@/utils/types/generic.type';
import { PagingREQ } from '@/utils/types/paging.type';

export type GetFeedbackFilter = WithOptional<
  {
    status: string;
    sort: string[];
  },
  'status' | 'sort'
>;

export type GetFeedbackREQ = GetFeedbackFilter & PagingREQ;
