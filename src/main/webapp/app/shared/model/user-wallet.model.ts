import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IUserWallet {
  id?: number;
  userWallet?: string | null;
  before?: number | null;
  amount?: number | null;
  current?: number | null;
  mobifyDate?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserWallet> = {};
