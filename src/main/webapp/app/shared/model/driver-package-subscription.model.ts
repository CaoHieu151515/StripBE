import dayjs from 'dayjs';
import { IDriver } from 'app/shared/model/driver.model';
import { IPackageDriver } from 'app/shared/model/package-driver.model';

export interface IDriverPackageSubscription {
  id?: string;
  purchaseDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  packagePrice?: number | null;
  active?: boolean | null;
  driver?: IDriver | null;
  packageDriver?: IPackageDriver | null;
}

export const defaultValue: Readonly<IDriverPackageSubscription> = {
  active: false,
};
