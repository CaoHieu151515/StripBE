import { MEMBER_SEARCH_BY } from '@/pages/members/types/MemberSearch.type';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { ParsedQuery } from 'query-string';
import { GetUserFilter } from './request/user-info.request';

export const memberRequestParamsToFilter = (searchParams: ParsedQuery<string>): GetUserFilter => {
  const searchBy = searchParams[PARAM_FIELD.SEARCH_BY] as MEMBER_SEARCH_BY;
  const searchKeyword = searchParams[PARAM_FIELD.SEARCH_KEYWORD] as string;

  return {
    ...(searchKeyword !== null && { searchKeyword }),
    ...(searchBy !== null && { searchBy }),
  };
};
