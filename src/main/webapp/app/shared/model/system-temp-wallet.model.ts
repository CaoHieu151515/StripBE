import dayjs from 'dayjs';

export interface ISystemTempWallet {
  id?: number;
  systemWalletID?: string | null;
  before?: number | null;
  amount?: number | null;
  current?: number | null;
  mobifyDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ISystemTempWallet> = {};
