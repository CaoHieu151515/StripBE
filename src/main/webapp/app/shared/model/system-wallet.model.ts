import dayjs from 'dayjs';

export interface ISystemWallet {
  id?: number;
  systemWalletID?: string | null;
  before?: number | null;
  amount?: number | null;
  current?: number | null;
  blockAmount?: number | null;
  mobifyDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ISystemWallet> = {};
