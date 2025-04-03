import dayjs from 'dayjs';
import { ITrip } from 'app/shared/model/trip.model';
import { IDriver } from 'app/shared/model/driver.model';
import { IUser } from 'app/shared/model/user.model';
import { ReportType } from 'app/shared/model/enumerations/report-type.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';

export interface IReport {
  id?: number;
  reportID?: string | null;
  reportType?: keyof typeof ReportType | null;
  date?: dayjs.Dayjs | null;
  content?: string | null;
  reportStatus?: keyof typeof ReportStatus | null;
  trip?: ITrip | null;
  driver?: IDriver | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IReport> = {};
