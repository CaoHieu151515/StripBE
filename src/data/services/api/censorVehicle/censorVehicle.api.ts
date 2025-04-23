import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';
import axiosClient from '../../axiosClient';

// export interface PagedResponse<T> {
//   data: T[];
//   totalCount: number;
// }

export const censorVehicleApi = {
  getAll: async () => {
    const response: CensorVehicleDTO[] = await axiosClient.get('manager/vehicles/pending-approval', {});
    return response;
  },
};
