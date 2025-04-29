import { PagedResponse } from '@/@types/dto/pagedResponse';
import { UserDetailDTO, UserGetAllDTO, UserGetMeDTO } from '@/@types/dto/userDTO';
import axiosClient from '../../axiosClient';

export const userApi = {
  getAll: async (params?: {
    firstName?: string;
    lastName?: string;
    email?: string;
    page?: number;
    size?: number;
  }): Promise<PagedResponse<UserGetAllDTO>> => {
    const response: PagedResponse<UserGetAllDTO> = await axiosClient.get('/manager/GetAllUsers', { params });
    return response;
  },

  getDetail: async (id: string): Promise<UserDetailDTO> => {
    const response: UserDetailDTO = await axiosClient.get(`manager/details/${id}`, {});
    return response;
  },

  getMe: async (): Promise<UserGetMeDTO[]> => {
    const response: UserGetMeDTO[] = await axiosClient.get('/getme', {
      // params: { page, size },
    });

    return response;
  },
};
