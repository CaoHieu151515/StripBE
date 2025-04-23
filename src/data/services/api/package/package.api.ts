import axiosClient from '../../axiosClient';
import { PackageGetAllDTO } from '@/@types/dto/packageDTO';

// export interface PagedResponse<T> {
//   data: T[];
//   totalCount: number;
// }

export const packageApi = {
  getAll: async (params?: { firstName?: string; lastName?: string }) => {
    const response: PackageGetAllDTO[] = await axiosClient.get('/mobile/user/view/allpackages', { params });
    return response;
  },

  create: async (data: { name: string; price: number; time: number; bonus: number; description: string }) => {
    const response = await axiosClient.post('/manager/packages/createPackage', {
      id: 0,
      packageID: crypto.randomUUID(),
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
