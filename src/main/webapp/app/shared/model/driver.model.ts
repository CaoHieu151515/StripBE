import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { DriverStatus } from 'app/shared/model/enumerations/driver-status.model';

export interface IDriver {
  id?: number;
  driverID?: string | null;
  usedtoDriver?: boolean | null;
  expirationDate?: dayjs.Dayjs | null;
  driverStatus?: keyof typeof DriverStatus | null;
  driverPoint?: number | null;
  bannedDay?: dayjs.Dayjs | null;
  driverLicenseContentType?: string | null;
  driverLicense?: string | null;
  identityCardFaceUpContentType?: string | null;
  identityCardFaceUp?: string | null;
  identityCardFacedownContentType?: string | null;
  identityCardFacedown?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IDriver> = {
  usedtoDriver: false,
};
