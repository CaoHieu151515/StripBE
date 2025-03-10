import { IUser } from 'app/shared/model/user.model';

export interface IUserDetail {
  id?: number;
  appUserDetail?: string | null;
  userimageContentType?: string | null;
  userimage?: string | null;
  phone?: string | null;
  gender?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserDetail> = {};
