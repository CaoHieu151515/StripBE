import axiosClient from '../../axiosClient';
import { PagedResponse } from '@/@types/dto/pagedResponse';
import { WalletDTO } from '@/@types/dto/walletDTO';

export const walletApi = {
  getAll: async (params?: { walletType?: string; page?: number; size?: number }): Promise<PagedResponse<WalletDTO>> => {
    const response: PagedResponse<WalletDTO> = await axiosClient.get('manager/wallet-transactions', {
      params,
    });
    return response;
  },
  // getTransactionhistory: async (params?: { walletType?: string }): Promise<PagedResponse<WalletDTO>> => {
  //   const response: PagedResponse<WalletDTO> = await axiosClient.get('manager/wallet/Transactionhistory', {
  //     params,
  //   });
  //   return response;
  // },

  getTransactionhistory: async (params?: {
    walletType?: string;
    page?: number;
    size?: number;
  }): Promise<PagedResponse<WalletDTO>> => {
    const response: PagedResponse<WalletDTO> = await axiosClient.get('manager/wallet/Transactionhistory', {
      params,
    });
    return response;
  },
};
