import dayjs from 'dayjs';
import { IDriver } from 'app/shared/model/driver.model';
import { IUserDetail } from 'app/shared/model/user-detail.model';
import { DriverPointHistoryStatus } from 'app/shared/model/enumerations/driver-point-history-status.model';

export interface IDriverPointHistory {
  id?: number;
  pointId?: string | null;
  point?: number | null;
  reason?: string | null;
  date?: dayjs.Dayjs | null;
  status?: keyof typeof DriverPointHistoryStatus | null;
  driver?: IDriver | null;
  userDetail?: IUserDetail | null;
}

export const defaultValue: Readonly<IDriverPointHistory> = {};
