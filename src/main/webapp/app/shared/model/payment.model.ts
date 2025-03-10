import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IPackageDriver } from 'app/shared/model/package-driver.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  paymentID?: string | null;
  amount?: number | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentStatus?: keyof typeof PaymentStatus | null;
  transactionId?: string | null;
  user?: IUser | null;
  packageDriver?: IPackageDriver | null;
}

export const defaultValue: Readonly<IPayment> = {};
