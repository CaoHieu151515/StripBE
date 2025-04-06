import { USER_ACCOUNT_GENDER } from '@/utils/enum/user-account/user-account.enum';

export interface RatingDTO {
  id: number;
  driverName: string;
  phoneNumber: string;
  email: string;
  gender: USER_ACCOUNT_GENDER;
  rating: number;
  content: string;
  status?: string;
}
