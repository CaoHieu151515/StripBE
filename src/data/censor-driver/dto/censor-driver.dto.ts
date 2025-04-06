import { CENSOR_DRIVER_STATUS } from '@/utils/enum/censor-driver/censor-driver.enum';
import { USER_ACCOUNT_GENDER } from '@/utils/enum/user-account/user-account.enum';

export interface CensorDriverDTO {
  id: number;
  name: string;
  phone: string;
  email: string;
  gender: USER_ACCOUNT_GENDER;
  status: CENSOR_DRIVER_STATUS;
  dob?: string;
  packageBuy?: string;
  address?: string;
}
