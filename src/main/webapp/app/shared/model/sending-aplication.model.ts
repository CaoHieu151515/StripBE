import { IUser } from 'app/shared/model/user.model';
import { AplicationType } from 'app/shared/model/enumerations/aplication-type.model';

export interface ISendingAplication {
  id?: number;
  apliID?: string | null;
  sendApplicationType?: keyof typeof AplicationType | null;
  content?: string | null;
  imgContentType?: string | null;
  img?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ISendingAplication> = {};
