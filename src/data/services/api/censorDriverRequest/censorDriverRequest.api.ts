import { CensorDriverRequestDTO } from '@/@types/dto/censorDriverRequestDTO';
import axiosClient from '../../axiosClient';
import { PagedResponse } from '@/@types/dto/pagedResponse';

// export const censorDriverRequestApi = {
//   getAll: async () => {
//     const response: CensorDriverRequestDTOAll = await axiosClient.get("manager/drivers/confirming/getAll");
//   return response.content ?? [];
//   },
// };

export const censorDriverRequestApi = {
  getAll: async (params?: {
    firstName?: string;
    lastName?: string;
    email?: string;
    phone?: string;
    page?: number;
    size?: number;
  }): Promise<PagedResponse<CensorDriverRequestDTO>> => {
    const response: PagedResponse<CensorDriverRequestDTO> = await axiosClient.get('manager/drivers/confirming/getAll', {
      params,
    });
    return response;
  },

  approveDriver: async (driverId: string): Promise<void> => {
    await axiosClient.patch(`manager/drivers/confirming/${driverId}/approve`);
  },

  rejectDriver: async (driverId: string): Promise<void> => {
    await axiosClient.patch(`manager/drivers/confirming/${driverId}/reject`);
  },
};
