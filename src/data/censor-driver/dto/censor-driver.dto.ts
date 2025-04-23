import { CENSOR_DRIVER_STATUS } from '@/utils/enum/censor-driver/censor-driver.enum';
import { GENDER } from '@/utils/enum/common.enum';

export interface CensorDriverDTO {
  id: number;
  name: string;
  phone: string;
  email: string;
  gender: GENDER;
  status: CENSOR_DRIVER_STATUS;
  dob?: string;
  packageBuy?: string;
  address?: string;
}
