import { GENDER } from '@/utils/enum/common.enum';

export interface RatingDTO {
  id: number;
  driverName: string;
  phoneNumber: string;
  email: string;
  gender: GENDER;
  rating: number;
  content: string;
  status?: string;
}
