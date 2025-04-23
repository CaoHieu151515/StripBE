import { WithOptional } from '@/utils/types/generic.type';
import { PagingREQ } from '@/utils/types/paging.type';
import { Dayjs } from 'dayjs';

export type GetStaffListFilter = WithOptional<
  {
    firstName: string;
    lastName: string;
    phone: string;
    email: string;
    sort: string[];
  },
  'firstName' | 'lastName' | 'phone' | 'email' | 'sort'
>;

export type GetStaffListREQ = GetStaffListFilter & PagingREQ;

export type StaffCreateREQ = {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  dob: Dayjs;
  login: string;
  password: string;
  gender: string;
};

export type StaffUpdateREQ = {
  userId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  dob: Dayjs;
  gender: string;
};

export type StaffUpdateStatusREQ = {
  id: number;
};
