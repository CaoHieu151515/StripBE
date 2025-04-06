import { USER_ACCOUNT_GENDER } from '@/utils/enum/user-account/user-account.enum';

export type UserInfo = {
  login: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  gender: USER_ACCOUNT_GENDER;
  address: string;
  dob: string;
  userImageUrl: string;
  roles: string[];
};
