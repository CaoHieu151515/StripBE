import { IUser } from 'app/shared/model/user.model';
import { ApplicationType } from 'app/shared/model/enumerations/application-type.model';

export interface ISendingAplication {
  id?: number;
  apliID?: string | null;
  sendApplicationType?: keyof typeof ApplicationType | null;
  content?: string | null;
  imgContentType?: string | null;
  img?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ISendingAplication> = {};
