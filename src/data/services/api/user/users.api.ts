import { PagedResponse } from '@/@types/dto/pagedResponse';
import { UserDetailDTO, UserGetAllDTO, UserGetMeDTO } from '@/@types/dto/userDTO';
import axiosClient from '../../axiosClient';

// export interface PagedResponse<T> {
//   data: T[];
//   totalCount: number;
// }

export const userApi = {
  // getAll: async (): Promise<PagedResponse<User>> => {
  //   const response: AxiosResponse<User[]> = await axiosClient.get("/manager/GetAllUsers", {
  //     // params: { page, size },
  //   });

  // getAll: async () => {
  //   const response: UserGetAllDTO[] = await axiosClient.get('/manager/GetAllUsers', {
  //     // params: { page, size },
  //   });

  //   return response;
  // },

  getAll: async (params?: { firstName?: string; lastName?: string }): Promise<PagedResponse<UserGetAllDTO>> => {
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
