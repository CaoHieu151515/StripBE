import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { initialPagingState } from '@/utils/types/paging.type';
import { ParsedQuery } from 'query-string';
import { GetFeedbackREQ } from './request/feedback.request';

export const feedbackListRequestParamsToFilter = (searchParams: ParsedQuery<string>): GetFeedbackREQ => {
  const page = searchParams[PARAM_FIELD.PAGE] || initialPagingState.page;
  const size = searchParams[PARAM_FIELD.SIZE] || initialPagingState.size;
  const status = searchParams[PARAM_FIELD.STATUS] as string;
  const sort = searchParams[PARAM_FIELD.SORT] as string;
  const tripId = searchParams[PARAM_FIELD.TRIP_ID] as string;

  return {
    ...(!!status && { status }),
    ...(!!sort && { sort: sort.split(',') }),
    ...(!!tripId && { tripId }),
    page: page ? Number(page) - 1 : initialPagingState.page,
    size: Number(size),
  };
};
