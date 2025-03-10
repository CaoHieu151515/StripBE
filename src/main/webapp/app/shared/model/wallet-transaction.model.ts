import dayjs from 'dayjs';
import { ISystemWallet } from 'app/shared/model/system-wallet.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IUserWallet } from 'app/shared/model/user-wallet.model';
import { ISystemTempWallet } from 'app/shared/model/system-temp-wallet.model';
import { WalletTransactionType } from 'app/shared/model/enumerations/wallet-transaction-type.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';

export interface IWalletTransaction {
  id?: number;
  transID?: string | null;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  walletType?: keyof typeof WalletTransactionType | null;
  transStatus?: keyof typeof TransactionStatus | null;
  transactionThirdPartyID?: string | null;
  systemWallet?: ISystemWallet | null;
  payment?: IPayment | null;
  userWallet?: IUserWallet | null;
  systemTempWallet?: ISystemTempWallet | null;
}

export const defaultValue: Readonly<IWalletTransaction> = {};
