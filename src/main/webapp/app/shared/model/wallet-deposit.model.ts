import dayjs from 'dayjs';
import { IUserWallet } from 'app/shared/model/user-wallet.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IWalletDeposit {
  id?: string;
  bankNumber?: string | null;
  nameOfBank?: string | null;
  bank?: string | null;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  status?: keyof typeof PaymentStatus | null;
  userWallet?: IUserWallet | null;
}

export const defaultValue: Readonly<IWalletDeposit> = {};
