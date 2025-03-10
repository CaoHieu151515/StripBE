import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';

export interface IReport {
  id?: number;
  reportID?: string | null;
  date?: dayjs.Dayjs | null;
  content?: string | null;
  reportStatus?: keyof typeof ReportStatus | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IReport> = {};
