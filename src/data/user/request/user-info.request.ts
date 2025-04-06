import { MEMBER_SEARCH_BY } from '@/pages/members/types/MemberSearch.type';
import { WithOptional } from '@/utils/types/generic.type';
import { PagingREQ } from '@/utils/types/paging.type';

export type UserInfoREQ = {
  userFirstName: string;
  userLastName: string;
  userPhoneNumber: string;
  userAvatarKey: string;
};

export type UpdateUserInfoREQ = {
  fullName: string;
  email: string;
  phoneNumber: string;
  birthday: string;
};

export type GetUserFilter = WithOptional<
  {
    pageSize: number;
    currentPage: number;
    searchBy: MEMBER_SEARCH_BY;
    searchKeyword: string;
  },
  'pageSize' | 'currentPage' | 'searchBy' | 'searchKeyword'
>;

export type GetAllUsersREQ = GetUserFilter & PagingREQ;

export type GetUserDetailDevicesFilter = WithOptional<
  {
    pageSize: number;
    currentPage: number;
  },
  'pageSize' | 'currentPage'
>;

export type GetUserDetailDevicesREQ = { id: number } & GetUserDetailDevicesFilter;

export type GetUserDetailDevicesLogsREQ = GetUserDetailDevicesREQ;
