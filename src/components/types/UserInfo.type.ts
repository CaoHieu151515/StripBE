import { GENDER } from '@/utils/enum/common.enum';

export type UserInfo = {
  login: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  gender: GENDER;
  address: string;
  dob: string;
  userImageUrl: string;
  roles: string[];
};
