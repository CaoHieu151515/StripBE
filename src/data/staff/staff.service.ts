import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { initialPagingState } from '@/utils/types/paging.type';
import { ParsedQuery } from 'query-string';
import { GetStaffListREQ } from './request/staff.request';

export const staffListRequestParamsToFilter = (searchParams: ParsedQuery<string>): GetStaffListREQ => {
  const page = searchParams[PARAM_FIELD.PAGE] || initialPagingState.page;
  const size = searchParams[PARAM_FIELD.SIZE] || initialPagingState.size;
  const firstName = searchParams[PARAM_FIELD.FIRST_NAME] as string;
  const lastName = searchParams[PARAM_FIELD.LAST_NAME] as string;
  const phone = searchParams[PARAM_FIELD.PHONE] as string;
  const email = searchParams[PARAM_FIELD.EMAIL] as string;

  return {
    ...(!!firstName && { firstName }),
    ...(!!lastName && { lastName }),
    ...(!!phone && { phone }),
    ...(!!email && { email }),
    page: page ? Number(page) - 1 : initialPagingState.page,
    size: Number(size),
  };
};
