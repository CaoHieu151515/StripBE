export interface WalletDTO {
  transactionId?: string;
  type?: string;
  amount?: number;
  fromOwner?: string;
  toOwner?: string;
  createdDate?: Date;
  description?: string;
}
