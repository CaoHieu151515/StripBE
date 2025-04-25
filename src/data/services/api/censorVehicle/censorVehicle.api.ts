import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';
import axiosClient from '../../axiosClient';
import { PagedResponse } from '@/@types/dto/pagedResponse';

// export interface PagedResponse<T> {
//   data: T[];
//   totalCount: number;
// }

export const censorVehicleApi = {
  getAll: async (params?: {
    firstName?: string;
    lastName?: string;
    email?: string;
    phone?: string;
    page?: number;
    size?: number;
  }): Promise<PagedResponse<CensorVehicleDTO>> => {
    const response: PagedResponse<CensorVehicleDTO> = await axiosClient.get('manager/vehicles/pending-approval', {
      params,
    });
    return response;
  },
};
