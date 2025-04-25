import { PagedResponse } from '@/@types/dto/pagedResponse';
import axiosClient from '../../axiosClient';
import { PackageGetAllDTO } from '@/@types/dto/packageDTO';

// export interface PagedResponse<T> {
//   data: T[];
//   totalCount: number;
// }

export const packageApi = {
  getAll: async (params?: {
    name?: string;
    price?: number;
    time?: number;
    page?: number;
    size?: number;
  }): Promise<PagedResponse<PackageGetAllDTO>> => {
    const response: PagedResponse<PackageGetAllDTO> = await axiosClient.get('manager/packages/getAllPackage', {
      params,
    });
    return response;
  },

  create: async (data: { name: string; price: number; time: number; bonus: number; description: string }) => {
    const response = await axiosClient.post('/manager/packages/createPackage', {
      id: 0,
      // packageID: crypto.randomUUID(),
      name: data.name,
      price: data.price,
      time: data.time,
      bonus: data.bonus,
      description: data.description || '',
      status: 'ACTIVE',
    });
    return response;
  },
};
