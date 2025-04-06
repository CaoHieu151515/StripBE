import { TRANSACTION_STATUS } from '@/utils/enum/transaction/transaction.enum';

export interface TransactionHistoryDTO {
  id: number;
  name: string;
  packageTime: number;
  paymentTime: number;
  paymentDate: number;
  paymentMethod: string;
  transactionCode: string;
  paymentStatus: TRANSACTION_STATUS;
}
